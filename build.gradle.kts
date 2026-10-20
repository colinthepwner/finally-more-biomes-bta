import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

plugins {
	alias(libs.plugins.loom)
    java
}

val lwjglNatives = resolveLwjglNatives()

val modVersion = "${providers.gradleProperty("mod_version").get()}+${libs.versions.bta.get()}"
val modGroup: Provider<String> = providers.gradleProperty("mod_group")
val modName: Provider<String> = providers.gradleProperty("mod_name")

val javaVersion: Provider<Int> = libs.versions.java.map { it.toInt() }

base.archivesName = modName
group = modGroup.get()
version = modVersion
loom {
	val btaChannel = libs.versions.btaChannel.get()
	val btaVersion = (if (btaChannel == "nightly") "" else "v") + libs.versions.bta.get()
    customMinecraftMetadata.set("https://downloads.betterthanadventure.net/bta-client/${btaChannel}/$btaVersion/manifest.json")
}
repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/") { name = "Fabric" }
    maven("https://maven.thesignalumproject.net/infrastructure") { name = "SignalumMavenInfrastructure" }
    maven("https://maven.thesignalumproject.net/releases") { name = "SignalumMavenReleases" }
	maven("https://maven.thesignalumproject.net/nightly") { name = "SignalumMavenNightly" }
    ivy("https://piston-data.mojang.com") {
        patternLayout { artifact("v1/[organisation]/[revision]/[module].jar") }
        metadataSources { artifact() }
    }
}

val bundledHalpLibe: Configuration by configurations.creating {
	isTransitive = false
}

dependencies {
    minecraft("::${libs.versions.bta.get()}")

	implementation(libs.loader)
	implementation(libs.halplibe)

	compileOnly(libs.bundles.btaLwjgl)
	compileOnly(libs.joml)
	compileOnly(libs.joml.primitives)
	compileOnly(libs.slf4jApi)

	localRuntime(libs.modMenu)
	runtimeClasspath(libs.clientJar)
	val lwjglVer = libs.versions.lwjgl.get()
	localRuntime(platform("org.lwjgl:lwjgl-bom:${lwjglVer}"))
	localRuntime("org.lwjgl:lwjgl::$lwjglNatives")
	localRuntime("org.lwjgl:lwjgl-glfw::$lwjglNatives")
	localRuntime("org.lwjgl:lwjgl-openal::$lwjglNatives")
	localRuntime("org.lwjgl:lwjgl-opengl::$lwjglNatives")
	localRuntime("org.lwjgl:lwjgl-stb::$lwjglNatives")

	bundledHalpLibe(libs.halplibe)
}

java {
	toolchain {
		languageVersion = javaVersion.map { JavaLanguageVersion.of(it) }
		vendor = JvmVendorSpec.ADOPTIUM
	}
	sourceCompatibility = JavaVersion.toVersion(javaVersion.get())
	targetCompatibility = JavaVersion.toVersion(javaVersion.get())
	withSourcesJar()
}
val licenseFile = run {
	val rootLicense = layout.projectDirectory.file("LICENSE")
	val parentLicense = layout.projectDirectory.file("../LICENSE")
	when {
		rootLicense.asFile.exists() -> {
			logger.lifecycle("Using LICENSE from project root: {}", rootLicense.asFile)
			rootLicense
		}
		parentLicense.asFile.exists() -> {
			logger.lifecycle("Using LICENSE from parent directory: {}", parentLicense.asFile)
			parentLicense
		}
		else -> {
			logger.warn("No LICENSE file found in project or parent directory.")
			null
		}
	}
}
tasks {
	withType<JavaCompile>().configureEach {
		options.encoding = "UTF-8"
		sourceCompatibility = javaVersion.get().toString()
		targetCompatibility = javaVersion.get().toString()
		if (javaVersion.get() > 8) options.release = javaVersion
	}
	named<UpdateDaemonJvm>("updateDaemonJvm") {
		languageVersion = libs.versions.gradleJava.map { JavaLanguageVersion.of(it.toInt()) }
		vendor = JvmVendorSpec.ADOPTIUM
	}
	withType<JavaExec>().configureEach { defaultCharacterEncoding = "UTF-8" }
	withType<Javadoc>().configureEach { options.encoding = "UTF-8" }
	withType<Test>().configureEach { defaultCharacterEncoding = "UTF-8" }
	withType<Jar>().configureEach {
		licenseFile?.let {
			from(it) {
				rename { original -> "${original}_${archiveBaseName.get()}" }
			}
		}
	}
	processResources {
		val resourceMap = mapOf(
			"version" to modVersion,
			"fabricloader" to libs.versions.loader.get(),
			"halplibe" to libs.versions.halplibe.get(),
			"java" to libs.versions.java.get(),
			"modmenu" to libs.versions.modMenu.get()
		)

		inputs.properties(resourceMap)

		duplicatesStrategy = DuplicatesStrategy.INCLUDE
		with(copySpec {
			from("src/main/resources/") {
				include("fabric.mod.json")
				include("*.mixins.json")
				expand(resourceMap)
			}
		})
	}

	named<Jar>("jar") {
		from(bundledHalpLibe) { into("META-INF/jars") }
		filesMatching("fabric.mod.json") {
			val nested = bundledHalpLibe.singleFile.name
			var inserted = false
			filter { line ->
				if (!inserted && line.trimStart().startsWith("\"depends\"")) {
					inserted = true
					"\t\"jars\": [\n\t\t{ \"file\": \"META-INF/jars/$nested\" }\n\t],\n$line"
				} else {
					line
				}
			}
		}
	}
}

