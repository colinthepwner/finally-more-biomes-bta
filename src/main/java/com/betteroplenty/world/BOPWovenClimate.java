package com.betteroplenty.world;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.mixin.BiomeProviderOverworldAccessor;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.provider.BiomeProviderOverworld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class BOPWovenClimate {

	public static final int BTA_SHARE_NUMERATOR = 18;
	public static final int BTA_SHARE_DENOMINATOR = 119;

	public static final double BOP_BAND_START =
		(double) BTA_SHARE_NUMERATOR / BTA_SHARE_DENOMINATOR;

	private BOPWovenClimate() {}

	private static double stretch(double share, double lo, double hi) {
		if (hi <= lo) return 0.0;
		double t = (share - lo) / (hi - lo);
		return t < 0.0 ? 0.0 : (t > 1.0 ? 1.0 : t);
	}

	@Nullable
	public static Biome lookup(double temperature, double humidity,
	                           double temperatureShare, double humidityShare,
	                           double altitude, double varietyShare,
	                           @NotNull BOPBiomeRangeMap bop) {

		if (altitude < BOPClimate.SEA_SHARE) {
			return bop.lookupBiome(temperatureShare, humidityShare, altitude, varietyShare);
		}

		if (varietyShare < BOP_BAND_START) {

			Biome bta = BiomeProviderOverworldAccessor.betteroplenty$brm().lookupBiome(
				temperature, humidity, 0.5,
				stretch(varietyShare, 0.0, BOP_BAND_START));

			if (bta != null) return bta;
		}

		return bop.lookupBiome(temperatureShare, humidityShare, altitude,
			stretch(varietyShare, BOP_BAND_START, 1.0));
	}

	@NotNull
	public static Biome[] allBiomes() {
		Set<Biome> all = new LinkedHashSet<>();

		for (Biome biome : BOPBiomes.allBiomes()) {
			if (biome != null) all.add(biome);
		}
		Biome[] bta = BiomeProviderOverworld.allBiomes();
		if (bta != null) {
			for (Biome biome : bta) {
				if (biome != null) all.add(biome);
			}
		}
		List<Biome> out = new ArrayList<>(all);
		return out.toArray(new Biome[0]);
	}

	public static void logPartition() {
		Biome[] bta = BiomeProviderOverworld.allBiomes();
		BetterOPlenty.LOGGER.info(
			"Woven overworld: BTA's {} biomes on land variety share [0.0, {}), {}/{} of the land; "
				+ "BOP's {} on [{}, 1.0]. The sea below altitude share {} stays BOP's, whole.",
			bta == null ? 0 : bta.length, String.format("%.4f", BOP_BAND_START),
			BTA_SHARE_NUMERATOR, BTA_SHARE_DENOMINATOR,
			BOPBiomes.allBiomes().length, String.format("%.4f", BOP_BAND_START),
			String.format("%.3f", BOPClimate.SEA_SHARE));
	}
}
