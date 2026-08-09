package com.betteroplenty.world;

import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.data.BiomeRange;
import net.minecraft.core.world.biome.provider.BiomeProvider;
import net.minecraft.core.world.noise.FractalNoise2D;
import net.minecraft.core.world.noise.SimplexNoise;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Set;

public final class BiomeProviderBOP extends BiomeProvider {

	private static final BOPBiomeRangeMap brm = new BOPBiomeRangeMap();

	private final FractalNoise2D<?> temperatureMap;
	private final FractalNoise2D<?> humidityMap;
	private final FractalNoise2D<?> varietyMap;
	private final FractalNoise2D<?> noiseMap;

	private final BOPLandform landform;

	private static final double CLIMATE_WIDENING = 0.90;

	private static final double CLIMATE_FUZZ_WEIGHT = 0.004;
	private static final double NOISE_X_SCALE = 0.1;
	private static final double NOISE_Z_SCALE = 0.1;
	private static final double NOISE_LACUNARITY = 1.0;

	private static final double TEMPERATURE_X_SCALE = 0.0125 / CLIMATE_WIDENING;
	private static final double TEMPERATURE_Z_SCALE = 0.0125 / CLIMATE_WIDENING;
	private static final double TEMPERATURE_LACUNARITY = 0.25;
	private static final double TEMPERATURE_NOISE = CLIMATE_FUZZ_WEIGHT;
	private static final double HUMIDITY_X_SCALE = 0.025 / CLIMATE_WIDENING;
	private static final double HUMIDITY_Z_SCALE = 0.025 / CLIMATE_WIDENING;
	private static final double HUMIDITY_LACUNARITY = 0.3;
	private static final double HUMIDITY_NOISE = CLIMATE_FUZZ_WEIGHT;
	private static final double VARIETY_X_SCALE = 0.25 / CLIMATE_WIDENING;
	private static final double VARIETY_Z_SCALE = 0.25 / CLIMATE_WIDENING;
	private static final double VARIETY_LACUNARITY = 0.3;
	private static final double VARIETY_NOISE = 0.0;

	private static final double GRADIENT_RATIO = 2.0;

	private static final int CLIMATE_OCTAVES = 4;

	public BiomeProviderBOP(@NotNull World world) {
		super(world);
		long seed = world.getRandomSeed();
		this.temperatureMap = new FractalNoise2D<>(SimplexNoise.genOctaves(seed * 9871L, CLIMATE_OCTAVES));
		this.humidityMap = new FractalNoise2D<>(SimplexNoise.genOctaves(seed * 39811L, CLIMATE_OCTAVES));
		this.varietyMap = new FractalNoise2D<>(SimplexNoise.genOctaves(seed, CLIMATE_OCTAVES));
		this.noiseMap = new FractalNoise2D<>(SimplexNoise.genOctaves(seed * 543321L, 2));
		this.landform = new BOPLandform(seed);
	}

	public static void init() {
		brm.clear();
		BOPBiomes.addRanges(brm);
		brm.lock();
		BetterOPlenty.LOGGER.info("Biome range map locked: {} boxes, {} biomes, no gaps. "
				+ "Land/water mask live: {}% of the world is sea, below landform {}.",
			brm.rangeCount(), brm.allBiomes().size(),
			String.format("%.1f", 100 * BOPClimate.SEA_SHARE),
			String.format("%.4f", landformAtSeaLevel()));
		if (Boolean.getBoolean("betteroplenty.landformProbe")) {
			BOPLandform.probe();
		}
		if (Boolean.getBoolean("betteroplenty.biomeMap")) {
			BetterOPlenty.LOGGER.info("BOP biome range map:\n{}", brm.describe());
			brm.writeDebugImage(new File("bop-biome-map.png"), 1024);
		}
	}

	public static boolean hasGaps() {
		return brm.hasGaps();
	}

	private static double landformAtSeaLevel() {
		double lo = -0.36;
		double hi = 0.13;
		for (int i = 0; i < 48; i++) {
			double mid = (lo + hi) * 0.5;
			if (BOPLandform.altitude(mid) < BOPClimate.SEA_SHARE) {
				lo = mid;
			} else {
				hi = mid;
			}
		}
		return (lo + hi) * 0.5;
	}