configurations.configureEach {
	exclude(group = "org.lwjgl.lwjgl")
	exclude(group = "net.java.jutils")
	exclude(group = "net.java.jinput")
	exclude(group = "net.sf.jopt-simple")
	exclude(group = "net.minecraft", module = "launchwrapper")
}

val obfSalt = "betteroplenty/pack/v1"

val obfKey: ByteArray = run {
	val source = layout.projectDirectory
		.file("src/main/java/com/betteroplenty/res/ObfResources.java").asFile
	val literal = Regex("""KEY\s*=\s*\{(.*?)}""", RegexOption.DOT_MATCHES_ALL)
		.find(source.readText())?.groupValues?.get(1)
		?: error("No `KEY = { ... }` literal in ${source.name}. The obfuscated build folds " +
			"its resources with that array and cannot be produced without it.")
	val bytes = Regex("""0x([0-9A-Fa-f]{2})""").findAll(literal)
		.map { it.groupValues[1].toInt(16).toByte() }
		.toList()
	check(bytes.size >= 16) {
		"Read only ${bytes.size} bytes out of ObfResources.KEY; expected the full array."
	}
	bytes.toByteArray()
}

val obfPayloadRoot = "assets/betteroplenty/x"
val obfManifestEntry = "betteroplenty.dat"

val obfMixinPackages = listOf("com.betteroplenty.mixin", "com.betteroplenty.mixin.client")

val obfExcludedClasses = listOf(
	"com/betteroplenty/agent/**",
	"com/betteroplenty/mixin/client/MinecraftAgentMixin.class",
)

val obfExcludedMixins = listOf("client.MinecraftAgentMixin")

val proguardTool: Configuration by configurations.creating {
	isCanBeConsumed = false
	isCanBeResolved = true
}

dependencies {
	proguardTool("com.guardsquare:proguard-base:7.7.0")
}

val obfWorkDir: Provider<Directory> = layout.buildDirectory.dir("obfuscation")

val obfJarFile: Provider<RegularFile> =
	layout.buildDirectory.file("libs/${modName.get()}-$modVersion-obf.jar")

val obfAgentJarFile: Provider<RegularFile> =
	obfWorkDir.map { it.file("${modName.get()}-$modVersion-obf-agent.jar") }

