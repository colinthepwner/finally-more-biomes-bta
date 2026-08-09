// `java` in a build script is the JavaPluginExtension, so java.io/java.util have
// to be imported rather than written out; without these the obfuscation helpers
// below resolve `java.util.zip` against the extension and fail to compile.
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
/**
 * HalpLibe, to be nested inside the built jar so the mod runs without a separate download.
 *
 * Why not loom's `include()`: it nests during **`remapJar`**, and the obfuscated jar -- the one
 * that actually gets handed out -- is assembled from the plain **`jar`** task instead (see
 * `registerObfuscatedJar`, which reads `tasks.named<Jar>("jar")`). So `include()` would put
 * HalpLibe into an artifact nobody ships and leave the distributed one exactly as broken as
 * before. Nesting into `jar` directly is what puts it where it is needed.
 *
 * The obfuscation pipeline carries it through untouched: `writeObfuscatedJar` copies every
 * non-class, non-packed resource byte for byte and explicitly preserves `META-INF/` directories,
 * and `rewriteModJson` edits the manifest **textually**, keeping "every field this build does not
 * know about" -- which is what lets the `jars` key survive obfuscation.
 *
 * `isTransitive = false` so this is HalpLibe alone. Its own dependencies are the loader and the
 * game, both already present at runtime; pulling its whole graph in would nest jars the game has.
 *
 * Legally clear: HalpLibe is CC0-1.0, per the `license` field in its own fabric.mod.json.
 */
val bundledHalpLibe: Configuration by configurations.creating {
	isTransitive = false
}

