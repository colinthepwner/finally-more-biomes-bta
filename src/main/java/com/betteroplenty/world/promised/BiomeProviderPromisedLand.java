package com.betteroplenty.world.promised;

import com.betteroplenty.world.BOPBiomes;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.provider.BiomeProvider;
import net.minecraft.core.world.noise.FractalNoise2D;
import net.minecraft.core.world.noise.SimplexNoise;

import java.util.Arrays;

public class BiomeProviderPromisedLand extends BiomeProvider {

	private static final int CELL = 96;

	private static final double WARP_AMPLITUDE = CELL / 2.0;

	private static final double WARP_SCALE = 1.0 / 320.0;

	private final FractalNoise2D<?> warpNoise;
	private final long seed;

	public BiomeProviderPromisedLand(World world) {
		super(world);
		this.seed = world.getRandomSeed();

		this.warpNoise = new FractalNoise2D<>(SimplexNoise.genOctaves(this.seed * 6449L, 3));
	}

	private Biome[] roster() {
		return new Biome[]{
			BOPBiomes.PROMISED_LAND_PLAINS,
			BOPBiomes.PROMISED_LAND_FOREST,
			BOPBiomes.PROMISED_LAND_SHRUB,
			BOPBiomes.PROMISED_LAND_SWAMP,
		};
	}

	private int slotAt(int x, int z, double warpX, double warpZ) {
		int cellX = MathHelper.floor((x + warpX) / (double) CELL);
		int cellZ = MathHelper.floor((z + warpZ) / (double) CELL);

		long h = this.seed;
		h = h * 6364136223846793005L + (cellX * 0x9E3779B97F4A7C15L);
		h = h * 6364136223846793005L + (cellZ * 0xC2B2AE3D27D4EB4FL);
		h ^= (h >>> 33);
		h *= 0xFF51AFD7ED558CCDL;
		h ^= (h >>> 33);
		return (int) Math.floorMod(h, 4L);
	}

	@Override
	public Biome[] getBiomes(Biome[] biomes, double[] temperatures, double[] humidities,
	                         double[] varieties, int x, int y, int z,
	                         int xSize, int ySize, int zSize) {
		if (biomes == null || biomes.length != xSize * ySize * zSize) {
			biomes = new Biome[xSize * ySize * zSize];
		}
		if (temperatures == null || temperatures.length < xSize * zSize) {
			temperatures = new double[xSize * zSize];
		}
		if (humidities == null || humidities.length < xSize * zSize) {
			humidities = new double[xSize * zSize];
		}
		if (varieties == null || varieties.length < xSize * zSize) {
			varieties = new double[xSize * zSize];
		}
		Arrays.fill(temperatures, 2.0);
		Arrays.fill(humidities, 2.0);

		double[] warpA = this.warpNoise.getRegion(null, x, z, xSize, zSize, WARP_SCALE, WARP_SCALE);
		double[] warpB = this.warpNoise.getRegion(null, x + 4093, z - 2711, xSize, zSize,
			WARP_SCALE, WARP_SCALE);

		Biome[] roster = roster();

		for (int xx = 0; xx < xSize; xx++) {
			for (int zz = 0; zz < zSize; zz++) {
				int i = xx * zSize + zz;
				int slot = slotAt(x + xx, z + zz,
					warpA[i] * WARP_AMPLITUDE, warpB[i] * WARP_AMPLITUDE);
				varieties[i] = slot / 3.0;
				for (int yy = 0; yy < ySize; yy++) {
					biomes[(xx * zSize + zz) * ySize + yy] = roster[slot];
				}
			}
		}
		return biomes;
	}

	@Override
	public double[] getTemperatures(double[] temperatures, int x, int z, int xSize, int zSize) {
		if (temperatures == null || temperatures.length < xSize * zSize) {
			temperatures = new double[xSize * zSize];
		}
		Arrays.fill(temperatures, 2.0);
		return temperatures;
	}

	@Override
	public double[] getHumidities(double[] humidities, int x, int z, int xSize, int zSize) {
		if (humidities == null || humidities.length < xSize * zSize) {
			humidities = new double[xSize * zSize];
		}
		Arrays.fill(humidities, 2.0);
		return humidities;
	}

	@Override
	public double[] getVarieties(double[] varieties, int x, int z, int xSize, int zSize) {
		if (varieties == null || varieties.length < xSize * zSize) {
			varieties = new double[xSize * zSize];
		}
		double[] warpA = this.warpNoise.getRegion(null, x, z, xSize, zSize, WARP_SCALE, WARP_SCALE);
		double[] warpB = this.warpNoise.getRegion(null, x + 4093, z - 2711, xSize, zSize,
			WARP_SCALE, WARP_SCALE);
		for (int xx = 0; xx < xSize; xx++) {
			for (int zz = 0; zz < zSize; zz++) {
				int i = xx * zSize + zz;
				varieties[i] = slotAt(x + xx, z + zz,
					warpA[i] * WARP_AMPLITUDE, warpB[i] * WARP_AMPLITUDE) / 3.0;
			}
		}
		return varieties;
	}

	@Override
	public double[] getBiomenesses(double[] biomenesses, int x, int y, int z,
	                               int xSize, int ySize, int zSize) {
		if (biomenesses == null || biomenesses.length < xSize * ySize * zSize) {
			biomenesses = new double[xSize * ySize * zSize];
		}
		Arrays.fill(biomenesses, 1.0);
		return biomenesses;
	}

	@Override
	public Biome lookupBiome(double temperature, double humidity, double altitude, double variety) {
		Biome[] roster = roster();
		int slot = (int) Math.round(MathHelper.clamp(variety, 0.0, 1.0) * 3.0);
		return roster[Math.max(0, Math.min(3, slot))];
	}
}
