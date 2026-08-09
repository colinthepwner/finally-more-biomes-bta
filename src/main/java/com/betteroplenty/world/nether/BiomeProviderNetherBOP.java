package com.betteroplenty.world.nether;

import com.betteroplenty.world.BOPClimate;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.provider.BiomeProvider;
import net.minecraft.core.world.noise.FractalNoise2D;
import net.minecraft.core.world.noise.FractalNoise3D;
import net.minecraft.core.world.noise.SimplexNoise;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class BiomeProviderNetherBOP extends BiomeProvider {

	private static final double TEMP_SCALE = 1.0 / 60.0;
	private static final double DOWNFALL_SCALE = 1.0 / 30.0;
	private static final double NOISE_SCALE = 1.0 / 6.0;
	private static final double TEMP_LACUNARITY = 0.25;
	private static final double DOWNFALL_LACUNARITY = 1.0 / 3.0;
	private static final double FUZZ_LACUNARITY = 0.5882352941176471;

	private static final double CLIMATE_WIDENING = 0.90;
	private static final double VARIETY_X_SCALE = 0.25 / CLIMATE_WIDENING;
	private static final double VARIETY_Z_SCALE = 0.25 / CLIMATE_WIDENING;
	private static final double VARIETY_LACUNARITY = 0.3;

	private static final double WARP_SCALE = 0.08;
	private static final double WARP_LACUNARITY = 0.4;

	private final FractalNoise2D<?> temperatureNoise;
	private final FractalNoise2D<?> downfallNoise;
	private final FractalNoise2D<?> fuzzinessNoise;
	private final FractalNoise2D<?> varietyNoise;
	private final FractalNoise2D<?> warpNoise;

	private final long worldSeed;

	public BiomeProviderNetherBOP(@NotNull World world) {
		super(world);
		long seed = world.getRandomSeed();
		this.worldSeed = seed;

		this.temperatureNoise = new FractalNoise3D<>(SimplexNoise.genOctaves(seed * 9871L, 4));
		this.downfallNoise = new FractalNoise3D<>(SimplexNoise.genOctaves(seed * 39811L, 4));
		this.fuzzinessNoise = new FractalNoise2D<>(SimplexNoise.genOctaves(seed * 543321L, 4));

		this.varietyNoise = new FractalNoise2D<>(SimplexNoise.genOctaves(seed, 4));

		this.warpNoise = new FractalNoise2D<>(SimplexNoise.genOctaves(seed * 7757L, 4));
	}

	@Override
	public Biome[] getBiomes(Biome[] biomes, double[] temperatures, double[] humidities, double[] varieties,
	                         int x, int y, int z, int xSize, int ySize, int zSize) {
		if (biomes == null || biomes.length < xSize * ySize * zSize) {
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

		this.temperatureNoise.setLacunarity(TEMP_LACUNARITY)
			.getRegion(temperatures, x, z, xSize, xSize, TEMP_SCALE, TEMP_SCALE);
		this.downfallNoise.setLacunarity(DOWNFALL_LACUNARITY)
			.getRegion(humidities, x, z, xSize, xSize, DOWNFALL_SCALE, DOWNFALL_SCALE);
		double[] noises = this.fuzzinessNoise.setLacunarity(FUZZ_LACUNARITY)
			.getRegion(null, x, z, xSize, xSize, NOISE_SCALE, NOISE_SCALE);

		double[] rawVariety = this.varietyNoise.setLacunarity(VARIETY_LACUNARITY)
			.getRegion(null, x, z, xSize, zSize, VARIETY_X_SCALE, VARIETY_Z_SCALE);

		double[] warp = this.warpNoise.setLacunarity(WARP_LACUNARITY)
			.getRegion(null, x, z, xSize, zSize, WARP_SCALE, WARP_SCALE);
		double[] warp2 = this.warpNoise.setLacunarity(WARP_LACUNARITY)
			.getRegion(null, x + 1731, z - 907, xSize, zSize, WARP_SCALE, WARP_SCALE);

		for (int xx = 0; xx < xSize; xx++) {
			for (int zz = 0; zz < zSize; zz++) {
				int i = xx * zSize + zz;

				double a = noises[i] * 1.1 + 0.5;
				double temperature = (temperatures[i] * 0.15 + 0.7) * 0.99 + a * 0.01;
				double downfall = (humidities[i] * 0.15 + 0.5) * 0.998 + a * 0.002;
				temperature = 1.0 - (1.0 - temperature) * (1.0 - temperature);
				temperature = clamp01(temperature);
				downfall = clamp01(downfall);

				double varietyRaw = rawVariety[i] * 0.15 + 0.5;
				double varietyShare = BOPClimate.VARIETY.share(varietyRaw);

				temperatures[i] = temperature;
				humidities[i] = downfall;
				varieties[i] = clamp01(varietyRaw);

				int slot = BOPNetherClimate.slotAt(x + xx, z + zz,
					warp[i] * BOPNetherClimate.WARP, warp2[i] * BOPNetherClimate.WARP,
					this.worldSeed);

				for (int yy = 0; yy < ySize; yy++) {

					double altitude = clamp01(((double) y + yy) / 128.0);
					biomes[yy * xSize * zSize + zz * xSize + xx] =
						BOPNetherClimate.lookup(temperature, downfall, altitude, varietyShare, slot);
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
		this.temperatureNoise.setLacunarity(TEMP_LACUNARITY)
			.getRegion(temperatures, x, z, xSize, xSize, TEMP_SCALE, TEMP_SCALE);
		double[] noises = this.fuzzinessNoise.setLacunarity(FUZZ_LACUNARITY)
			.getRegion(null, x, z, xSize, zSize, NOISE_SCALE, NOISE_SCALE);

		for (int i = 0; i < xSize * zSize; i++) {
			double a = noises[i] * 1.1 + 0.5;
			double temperature = (temperatures[i] * 0.15 + 0.7) * 0.99 + a * 0.01;
			temperature = 1.0 - (1.0 - temperature) * (1.0 - temperature);
			temperatures[i] = clamp01(temperature);
		}
		return temperatures;
	}

	@Override
	public double[] getHumidities(double[] humidities, int x, int z, int xSize, int zSize) {
		if (humidities == null || humidities.length < xSize * zSize) {
			humidities = new double[xSize * zSize];
		}

		return this.downfallNoise.setLacunarity(0.5)
			.getRegion(humidities, x, z, xSize, zSize, DOWNFALL_SCALE, DOWNFALL_SCALE);
	}

	@Override
	public double[] getVarieties(double[] varieties, int x, int z, int xSize, int zSize) {
		if (varieties == null || varieties.length < xSize * zSize) {
			varieties = new double[xSize * zSize];
		}
		double[] raw = this.varietyNoise.setLacunarity(VARIETY_LACUNARITY)
			.getRegion(null, x, z, xSize, zSize, VARIETY_X_SCALE, VARIETY_Z_SCALE);
		for (int i = 0; i < xSize * zSize; i++) {
			varieties[i] = clamp01(raw[i] * 0.15 + 0.5);
		}
		return varieties;
	}

	@Override
	public double[] getBiomenesses(double[] biomenesses, int x, int y, int z, int xSize, int ySize, int zSize) {
		if (biomenesses == null || biomenesses.length < xSize * ySize * zSize) {
			biomenesses = new double[xSize * ySize * zSize];
		}

		Arrays.fill(biomenesses, 1.0);
		return biomenesses;
	}

	@Override
	public Biome lookupBiome(double temperature, double humidity, double altitude, double variety) {

		return BOPNetherClimate.lookup(temperature, humidity, altitude,
			BOPClimate.VARIETY.share(variety), 0);
	}

	private static double clamp01(double value) {
		return value < 0.0 ? 0.0 : (value > 1.0 ? 1.0 : value);
	}
}