	@Override
	public Biome[] getBiomes(Biome[] biomes, double[] temperatures, double[] humidities, double[] varieties,
	                         int x, int y, int z, int xSize, int ySize, int zSize) {
		int area = xSize * zSize;

		double[] placementTemperature = placement(this.temperatureMap, TEMPERATURE_LACUNARITY,
			TEMPERATURE_X_SCALE, TEMPERATURE_Z_SCALE, 0.15, 0.7, TEMPERATURE_NOISE, x, z, xSize, zSize);
		double[] placementHumidity = placement(this.humidityMap, HUMIDITY_LACUNARITY,
			HUMIDITY_X_SCALE, HUMIDITY_Z_SCALE, 0.15, 0.5, HUMIDITY_NOISE, x, z, xSize, zSize);
		double[] placementVariety = placement(this.varietyMap, VARIETY_LACUNARITY,
			VARIETY_X_SCALE, VARIETY_Z_SCALE, 0.15, 0.5, VARIETY_NOISE, x, z, xSize, zSize);

		if (temperatures == null || temperatures.length < area) temperatures = new double[area];
		if (humidities == null || humidities.length < area) humidities = new double[area];
		if (varieties == null || varieties.length < area) varieties = new double[area];
		for (int i = 0; i < area; i++) {
			temperatures[i] = MathHelper.clamp(placementTemperature[i], 0.0, 1.0);
			humidities[i] = MathHelper.clamp(placementHumidity[i], 0.0, 1.0);
			varieties[i] = MathHelper.clamp(placementVariety[i], 0.0, 1.0);
		}

		if (biomes == null || biomes.length < xSize * ySize * zSize) {
			biomes = new Biome[xSize * ySize * zSize];
		}

		double[] mask = this.landform.region(x, z, xSize, zSize, 1);

		for (int dx = 0; dx < xSize; dx++) {
			for (int dz = 0; dz < zSize; dz++) {
				double temperature = placementTemperature[dx * zSize + dz];
				double humidity = placementHumidity[dx * zSize + dz];
				double variety = placementVariety[dx * zSize + dz];
				double landform = mask[dx * zSize + dz];

				for (int dy = 0; dy < ySize; dy++) {
					biomes[dy * xSize * zSize + dz * xSize + dx] =
						this.lookupBiome(temperature, humidity, landform, variety);
				}
			}
		}

		return biomes;
	}

	@Override
	public double[] getTemperatures(double[] temperatures, int x, int z, int xSize, int zSize) {
		double[] raw = placement(this.temperatureMap, TEMPERATURE_LACUNARITY, TEMPERATURE_X_SCALE,
			TEMPERATURE_Z_SCALE, 0.15, 0.7, TEMPERATURE_NOISE, x, z, xSize, zSize);
		return clampInto(temperatures, raw, xSize * zSize);
	}

	@Override
	public double[] getHumidities(double[] humidities, int x, int z, int xSize, int zSize) {
		double[] raw = placement(this.humidityMap, HUMIDITY_LACUNARITY, HUMIDITY_X_SCALE,
			HUMIDITY_Z_SCALE, 0.15, 0.5, HUMIDITY_NOISE, x, z, xSize, zSize);
		return clampInto(humidities, raw, xSize * zSize);
	}

	@Override
	public double[] getVarieties(double[] varieties, int x, int z, int xSize, int zSize) {
		double[] raw = placement(this.varietyMap, VARIETY_LACUNARITY, VARIETY_X_SCALE,
			VARIETY_Z_SCALE, 0.15, 0.5, VARIETY_NOISE, x, z, xSize, zSize);
		return clampInto(varieties, raw, xSize * zSize);
	}

