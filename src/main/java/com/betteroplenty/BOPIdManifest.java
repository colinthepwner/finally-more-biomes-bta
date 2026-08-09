package com.betteroplenty;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.block.ItemBlock;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;

public final class BOPIdManifest {
	private BOPIdManifest() {}

	private static final String RESOURCE = "/betteroplenty-ids.tsv";
	private static final String WRITE_PROPERTY = "betteroplenty.writeIdManifest";

	private static final Path RECORD = Path.of("config", "betteroplenty-ids.tsv");

	private static final Path LEGACY_RECORD = Path.of("betteroplenty-ids.tsv");

	public static void run() {
		Map<String, Integer> live = live();

		Map<Integer, String> byId = new TreeMap<>();
		List<String> collisions = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : live.entrySet()) {
			String previous = byId.put(entry.getValue(), entry.getKey());
			if (previous != null) {
				collisions.add(entry.getValue() + " claimed by both " + previous + " and " + entry.getKey());
			}
		}
		if (!collisions.isEmpty()) {
			throw new IllegalStateException("Two of this mod's blocks/items share one id, so a "
				+ "saved chunk holding that number is ambiguous: " + String.join("; ", collisions)
				+ ". This is registry corruption, not a configuration problem -- please report it.");
		}

		Baseline baseline = baseline();

		if (Boolean.getBoolean(WRITE_PROPERTY)) {
			boolean matched = baseline != null && diff(baseline.ids, live).isEmpty();

			write(live, List.of(), LEGACY_RECORD,
				"Copy it over src/main/resources" + RESOURCE + " if this numbering is the "
					+ "canonical one (a clean instance, no other mods).");
			write(live, List.of(), RECORD, null);
			if (matched) {
				BetterOPlenty.LOGGER.info("Id manifest: -D{}=true is set but the recorded ids "
					+ "already match; the flag is doing nothing and can be removed.", WRITE_PROPERTY);
			} else {
				BetterOPlenty.LOGGER.info("Id manifest: accepted the current numbering as this "
					+ "instance's baseline ({}). Remove -D{}=true; protection resumes on the next "
					+ "launch.", RECORD, WRITE_PROPERTY);
			}
			return;
		}

		if (baseline == null) {

			BetterOPlenty.LOGGER.warn("No id manifest anywhere ({} missing, no {}), so nothing was "
				+ "pinned until now. Recorded the current {} ids as this instance's baseline; run "
				+ "once with -D{}=true and check the resource in.", RESOURCE, RECORD, live.size(),
				WRITE_PROPERTY);
			write(live, List.of(), RECORD, null);
			return;
		}

		Diff diff = diff(baseline.ids, live);

		if (!diff.vanished.isEmpty()) {
			BetterOPlenty.LOGGER.error("Id manifest: {} pinned entr(ies) no longer exist -- a rename "
				+ "is harmless, a deletion leaves saved worlds holding an id nothing answers to: {}",
				diff.vanished.size(), String.join(", ", diff.vanished));
		}

		if (diff.moved.isEmpty()) {

			if (baseline.source != Source.RECORD || !diff.added.isEmpty()) {
				write(live, baseline.vanishedRows(diff), RECORD, null);
			}
			if (baseline.source == Source.LEGACY) {
				BetterOPlenty.LOGGER.info("Id manifest: adopted {} (the pre-0.1.2 flag output) as "
					+ "this instance's baseline; it now lives at {} and the flag can stay off.",
					LEGACY_RECORD, RECORD);
			}
			if (!diff.added.isEmpty()) {
				BetterOPlenty.LOGGER.info("Id manifest: {} new id(s) recorded ({}).",
					diff.added.size(), String.join(", ", diff.added));
			}
			BetterOPlenty.LOGGER.info("Id manifest: {} of {} block/item ids pinned and unchanged "
				+ "(baseline: {}).", baseline.ids.size() - diff.vanished.size(), live.size(),
				baseline.describe());
			return;
		}

		List<String> movedDetail = describeMoves(diff);
		List<String> atStake = worldsWithOurTerrain();

