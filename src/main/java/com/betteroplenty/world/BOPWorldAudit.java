package com.betteroplenty.world;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.BiomeTags;
import net.minecraft.core.world.biome.data.BiomeRange;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class BOPWorldAudit {
	private BOPWorldAudit() {}

	private static final int WORLD_HEIGHT = 256;

	public static void run() {
		List<BiomeGenBase> registered = BOPBiomes.registered();
		if (registered.isEmpty()) {
			BetterOPlenty.LOGGER.error("World audit ran before any biome was registered; "
				+ "every check below would pass against nothing. Fix the call site.");
			return;
		}

		auditPartition();
		auditLandWaterMask();
		auditFullRoster();
		auditLiveMap(registered);
		auditNames(registered);
		auditTags(registered);
		auditTerrainShape(registered);
		auditRarity();
		auditSubBiomesInsideParents();
		auditSeasonResistance(registered);
		auditIdHeadroom();
	}

	private static void auditSeasonResistance(@NotNull List<BiomeGenBase> registered) {
		Map<String, Double> table = BOPClimate.seasonResistances();
		List<String> unknown = new ArrayList<>();
		for (String id : table.keySet()) {
			if (BOPClimate.planned(id) == null) {
				unknown.add(id);
			}
		}
		if (!unknown.isEmpty()) {
			BetterOPlenty.LOGGER.error("Seasonal resistance names {} biome(s) that are not in the "
				+ "planned roster: {}. Either the id is a typo or the roster row was removed.",
				unknown.size(), String.join(", ", unknown));
			return;
		}

		List<String> notApplied = new ArrayList<>();
		for (BiomeGenBase biome : registered) {
			String key = String.valueOf(Registries.BIOMES.getKey(biome));
			String id = key.contains(".") ? key.substring(key.lastIndexOf('.') + 1) : key;
			double expected = BOPClimate.seasonResist(id);
			if (BOPClimate.planned(id) != null && Math.abs(biome.seasonResist - expected) > 1e-9) {
				notApplied.add(id + " is " + biome.seasonResist + ", expected " + expected);
			}
		}
		if (!notApplied.isEmpty()) {
			BetterOPlenty.LOGGER.error("Seasonal resistance did not reach {} registered biome(s): {}."
				+ " BOPBiomes.add must copy BOPClimate.seasonResist onto the biome.",
				notApplied.size(), String.join("; ", notApplied));
			return;
		}

		int planned = BOPClimate.roster().size();
		long normal = BOPClimate.roster().stream()
			.filter(p -> BOPClimate.seasonsNormally(p.id)).count();
		Map<Double, Integer> byValue = new java.util.TreeMap<>();
		for (double v : table.values()) {
			byValue.merge(v, 1, Integer::sum);
		}
		BetterOPlenty.LOGGER.info("Seasonal resistance: {} of {} planned biome(s) resist ({}), "
			+ "{} season normally; applied to all {} registered.",
			table.size(), planned, byValue, normal, registered.size());
	}

	private static void auditRarity() {
		Map<String, Double> volumes = new LinkedHashMap<>();
		for (BOPClimate.Cell cell : BOPClimate.partition()) {
			volumes.merge(cell.owner.id, cell.volume(), Double::sum);
		}

		List<String> offenders = new ArrayList<>();
		double worstRelative = 0.0;
		String worst = "";
		double totalAbsolute = 0.0;
		for (BOPClimate.Planned planned : BOPClimate.roster()) {
			double got = volumes.getOrDefault(planned.id, 0.0);
			double relative = (got - planned.share) / planned.share;
			totalAbsolute += Math.abs(got - planned.share);
			if (Math.abs(relative) > Math.abs(worstRelative)) {
				worstRelative = relative;
				worst = planned.id;
			}

			if (Math.abs(relative) > 0.01) {
				offenders.add(String.format("%s %+.1f%%", planned.id, 100 * relative));
			}
		}

		if (!offenders.isEmpty()) {
			BetterOPlenty.LOGGER.error("Rarity drift: {} biome(s) do not own the share of the world "
				+ "BOP gave them: {}. A biome's box volume must equal its roster share; see "
				+ "BOPClimate.partition().", offenders.size(), String.join(", ", offenders));
			return;
		}
		BetterOPlenty.LOGGER.info("Rarity, as planned: all {} planned biome(s) are allocated their "
			+ "BOP share exactly (worst {} at {}{}%, total absolute error {}).",
			BOPClimate.roster().size(), worst, worstRelative >= 0 ? "+" : "",
			String.format("%.4f", 100 * worstRelative), String.format("%.2e", totalAbsolute));

		auditRealisedRarity();
	}

	private static void auditRealisedRarity() {
		double standInShare = 0.0;
		double btaShare = 0.0;
		List<String> standingIn = new ArrayList<>();
		List<String> filled = new ArrayList<>();
		Map<String, Double> absorbedBy = new LinkedHashMap<>();

		for (BOPClimate.Planned planned : BOPClimate.roster()) {
			if (BOPBiomes.byPlanId(planned.id) != null) {
				continue;
			}
			if (BOPBiomes.filledBy(planned.id) != null) {

				btaShare += planned.share;
				filled.add(planned.id);
			} else {
				standInShare += planned.share;
				standingIn.add(planned.id);
			}
		}

		for (BOPClimate.Cell cell : BOPClimate.partition()) {
			if (BOPBiomes.byPlanId(cell.owner.id) != null
				|| BOPBiomes.filledBy(cell.owner.id) != null) {
				continue;
			}
			absorbedBy.merge(String.valueOf(Registries.BIOMES.getKey(
				BOPBiomes.servingBiome(cell.owner))), cell.volume(), Double::sum);
		}

		if (!filled.isEmpty()) {
			BetterOPlenty.LOGGER.info("Rarity, filled slots: {}% of the world is held by a deliberate "
				+ "filler rather than its own biome -- BTA's under E5, plus mountain for extreme_hills "
				+ "-- covering {}.", String.format("%.2f", 100 * btaShare),
				String.join(", ", filled));
		}

		if (standingIn.isEmpty()) {
			BetterOPlenty.LOGGER.info("Rarity, as realised: every roster slot is served by the biome "
				+ "it is meant to be, so the world delivers BOP's frequencies exactly.");
			return;
		}

		List<String> worst = new ArrayList<>();
		absorbedBy.entrySet().stream()
			.sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
			.limit(5)
			.forEach(e -> worst.add(String.format("%s +%.2f%%", e.getKey(), 100 * e.getValue())));

		BetterOPlenty.LOGGER.warn("Rarity, as realised: {} roster slot(s) have neither a ported biome "
			+ "nor an E5 filler, so {}% of the world is a stand-in and those neighbours are commoner "
			+ "than BOP made them. Standing in for: {}. Over-represented: {}.",
			standingIn.size(), String.format("%.2f", 100 * standInShare),
			String.join(", ", standingIn), String.join(", ", worst));
	}

	private static void auditSubBiomesInsideParents() {
		Map<String, BOPClimate.Cell> cells = new LinkedHashMap<>();
		for (BOPClimate.Cell cell : BOPClimate.partition()) {
			cells.putIfAbsent(cell.owner.id, cell);
		}

		List<String> broken = new ArrayList<>();
		int pairs = 0;
		for (BOPClimate.Planned planned : BOPClimate.roster()) {
			if (planned.parent == null) {
				continue;
			}
			pairs++;
			BOPClimate.Cell child = cells.get(planned.id);
			BOPClimate.Cell parent = cells.get(planned.parent);
			if (child == null || parent == null) {
				broken.add(planned.id + " (parent '" + planned.parent + "' is not in the partition)");
				continue;
			}
			boolean sameBand = child.tMin == parent.tMin && child.tMax == parent.tMax;
			boolean sameSlice = child.hMin == parent.hMin && child.hMax == parent.hMax;
			if (!sameBand || !sameSlice) {
				broken.add(String.format("%s is not inside %s (%s)", planned.id, planned.parent,
					!sameBand ? "different temperature band" : "different humidity slice"));
			}
		}

		if (!broken.isEmpty()) {
			BetterOPlenty.LOGGER.error("Sub-biome placement: {} of {} sub-biome(s) do not sit inside "
				+ "their parent: {}. BiomeLayerSub carves a child out of its parent's own cells, so "
				+ "the two must share a temperature band and a humidity slice and differ only on "
				+ "variety.", broken.size(), pairs, String.join("; ", broken));
			return;
		}
		BetterOPlenty.LOGGER.info("Sub-biome placement: all {} sub-biome(s) sit inside their parent, "
			+ "sharing its band and humidity slice and splitting only variety.", pairs);
	}

	private static void auditNames(@NotNull List<BiomeGenBase> registered) {
		I18n i18n = I18n.getInstance();
		if (i18n == null) {
			BetterOPlenty.LOGGER.info("Biome names: not checked, no language loaded "
				+ "(dedicated server). The client run is the one that proves this.");
			return;
		}

		List<String> missing = new ArrayList<>();
		for (BiomeGenBase biome : registered) {
			if (biome.translationKey.equals(i18n.translateKey(biome.translationKey))) {
				String key = Registries.BIOMES.getKey(biome);
				missing.add((key == null ? biome.translationKey : key)
					+ " (wants '" + biome.translationKey + "')");
			}
		}
		if (!missing.isEmpty()) {
			BetterOPlenty.LOGGER.error("Biome names: {} biome(s) have no lang entry and will show "
				+ "the raw key in the F3 overlay: {}", missing.size(), String.join(", ", missing));
			return;
		}
		BetterOPlenty.LOGGER.info("Biome names: all {} ported biome(s) resolve a display name.",
			registered.size());
	}

	private static void auditPartition() {
		List<BOPClimate.Cell> cells = BOPClimate.partition();
		double volume = 0.0;
		double worstError = 0.0;
		String worstId = "";
		for (BOPClimate.Cell cell : cells) {
			volume += cell.volume();
			double error = Math.abs(cell.volume() - cell.owner.share) / cell.owner.share;
			if (error > worstError) {
				worstError = error;
				worstId = cell.owner.id;
			}
		}

		if (Math.abs(volume - 1.0) > 1.0e-6) {
			BetterOPlenty.LOGGER.error(
				"Climate partition covers {} of the space, not 1.0 -- boxes overlap or leave a hole.",
				String.format("%.9f", volume));
			return;
		}
		BetterOPlenty.LOGGER.info(
			"Climate partition: {} boxes tiling the space exactly; worst box-vs-plan share error {}% ({}).",
			cells.size(), String.format("%.4f", worstError * 100.0), worstId);
	}

	private static void auditFullRoster() {
		BOPBiomeRangeMap probe = new BOPBiomeRangeMap();
		Map<String, Biome> stubs = new HashMap<>();
		for (BOPClimate.Cell cell : BOPClimate.partition()) {
			probe.addRange(stubs.computeIfAbsent(cell.owner.id, id -> new Biome("audit." + id)),
				cell.toRange());
		}
		probe.addRange(stubs.computeIfAbsent("ocean", id -> new Biome("audit." + id)),
			BOPClimate.oceanRange());
		try {
			probe.lock();
		} catch (RuntimeException e) {
			BetterOPlenty.LOGGER.error("Full-roster climate partition does not resolve: {}", e.getMessage());
			return;
		}

		int steps = 64;
		int holes = 0;
		for (int a = 0; a < steps; a++) {
			for (int b = 0; b < steps; b++) {
				for (int c = 0; c < steps; c++) {
					if (probe.lookupBiome((a + 0.5) / steps, (b + 0.5) / steps, 0.5, (c + 0.5) / steps) == null) {
						holes++;
					}
				}
			}
		}
		if (holes > 0) {
			BetterOPlenty.LOGGER.error("Full-roster climate map has {} unresolved points of {}.",
				holes, steps * steps * steps);
			return;
		}
		BetterOPlenty.LOGGER.info(
			"Climate map holds all {} planned biomes with no gaps ({} points sampled).",
			stubs.size(), steps * steps * steps);
	}

	private static void auditLiveMap(@NotNull List<BiomeGenBase> registered) {
		if (BiomeProviderBOP.hasGaps()) {
			BetterOPlenty.LOGGER.error("The live biome range map has gaps; worldgen will produce "
				+ "null biomes. Check BOPBiomes.addRanges.");
			return;
		}
		BetterOPlenty.LOGGER.info("Live biome range map: no gaps, {} biome(s) ported of {} planned.",
			registered.size(), BOPClimate.roster().size());
	}

	private static void auditTags(@NotNull List<BiomeGenBase> registered) {
		List<String> missing = new ArrayList<>();
		int tagged = 0;
		for (BOPClimate.Planned planned : BOPClimate.roster()) {
			BiomeGenBase biome = BOPBiomes.byPlanId(planned.id);
			if (biome == null) {
				continue;
			}
			boolean wantsSnow = planned.tags.contains(BiomeTags.HAS_SURFACE_SNOW);
			if (wantsSnow && !biome.hasTag(BiomeTags.HAS_SURFACE_SNOW)) {
				missing.add(planned.id);
			}
			if (biome.hasTag(BiomeTags.HAS_SURFACE_SNOW)) {
				tagged++;
			}
		}
		if (!missing.isEmpty()) {
			BetterOPlenty.LOGGER.error("Biomes below temperature 0.15 with no HAS_SURFACE_SNOW: {}. "
				+ "They will generate bare with no warning.", String.join(", ", missing));
			return;
		}
		BetterOPlenty.LOGGER.info(
			"Biome tags: every ported biome carries what its climate calls for ({} of {} snowy).",
			tagged, registered.size());
	}

	private static void auditTerrainShape(@NotNull List<BiomeGenBase> registered) {
		StringBuilder sb = new StringBuilder();
		double flattest = Double.MAX_VALUE;
		double tallest = -Double.MAX_VALUE;
		int shaping = 0;
		for (BiomeGenBase biome : registered) {
			double base = DensityGeneratorBOP.baseSurfaceY(biome.rootHeight, WORLD_HEIGHT);
			double relief = DensityGeneratorBOP.reliefMultiplier(biome.heightVariation);
			String key = Registries.BIOMES.getKey(biome);

			if (BOPBiomes.terrainPlaced().contains(biome)) {
				sb.append(String.format("%n    %-40s rootHeight %.2f -> inert, placed by terrain",
					key == null ? biome.translationKey : key, biome.rootHeight));
				continue;
			}

			shaping++;
			flattest = Math.min(flattest, base);
			tallest = Math.max(tallest, base);
			sb.append(String.format("%n    %-40s rootHeight %.2f -> base y=%.0f, relief x%.2f",
				key == null ? biome.translationKey : key, biome.rootHeight, base, relief));
		}
		BetterOPlenty.LOGGER.info("Per-biome terrain is live; base surface spans y={} to y={}"
				+ " across {} climate-placed biome(s) (sea level is y=128); {} more are placed by"
				+ " terrain adjacency and shape none of it:{}",
			Math.round(flattest), Math.round(tallest), shaping,
			registered.size() - shaping, sb);
	}

	private static void auditLandWaterMask() {
		double sea = BOPClimate.SEA_SHARE;
		List<String> wrong = new ArrayList<>();
		for (BOPClimate.Cell cell : BOPClimate.partition()) {
			BiomeRange range = cell.toRange();
			if (range.getMinAltitude() != sea || range.getMaxAltitude() != 1.0) {
				wrong.add(cell.owner.id);
			}
		}
		if (!wrong.isEmpty()) {
			BetterOPlenty.LOGGER.error("Land/water mask: {} climate cell(s) do not span the land"
					+ " band [{}, 1.0] on the altitude axis: {}. A land biome that reaches below the"
					+ " sea share is placed on the sea floor, which is the defect this exists to"
					+ " remove.", wrong.size(), String.format("%.2f", sea),
				String.join(", ", wrong.subList(0, Math.min(8, wrong.size()))));
			return;
		}

		BiomeRange ocean = BOPClimate.oceanRange();
		double oceanVolume = (ocean.getMaxAltitude() - ocean.getMinAltitude()) *
			(ocean.getMaxTemperature() - ocean.getMinTemperature()) *
			(ocean.getMaxHumidity() - ocean.getMinHumidity()) *
			(ocean.getMaxVariety() - ocean.getMinVariety());
		if (Math.abs(oceanVolume - sea) > 1.0e-9) {
			BetterOPlenty.LOGGER.error("Land/water mask: the sea's box is worth {} of the world but"
					+ " the split says {}. The two must be complements or the axis has a hole.",
				String.format("%.6f", oceanVolume), String.format("%.6f", sea));
			return;
		}

		BiomeGenBase oceanBiome = BOPBiomes.OCEAN;
		if (oceanBiome == null) {
			BetterOPlenty.LOGGER.error("Land/water mask: no ocean biome is registered, so {}% of the"
					+ " world has no biome of its own and BOPBiomeRangeMap.lock() will refuse to"
					+ " build.", String.format("%.1f", 100 * sea));
			return;
		}
		double base = DensityGeneratorBOP.baseSurfaceY(oceanBiome.rootHeight, WORLD_HEIGHT);
		if (base >= WORLD_HEIGHT / 2.0) {
			BetterOPlenty.LOGGER.error("Land/water mask: the ocean biome's rootHeight {} puts its"
					+ " base at y={}, which is not under the sea at y={}. The mask would be placing"
					+ " dry land called Ocean.", oceanBiome.rootHeight, Math.round(base),
				WORLD_HEIGHT / 2);
			return;
		}

		BetterOPlenty.LOGGER.info("Land/water mask: {}% of the world is sea, held by {} alone"
				+ " (rootHeight {} -> sea floor y={}, {} blocks of water); the other {}% is land and"
				+ " all {} climate cells sit in it. Rarity is unmoved -- the partition is a partition"
				+ " of the land, exactly as BiomeLayerBiomes drew from surfaceBiomes only for a cell"
				+ " BiomeLayerCreate had already called land.",
			String.format("%.1f", 100 * sea), Registries.BIOMES.getKey(oceanBiome),
			oceanBiome.rootHeight, Math.round(base), Math.round(WORLD_HEIGHT / 2.0 - base),
			String.format("%.1f", 100 * (1 - sea)), BOPClimate.partition().size());
	}

	private static void auditIdHeadroom() {
		int used = Registries.BIOMES.size();
		int remaining = BOPBiomes.BIOME_ID_CEILING - used;
		BetterOPlenty.LOGGER.info(
			"Biome registry: {} of {} ids used, {} free. Roster coverage is the range-map line above.",
			used, BOPBiomes.BIOME_ID_CEILING, remaining);
	}
}