	@Override
	public double[] getBiomenesses(double[] biomenesses, int x, int y, int z, int xSize, int ySize, int zSize) {
		if (biomenesses == null || biomenesses.length < xSize * ySize * zSize) {
			biomenesses = new double[xSize * ySize * zSize];
		}

		double[] placementTemperature = placement(this.temperatureMap, TEMPERATURE_LACUNARITY,
			TEMPERATURE_X_SCALE, TEMPERATURE_Z_SCALE, 0.15, 0.7, TEMPERATURE_NOISE, x, z, xSize, zSize);
		double[] placementHumidity = placement(this.humidityMap, HUMIDITY_LACUNARITY,
			HUMIDITY_X_SCALE, HUMIDITY_Z_SCALE, 0.15, 0.5, HUMIDITY_NOISE, x, z, xSize, zSize);
		double[] placementVariety = placement(this.varietyMap, VARIETY_LACUNARITY,
			VARIETY_X_SCALE, VARIETY_Z_SCALE, 0.15, 0.5, VARIETY_NOISE, x, z, xSize, zSize);

		double[] mask = this.landform.region(x, z, xSize, zSize, 1);

		for (int dx = 0; dx < xSize; dx++) {
			for (int dz = 0; dz < zSize; dz++) {
				double temperature = BOPClimate.TEMPERATURE.share(placementTemperature[dx * zSize + dz]);
				double humidity = BOPClimate.HUMIDITY.share(placementHumidity[dx * zSize + dz]);
				double variety = BOPClimate.VARIETY.share(placementVariety[dx * zSize + dz]);
				double altitude = BOPLandform.altitude(mask[dx * zSize + dz]);

				for (int dy = 0; dy < ySize; dy++) {
					biomenesses[dy * xSize * zSize + dz * xSize + dx] =
						biomeness(temperature, humidity, altitude, variety);
				}
			}
		}

		return biomenesses;
	}

	private static double biomeness(double temperature, double humidity, double altitude, double variety) {
		return biomeness(brm.lookupBiome(temperature, humidity, altitude, variety),
			temperature, humidity, altitude, variety);
	}

	private static double biomeness(@Nullable Biome biome, double temperature, double humidity,
	                                double altitude, double variety) {
		if (biome == null) {
			return 0.0;
		}
		Set<BiomeRange> ranges = brm.getRanges(biome);
		if (ranges == null) {
			return 0.0;
		}

		double best = 0.0;
		for (BiomeRange range : ranges) {
			if (!contains(range, temperature, humidity, altitude, variety)) {
				continue;
			}
			double weight = depth(temperature, range.getMinTemperature(), range.getMaxTemperature()) *
				depth(humidity, range.getMinHumidity(), range.getMaxHumidity()) *
				depth(altitude, range.getMinAltitude(), range.getMaxAltitude()) *
				depth(variety, range.getMinVariety(), range.getMaxVariety());
			if (weight > best) {
				best = weight;
			}
		}
		return best;
	}

	private static boolean contains(@NotNull BiomeRange range, double temperature, double humidity,
	                                double altitude, double variety) {
		return within(temperature, range.getMinTemperature(), range.getMaxTemperature())
			&& within(humidity, range.getMinHumidity(), range.getMaxHumidity())
			&& within(altitude, range.getMinAltitude(), range.getMaxAltitude())
			&& within(variety, range.getMinVariety(), range.getMaxVariety());
	}

	private static boolean within(double value, double min, double max) {
		return value >= min && (value < max || (max >= 1.0 && value <= max));
	}

	private static double depth(double value, double min, double max) {
		double span = max - min;
		if (span <= 0.0) {
			return 1.0;
		}
		double u = (value - min) / span;
		if (min <= 0.0 && u <= 0.5) {
			return 1.0;
		}
		if (max >= 1.0 && u >= 0.5) {
			return 1.0;
		}
		return MathHelper.clamp(1.0 - Math.abs(u * 2.0 - 1.0), 0.0, 1.0);
	}

	@Override
	public Biome lookupBiome(double temperature, double humidity, double landform, double variety) {
		return brm.lookupBiome(
			BOPClimate.TEMPERATURE.share(temperature),
			BOPClimate.HUMIDITY.share(humidity),
			BOPLandform.altitude(landform),
			BOPClimate.VARIETY.share(variety));
	}

