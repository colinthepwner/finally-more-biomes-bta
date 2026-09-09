package com.betteroplenty;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.world.Dimension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;

public final class BOPDimensionId {
	private BOPDimensionId() {}

	public static final int PREFERRED = 3;

	private static final int FIRST_MOD_ID = 3;

	private static final int LAST_CANDIDATE = 127;

	private static final Path RECORD = Path.of("config", "betteroplenty-dimensions.tsv");

	private static final String PROMISED_ROW = "promised";
	private static final String PROMISED_WORLD_TYPE = BetterOPlenty.MOD_ID + ":bop.promised";

	private static int chosen = -1;

	public static int choosePromised() {
		if (chosen != -1) {
			return chosen;
		}

		Integer recorded = readRecord();
		int wanted = recorded != null ? recorded : PREFERRED;

		if (isFree(wanted)) {

			int lowest = lowestFree();
			if (lowest != -1 && lowest < wanted && worldsWithPromisedLandAt(wanted).isEmpty()) {
				BetterOPlenty.LOGGER.info("This instance recorded dimension id {} for the Promised "
					+ "Land, but {} is free again and no world here has a Promised Land at {}. "
					+ "Moving down to {} so the dimension id space stays contiguous, which is what "
					+ "BTA's world type groups require.", wanted, lowest, wanted, lowest);
				chosen = lowest;
				writeRecord(chosen);
				return chosen;
			}

			chosen = wanted;
			if (recorded == null) {
				writeRecord(chosen);
			} else if (recorded != PREFERRED) {

				BetterOPlenty.LOGGER.info("The Promised Land keeps this instance's recorded "
					+ "dimension id {} (its default is {}); {} names it.", chosen, PREFERRED, RECORD);
			}
			return chosen;
		}

		int replacement = lowestFree();
		Dimension holder = Dimension.getDimensionList().get(wanted);
		String holderName = holder != null ? holder.languageKey : "another mod";

		if (replacement == -1) {
			BetterOPlenty.LOGGER.error("Dimension id {} is held by '{}' and every id from {} to {} "
				+ "is taken as well, so the Promised Land cannot be registered at all. Everything "
				+ "else in this mod is unaffected.", wanted, holderName, FIRST_MOD_ID, LAST_CANDIDATE);
			chosen = -1;
			return -1;
		}

		List<String> atStake = worldsWithPromisedLandAt(wanted);
		if (atStake.isEmpty()) {
			BetterOPlenty.LOGGER.warn("Dimension id {} is held by '{}', so the Promised Land takes "
				+ "id {} in this instance instead. No world here has a Promised Land at {}, so "
				+ "nothing is lost; the choice is now recorded in {} and will not move again.",
				wanted, holderName, replacement, wanted, RECORD);
		} else {

			BetterOPlenty.LOGGER.error("Dimension id {} is held by '{}', but {} world(s) in this "
				+ "instance already have a Promised Land saved at id {}: {}. Those worlds keep "
				+ "their terrain on disk under dimensions/{}/, but this mod can no longer read it "
				+ "-- the Promised Land moves to id {} for every world in this instance, and in the "
				+ "old worlds it will generate afresh. To get the old one back, remove whatever "
				+ "added '{}' and delete {}. A player who was standing in the Promised Land when "
				+ "the mod set changed will arrive in '{}' instead.",
				wanted, holderName, atStake.size(), wanted, String.join(", ", atStake),
				wanted, replacement, holderName, RECORD, holderName);
		}

		chosen = replacement;
		writeRecord(chosen);
		return chosen;
	}

	private static boolean isFree(int id) {
		return id >= FIRST_MOD_ID && !Dimension.getDimensionList().containsKey(id);
	}

	private static int lowestFree() {
		for (int id = FIRST_MOD_ID; id <= LAST_CANDIDATE; id++) {
			if (isFree(id)) {
				return id;
			}
		}
		return -1;
	}

