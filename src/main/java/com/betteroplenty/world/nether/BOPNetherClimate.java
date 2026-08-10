package com.betteroplenty.world.nether;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.world.BOPClimate;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.provider.BiomeProviderNether;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class BOPNetherClimate {

	private BOPNetherClimate() {
	}

	public static final int BOP_SHARE_NUMERATOR = 6;

	public static final int BOP_SHARE_DENOMINATOR = 14;

	public static final double BOP_BAND_START =
		1.0 - (double) BOP_SHARE_NUMERATOR / BOP_SHARE_DENOMINATOR;

	private static final List<Biome> ROSTER = new ArrayList<>();

	public static void add(@NotNull Biome biome) {
		if (ROSTER.size() >= BOP_SHARE_NUMERATOR) {
			throw new IllegalStateException("The BOP Nether roster is sized for "
				+ BOP_SHARE_NUMERATOR + " biomes and the share arithmetic above is written against"
				+ " that number. Adding a seventh means re-deciding BOP_SHARE_NUMERATOR, which"
				+ " moves every band and reshuffles existing worlds.");
		}
		ROSTER.add(biome);
	}

	@NotNull
	public static List<Biome> roster() {
		return ROSTER;
	}

	public static boolean isBOPNether(@Nullable Biome biome) {
		if (biome == null) {
			return false;
		}
		for (int i = 0; i < ROSTER.size(); i++) {
			if (ROSTER.get(i) == biome) {
				return true;
			}
		}
		return false;
	}

	private static final int CELL = 64;

	public static final double WARP = CELL / 2.0;

	public static int cellCoord(int coord, double warp) {
		return Math.floorDiv((int) Math.floor(coord + warp), CELL);
	}

	public static double cellCenter(int cell) {
		return cell * CELL + CELL / 2.0;
	}

	public static int slotAt(int x, int z, double warpX, double warpZ, long worldSeed) {
		int cellX = cellCoord(x, warpX);
		int cellZ = cellCoord(z, warpZ);

		long h = worldSeed * 0x9E3779B97F4A7C15L
			+ cellX * 0xBF58476D1CE4E5B9L
			+ cellZ * 0x94D049BB133111EBL;
		h ^= h >>> 30;
		h *= 0xBF58476D1CE4E5B9L;
		h ^= h >>> 27;
		h *= 0x94D049BB133111EBL;
		h ^= h >>> 31;

		return (int) Math.floorMod(h, (long) ROSTER.size());
	}

	@NotNull
	public static Biome[] allBiomes() {
		List<Biome> all = new ArrayList<>(ROSTER);
		for (Biome biome : BiomeProviderNether.allBiomes()) {
			if (!all.contains(biome)) {
				all.add(biome);
			}
		}
		return all.toArray(new Biome[0]);
	}

	@Nullable
	public static Biome lookup(double temperature, double humidity, double altitude,
	                           double varietyShare, int slot) {
		if (varietyShare >= BOP_BAND_START && !ROSTER.isEmpty()) {
			return ROSTER.get(Math.floorMod(slot, ROSTER.size()));
		}

		return BiomeProviderNether.brm.lookupBiome(temperature, humidity, altitude, 0.0);
	}

	public static void logPartition() {
		BetterOPlenty.LOGGER.info(
			"BOP Nether roster: {} biomes on variety share [{}, 1.0], {}/{} of the Nether; "
				+ "BTA's {} keep [0.0, {}) with their own arrangement.",
			ROSTER.size(), String.format("%.4f", BOP_BAND_START),
			BOP_SHARE_NUMERATOR, BOP_SHARE_DENOMINATOR,
			BiomeProviderNether.allBiomes().length, String.format("%.4f", BOP_BAND_START));
	}
}