	@NotNull
	public static Biome[] sampleGrid(@NotNull BiomeProvider provider, int x, int z,
	                                 int xSize, int zSize, int stride) {
		Biome[] out = new Biome[xSize * zSize];
		if (!(provider instanceof BiomeProviderBOP bop) || x % stride != 0 || z % stride != 0) {
			for (int dx = 0; dx < xSize; dx++) {
				for (int dz = 0; dz < zSize; dz++) {
					out[dx * zSize + dz] = provider.getBiome(x + dx * stride, 64, z + dz * stride);
				}
			}
			return out;
		}

		int originX = x / stride;
		int originZ = z / stride;
		double[] temperature = bop.placement(bop.temperatureMap, TEMPERATURE_LACUNARITY,
			TEMPERATURE_X_SCALE, TEMPERATURE_Z_SCALE, 0.15, 0.7, TEMPERATURE_NOISE,
			originX, originZ, xSize, zSize, stride);
		double[] humidity = bop.placement(bop.humidityMap, HUMIDITY_LACUNARITY,
			HUMIDITY_X_SCALE, HUMIDITY_Z_SCALE, 0.15, 0.5, HUMIDITY_NOISE,
			originX, originZ, xSize, zSize, stride);
		double[] variety = bop.placement(bop.varietyMap, VARIETY_LACUNARITY,
			VARIETY_X_SCALE, VARIETY_Z_SCALE, 0.15, 0.5, VARIETY_NOISE,
			originX, originZ, xSize, zSize, stride);

		double[] mask = bop.landform.region(x, z, xSize, zSize, stride);

		for (int i = 0; i < out.length; i++) {
			out[i] = bop.lookupBiome(temperature[i], humidity[i], mask[i], variety[i]);
		}
		return out;
	}

	public static final class Blend {

		@NotNull public final Biome[] biomes;

		@NotNull public final double[] weights;

		private Blend(@NotNull Biome[] biomes, @NotNull double[] weights) {
			this.biomes = biomes;
			this.weights = weights;
		}
	}

	@NotNull
	public static Blend sampleBlendGrid(@NotNull BiomeProvider provider, int x, int z,
	                                    int xSize, int zSize, int stride) {
		int area = xSize * zSize;
		if (!(provider instanceof BiomeProviderBOP bop) || x % stride != 0 || z % stride != 0) {
			Biome[] biomes = new Biome[area];
			double[] weights = new double[area];
			for (int dx = 0; dx < xSize; dx++) {
				for (int dz = 0; dz < zSize; dz++) {
					biomes[dx * zSize + dz] = provider.getBiome(x + dx * stride, 64, z + dz * stride);
					weights[dx * zSize + dz] = 1.0;
				}
			}
			return new Blend(biomes, weights);
		}

		int originX = x / stride;
		int originZ = z / stride;
		double[] temperature = bop.placement(bop.temperatureMap, TEMPERATURE_LACUNARITY,
			TEMPERATURE_X_SCALE, TEMPERATURE_Z_SCALE, 0.15, 0.7, TEMPERATURE_NOISE,
			originX, originZ, xSize, zSize, stride);
		double[] humidity = bop.placement(bop.humidityMap, HUMIDITY_LACUNARITY,
			HUMIDITY_X_SCALE, HUMIDITY_Z_SCALE, 0.15, 0.5, HUMIDITY_NOISE,
			originX, originZ, xSize, zSize, stride);
		double[] variety = bop.placement(bop.varietyMap, VARIETY_LACUNARITY,
			VARIETY_X_SCALE, VARIETY_Z_SCALE, 0.15, 0.5, VARIETY_NOISE,
			originX, originZ, xSize, zSize, stride);

		double[] mask = bop.landform.region(x, z, xSize, zSize, stride);

		Biome[] biomes = new Biome[area];
		double[] weights = new double[area];
		for (int i = 0; i < area; i++) {
			double t = BOPClimate.TEMPERATURE.share(temperature[i]);
			double h = BOPClimate.HUMIDITY.share(humidity[i]);
			double v = BOPClimate.VARIETY.share(variety[i]);
			double a = BOPLandform.altitude(mask[i]);
			biomes[i] = brm.lookupBiome(t, h, a, v);
			weights[i] = biomeness(biomes[i], t, h, a, v);
		}
		return new Blend(biomes, weights);
	}

