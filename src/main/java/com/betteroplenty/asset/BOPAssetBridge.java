package com.betteroplenty.asset;

import com.betteroplenty.BetterOPlenty;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.texturepack.TexturePack;
import net.minecraft.client.render.texturepack.TexturePackList;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public final class BOPAssetBridge {
	private BOPAssetBridge() {}

	public static final String PACK_NAME = "FinallyMoreBiomesAssets";
	private static final String MANIFEST = "/assets/betteroplenty/asset-bridge.properties";

	private static final String HASHES = "/assets/betteroplenty/asset-bridge-hashes.properties";

	private static final String SOUNDS_JSON = "/assets/betteroplenty/sounds/sounds.json";
	private static final String SOUNDS_JSON_IN_PACK = "assets/betteroplenty/sounds/sounds.json";

	private static final String STAMP = "bridge-source.txt";

	private static final int BRIDGE_REVISION = 2;

	private static final int MAX_NESTING = 3;

	public static int bridgedCount = -1;
	public static int missingCount = -1;
	public static String sourceArchive = null;
	public static boolean packAutoEnabled = false;
	public static boolean usedCache = false;

	public static File run(File gameDir) {
		if (gameDir == null) return null;

		Map<String, List<String>> manifest = readManifest();
		if (manifest.isEmpty()) {
			BetterOPlenty.LOGGER.warn("Asset bridge: manifest {} is empty or missing, skipping", MANIFEST);
			return null;
		}

		File packDir = new File(gameDir, "texturepacks/" + PACK_NAME);

		Stamp previous = Stamp.read(packDir);
		if (previous != null && previous.stillValid(gameDir)) {
			usedCache = true;
			sourceArchive = previous.label;
			BetterOPlenty.LOGGER.info("Asset bridge: '{}' is already built from {}, skipping extraction",
				PACK_NAME, previous.label);
			return packDir;
		}

		Set<String> wanted = new LinkedHashSet<>(manifest.keySet());

		List<Source> sources = findSources(gameDir, packDir, wanted);
		if (sources.isEmpty()) {
			reportNothingFound(gameDir, manifest.size());
			return null;
		}

		List<Source> used = new ArrayList<>();
		Map<String, byte[]> entries = collect(sources, wanted, readHashes(), used);
		if (entries.isEmpty()) {
			reportNothingFound(gameDir, manifest.size());
			return null;
		}
		sourceArchive = describe(gameDir, used);

		int expected = 0;
		for (List<String> destinations : manifest.values()) expected += destinations.size();

		List<String> missing = new ArrayList<>();
		int written = 0;
		try {
			for (Map.Entry<String, List<String>> mapping : manifest.entrySet()) {
				byte[] bytes = entries.get(mapping.getKey());
				if (bytes == null) {
					missing.add(mapping.getKey());
					continue;
				}

				for (String path : mapping.getValue()) {
					write(new File(packDir, path), bytes);
					written++;
				}
			}
			copySoundsManifest(packDir);
			writePackMeta(packDir);
		} catch (IOException e) {
			BetterOPlenty.LOGGER.warn("Asset bridge: could not write into '{}': {}", packDir.getPath(), e.toString());
			return null;
		}

		bridgedCount = written;
		missingCount = missing.size();

		BetterOPlenty.LOGGER.info("Asset bridge: {} of {} files bridged from {} into texture pack '{}'",
			written, expected, sourceArchive, PACK_NAME);
		if (used.size() > 1) {
			BetterOPlenty.LOGGER.info("Asset bridge: sources used, best first: {}", paths(gameDir, used));
		}
		if (!missing.isEmpty()) {

			BetterOPlenty.LOGGER.warn("Asset bridge: {} file(s) not found in any source. Those textures will "
				+ "render as the missing-texture checker. Missing: {}", missing.size(), summarise(missing));
		}

		new Stamp(sourceArchive, used, gameDir).write(packDir);
		return packDir;
	}

	public static void enablePack(TexturePackList packs, File packDir) {
		if (packs == null || packDir == null || !packDir.isDirectory()) return;
		try {
			packs.updateAvailableTexturePacks();
			for (TexturePack pack : packs.availableTexturePacks()) {
				if (!PACK_NAME.equals(pack.fileName)) continue;
				if (packs.selectedPacks.contains(pack)) {
					packAutoEnabled = true;
					return;
				}

				packs.setTexturePack(pack);
				packAutoEnabled = true;
				BetterOPlenty.LOGGER.info("Asset bridge: texture pack '{}' enabled automatically", PACK_NAME);
				return;
			}
			BetterOPlenty.LOGGER.info("Asset bridge: texture pack '{}' was written but BTA did not list it; "
				+ "enable it in Options to use it", PACK_NAME);
		} catch (Throwable t) {
			BetterOPlenty.LOGGER.warn("Asset bridge: could not enable texture pack '{}' automatically ({}); "
				+ "enable it in Options to use it", PACK_NAME, t.toString());
		}
	}

	public static void logSummary() {
		if (bridgedCount < 0 && !usedCache) {
			BetterOPlenty.LOGGER.warn("Asset bridge: no BOP art is loaded. The mod's own art still renders; "
				+ "everything from BOP will show as the missing-texture checker until a copy of BOP is "
				+ "dropped into the game directory. See the README.");
		}
	}

	private static void reportNothingFound(File gameDir, int manifestSize) {
		bridgedCount = 0;
		missingCount = manifestSize;
		BetterOPlenty.LOGGER.info("Asset bridge: no Biomes O' Plenty files found anywhere under '{}'. "
			+ "Drop your own copy of BOP in (zip, jar or unpacked folder, any name, any depth) to give the "
			+ "blocks their art. This mod does not ship BOP's textures and never downloads them.",
			gameDir.getPath());
	}

	private static String summarise(List<String> missing) {
		int shown = Math.min(missing.size(), MAX_PATHS_LOGGED);
		String head = String.join(", ", missing.subList(0, shown));
		return missing.size() > shown ? head + ", and " + (missing.size() - shown) + " more" : head;
	}

	private static final class Source implements Comparable<Source> {
		final File file;

		final boolean container;

		final String name;
		final int rank;
		final int depth;

		Source(File file, boolean container, String name, int rank, int depth) {
			this.file = file;
			this.container = container;
			this.name = name;
			this.rank = rank;
			this.depth = depth;
		}

		@Override
		public int compareTo(Source other) {
			if (rank != other.rank) return Integer.compare(rank, other.rank);
			if (depth != other.depth) return Integer.compare(depth, other.depth);
			return file.getPath().compareToIgnoreCase(other.file.getPath());
		}
	}

	private static final int RANK_EXPLICIT = 0;

	private static final int RANK_NAMED = 1;

	private static final int RANK_MODS = 2;

	private static final int RANK_ANYWHERE = 3;

	private static final int MAX_SCAN_DEPTH = 6;

	private static final int MAX_SCAN_FILES = 40000;

	private static final int MAX_CONTAINERS_READ = 400;

	private static final Set<String> SKIPPED_DIRS = new HashSet<>(Arrays.asList(
		"saves", "logs", "crash-reports", "screenshots", "stats", "assets", "libraries",
		"bin", "natives", "server-resource-packs", "backups", "shaderpacks", "packresources",
		".git", ".fabric", ".mixin.out"));

	private static List<Source> findSources(File gameDir, File packDir, Set<String> wanted) {
		List<Source> sources = new ArrayList<>();

		String rootPath = safePath(gameDir);
		String packPath = safePath(packDir);
		scan(gameDir, rootPath, packPath, wanted, sources);
		Collections.sort(sources);
		return sources;
	}

	private static void scan(File root, String rootPath, String packPath, Set<String> wanted,
		List<Source> sources) {
		List<File> current = new ArrayList<>();
		current.add(root);
		int budget = MAX_SCAN_FILES;
		int examined = 0;

		for (int depth = 0; depth <= MAX_SCAN_DEPTH && !current.isEmpty(); depth++) {
			List<File> next = new ArrayList<>();

			for (File dir : current) {
				File[] children = dir.listFiles();
				if (children == null) continue;

				for (File child : children) {
					if (budget <= 0) {
						BetterOPlenty.LOGGER.warn("Asset bridge: stopped searching after {} files, at depth {}. "
							+ "Everything shallower than that was examined. If your copy of BOP is buried deep "
							+ "in a large game directory, move it nearer the top.", examined, depth);
						return;
					}
					budget--;
					examined++;

					if (child.isDirectory()) {
						String name = child.getName().toLowerCase(Locale.ROOT);

						if (SKIPPED_DIRS.contains(name) || safePath(child).equals(packPath)) continue;
						next.add(child);
						continue;
					}
					if (!child.isFile()) continue;

					String key = looseKey(child, wanted);
					if (key != null) {
						sources.add(new Source(child, false, key, rank(rootPath, child), depth));
					} else if (!isOwnJar(child.getName()) && isZip(child)) {
						sources.add(new Source(child, true, baseName(child.getName()),
							rank(rootPath, child), depth));
					}
				}
			}
			current = next;
		}
	}

	private static String looseKey(File file, Set<String> wanted) {
		File parent = file.getParentFile();
		if (parent != null) {
			String scoped = (parent.getName() + "/" + file.getName()).toLowerCase(Locale.ROOT);
			if (wanted.contains(scoped)) return scoped;
		}
		String base = baseName(file.getName());
		return wanted.contains(base) ? base : null;
	}

	private static int rank(String rootPath, File file) {
		String relative = relativeTo(rootPath, safePath(file)).toLowerCase(Locale.ROOT);
		if (baseName(file.getName()).startsWith("betteroplenty-assets")) return RANK_EXPLICIT;

		if (relative.contains("biomesoplenty") || relative.contains("biomes-o-plenty")
			|| relative.contains("biomes o plenty") || relative.contains("biomes_o_plenty")
			|| relative.contains("bop-")) {
			return RANK_NAMED;
		}
		if (relative.startsWith("mods/")) return RANK_MODS;
		return RANK_ANYWHERE;
	}

	private static boolean isOwnJar(String name) {
		return name.toLowerCase(Locale.ROOT).contains("betteroplenty");
	}

	private static final long MIN_CONTAINER_BYTES = 4096;

	private static boolean isZip(File file) {
		if (file.length() < MIN_CONTAINER_BYTES) return false;
		try (InputStream in = new FileInputStream(file)) {
			byte[] header = new byte[4];
			int read = 0;
			while (read < 4) {
				int step = in.read(header, read, 4 - read);
				if (step < 0) return false;
				read += step;
			}
			return isZipHeader(header);
		} catch (IOException e) {
			return false;
		}
	}

	private static boolean isZipHeader(byte[] header) {
		if (header.length < 4 || header[0] != 'P' || header[1] != 'K') return false;
		int third = header[2] & 0xFF;
		int fourth = header[3] & 0xFF;
		return (third == 0x03 && fourth == 0x04)
			|| (third == 0x05 && fourth == 0x06)
			|| (third == 0x07 && fourth == 0x08);
	}

	private static String[] keysFor(String path) {
		String normalised = path.replace('\\', '/');
		int slash = normalised.lastIndexOf('/');
		String base = normalised.substring(slash + 1).toLowerCase(Locale.ROOT);
		if (slash < 0) return new String[]{base};
		int previous = normalised.lastIndexOf('/', slash - 1);
		String scoped = normalised.substring(previous + 1).toLowerCase(Locale.ROOT);
		return new String[]{scoped, base};
	}

	private static final class Harvest {
		final Map<String, byte[]> found = new HashMap<>();
		final Set<String> satisfied = new HashSet<>();
		final Map<String, String> expected;
		final int wantedCount;

		Harvest(Map<String, String> expected, int wantedCount) {
			this.expected = expected;
			this.wantedCount = wantedCount;
		}

		boolean complete() {
			return satisfied.size() >= wantedCount;
		}

		boolean wants(String key) {
			return !satisfied.contains(key);
		}

		void offer(String key, byte[] bytes) {
			String want = expected.get(key);
			boolean exact = want == null || want.equalsIgnoreCase(sha256(bytes));
			if (exact) {
				found.put(key, bytes);
				satisfied.add(key);
			} else if (!found.containsKey(key)) {
				found.put(key, bytes);
			}
		}
	}

	private static String sha256(byte[] bytes) {
		try {
			byte[] digest = java.security.MessageDigest.getInstance("SHA-256").digest(bytes);
			StringBuilder out = new StringBuilder(digest.length * 2);
			for (byte b : digest) out.append(Character.forDigit((b >> 4) & 0xF, 16)).append(Character.forDigit(b & 0xF, 16));
			return out.toString();
		} catch (java.security.NoSuchAlgorithmException impossible) {

			throw new AssertionError(impossible);
		}
	}

	private static Map<String, byte[]> collect(List<Source> sources, Set<String> wanted,
		Map<String, String> expected, List<Source> used) {
		Harvest harvest = new Harvest(expected, wanted.size());
		int containersRead = 0;

		for (Source source : sources) {
			if (harvest.complete()) break;

			int before = harvest.found.size();
			int exactBefore = harvest.satisfied.size();
			if (source.container) {
				if (containersRead >= MAX_CONTAINERS_READ) continue;
				containersRead++;
				try {
					collectFromZipFile(source.file, wanted, harvest);
				} catch (IOException e) {

					BetterOPlenty.LOGGER.debug("Asset bridge: skipped '{}': {}", source.file.getPath(), e.toString());
					continue;
				}
			} else if (harvest.wants(source.name)) {
				try {
					harvest.offer(source.name, Files.readAllBytes(source.file.toPath()));
				} catch (IOException e) {
					BetterOPlenty.LOGGER.debug("Asset bridge: skipped '{}': {}", source.file.getPath(), e.toString());
					continue;
				}
			}

			if (harvest.found.size() > before || harvest.satisfied.size() > exactBefore) used.add(source);
		}

		int drifted = harvest.found.size() - harvest.satisfied.size();
		if (drifted > 0) {
			BetterOPlenty.LOGGER.info("Asset bridge: {} file(s) came from a BOP build this port was not "
				+ "checked against, and are used as-is. Harmless; they may just look slightly different.",
				drifted);
		}
		return harvest.found;
	}

	private static void collectFromZipFile(File file, Set<String> wanted, Harvest harvest)
		throws IOException {
		try (ZipFile zip = new ZipFile(file)) {
			List<ZipEntry> nested = new ArrayList<>();

			for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements(); ) {
				ZipEntry entry = e.nextElement();
				if (entry.isDirectory()) continue;
				String key = firstWanted(entry.getName(), wanted, harvest);
				if (key != null) {
					try (InputStream in = zip.getInputStream(entry)) {
						harvest.offer(key, readFully(in));
					}
				} else if (isNestedArchiveName(baseName(entry.getName()))) {
					nested.add(entry);
				}
			}

			for (ZipEntry entry : nested) {
				if (harvest.complete()) break;
				try (InputStream in = zip.getInputStream(entry)) {
					collectFromStream(in, wanted, harvest, 1);
				} catch (IOException ignored) {

				}
			}
		}
	}

	private static void collectFromStream(InputStream raw, Set<String> wanted, Harvest harvest, int depth)
		throws IOException {
		if (depth > MAX_NESTING) return;

		ZipInputStream zip = new ZipInputStream(new BufferedInputStream(raw));
		ZipEntry entry;
		while ((entry = zip.getNextEntry()) != null) {
			if (entry.isDirectory()) continue;
			if (harvest.complete()) return;

			String key = firstWanted(entry.getName(), wanted, harvest);
			if (key != null) {
				harvest.offer(key, readFully(zip));
			} else if (isNestedArchiveName(baseName(entry.getName()))) {
				collectFromStream(zip, wanted, harvest, depth + 1);
			}
		}
	}

	private static String firstWanted(String entryName, Set<String> wanted, Harvest harvest) {
		for (String key : keysFor(entryName)) {
			if (wanted.contains(key) && harvest.wants(key)) return key;
		}
		return null;
	}

	private static boolean isNestedArchiveName(String name) {
		return name.endsWith(".zip") || name.endsWith(".jar") || name.endsWith(".mod")
			|| name.endsWith(".pk3") || name.endsWith(".litemod") || name.endsWith(".disabled");
	}

	static byte[] readFully(InputStream in) throws IOException {
		byte[] buffer = new byte[8192];
		java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
		int read;
		while ((read = in.read(buffer)) > 0) out.write(buffer, 0, read);
		return out.toByteArray();
	}

	private static final class Stamp {
		final String label;
		final List<String> lines = new ArrayList<>();

		Stamp(String label, List<Source> used, File gameDir) {
			this.label = label;
			for (Source source : used) lines.add(line(gameDir, source.file));
		}

		private Stamp(String label, List<String> lines) {
			this.label = label;
			this.lines.addAll(lines);
		}

		static String line(File gameDir, File file) {
			return relativePath(gameDir, file) + "|" + file.length() + "|" + file.lastModified();
		}

		static Stamp read(File packDir) {
			File file = new File(packDir, STAMP);
			if (!file.isFile()) return null;
			try {
				List<String> all = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
				if (all.size() < 3 || !all.get(0).trim().equals(String.valueOf(BRIDGE_REVISION))) return null;
				return new Stamp(all.get(1).trim(), all.subList(2, all.size()));
			} catch (IOException | RuntimeException e) {
				return null;
			}
		}

		boolean stillValid(File gameDir) {
			if (lines.isEmpty()) return false;
			for (String recorded : lines) {
				String trimmed = recorded.trim();
				if (trimmed.isEmpty()) continue;
				int split = trimmed.indexOf('|');
				if (split < 0) return false;
				File file = new File(gameDir, trimmed.substring(0, split));
				if (!file.isFile() || !line(gameDir, file).equals(trimmed)) return false;
			}
			return true;
		}

		void write(File packDir) {

			if (lines.isEmpty()) return;
			StringBuilder out = new StringBuilder();
			out.append(BRIDGE_REVISION).append('\n').append(label).append('\n');
			for (String recorded : lines) out.append(recorded).append('\n');
			try {
				BOPAssetBridge.write(new File(packDir, STAMP), out.toString().getBytes(StandardCharsets.UTF_8));
			} catch (IOException e) {
				BetterOPlenty.LOGGER.warn("Asset bridge: could not record what the pack was built from: {}",
					e.toString());
			}
		}
	}

	private static String describe(File gameDir, List<Source> used) {
		if (used.isEmpty()) return "nothing";
		if (used.size() == 1) return "'" + used.get(0).file.getName() + "'";

		File shared = commonAncestor(used);
		if (shared != null && !safePath(shared).equals(safePath(gameDir))) {
			return "'" + relativePath(gameDir, shared) + "' (" + used.size() + " files)";
		}
		return "'" + used.get(0).file.getName() + "' and " + (used.size() - 1) + " more";
	}

	private static File commonAncestor(List<Source> used) {
		File shared = null;
		for (Source source : used) {
			File parent = source.file.getParentFile();
			if (parent == null) return null;
			shared = shared == null ? parent : commonAncestor(shared, parent);
			if (shared == null) return null;
		}
		return shared;
	}

	private static File commonAncestor(File a, File b) {
		String candidate = safePath(a);
		String target = safePath(b);
		while (!target.equals(candidate) && !target.startsWith(candidate + File.separator)) {
			File up = new File(candidate).getParentFile();
			if (up == null) return null;
			candidate = safePath(up);
		}
		return new File(candidate);
	}

	private static final int MAX_PATHS_LOGGED = 8;

	private static String paths(File gameDir, List<Source> used) {
		List<String> shown = new ArrayList<>();
		for (Source source : used) {
			if (shown.size() == MAX_PATHS_LOGGED) {
				shown.add("and " + (used.size() - MAX_PATHS_LOGGED) + " more");
				break;
			}
			shown.add(relativePath(gameDir, source.file));
		}
		return String.join(", ", shown);
	}

	private static String relativePath(File root, File file) {
		return relativeTo(safePath(root), safePath(file));
	}

	private static String relativeTo(String rootPath, String filePath) {
		String relative = filePath.startsWith(rootPath + File.separator)
			? filePath.substring(rootPath.length() + 1)
			: filePath;
		return relative.replace(File.separatorChar, '/');
	}

	private static String safePath(File file) {
		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			return file.getAbsolutePath();
		}
	}

	private static void write(File target, byte[] bytes) throws IOException {
		File parent = target.getParentFile();
		if (!parent.isDirectory() && !parent.mkdirs()) throw new IOException("could not create " + parent);
		try (OutputStream out = new FileOutputStream(target)) {
			out.write(bytes);
		}
	}

	private static void copySoundsManifest(File packDir) throws IOException {
		byte[] raw;
		try (InputStream in = BOPAssetBridge.class.getResourceAsStream(SOUNDS_JSON)) {
			if (in == null) {
				BetterOPlenty.LOGGER.warn("Asset bridge: {} is missing from the jar, so bridged audio will "
					+ "not bind to the pack and BOP's sounds will stay silent", SOUNDS_JSON);
				return;
			}
			raw = readFully(in);
		}

		byte[] out = raw;
		try {
			JsonObject root = JsonParser.parseString(new String(raw, StandardCharsets.UTF_8)).getAsJsonObject();
			for (Map.Entry<String, JsonElement> event : root.entrySet()) {
				if (event.getValue().isJsonObject()) {
					event.getValue().getAsJsonObject().addProperty("replace", true);
				}
			}
			out = root.toString().getBytes(StandardCharsets.UTF_8);
		} catch (RuntimeException e) {

			BetterOPlenty.LOGGER.warn("Asset bridge: could not add 'replace' to the pack's sounds.json ({}); "
				+ "sounds will work but each will be declared twice and log an error", e.toString());
		}
		write(new File(packDir, SOUNDS_JSON_IN_PACK), out);
	}

	private static void writePackMeta(File packDir) throws IOException {

		write(new File(packDir, "pack.txt"),
			("Generated by Finally More Biomes from a copy of Biomes O' Plenty found on this computer.\n"
				+ "\n"
				+ "Biomes O' Plenty is Glitchfiend's work, licensed CC BY-NC-ND 4.0. These files were\n"
				+ "extracted from a copy already on this disk and were NOT downloaded or redistributed.\n"
				+ "Do not share this folder.\n"
				+ "\n"
				+ "Delete this folder to remove it, or to force the search to run again.\n")
				.getBytes(StandardCharsets.UTF_8));
	}

	private static String baseName(String path) {
		int slash = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
		return path.substring(slash + 1).toLowerCase(Locale.ROOT);
	}

	private static Map<String, String> readHashes() {
		Map<String, String> map = new HashMap<>();
		try (InputStream in = BOPAssetBridge.class.getResourceAsStream(HASHES)) {
			if (in == null) return map;
			Properties props = new Properties();
			props.load(in);
			for (String key : props.stringPropertyNames()) {
				map.put(key.toLowerCase(Locale.ROOT), props.getProperty(key).trim());
			}
		} catch (IOException e) {
			BetterOPlenty.LOGGER.warn("Asset bridge: could not read expected hashes ({}); whichever copy "
				+ "of BOP is read first will supply the art.", e.toString());
		}
		return map;
	}

	private static Map<String, List<String>> readManifest() {
		Map<String, List<String>> map = new HashMap<>();
		try (InputStream in = BOPAssetBridge.class.getResourceAsStream(MANIFEST)) {
			if (in == null) return map;
			Properties props = new Properties();
			props.load(in);
			for (String key : props.stringPropertyNames()) {
				List<String> paths = new ArrayList<>();
				for (String path : props.getProperty(key).split(",")) {
					String trimmed = path.trim();
					if (!trimmed.isEmpty()) paths.add(trimmed);
				}
				if (!paths.isEmpty()) map.put(key.toLowerCase(Locale.ROOT), paths);
			}
		} catch (IOException e) {
			BetterOPlenty.LOGGER.warn("Asset bridge: could not read manifest: {}", e.toString());
		}
		return map;
	}

	public static File gameDir() {
		Minecraft client;
		try {
			client = Minecraft.getMinecraft();
		} catch (Throwable t) {

			return null;
		}
		if (client == null) {
			BetterOPlenty.LOGGER.warn("Asset bridge: no Minecraft instance yet, so the game directory "
				+ "is unknown and no art can be bridged. This is a startup-order problem, not a "
				+ "missing archive.");
			return null;
		}
		File dir;
		try {
			dir = client.getMinecraftDir();
		} catch (Throwable t) {
			BetterOPlenty.LOGGER.warn("Asset bridge: could not resolve the game directory ({}), so no "
				+ "art can be bridged.", t.toString());
			return null;
		}
		if (dir == null) {
			BetterOPlenty.LOGGER.warn("Asset bridge: the game directory is null, so no art can be bridged.");
		}
		return dir;
	}
}