fun registerObfuscatedJar(
	jarTaskName: String,
	variant: String,
	keepAgent: Boolean,
	destination: Provider<RegularFile>,
	summary: String,
): TaskProvider<Task> {
	val work = obfWorkDir.map { it.dir(variant) }
	val classesJar = work.map { it.file("classes.jar") }
	val mappingFile = work.map { it.file("mapping.txt") }
	val configFile = work.map { it.file("betteroplenty.pro") }

	val obfuscateClasses = tasks.register<JavaExec>("${jarTaskName}Classes") {
		group = "build"
		description = "Renames the mod's classes and members with ProGuard ($variant)."

		val jarTask = tasks.named<Jar>("jar")
		dependsOn(jarTask)

		val inputJar = jarTask.flatMap { it.archiveFile }

		val libraryJars = sourceSets.main.map { it.compileClasspath }

		inputs.file(inputJar)
		inputs.files(libraryJars)
		outputs.file(classesJar)
		outputs.file(mappingFile)

		classpath = proguardTool
		mainClass = "proguard.ProGuard"

		val toolchainHome = javaToolchains
			.launcherFor(java.toolchain)
			.map { it.metadata.installationPath.asFile }

		doFirst {
			work.get().asFile.mkdirs()

			fun path(file: File) = file.absolutePath.replace('\\', '/')

			val dropped = if (keepAgent) emptyList() else obfExcludedClasses
			val lines = mutableListOf<String>()
			lines += "# Generated by the $jarTaskName pipeline. Do not edit; edit build.gradle.kts."
			lines += "-injars '${path(inputJar.get().asFile)}'(" +
				dropped.joinToString("") { "!$it," } + "**.class)"
			lines += "-outjars '${path(classesJar.get().asFile)}'"

			val jmods = File(toolchainHome.get(), "jmods").listFiles()
				?.filter { it.name.endsWith(".jmod") }
				?.sortedBy { it.name }
				.orEmpty()
			check(jmods.isNotEmpty()) {
				"No jmods under ${toolchainHome.get()}; cannot give ProGuard the platform classes."
			}
			jmods.forEach { lines += "-libraryjars '${path(it)}'(!**.jar;!module-info.class)" }
			libraryJars.get().files
				.filter { it.exists() && it.name.endsWith(".jar") }
				.sortedBy { it.name }
				.forEach { lines += "-libraryjars '${path(it)}'" }

			lines += ""
			lines += "-printmapping '${path(mappingFile.get().asFile)}'"

			lines += "-dontshrink"
			lines += "-dontoptimize"
			lines += "-dontnote"

			lines += "-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod,Exceptions"

			lines += ""
			lines += "# Mixin classes are renamed, but nothing inside them is."
			lines += "# @Shadow and @Invoker members are matched to the target class BY NAME, so a"
			lines += "# renamed field is not a broken reference that fails to verify -- it is a"
			lines += "# silent no-match that leaves the injection out of the built class."
			obfMixinPackages.forEach { lines += "-keeppackagenames $it" }
			lines += "-keepclassmembernames class com.betteroplenty.mixin.** { *; }"

			lines += ""
			lines += "# Members the mod looks up BY NAME at runtime, scraped from the source below."
			lines += "# A reflective lookup is the one reference ProGuard cannot see and therefore"
			lines += "# cannot update, so a renamed member turns into a NoSuchMethodException at"
			lines += "# startup -- which is exactly how BetterOPlentyClientInit.auditBiomeColors,"
			lines += "# asking each biome whether it overrides getSkyColorByTemp, first broke this"
			lines += "# build. Scraped rather than listed so that adding another lookup cannot"
			lines += "# quietly reintroduce that, and printed so the list is never a mystery."
			val reflected = scrapeReflectedMembers()
			check(reflected.isNotEmpty()) {
				"Found no reflective lookups in the source. auditBiomeColors has one, so the scrape " +
					"is broken and every reflected member is about to be renamed."
			}
			logger.lifecycle("Keeping {} reflectively-named member(s): {}",
				reflected.size, reflected.sorted().joinToString(", "))
			lines += "-keepclassmembernames class * {"
			reflected.sorted().forEach {
				lines += "    *** $it(...);"
				lines += "    *** $it;"
			}
			lines += "}"

			lines += ""
			lines += "# Enum identity survives valueOf/values, which HalpLibe's builders use."
			lines += "-keepclassmembers enum * {"
			lines += "    public static **[] values();"
			lines += "    public static ** valueOf(java.lang.String);"
			lines += "}"

			configFile.get().asFile.writeText(lines.joinToString(System.lineSeparator()))
			args = listOf("@${path(configFile.get().asFile)}")
		}
	}

	return tasks.register(jarTaskName) {
		group = "build"
		description = summary

		val jarTask = tasks.named<Jar>("jar")
		dependsOn(obfuscateClasses)

		val inputJar = jarTask.flatMap { it.archiveFile }
		inputs.file(inputJar)
		inputs.file(classesJar)
		inputs.file(mappingFile)
		outputs.file(destination)

		doLast {
			logger.lifecycle(
				writeObfuscatedJar(
					source = inputJar.get().asFile,
					obfuscatedClasses = classesJar.get().asFile,
					mapping = mappingFile.get().asFile,
					destination = destination.get().asFile,
					keepAgent = keepAgent,
				)
			)
		}
	}
}