	private static Integer readRecord() {
		if (!Files.isRegularFile(RECORD)) {
			return null;
		}
		try {
			for (String line : Files.readAllLines(RECORD, StandardCharsets.UTF_8)) {
				String row = line.trim();
				if (row.isEmpty() || row.startsWith("#")) {
					continue;
				}
				String[] parts = row.split("\t+");
				if (parts.length != 2 || !PROMISED_ROW.equals(parts[0].trim())) {
					continue;
				}
				int id = Integer.parseInt(parts[1].trim());
				if (id < FIRST_MOD_ID || id > LAST_CANDIDATE) {
					BetterOPlenty.LOGGER.warn("{} records dimension id {} for the Promised Land, "
						+ "which is out of range; ignoring it and choosing afresh.", RECORD, id);
					return null;
				}
				return id;
			}
		} catch (IOException | RuntimeException e) {
			BetterOPlenty.LOGGER.warn("Could not read {}; choosing the Promised Land's dimension "
				+ "id afresh.", RECORD, e);
		}
		return null;
	}

	private static void writeRecord(int id) {
		String body = "# Finally More Biomes -- this instance's dimension ids.\n"
			+ "#\n"
			+ "# Dimension ids are first-come integers with no registry behind them, and they are\n"
			+ "# baked into every save: dimensions/<id>/ holds the terrain and every player's\n"
			+ "# Dimension tag names the number. This file pins the number this instance chose, so\n"
			+ "# that adding or removing an unrelated dimension mod cannot renumber a world you\n"
			+ "# already have. Delete it only if you have no world with a Promised Land in it.\n"
			+ "#\n"
			+ "# name\tid\n"
			+ PROMISED_ROW + "\t" + id + "\n";
		try {
			Path parent = RECORD.getParent();
			if (parent != null) {
				Files.createDirectories(parent);
			}
			Files.writeString(RECORD, body, StandardCharsets.UTF_8);
		} catch (IOException | RuntimeException e) {

			BetterOPlenty.LOGGER.warn("Could not write {}. The Promised Land is at dimension {} "
				+ "this launch, but without the record a later change to this instance's mods "
				+ "could move it.", RECORD, id, e);
		}
	}

	private static List<String> worldsWithPromisedLandAt(int id) {
		List<String> out = new ArrayList<>();
		scanWorldRoot(Path.of("saves"), id, out);
		scanWorldRoot(Path.of("."), id, out);
		return out;
	}

	private static void scanWorldRoot(Path root, int id, List<String> out) {
		if (!Files.isDirectory(root)) {
			return;
		}
		try (var worlds = Files.newDirectoryStream(root, Files::isDirectory)) {
			for (Path world : worlds) {
				Path dat = world.resolve("dimensions").resolve(String.valueOf(id))
					.resolve("dimension.dat");
				if (Files.isRegularFile(dat) && isPromisedLand(dat)) {
					out.add(world.getFileName().toString());
				}
			}
		} catch (IOException | RuntimeException e) {
			BetterOPlenty.LOGGER.warn("Could not scan {} for worlds with a Promised Land at "
				+ "dimension {}; assuming none.", root, id, e);
		}
	}

	private static boolean isPromisedLand(Path dat) {
		byte[] data;
		try {
			byte[] raw = Files.readAllBytes(dat);
			try (GZIPInputStream gz = new GZIPInputStream(new ByteArrayInputStream(raw))) {
				data = gz.readAllBytes();
			} catch (IOException notGzip) {
				data = raw;
			}
		} catch (IOException | RuntimeException e) {

			BetterOPlenty.LOGGER.warn("Could not read {}; not counting its world.", dat, e);
			return false;
		}

		return new String(data, StandardCharsets.ISO_8859_1).contains(PROMISED_WORLD_TYPE);
	}

	public static String describeIdSpace() {
		Int2ObjectMap<Dimension> list = Dimension.getDimensionList();
		List<String> parts = new ArrayList<>();
		for (int id = 0; id <= LAST_CANDIDATE; id++) {
			Dimension dimension = list.get(id);
			if (dimension != null) {
				parts.add(id + " ('" + dimension.languageKey + "')");
			}
		}
		return String.join(", ", parts);
	}
}