	@NotNull
	public static double[][] sampleClimateGrid(@NotNull BiomeProvider provider, int x, int z,
	                                           int xSize, int zSize, int stride) {
		int area = xSize * zSize;
		if (!(provider instanceof BiomeProviderBOP bop) || x % stride != 0 || z % stride != 0) {
			double[] temperature = new double[area];
			double[] humidity = new double[area];
			for (int dx = 0; dx < xSize; dx++) {
				for (int dz = 0; dz < zSize; dz++) {
					temperature[dx * zSize + dz] = provider.getTemperature(x + dx * stride, z + dz * stride);
					humidity[dx * zSize + dz] = provider.getHumidity(x + dx * stride, z + dz * stride);
				}
			}
			return new double[][]{temperature, humidity};
		}

		int originX = x / stride;
		int originZ = z / stride;
		double[] temperature = bop.placement(bop.temperatureMap, TEMPERATURE_LACUNARITY,
			TEMPERATURE_X_SCALE, TEMPERATURE_Z_SCALE, 0.15, 0.7, TEMPERATURE_NOISE,
			originX, originZ, xSize, zSize, stride);
		double[] humidity = bop.placement(bop.humidityMap, HUMIDITY_LACUNARITY,
			HUMIDITY_X_SCALE, HUMIDITY_Z_SCALE, 0.15, 0.5, HUMIDITY_NOISE,
			originX, originZ, xSize, zSize, stride);
		return new double[][]{clampInto(null, temperature, area), clampInto(null, humidity, area)};
	}

	@NotNull
	private double[] placement(@NotNull FractalNoise2D<?> map, double lacunarity,
	                           double xScale, double zScale, double amplitude, double offset,
	                           double fuzzWeight, int x, int z, int xSize, int zSize) {
		return placement(map, lacunarity, xScale, zScale, amplitude, offset, fuzzWeight,
			x, z, xSize, zSize, 1);
	}

	@NotNull
	private double[] placement(@NotNull FractalNoise2D<?> map, double lacunarity,
	                           double xScale, double zScale, double amplitude, double offset,
	                           double fuzzWeight, int x, int z, int xSize, int zSize, int stride) {
		double persistence = persistenceFor(lacunarity);
		double[] field = map.setLacunarity(lacunarity).setPersistence(persistence)
			.addRegion(null, x, z, xSize, zSize, xScale * stride, zScale * stride,
				normalisedAmplitude(persistence));
		double[] fuzzField = this.noiseMap.setLacunarity(NOISE_LACUNARITY)
			.getRegion(null, x, z, xSize, zSize, NOISE_X_SCALE * stride, NOISE_Z_SCALE * stride);

		double[] out = new double[xSize * zSize];
		for (int i = 0; i < out.length; i++) {
			double fuzziness = fuzzField[i] * 1.1 + 0.5;
			out[i] = (field[i] * amplitude + offset) * (1.0 - fuzzWeight) + fuzziness * fuzzWeight;
		}
		return out;
	}

	private static double persistenceFor(double lacunarity) {
		return GRADIENT_RATIO / lacunarity;
	}

	private static double normalisedAmplitude(double persistence) {
		double shipped = 0.0;
		double relayered = 0.0;
		for (int octave = 0; octave < CLIMATE_OCTAVES; octave++) {
			shipped += Math.pow(FractalNoise2D.DEFAULT_PERSISTENCE, 2 * octave);
			relayered += Math.pow(persistence, 2 * octave);
		}
		return Math.sqrt(shipped) / Math.sqrt(relayered);
	}

	@NotNull
	private static double[] clampInto(double[] out, @NotNull double[] raw, int length) {
		if (out == null || out.length < length) {
			out = new double[length];
		}
		for (int i = 0; i < length; i++) {
			out[i] = MathHelper.clamp(raw[i], 0.0, 1.0);
		}
		return out;
	}
}