val obfuscatedJar = registerObfuscatedJar(
	jarTaskName = "obfuscatedJar",
	variant = "release",
	keepAgent = false,
	destination = obfJarFile,
	summary = "Builds the obfuscated, agent-free jar beside the normal one.",
)

val obfuscatedAgentJar = registerObfuscatedJar(
	jarTaskName = "obfuscatedAgentJar",
	variant = "agent",
	keepAgent = true,
	destination = obfAgentJarFile,
	summary = "Builds the obfuscated jar with agent mode kept, for runObfClient to drive.",
)

tasks.named("assemble") { dependsOn(obfuscatedJar) }

loom {
	runs {
		create("obfClient") {
			client()
			configName = "Finally More Biomes (obfuscated jar)"
			runDir = "run-obf"
			ideConfigGenerated(false)
		}

		create("obfServer") {
			server()
			configName = "Finally More Biomes server (obfuscated jar)"
			runDir = "run-obf-server"
			ideConfigGenerated(false)
		}
	}
}

tasks.named<JavaExec>("runObfClient") {
	description = "Runs the obfuscated jar itself, rather than the loose dev classes."

	dependsOn(obfuscatedAgentJar)
}

tasks.named<JavaExec>("runObfServer") {
	description = "Runs a dedicated server from the obfuscated jar -- the artefact that ships."

	dependsOn(obfuscatedJar)
	standardInput = System.`in`
}

afterEvaluate {
	tasks.named<JavaExec>("runObfClient") {

		val jar = providers.gradleProperty("obfJar").map { File(it) }
			.orElse(obfAgentJarFile.map { it.asFile }).get()
		check(jar.isFile) { "No such jar: $jar" }

		val devOutput = sourceSets.main.get().output.files
		val stripped = classpath.files.filterNot { it in devOutput }
		check(stripped.size < classpath.files.size) {
			"The dev classes were not on runObfClient's classpath, so removing them did " +
				"nothing -- this run would not have been testing the jar."
		}
		classpath = files(stripped, jar)
		logger.lifecycle("runObfClient will run {} ({} classpath entries)", jar.name, stripped.size + 1)
	}

	tasks.named<JavaExec>("runObfServer") {
		val jar = providers.gradleProperty("obfJar").map { File(it) }
			.orElse(obfJarFile.map { it.asFile }).get()
		check(jar.isFile) { "No such jar: $jar" }

		val devOutput = sourceSets.main.get().output.files
		val stripped = classpath.files.filterNot { it in devOutput }
		check(stripped.size < classpath.files.size) {
			"The dev classes were not on runObfServer's classpath, so removing them did " +
				"nothing -- this run would not have been testing the jar."
		}
		classpath = files(stripped, jar)
		logger.lifecycle("runObfServer will run {} ({} classpath entries)", jar.name, stripped.size + 1)
	}
}