dependencies {
    minecraft("::${libs.versions.bta.get()}")

	// Required at compilation & runtime
	// included in builds as a runtime dependency
	implementation(libs.loader)
	implementation(libs.halplibe) // If you do not need halplibe you can delete this line

	// Only required at compilation
	// provides documentation, can be removed if that isn't needed
	compileOnly(libs.bundles.btaLwjgl)
	compileOnly(libs.joml)
	compileOnly(libs.joml.primitives)
	compileOnly(libs.slf4jApi)

	// Only required for development/launch at runtime, won't be part of any builds
	localRuntime(libs.modMenu) // Optional, can be removed
	runtimeClasspath(libs.clientJar)
	val lwjglVer = libs.versions.lwjgl.get()
	localRuntime(platform("org.lwjgl:lwjgl-bom:${lwjglVer}"))
	localRuntime("org.lwjgl:lwjgl::$lwjglNatives")
	localRuntime("org.lwjgl:lwjgl-glfw::$lwjglNatives")
	localRuntime("org.lwjgl:lwjgl-openal::$lwjglNatives")
	localRuntime("org.lwjgl:lwjgl-opengl::$lwjglNatives")
	localRuntime("org.lwjgl:lwjgl-stb::$lwjglNatives")

	// HalpLibe, resolved on its own so it can be nested inside the jar. See `bundledHalpLibe`.
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
		// This is needed for gradle to recognize changes
		// made to expanded files
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
	/**
	 * Nests HalpLibe inside the jar, and declares it there.
	 *
	 * The `jars` key is added HERE rather than in `src/main/resources/fabric.mod.json`, and that
	 * placement is the whole point: a checked-in `jars` entry would also land in
	 * `build/resources/main`, which is what the development client loads the mod from -- and there
	 * is no `META-INF/jars/` in a directory-based mod, so every `runClient`, `runServer` and agent
	 * client would be pointed at a nested jar that does not exist. Injecting it into the archive
	 * only means the shipped jar carries HalpLibe while the dev run keeps resolving it off the
	 * classpath exactly as before.
	 *
	 * Textual, because `writeObfuscatedJar` rewrites this same file textually to preserve fields it
	 * does not model; parsing and re-emitting here would reformat the file out from under it.
	 */
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
// Removes all outdated manifest.json dependencies
configurations.configureEach {
	exclude(group = "org.lwjgl.lwjgl")
	exclude(group = "net.java.jutils")
	exclude(group = "net.java.jinput")
	exclude(group = "net.sf.jopt-simple")
	exclude(group = "net.minecraft", module = "launchwrapper")
}

// =============================================================================
//  The obfuscated build
//
//  `gradlew build` writes two jars. The normal one is unchanged. Beside it goes
//  `-obf.jar`: the same mod with its class and member names replaced, its
//  textures and sounds renamed and their bytes folded, and agent mode absent.
//
//  Three things make that possible without touching a line of mod logic:
//
//    * ProGuard renames the code. It runs with -dontshrink -dontoptimize, so it
//      is a renamer and nothing else -- no method is inlined, no branch moved,
//      no class dropped. What ships is the same bytecode under other names.
//    * The names that live outside bytecode -- fabric.mod.json's entrypoints and
//      the mixin config's class list -- are rewritten from ProGuard's own
//      mapping file, so they cannot drift from what it produced.
//    * Resources are renamed here and resolved at runtime by ObfResources. See
//      that class for why the reverse (rewriting the paths in the code) is not
//      possible for this mod.
//
//  Agent mode is removed at the ProGuard input filter, which is the whole of it:
//  com.betteroplenty.agent is reachable from exactly one class, the mixin that
//  calls into it, and that goes out in the same filter.
// =============================================================================

/**
 * Fixed, so two builds of the same source produce byte-identical jars. This is
 * not a secret and is not used as one -- it only decides which hashed name a
 * given texture lands on.
 */
val obfSalt = "betteroplenty/pack/v1"

/**
 * Folds every packed resource, and the manifest that lists them.
 *
 * Read out of `ObfResources.java` rather than written here as well, because the
 * packer and the class that unpacks have to agree byte for byte and there is no
 * check that can be run on the jar to prove they do -- both sides of any such
 * check would be this build. Two copies of the same constant is exactly the shape
 * of a bug that ships: the build succeeds, verification passes, and every texture
 * in the mod renders as noise on the first launch. So there is one copy, and the
 * class that has to have it holds it.
 */
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

/** Where the packed bytes live, and where the manifest that maps to them lives. */
val obfPayloadRoot = "assets/betteroplenty/x"
val obfManifestEntry = "betteroplenty.dat"

/** The mixin config's `package`; ProGuard is told to leave these two intact. */
val obfMixinPackages = listOf("com.betteroplenty.mixin", "com.betteroplenty.mixin.client")

/**
 * Dropped from the obfuscated jar. The mixin has to go with the package: it is
 * the only thing outside it that names AgentMode, and a mixin config entry that
 * points at an absent class is a hard startup failure, not a warning.
 */
val obfExcludedClasses = listOf(
	"com/betteroplenty/agent/**",
	"com/betteroplenty/mixin/client/MinecraftAgentMixin.class",
)

/** Mixin config entries that go with them, by their name in the config's list. */
val obfExcludedMixins = listOf("client.MinecraftAgentMixin")

val proguardTool: Configuration by configurations.creating {
	isCanBeConsumed = false
	isCanBeResolved = true
}

dependencies {
	proguardTool("com.guardsquare:proguard-base:7.7.0")
}

val obfWorkDir: Provider<Directory> = layout.buildDirectory.dir("obfuscation")

/** The jar that ships. */
val obfJarFile: Provider<RegularFile> =
	layout.buildDirectory.file("libs/${modName.get()}-$modVersion-obf.jar")

/**
 * The same jar with agent mode left in, for `runObfClient` to drive.
 *
 * Not in `libs/`, and not built by `build` -- it exists so that the obfuscation
 * can be tested by playing it. Testing the shipped jar directly is not possible:
 * driving the client is what agent mode is, and the shipped jar is defined by not
 * having it. Everything else about the two is produced by the same code below
 * from the same input jar, so what this proves about ProGuard's renaming and the
 * resource packing holds for both; `verifyObfuscatedJar` checks that the only
 * difference is the one intended.
 */
val obfAgentJarFile: Provider<RegularFile> =
	obfWorkDir.map { it.file("${modName.get()}-$modVersion-obf-agent.jar") }

/**
 * Registers the two-task pipeline -- ProGuard, then packaging -- for one variant.
 *
 * ProGuard's input is filtered to `**.class`: resources are not passed through
 * it at all. Its resource handling would have to be told file by file which
 * names to adapt and which to leave alone, and the packaging step rewrites most
 * of them anyway.
 */
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
		// compileClasspath carries BTA, the loader (and with it Mixin), HalpLibe and
		// the LWJGL/JOML/slf4j stubs. ProGuard needs the class hierarchy of all of
		// them to know which of our methods override a library method and therefore
		// cannot be renamed -- onInitialize() being the one that matters most.
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

			// Java 9+ has no rt.jar; the platform classes come from the toolchain's
			// jmods. java.desktop is not optional here -- BufferedImage and ImageIO
			// are in the signature of everything the atlas touches.
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
			// A renamer, not an optimiser. Shrinking would decide for itself that a
			// biome only the range map ever names is unused; optimising would move
			// code a mixin injects into. Neither risk buys anything here -- the ask
			// is unreadable names, and renaming alone delivers exactly that.
			lines += "-dontshrink"
			lines += "-dontoptimize"
			lines += "-dontnote"
			// Mixin reads its own annotations off these classes at load time, and
			// InnerClasses is what keeps CallbackInfoReturnable's nesting resolvable.
			// SourceFile and LineNumberTable are deliberately absent: dropping them
			// is part of the point, at the cost of line numbers in stack traces.
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

			// There is deliberately no -dontwarn here. Against the classpath assembled
			// above ProGuard resolves every reference in the mod and reports nothing,
			// so a warning appearing later is real news: a library that stopped being
			// on compileClasspath, and with it a class hierarchy ProGuard can no longer
			// see -- which is how an override it should have left alone gets renamed.
			// ProGuard refuses to write an output jar while any warning stands, so that
			// case fails the build rather than shipping quietly.

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

// -----------------------------------------------------------------------------
//  Running the obfuscated jar
//
//  `runClient` launches the mod from build/classes/java/main -- the loose,
//  un-obfuscated classes -- so it can say nothing about whether the jar works.
//  This run configuration is identical to it except that those two directories
//  are off the classpath and the jar is on it, which is also how the loader then
//  discovers the mod: through the fabric.mod.json inside the jar, entrypoints,
//  mixin config, packed resources and all.
//
//  It gets its own run directory. The obfuscated build has its own id manifest
//  behaviour to prove and its own world to prove it in, and nothing here should
//  be able to touch the world in run/.
// -----------------------------------------------------------------------------
loom {
	runs {
		create("obfClient") {
			client()
			configName = "Finally More Biomes (obfuscated jar)"
			runDir = "run-obf"
			ideConfigGenerated(false)
		}
	}
}

tasks.named<JavaExec>("runObfClient") {
	description = "Runs the obfuscated jar itself, rather than the loose dev classes."

	// The agent variant, because this is the configuration that has to be driven
	// to be worth anything. -PobfJar=<path> overrides it to run any other build,
	// which is how the shipped jar gets its own launch.
	dependsOn(obfuscatedAgentJar)
}

// Swapping the classpath has to happen in `afterEvaluate`, not in the task's own `doFirst`.
//
// It used to be a `doFirst`, and that stopped working: Gradle finalizes `JavaExec.classpath`
// before the task executes, so assigning to it there now fails outright with "the value for
// property 'internalClasspath' is final and cannot be changed any further". The task simply could
// not run, which is how the obfuscated jar went unexercised.
//
// `afterEvaluate` is the window that works. Loom has populated the run configuration's classpath by
// then, and nothing has been finalized yet. It costs eager resolution of the classpath at
// configuration time, which for a dev-only task nobody runs by accident is a fair price.
afterEvaluate {
	tasks.named<JavaExec>("runObfClient") {
		// The agent variant by default, because this is the configuration that has to be driven to
		// be worth anything. -PobfJar=<path> overrides it to run any other build, which is how the
		// shipped jar gets its own launch.
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
}

/**
 * Every member name the mod passes to a reflective lookup.
 *
 * Matches the literal argument of `getMethod`, `getDeclaredMethod`, `getField` and
 * `getDeclaredField` across the main source set. Names belonging to BTA rather
 * than to the mod (`Dimension.portalBlock`) come back too and are harmless:
 * ProGuard does not rename library members, so keeping their names is a no-op.
 *
 * A lookup built from a non-literal string would be missed. Nothing in the mod
 * does that today; if one is ever added, it fails the same loud way -- a
 * NoSuchMethodException on the first launch of the obfuscated build -- rather
 * than corrupting anything.
 */
fun scrapeReflectedMembers(): Set<String> {
	val call = Regex("""get(?:Declared)?(?:Method|Field)\s*\(\s*"([A-Za-z_$][A-Za-z0-9_$]*)"""")
	return sourceSets.main.get().allJava.asFileTree.files
		.filter { it.extension == "java" }
		.flatMap { file -> call.findAll(file.readText()).map { it.groupValues[1] } }
		.toSet()
}

/** ProGuard's mapping file, as `old fully-qualified name -> new one`. */
fun readObfMapping(file: File): Map<String, String> {
	val renames = LinkedHashMap<String, String>()
	file.forEachLine { line ->
		// Class lines start at column 0 and end in a colon; member lines are indented.
		if (line.isNotEmpty() && !line[0].isWhitespace() && line.endsWith(":")) {
			val arrow = line.indexOf(" -> ")
			if (arrow > 0) {
				renames[line.substring(0, arrow)] = line.substring(arrow + 4, line.length - 1)
			}
		}
	}
	return renames
}

/** The hashed classpath path a logical resource path is served from. */
fun obfPhysicalPath(logical: String): String {
	val digest = MessageDigest.getInstance("SHA-256")
	digest.update(obfSalt.toByteArray(Charsets.UTF_8))
	val hex = digest.digest(logical.toByteArray(Charsets.UTF_8))
		.joinToString("") { "%02x".format(it) }
	return "/$obfPayloadRoot/${hex.substring(0, 2)}/${hex.substring(2, 14)}"
}

/** XOR in place. Must stay identical to `ObfResources.fold`. */
fun obfFold(bytes: ByteArray, seed: Int) {
	for (i in bytes.indices) {
		bytes[i] = (bytes[i].toInt() xor obfKey[(i + seed) % obfKey.size].toInt()).toByte()
	}
}

/** Must stay identical to `ObfResources.seed`. */
fun obfSeed(physical: String): Int {
	var hash = 0
	for (c in physical) {
		hash = hash * 31 + c.code
	}
	return Math.floorMod(hash, obfKey.size)
}

/** A mixin config entry, relative to the config's package, after renaming. */
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

/**
 * Assembles the obfuscated jar: ProGuard's classes, the mod's resources with
 * textures and sounds renamed and folded, and the manifest tying the two back
 * together.
 */
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

	// Source jar entry -> the logical path the game will ask for it by.
	val packed = LinkedHashMap<String, String>()
	// sounds.json's "name" field, before -> after. Sounds are the one resource
	// whose logical name is data rather than a path, so it is renamed too and
	// nothing is left pointing at "records/bopdisc.ogg".
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
						// The extension has to survive: paulscode picks its codec
						// by it (SoundEngine registers "ogg" -> CodecJOrbis), and a
						// file called 9af3c2 with no suffix simply will not play.
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
			// Fixed timestamp, matching what the Jar task writes, so two builds of
			// the same source produce identical jars.
			out.putNextEntry(ZipEntry(name).apply { time = 315532800000L })
			out.write(bytes)
			out.closeEntry()
		}
		fun putDirectory(name: String) {
			if (!written.add(name)) return
			out.putNextEntry(ZipEntry(name).apply { time = 315532800000L })
			out.closeEntry()
		}

		// 1. Every class, straight from ProGuard, renamed and already stripped of
		//    the agent package by the input filter.
		ZipFile(obfuscatedClasses).use { zip ->
			zip.entries().asSequence().filter { !it.isDirectory }.sortedBy { it.name }.forEach {
				put(it.name, zip.getInputStream(it).readBytes())
			}
		}

		// 2. Resources. Packed ones are renamed and folded; three name-carrying
		//    files are rewritten; the rest are copied byte for byte.
		ZipFile(source).use { zip ->
			zip.entries().asSequence().sortedBy { it.name }.forEach { entry ->
				val name = entry.name
				if (entry.isDirectory) {
					// Only asset directories. The class package directories would
					// spell out the original package tree beside the renamed one.
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

			// 3. The packed payloads, under their hashed names.
			packed.forEach { (entryName, logical) ->
				val physical = physicalOf.getValue(logical).removePrefix("/")
				val bytes = zip.getInputStream(zip.getEntry(entryName)).readBytes()
				obfFold(bytes, obfSeed(physicalOf.getValue(logical)))
				putDirectory(physical.substringBeforeLast('/') + "/")
				put(physical, bytes)
			}
		}

		// 4. The map from logical to hashed, without which none of the above
		//    can be found.
		put(obfManifestEntry, manifestBytes)
	}

	verifyObfuscatedJar(destination, source, keepAgent)

	return "Obfuscated jar: ${destination.name} " +
		"(${renames.size} classes renamed, ${packed.size} resources packed, verified)"
}

/**
 * Reads the finished jar back and fails the build if it is not coherent.
 *
 * Every check here is for a break that produces a jar which looks perfectly
 * well-formed and then misbehaves at runtime, mostly by rendering magenta or by
 * refusing to start:
 *
 *  * a manifest entry whose payload is not in the jar, or a texture in the normal
 *    jar with no manifest entry -- either way that texture is gone;
 *  * a payload that does not unfold to the format its name claims, i.e. a file
 *    that was packed from the wrong source or folded twice;
 *  * an entrypoint or mixin class named in JSON that ProGuard renamed out from
 *    under it, which is a hard startup failure;
 *  * a plaintext asset or an agent class that should not have shipped.
 */
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
			// If it is already readable it was never folded, and the jar is not
			// obfuscated at all.
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

/** Points fabric.mod.json's entrypoints at the names ProGuard gave those classes. */
fun rewriteModJson(bytes: ByteArray, renames: Map<String, String>): ByteArray {
	var text = String(bytes, Charsets.UTF_8)
	val json = JsonSlurper().parseText(text) as Map<*, *>
	val entrypoints = json["entrypoints"] as? Map<*, *> ?: emptyMap<String, Any>()
	entrypoints.values.filterIsInstance<List<*>>().flatten().filterIsInstance<String>().forEach {
		val new = renames[it] ?: error("fabric.mod.json names '$it', which ProGuard did not map.")
		// Textual, so the file keeps its formatting and every field this build
		// does not know about. The names are fully qualified and unique.
		text = text.replace("\"$it\"", "\"$new\"")
	}
	return text.toByteArray(Charsets.UTF_8)
}

/** Renames the mixin classes in the config, and drops the ones left out of this jar. */
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

/**
 * Points each sound event at the renamed .ogg beside it -- where there is one.
 *
 * A name with no .ogg in the jar used to be an error, on the reasonable assumption that
 * sounds.json only ever named audio the jar shipped. That stopped being true when BOP's audio
 * moved out to the runtime asset bridge: all eleven files now come from a copy the player
 * supplies, and none of them is in the jar to rename.
 *
 * Such a name is left exactly as it is, which is what the bridge needs. The generated pack is a
 * TexturePackCustom and serves the logical path directly -- obfuscation never reaches it, by the
 * same deliberate design that lets a player's own texture pack retexture an obfuscated build (see
 * TexturePackPackedMixin). Renaming the reference here would point it at a hashed name that
 * exists in neither the jar nor the pack.
 */
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

fun resolveLwjglNatives(): String { // Sourced from https://www.lwjgl.org/
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
