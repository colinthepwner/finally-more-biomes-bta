val modName: Provider<String> = providers.gradleProperty("mod_name")
rootProject.name = modName.get()
pluginManagement {
	fun isRepoHealthy(url: String): Boolean {
		var connection: javax.net.ssl.HttpsURLConnection? = null
		return try {
			connection = java.net.URI(url).toURL().openConnection() as javax.net.ssl.HttpsURLConnection
			connection.requestMethod = "HEAD"
			connection.connectTimeout = 2000
			connection.readTimeout = 2000
			connection.instanceFollowRedirects = true
			connection.connect()
			val code = connection.responseCode
			code in 200..399
		} catch (_: Exception) {
			false
		} finally {
			connection?.disconnect()
		}
	}
	fun repoUrlWithFallbacks(candidates: List<String>): String {
		if (candidates.isEmpty()) {
			val badLink = "https://mock.httpstatus.io/500"
			logger.error("No repositories have been provided. Defaulting to: {}", badLink)
			return badLink
		}
		val chosenRepository = candidates.firstOrNull { isRepoHealthy(it) } ?: run {
			if (candidates.size == 1) {
				logger.error("\"{}\" could not be resolved.", candidates.first())
			} else {
				logger.error("All {} repositories could not be resolved. Defaulting to: {}", candidates.size, candidates.first())
			}
			return candidates.first()
		}
		logger.lifecycle("Using \"{}\" as the Fabric repository.", chosenRepository)
		return chosenRepository
	}
	repositories {
		// The two general-purpose repositories go FIRST, and that ordering is load-bearing rather
		// than tidiness.
		//
		// Gradle stops at the first repository that answers, but a repository that answers with a
		// 5xx aborts plugin resolution outright instead of falling through to the next one. So when
		// maven.thesignalumproject.net went down and started returning HTTP 522, resolution of
		// `foojay-resolver-convention` -- a plugin that lives on the Gradle plugin portal and has
		// nothing to do with any mod repository -- failed on every clean checkout, while a warm
		// cache locally hid it completely.
		//
		// With the portal first, the plugins this block resolves are found before an unhealthy mod
		// repository is ever consulted. It does not make a mod repository being down harmless -- the
		// dependencies in build.gradle.kts genuinely live there -- but it stops an outage on one
		// host from breaking resolution of things that host never had.
		gradlePluginPortal()
		mavenCentral()
		maven(
			repoUrlWithFallbacks(
				listOf(
					"https://maven.fabricmc.net",
					"https://maven2.fabricmc.net",
					"https://maven3.fabricmc.net"
				)
			)
		) { name = "Fabric" }
		maven("https://maven.thesignalumproject.net/infrastructure") { name = "SignalumMavenInfrastructure" }
	}
	val foojayResolverVersion = providers.gradleProperty("foojay_resolver_version")
	plugins {
		id("org.gradle.toolchains.foojay-resolver-convention").version(foojayResolverVersion.get())
	}
}
plugins {
	id("org.gradle.toolchains.foojay-resolver-convention")
}