fun scrapeReflectedMembers(): Set<String> {
	val call = Regex("""get(?:Declared)?(?:Method|Field)\s*\(\s*"([A-Za-z_$][A-Za-z0-9_$]*)"""")
	return sourceSets.main.get().allJava.asFileTree.files
		.filter { it.extension == "java" }
		.flatMap { file -> call.findAll(file.readText()).map { it.groupValues[1] } }
		.toSet()
}

fun readObfMapping(file: File): Map<String, String> {
	val renames = LinkedHashMap<String, String>()
	file.forEachLine { line ->

		if (line.isNotEmpty() && !line[0].isWhitespace() && line.endsWith(":")) {
			val arrow = line.indexOf(" -> ")
			if (arrow > 0) {
				renames[line.substring(0, arrow)] = line.substring(arrow + 4, line.length - 1)
			}
		}
	}
	return renames
}

fun obfPhysicalPath(logical: String): String {
	val digest = MessageDigest.getInstance("SHA-256")
	digest.update(obfSalt.toByteArray(Charsets.UTF_8))
	val hex = digest.digest(logical.toByteArray(Charsets.UTF_8))
		.joinToString("") { "%02x".format(it) }
	return "/$obfPayloadRoot/${hex.substring(0, 2)}/${hex.substring(2, 14)}"
}

fun obfFold(bytes: ByteArray, seed: Int) {
	for (i in bytes.indices) {
		bytes[i] = (bytes[i].toInt() xor obfKey[(i + seed) % obfKey.size].toInt()).toByte()
	}
}

fun obfSeed(physical: String): Int {
	var hash = 0
	for (c in physical) {
		hash = hash * 31 + c.code
	}
	return Math.floorMod(hash, obfKey.size)
}

fun obfMixinEntry(entry: String, renames: Map<String, String>): String {
	val pkg = obfMixinPackages.first()
	val old = "$pkg.$entry"
	val new = renames[old]
		?: error("ProGuard produced no mapping for mixin '$old'. The mixin config would " +
			"point at a class that is not in the jar, which is a startup failure.")
	check(new.startsWith("$pkg.")) {
		"ProGuard moved mixin '$old' to '$new', out of the package the mixin config names. " +
			"-keeppackagenames did not hold; the config cannot address it relatively."
	}
	return new.removePrefix("$pkg.")
}

fun writeObfuscatedJar(
	source: File,
	obfuscatedClasses: File,
	mapping: File,
	destination: File,
	keepAgent: Boolean,
): String {
	val renames = readObfMapping(mapping)
	val texturesRoot = "assets/betteroplenty/textures/"
	val soundsRoot = "assets/betteroplenty/sounds/"

	val packed = LinkedHashMap<String, String>()

	val soundNames = LinkedHashMap<String, String>()

	ZipFile(source).use { zip ->
		zip.entries().asSequence()
			.filter { !it.isDirectory }
			.map { it.name }
			.sorted()
			.forEach { name ->
				when {
					name.startsWith(texturesRoot) &&
						(name.endsWith(".png") || name.endsWith(".png.mcmeta")) -> {
						packed[name] = "/$name"
					}
					name.startsWith(soundsRoot) && name.endsWith(".ogg") -> {

						val renamed = obfPhysicalPath("/$name").substringAfterLast('/') + ".ogg"
						soundNames[name.removePrefix(soundsRoot)] = renamed
						packed[name] = "/$soundsRoot$renamed"
					}
				}
			}
	}

	val physicalOf = packed.values.associateWith { obfPhysicalPath(it) }
	check(physicalOf.values.toSet().size == physicalOf.size) {
		"Two resources hashed to the same packed name; widen the slice in obfPhysicalPath."
	}

	val manifestText = physicalOf.entries
		.sortedBy { it.key }
		.joinToString("\n") { "${it.key}|${it.value}" }
	val manifestBytes = ByteArrayOutputStream().also { sink ->
		DeflaterOutputStream(sink).use { it.write(manifestText.toByteArray(Charsets.UTF_8)) }
	}.toByteArray()
	obfFold(manifestBytes, 0)

	destination.parentFile.mkdirs()
	val written = HashSet<String>()

	ZipOutputStream(destination.outputStream().buffered()).use { out ->
		fun put(name: String, bytes: ByteArray) {
			if (!written.add(name)) return

			out.putNextEntry(ZipEntry(name).apply { time = 315532800000L })
			out.write(bytes)
			out.closeEntry()
		}
		fun putDirectory(name: String) {
			if (!written.add(name)) return
			out.putNextEntry(ZipEntry(name).apply { time = 315532800000L })
			out.closeEntry()
		}

		ZipFile(obfuscatedClasses).use { zip ->
			zip.entries().asSequence().filter { !it.isDirectory }.sortedBy { it.name }.forEach {
				put(it.name, zip.getInputStream(it).readBytes())
			}
		}

		ZipFile(source).use { zip ->
			zip.entries().asSequence().sortedBy { it.name }.forEach { entry ->
				val name = entry.name
				if (entry.isDirectory) {

					if (name.startsWith("assets/") || name.startsWith("META-INF/")) {
						putDirectory(name)
					}
					return@forEach
				}
				if (name.endsWith(".class") || name in packed) {
					return@forEach
				}
				val bytes = zip.getInputStream(entry).readBytes()
				when (name) {
					"fabric.mod.json" -> put(name, rewriteModJson(bytes, renames))
					"betteroplenty.mixins.json" ->
						put(name, rewriteMixinConfig(bytes, renames, keepAgent))
					"${soundsRoot}sounds.json" -> put(name, rewriteSoundsJson(bytes, soundNames))
					else -> put(name, bytes)
				}
			}

			packed.forEach { (entryName, logical) ->
				val physical = physicalOf.getValue(logical).removePrefix("/")
				val bytes = zip.getInputStream(zip.getEntry(entryName)).readBytes()
				obfFold(bytes, obfSeed(physicalOf.getValue(logical)))
				putDirectory(physical.substringBeforeLast('/') + "/")
				put(physical, bytes)
			}
		}

		put(obfManifestEntry, manifestBytes)
	}

	verifyObfuscatedJar(destination, source, keepAgent)

	return "Obfuscated jar: ${destination.name} " +
		"(${renames.size} classes renamed, ${packed.size} resources packed, verified)"
}

fun verifyObfuscatedJar(jar: File, source: File, keepAgent: Boolean) {
	val problems = mutableListOf<String>()

	fun packable(name: String) =
		(name.startsWith("assets/betteroplenty/textures/") &&
			(name.endsWith(".png") || name.endsWith(".png.mcmeta"))) ||
			(name.startsWith("assets/betteroplenty/sounds/") && name.endsWith(".ogg"))

	ZipFile(jar).use { zip ->
		val names = zip.entries().asSequence().map { it.name }.toSet()
		fun bytes(name: String) = zip.getInputStream(zip.getEntry(name)).readBytes()

		val folded = bytes(obfManifestEntry)
		obfFold(folded, 0)
		val manifest = InflaterInputStream(folded.inputStream()).readBytes()
			.toString(Charsets.UTF_8)
			.lineSequence()
			.filter { it.contains('|') }
			.associate { it.substringBefore('|') to it.substringAfter('|') }
		check(manifest.isNotEmpty()) { "$obfManifestEntry decoded to nothing." }

		manifest.forEach { (logical, physical) ->
			val entry = physical.removePrefix("/")
			if (entry !in names) {
				problems += "$logical maps to $physical, which is not in the jar"
				return@forEach
			}
			val payload = bytes(entry)

			if (logical.endsWith(".png") && payload.take(4) == listOf<Byte>(-119, 80, 78, 71)) {
				problems += "$logical was written unfolded"
			}
			obfFold(payload, obfSeed(physical))
			val magic = when {
				logical.endsWith(".png") -> payload.take(4) == listOf<Byte>(-119, 80, 78, 71)
				logical.endsWith(".ogg") -> payload.take(4).toByteArray()
					.toString(Charsets.US_ASCII) == "OggS"
				else -> true
			}
			if (!magic) {
				problems += "$logical does not unfold to its declared format -- it was packed " +
					"from something that is not the file its name claims"
			}
		}

		ZipFile(source).use { original ->
			val expected = original.entries().asSequence()
				.map { it.name }
				.filter { packable(it) && !it.endsWith(".ogg") }
				.map { "/$it" }
				.toSet()
			val listed = manifest.keys.filterNot { it.endsWith(".ogg") }.toSet()
			(expected - listed).forEach { problems += "texture not packed: $it" }
			(listed - expected).forEach { problems += "manifest names a texture that is not in the normal jar: $it" }
		}

		names.filter { packable(it) }.forEach { problems += "plaintext asset shipped: $it" }

		@Suppress("UNCHECKED_CAST")
		val mod = JsonSlurper().parseText(bytes("fabric.mod.json").toString(Charsets.UTF_8))
			as Map<String, Any>
		((mod["entrypoints"] as? Map<*, *>).orEmpty()).values
			.filterIsInstance<List<*>>().flatten().filterIsInstance<String>()
			.forEach {
				if (it.replace('.', '/') + ".class" !in names) {
					problems += "fabric.mod.json entrypoint $it is not in the jar"
				}
			}

		@Suppress("UNCHECKED_CAST")
		val mixins = JsonSlurper().parseText(
			bytes("betteroplenty.mixins.json").toString(Charsets.UTF_8)) as Map<String, Any>
		val pkg = mixins["package"] as String
		listOf("mixins", "client", "server")
			.flatMap { (mixins[it] as? List<*>).orEmpty() }
			.filterIsInstance<String>()
			.forEach {
				if ("$pkg.$it".replace('.', '/') + ".class" !in names) {
					problems += "mixin config lists $it, which is not in the jar"
				}
			}

		if (!keepAgent) {
			names.filter { it.contains("agent", ignoreCase = true) }
				.forEach { problems += "agent-named entry shipped: $it" }
			names.filter { it.endsWith(".class") }.forEach { name ->
				val body = bytes(name).toString(Charsets.ISO_8859_1)
				if (body.contains("betteroplenty.agent") || body.contains("AgentController")) {
					problems += "$name still references agent mode"
				}
			}
		}
	}

	check(problems.isEmpty()) {
		"${jar.name} failed verification:\n" + problems.take(20).joinToString("\n") { "  - $it" } +
			if (problems.size > 20) "\n  ... and ${problems.size - 20} more" else ""
	}
}

fun rewriteModJson(bytes: ByteArray, renames: Map<String, String>): ByteArray {
	var text = String(bytes, Charsets.UTF_8)
	val json = JsonSlurper().parseText(text) as Map<*, *>
	val entrypoints = json["entrypoints"] as? Map<*, *> ?: emptyMap<String, Any>()
	entrypoints.values.filterIsInstance<List<*>>().flatten().filterIsInstance<String>().forEach {
		val new = renames[it] ?: error("fabric.mod.json names '$it', which ProGuard did not map.")

		text = text.replace("\"$it\"", "\"$new\"")
	}
	return text.toByteArray(Charsets.UTF_8)
}

fun rewriteMixinConfig(
	bytes: ByteArray,
	renames: Map<String, String>,
	keepAgent: Boolean,
): ByteArray {
	@Suppress("UNCHECKED_CAST")
	val json = JsonSlurper().parseText(String(bytes, Charsets.UTF_8))
		as MutableMap<String, Any>
	val dropped = if (keepAgent) emptyList() else obfExcludedMixins
	listOf("mixins", "client", "server").forEach { side ->
		val entries = json[side] as? List<*> ?: return@forEach
		json[side] = entries.filterIsInstance<String>()
			.filterNot { it in dropped }
			.map { obfMixinEntry(it, renames) }
	}
	return JsonOutput.prettyPrint(JsonOutput.toJson(json))
		.toByteArray(Charsets.UTF_8)
}

fun rewriteSoundsJson(bytes: ByteArray, soundNames: Map<String, String>): ByteArray {
	@Suppress("UNCHECKED_CAST")
	val json = JsonSlurper().parseText(String(bytes, Charsets.UTF_8))
		as MutableMap<String, Any>
	json.values.filterIsInstance<MutableMap<String, Any>>().forEach { event ->
		(event["sounds"] as? List<*>)?.filterIsInstance<MutableMap<String, Any>>()?.forEach { sound ->
			val name = sound["name"] as? String ?: return@forEach
			sound["name"] = soundNames[name] ?: name
		}
	}
	return JsonOutput.prettyPrint(JsonOutput.toJson(json))
		.toByteArray(Charsets.UTF_8)
}

fun resolveLwjglNatives(): String {
	return Pair(
		System.getProperty("os.name")!!,
		System.getProperty("os.arch")!!
	).let { (name, arch) ->
		when {
			arrayOf("Linux", "SunOS", "Unit").any { name.startsWith(it) } ->
				if (arrayOf("arm", "aarch64").any { arch.startsWith(it) })
					"natives-linux${if (arch.contains("64") || arch.startsWith("armv8")) "-arm64" else "-arm32"}"
				else
					"natives-linux"
			arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) } ->
				"natives-macos${if (arch.startsWith("aarch64")) "-arm64" else ""}"
			arrayOf("Windows").any { name.startsWith(it) } ->
				if (arch.contains("64"))
					"natives-windows${if (arch.startsWith("aarch64")) "-arm64" else ""}"
				else
					"natives-windows-x86"
			else ->
				throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
		}
	}
}
