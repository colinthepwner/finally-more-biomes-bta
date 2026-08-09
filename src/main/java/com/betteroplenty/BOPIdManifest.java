package com.betteroplenty;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.block.ItemBlock;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class BOPIdManifest {
	private BOPIdManifest() {}

	private static final String RESOURCE = "/betteroplenty-ids.tsv";
	private static final String WRITE_PROPERTY = "betteroplenty.writeIdManifest";

	public static void run() {
		Map<String, Integer> live = live();

		if (Boolean.getBoolean(WRITE_PROPERTY)) {
			write(live);
			return;
		}

		Map<String, Integer> recorded = read();
		if (recorded == null) {
			BetterOPlenty.LOGGER.warn("No id manifest on the classpath ({}), so nothing is pinned. "
				+ "Run once with -D{}=true and check the file in.", RESOURCE, WRITE_PROPERTY);
			write(live);
			return;
		}

		List<String> moved = new ArrayList<>();
		List<String> vanished = new ArrayList<>();
		List<String> added = new ArrayList<>();

		for (Map.Entry<String, Integer> entry : recorded.entrySet()) {
			Integer now = live.get(entry.getKey());
			if (now == null) {
				vanished.add(entry.getKey() + " (was " + entry.getValue() + ")");
			} else if (!now.equals(entry.getValue())) {
				moved.add(entry.getKey() + ": " + entry.getValue() + " -> " + now);
			}
		}
		for (Map.Entry<String, Integer> entry : live.entrySet()) {
			if (!recorded.containsKey(entry.getKey())) {
				added.add(entry.getKey() + " = " + entry.getValue());
			}
		}

		Map<Integer, String> byId = new TreeMap<>();
		List<String> collisions = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : live.entrySet()) {
			String previous = byId.put(entry.getValue(), entry.getKey());
			if (previous != null) {
				collisions.add(entry.getValue() + " claimed by both " + previous + " and " + entry.getKey());
			}
		}

		if (!vanished.isEmpty()) {
			BetterOPlenty.LOGGER.error("Id manifest: {} pinned entr(ies) no longer exist -- a rename "
				+ "is harmless, a deletion leaves saved worlds holding an id nothing answers to: {}",
				vanished.size(), String.join(", ", vanished));
		}
		if (!added.isEmpty()) {
			BetterOPlenty.LOGGER.info("Id manifest: {} new id(s) since it was written ({}). "
				+ "Refresh it with -D{}=true.", added.size(), String.join(", ", added), WRITE_PROPERTY);
		}
		if (!moved.isEmpty() || !collisions.isEmpty()) {
			throw new IllegalStateException("Block/item ids are append-only and some have changed. "
				+ "Every existing world stores these numbers, so moving one silently replaces that "
				+ "block everywhere it was placed. Moved: " + String.join("; ", moved)
				+ (collisions.isEmpty() ? "" : ". Colliding: " + String.join("; ", collisions))
				+ ". Put them back, or -D" + WRITE_PROPERTY + "=true if the change is deliberate "
				+ "and no world depends on the old numbering.");
		}

		BetterOPlenty.LOGGER.info("Id manifest: {} of {} block/item ids pinned and unchanged.",
			recorded.size() - vanished.size(), live.size());
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

	private static Map<String, Integer> read() {
		try (InputStream in = BOPIdManifest.class.getResourceAsStream(RESOURCE)) {
			if (in == null) {
				return null;
			}
			Map<String, Integer> out = new LinkedHashMap<>();
			List<String> unpinned = new ArrayList<>();
			List<String> malformed = new ArrayList<>();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
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
		} catch (IOException e) {
			BetterOPlenty.LOGGER.error("Could not read the id manifest; ids are unpinned.", e);
			return null;
		}
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

	private static void write(Map<String, Integer> live) {
		File target = new File("betteroplenty-ids.tsv");
		StringBuilder sb = new StringBuilder();
		sb.append("# Finally More Biomes block and item ids. Append-only: a saved chunk stores the\n");
		sb.append("# number, not the name, so moving one replaces that block in every world that\n");
		sb.append("# has it. BOPIdManifest refuses to start when one moves.\n");
		sb.append("# An id of '-' means deliberately unpinned: biome ids are alphabetical rank by\n");
		sb.append("# registry key and move whenever any biome is added, so there is no stable\n");
		sb.append("# number to record. Those rows are carried over verbatim, not regenerated.\n");
		sb.append("# kind\tnamespaced name\tid\n");
		for (Map.Entry<String, Integer> entry : live.entrySet()) {
			sb.append(entry.getKey()).append('\t').append(entry.getValue()).append('\n');
		}
		for (String name : unpinnedRows()) {
			sb.append(name).append("\t-\n");
		}
		try {
			Files.writeString(target.toPath(), sb.toString(), StandardCharsets.UTF_8);
			BetterOPlenty.LOGGER.info("Wrote {} ids to {}. Copy it over src/main/resources{}.",
				live.size(), target.getAbsolutePath(), RESOURCE);
		} catch (IOException e) {
			BetterOPlenty.LOGGER.error("Could not write the id manifest.", e);
		}
	}
}
