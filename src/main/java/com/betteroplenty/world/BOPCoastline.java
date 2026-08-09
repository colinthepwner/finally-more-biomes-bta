package com.betteroplenty.world;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BOPCoastline {

	private static final int MARK_ABYSS = 2;

	private static final int MARK_CORAL = 5;

	private static final int MARK_KELP = 5;

	private static final int OCEAN_POOL_SIZE = 4;

	static final int SPECIAL_PERMILLE = 87;

	private static final long SEED_CREATE = 1L;
	private static final long SEED_SHORE = 1000L;

	private static final int BEACH_CELL = 32;

	private static final int OCEAN_CELL = 256;

	private static final int[] WATER_DEPTHS = {112, 120, 128};

	private static final int LAND = 0;
	private static final int WATER_SHALLOW = 1;
	private static final int WATER_MID = 2;
	private static final int WATER_DEEP = 3;

	private static final int PAD = 2;

	private static final int CHUNK_SAMPLES = 4;
	private static final int GRID = CHUNK_SAMPLES + PAD * 2 + 1;

	@NotNull private final World world;
	@NotNull private final DensityGeneratorBOP density;

	private static boolean loggedRoster;

	public BOPCoastline(@NotNull World world, @NotNull DensityGeneratorBOP density) {
		this.world = world;
		this.density = density;
	}

	public void apply(@NotNull Chunk chunk) {
		BiomeGenBase shore = BOPBiomes.SHORE;
		if (shore == null) {
			return;
		}
		logRosterOnce();

		long seed = this.world.getRandomSeed();
		int[] profile = this.density.waterProfile(
			chunk.pos.x * CHUNK_SAMPLES - PAD, chunk.pos.z * CHUNK_SAMPLES - PAD, GRID, WATER_DEPTHS);

		for (int lx = 0; lx < 16; lx++) {
			for (int lz = 0; lz < 16; lz++) {
				int gi = PAD + lx / 4;
				int gj = PAD + lz / 4;
				int here = profile[gi * GRID + gj];

				int worldX = chunk.pos.x * 16 + lx;
				int worldZ = chunk.pos.z * 16 + lz;

				BiomeGenBase replacement = here == LAND
					? beachAt(profile, gi, gj, worldX, worldZ, seed)
					: oceanAt(here, worldX, worldZ, seed);

				if (replacement != null) {
					setColumnBiome(chunk, lx, lz, replacement);
				}
			}
		}
	}

	@Nullable
	private BiomeGenBase beachAt(@NotNull int[] profile, int gi, int gj, int worldX, int worldZ, long seed) {
		boolean coastal = profile[(gi - 1) * GRID + gj] != LAND
			|| profile[(gi + 1) * GRID + gj] != LAND
			|| profile[gi * GRID + (gj - 1)] != LAND
			|| profile[gi * GRID + (gj + 1)] != LAND;
		if (!coastal) {
			return null;
		}

		LayerRandom random = LayerRandom.at(SEED_SHORE, seed,
			Math.floorDiv(worldX, BEACH_CELL), Math.floorDiv(worldZ, BEACH_CELL));
		switch (random.nextInt(3)) {
			case 0:
				return BOPBiomes.SHORE;
			case 1:
				return BOPBiomes.BEACH_GRAVEL;
			default:
				return BOPBiomes.BEACH_OVERGROWN;
		}
	}

	@Nullable
	private BiomeGenBase oceanAt(int water, int worldX, int worldZ, long seed) {
		LayerRandom random = LayerRandom.at(SEED_CREATE, seed,
			Math.floorDiv(worldX, OCEAN_CELL), Math.floorDiv(worldZ, OCEAN_CELL));

		boolean marked = random.nextInt(100) < MARK_ABYSS
			|| random.nextInt(100) < MARK_CORAL
			|| random.nextInt(100) < MARK_KELP;
		if (!marked) {
			return null;
		}

		if (random.nextInt(OCEAN_POOL_SIZE) == 0) {
			return null;
		}

		switch (water) {
			case WATER_DEEP:
				return BOPBiomes.OCEAN_ABYSS;
			case WATER_MID:
				return BOPBiomes.OCEAN_KELP;
			case WATER_SHALLOW:
			default:
				return BOPBiomes.OCEAN_CORAL;
		}
	}

	private static void setColumnBiome(@NotNull Chunk chunk, int lx, int lz, @NotNull BiomeGenBase biome) {
		byte id = (byte) Registries.BIOMES.getNumericIdOfItem(biome);
		for (int sectionY = 0; sectionY < 16; sectionY++) {
			ChunkSection section = chunk.getSection(sectionY);
			section.biome[lz * 16 + lx] = id;
			section.biome[256 + lz * 16 + lx] = id;
		}
	}

	private static void logRosterOnce() {
		if (loggedRoster) {
			return;
		}
		loggedRoster = true;
		BetterOPlenty.LOGGER.info(
			"Coastline pass live: beach pool [{}, {}, {}] on {}-block cells, ocean variants "
				+ "[{}, {}, {}] on {}-block cells at {} per mille of the sea.",
			name(BOPBiomes.SHORE), name(BOPBiomes.BEACH_GRAVEL), name(BOPBiomes.BEACH_OVERGROWN),
			BEACH_CELL, name(BOPBiomes.OCEAN_CORAL), name(BOPBiomes.OCEAN_KELP),
			name(BOPBiomes.OCEAN_ABYSS), OCEAN_CELL, SPECIAL_PERMILLE);
	}

	@NotNull
	private static String name(@Nullable BiomeGenBase biome) {
		if (biome == null) {
			return "MISSING";
		}
		String key = Registries.BIOMES.getKey(biome);
		return key == null ? biome.translationKey : key;
	}

	static final class LayerRandom {
		private static final long MULTIPLIER = 6364136223846793005L;
		private static final long ADDEND = 1442695040888963407L;

		private final long worldGenSeed;
		private long chunkSeed;

		private LayerRandom(long worldGenSeed, long chunkSeed) {
			this.worldGenSeed = worldGenSeed;
			this.chunkSeed = chunkSeed;
		}

		@NotNull
		static LayerRandom at(long layerSeed, long worldSeed, long cellX, long cellZ) {
			long baseSeed = layerSeed;
			for (int i = 0; i < 3; i++) {
				baseSeed *= baseSeed * MULTIPLIER + ADDEND;
				baseSeed += layerSeed;
			}

			long worldGenSeed = worldSeed;
			for (int i = 0; i < 3; i++) {
				worldGenSeed *= worldGenSeed * MULTIPLIER + ADDEND;
				worldGenSeed += baseSeed;
			}

			long chunkSeed = worldGenSeed;
			chunkSeed *= chunkSeed * MULTIPLIER + ADDEND;
			chunkSeed += cellX;
			chunkSeed *= chunkSeed * MULTIPLIER + ADDEND;
			chunkSeed += cellZ;
			chunkSeed *= chunkSeed * MULTIPLIER + ADDEND;
			chunkSeed += cellX;
			chunkSeed *= chunkSeed * MULTIPLIER + ADDEND;
			chunkSeed += cellZ;
			return new LayerRandom(worldGenSeed, chunkSeed);
		}

		int nextInt(int bound) {
			int value = (int) ((this.chunkSeed >> 24) % bound);
			if (value < 0) {
				value += bound;
			}

			this.chunkSeed *= this.chunkSeed * MULTIPLIER + ADDEND;
			this.chunkSeed += this.worldGenSeed;
			return value;
		}
	}
}