		if (atStake.isEmpty()) {
			write(live, List.of(), RECORD, null);
			BetterOPlenty.LOGGER.warn("Id manifest: {} block/item id(s) moved -- the mod set "
				+ "changed ({}). No world in this instance has Finally More Biomes terrain, so the "
				+ "new numbering was adopted as the baseline ({}) and the game will start normally. "
				+ "Moved: {}. From here on the new numbers are pinned: if they move again while a "
				+ "world exists, the game will refuse to start instead of scrambling that world. "
				+ "(Worlds are recognised by world type; blocks hand-placed via /give in a "
				+ "plain-BTA world cannot be seen and were not protected by this adoption.)",
				diff.moved.size(), foreignHolders(diff), RECORD, String.join("; ", movedDetail));
			return;
		}

		throw new IllegalStateException("Block/item ids moved, and world(s) in this instance "
			+ "already store the old numbers: " + String.join(", ", atStake) + ". A saved chunk "
			+ "stores only the number, so launching now would silently turn each moved block into "
			+ "whatever holds its old number today. Moved: " + String.join("; ", movedDetail)
			+ ". This follows a mod-list change (" + foreignHolders(diff) + "). Either restore the "
			+ "previous mod set -- or re-id the conflicting mod in its own config -- so the old "
			+ "numbers come back, or, if those worlds are expendable, accept the new numbering "
			+ "once with -D" + WRITE_PROPERTY + "=true (rewrites " + RECORD + "; remove the flag "
			+ "after one launch).");
	}

	public static String span(int... ids) {
		int min = ids[0];
		int max = ids[0];
		for (int id : ids) {
			min = Math.min(min, id);
			max = Math.max(max, id);
		}
		if (min == max) {
			return String.valueOf(min);
		}
		return min + "-" + max + (max - min + 1 == ids.length ? "" : ", with gaps");
	}

	private static Map<String, Integer> live() {
		Map<String, Integer> out = new TreeMap<>();
		for (Block<?> block : Blocks.blocksList) {
			if (block != null && BetterOPlenty.MOD_ID.equals(block.namespaceId().namespace())) {
				out.put("block\t" + block.namespaceId(), block.id());
			}
		}
		for (Item item : Item.itemsList) {

			if (item != null && !(item instanceof ItemBlock)
					&& BetterOPlenty.MOD_ID.equals(item.namespaceID.namespace())) {
				out.put("item\t" + item.namespaceID, item.id);
			}
		}
		return out;
	}

	private enum Source { RECORD, LEGACY, SHIPPED }

	private static final class Baseline {
		final Source source;
		final Map<String, Integer> ids;

		Baseline(Source source, Map<String, Integer> ids) {
			this.source = source;
			this.ids = ids;
		}

		String describe() {
			switch (source) {
				case RECORD: return RECORD.toString();
				case LEGACY: return LEGACY_RECORD + " (pre-0.1.2)";
				default: return "the shipped manifest";
			}
		}

		List<String> vanishedRows(Diff diff) {
			List<String> rows = new ArrayList<>();
			for (String name : diff.vanishedNames) {
				rows.add(name + "\t" + ids.get(name));
			}
			return rows;
		}
	}

	private static Baseline baseline() {
		if (Files.isRegularFile(RECORD)) {
			Map<String, Integer> ids = parseFile(RECORD);
			if (ids != null) {
				return new Baseline(Source.RECORD, ids);
			}
		}
		if (Files.isRegularFile(LEGACY_RECORD)) {
			Map<String, Integer> ids = parseFile(LEGACY_RECORD);
			if (ids != null) {
				return new Baseline(Source.LEGACY, ids);
			}
		}
		try (InputStream in = BOPIdManifest.class.getResourceAsStream(RESOURCE)) {
			if (in != null) {
				return new Baseline(Source.SHIPPED,
					parse(new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))));
			}
		} catch (IOException e) {
			BetterOPlenty.LOGGER.error("Could not read the shipped id manifest.", e);
		}
		return null;
	}

	private static Map<String, Integer> parseFile(Path file) {
		try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
			return parse(reader);
		} catch (IOException e) {

			BetterOPlenty.LOGGER.error("Could not read {}; falling back.", file, e);
			return null;
		}
	}

	private static Map<String, Integer> parse(BufferedReader reader) throws IOException {
		Map<String, Integer> out = new LinkedHashMap<>();
		List<String> unpinned = new ArrayList<>();
		List<String> malformed = new ArrayList<>();
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.isBlank() || line.startsWith("#")) {
				continue;
			}
			int last = line.lastIndexOf('\t');
			if (last < 0) {
				continue;
			}
			String name = line.substring(0, last);
			String id = line.substring(last + 1).trim();
			if (id.equals("-")) {
				unpinned.add(name);
				continue;
			}
			try {
				out.put(name, Integer.parseInt(id));
			} catch (NumberFormatException e) {
				malformed.add(name + " = '" + id + "'");
			}
		}
		if (!unpinned.isEmpty()) {
			BetterOPlenty.LOGGER.info("Id manifest: {} row(s) carry no id and are deliberately "
				+ "unpinned (biome ids are alphabetical rank and move on every insert).",
				unpinned.size());
		}
		if (!malformed.isEmpty()) {
			BetterOPlenty.LOGGER.error("Id manifest: {} row(s) have an unreadable id and are NOT "
				+ "pinned: {}. Every other row is still checked.",
				malformed.size(), String.join(", ", malformed));
		}
		return out;
	}

	private static final class Move {
		final String name;
		final int from;
		final int to;

		Move(String name, int from, int to) {
			this.name = name;
			this.from = from;
			this.to = to;
		}

		boolean isBlock() {
			return name.startsWith("block\t");
		}
	}

	private static final class Diff {
		final List<Move> moved = new ArrayList<>();
		final List<String> vanished = new ArrayList<>();
		final List<String> vanishedNames = new ArrayList<>();
		final List<String> added = new ArrayList<>();

		boolean isEmpty() {
			return moved.isEmpty();
		}
	}

	private static Diff diff(Map<String, Integer> baseline, Map<String, Integer> live) {
		Diff diff = new Diff();
		for (Map.Entry<String, Integer> entry : baseline.entrySet()) {
			Integer now = live.get(entry.getKey());
			if (now == null) {
				diff.vanished.add(entry.getKey() + " (was " + entry.getValue() + ")");
				diff.vanishedNames.add(entry.getKey());
			} else if (!now.equals(entry.getValue())) {
				diff.moved.add(new Move(entry.getKey(), entry.getValue(), now));
			}
		}
		for (Map.Entry<String, Integer> entry : live.entrySet()) {
			if (!baseline.containsKey(entry.getKey())) {
				diff.added.add(entry.getKey() + " = " + entry.getValue());
			}
		}
		return diff;
	}

	private static List<String> describeMoves(Diff diff) {
		List<String> out = new ArrayList<>();
		for (Move move : diff.moved) {
			out.add(move.name + ": " + move.from + " -> " + move.to
				+ " (" + move.from + " " + holderOf(move.isBlock(), move.from) + ")");
		}
		return out;
	}

	private static String holderOf(boolean block, int id) {
		if (block) {
			Block<?> holder = id >= 0 && id < Blocks.blocksList.length ? Blocks.blocksList[id] : null;
			return holder == null ? "now unassigned" : "held by " + holder.namespaceId();
		}
		Item holder = id >= 0 && id < Item.itemsList.length ? Item.itemsList[id] : null;
		return holder == null ? "now unassigned" : "held by " + holder.namespaceID;
	}

	private static String foreignHolders(Diff diff) {
		Set<String> namespaces = new LinkedHashSet<>();
		for (Move move : diff.moved) {
			String namespace = holderNamespace(move.isBlock(), move.from);
			if (namespace != null && !BetterOPlenty.MOD_ID.equals(namespace)) {
				namespaces.add(namespace);
			}
		}
		if (namespaces.isEmpty()) {
			return "the mod that had taken the old numbers is gone";
		}
		return "the old numbers are now held by: " + String.join(", ", namespaces);
	}

	private static String holderNamespace(boolean block, int id) {
		if (block) {
			Block<?> holder = id >= 0 && id < Blocks.blocksList.length ? Blocks.blocksList[id] : null;
			return holder == null ? null : holder.namespaceId().namespace();
		}
		Item holder = id >= 0 && id < Item.itemsList.length ? Item.itemsList[id] : null;
		return holder == null ? null : holder.namespaceID.namespace();
	}

	private static List<String> worldsWithOurTerrain() {
		List<String> out = new ArrayList<>();
		scanWorldRoot(Path.of("saves"), out);
		scanWorldRoot(Path.of("."), out);
		return out;
	}

	private static void scanWorldRoot(Path root, List<String> out) {
		if (!Files.isDirectory(root)) {
			return;
		}
		try (DirectoryStream<Path> worlds = Files.newDirectoryStream(root, Files::isDirectory)) {
			for (Path world : worlds) {
				Path dims = world.resolve("dimensions");
				if (!Files.isDirectory(dims)) {
					continue;
				}
				try (DirectoryStream<Path> dimDirs = Files.newDirectoryStream(dims, Files::isDirectory)) {
					for (Path dim : dimDirs) {
						Path dat = dim.resolve("dimension.dat");
						if (!Files.isRegularFile(dat)) {
							continue;
						}
						if (mentionsUs(dat)) {
							out.add(world.getFileName().toString());
							break;
						}
					}
				}
			}
		} catch (IOException e) {
			BetterOPlenty.LOGGER.error("Could not scan {} for worlds; assuming none.", root, e);
		}
	}

	private static boolean mentionsUs(Path dat) {
		byte[] data;
		try {
			byte[] raw = Files.readAllBytes(dat);
			try (GZIPInputStream gz = new GZIPInputStream(new ByteArrayInputStream(raw))) {
				data = gz.readAllBytes();
			} catch (IOException notGzip) {
				data = raw;
			}
		} catch (IOException e) {
			BetterOPlenty.LOGGER.warn("Could not read {}; treating its world as one of ours.", dat, e);
			return true;
		}

		return new String(data, StandardCharsets.ISO_8859_1).contains(BetterOPlenty.MOD_ID);
	}

	private static List<String> unpinnedRows() {
		List<String> out = new ArrayList<>();
		try (InputStream in = BOPIdManifest.class.getResourceAsStream(RESOURCE)) {
			if (in == null) {
				return out;
			}
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null) {
					int last = line.lastIndexOf('\t');
					if (!line.isBlank() && !line.startsWith("#") && last >= 0
							&& line.substring(last + 1).trim().equals("-")) {
						out.add(line.substring(0, last));
					}
				}
			}
		} catch (IOException e) {
			BetterOPlenty.LOGGER.error("Could not re-read the id manifest for its unpinned rows; the "
				+ "file written below will be missing them. Merge by hand.", e);
		}
		return out;
	}

	private static void write(Map<String, Integer> live, List<String> keepRows, Path target,
			String followUp) {
		StringBuilder sb = new StringBuilder();
		sb.append("# Finally More Biomes block and item ids AS THIS INSTANCE USES THEM. A saved\n");
		sb.append("# chunk stores the number, not the name, so a number that moves replaces that\n");
		sb.append("# block in every world that has it; the mod refuses to start when one moves\n");
		sb.append("# while a world with its terrain exists, and adopts the move when none does.\n");
		sb.append("# Keep this file with the instance. Delete it only if no world here contains\n");
		sb.append("# Finally More Biomes blocks -- including blocks placed by hand in plain BTA\n");
		sb.append("# worlds, which the world-type check cannot see.\n");
		sb.append("# An id of '-' means deliberately unpinned: biome ids are alphabetical rank by\n");
		sb.append("# registry key and move whenever any biome is added, so there is no stable\n");
		sb.append("# number to record. Those rows are carried over verbatim, not regenerated.\n");
		sb.append("# kind\tnamespaced name\tid\n");
		for (Map.Entry<String, Integer> entry : live.entrySet()) {
			sb.append(entry.getKey()).append('\t').append(entry.getValue()).append('\n');
		}

		for (String row : keepRows) {
			sb.append(row).append('\n');
		}
		for (String name : unpinnedRows()) {
			sb.append(name).append("\t-\n");
		}
		try {
			if (target.getParent() != null) {
				Files.createDirectories(target.getParent());
			}
			Files.writeString(target, sb.toString(), StandardCharsets.UTF_8);
			if (followUp != null) {
				BetterOPlenty.LOGGER.info("Wrote {} ids to {}. {}", live.size(),
					target.toAbsolutePath(), followUp);
			}
		} catch (IOException e) {

			BetterOPlenty.LOGGER.error("Could not write the id record {}.", target, e);
		}
	}
}
