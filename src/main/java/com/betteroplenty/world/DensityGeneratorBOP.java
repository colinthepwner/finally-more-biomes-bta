package com.betteroplenty.world;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.perlin.DensityGenerator;
import net.minecraft.core.world.noise.FractalNoise2D;
import net.minecraft.core.world.noise.FractalNoise3D;
import net.minecraft.core.world.noise.ImprovedPerlinNoise;
import net.minecraft.core.world.noise.Noise2D;
import net.minecraft.core.world.noise.Noise3D;
import org.jetbrains.annotations.NotNull;

public class DensityGeneratorBOP implements DensityGenerator {

	private static final int GRID_SIZE = 5;

	private static final int BLEND_RADIUS = 2;
	private static final int BLEND_SIZE = GRID_SIZE + BLEND_RADIUS * 2;

	private static final float[] BLEND_WEIGHTS = new float[(BLEND_RADIUS * 2 + 1) * (BLEND_RADIUS * 2 + 1)];

	static {
		for (int dx = -BLEND_RADIUS; dx <= BLEND_RADIUS; dx++) {
			for (int dz = -BLEND_RADIUS; dz <= BLEND_RADIUS; dz++) {
				BLEND_WEIGHTS[(dx + BLEND_RADIUS) + (dz + BLEND_RADIUS) * (BLEND_RADIUS * 2 + 1)] =
					(float) (10.0 / Math.sqrt(dx * dx + dz * dz + 0.2));
			}
		}
	}

	private static final double BASELINE_RELIEF = 0.2 * 0.9 + 0.1;

	private static final double MAX_RELIEF_MULTIPLIER = 1.5;

	private static final double RELIEF_RESPONSE = tunable("reliefResponse", 0.2);

	private static double tunable(String name, double shipped) {
		String override = System.getProperty("betteroplenty." + name);
		if (override == null) {
			return shipped;
		}
		try {
			double value = Double.parseDouble(override);
			BetterOPlenty.LOGGER.warn("Terrain constant '{}' overridden: {} instead of the shipped {}."
				+ " This world's terrain is NOT what the port ships.", name, value, shipped);
			return value;
		} catch (NumberFormatException e) {
			BetterOPlenty.LOGGER.error("Terrain constant '{}' is not a number: '{}'. Using {}.",
				name, override, shipped);
			return shipped;
		}
	}

	private static double rootHeightDatum(double depthBaseSize) {
		return 8.0 / depthBaseSize;
	}

	private static final double LANDFORM_GAIN = tunable("landformGain", 0.2);

	@NotNull private final World world;
	@NotNull private final Noise3D minLimitNoise;
	@NotNull private final Noise3D maxLimitNoise;
	@NotNull private final Noise3D mainNoise;
	@NotNull private final Noise2D scaleNoise;
	@NotNull private final Noise2D depthNoise;
	@NotNull private final Noise2D smoothingRiverNoise;
	@NotNull private final Noise2D carvingRiverNoise;

	public DensityGeneratorBOP(@NotNull World world) {
		long seed = world.getRandomSeed();
		this.world = world;
		this.minLimitNoise = new FractalNoise3D<>(ImprovedPerlinNoise.genOctaves(seed, 16, 0));
		this.maxLimitNoise = new FractalNoise3D<>(ImprovedPerlinNoise.genOctaves(seed, 16, 16));
		this.mainNoise = new FractalNoise3D<>(ImprovedPerlinNoise.genOctaves(seed, 8, 32));
		this.scaleNoise = new FractalNoise2D<>(ImprovedPerlinNoise.genOctaves(seed, 10, 48));
		this.depthNoise = new FractalNoise2D<>(ImprovedPerlinNoise.genOctaves(seed, 16, 58));
		this.smoothingRiverNoise = new FractalNoise2D<>(ImprovedPerlinNoise.genOctaves(seed, 1));
		this.carvingRiverNoise = new FractalNoise2D<>(ImprovedPerlinNoise.genOctaves(seed + 1L, 1));
	}

	@NotNull
	@Override
	public double[] generateDensityMap(@NotNull Chunk chunk) {
		int terrainHeight = this.world.getWorldType().getMaxY(this.world) + 1
			- this.world.getWorldType().getMinY(this.world);
		int xSize = GRID_SIZE;
		int ySize = terrainHeight / 8 + 1;
		int zSize = GRID_SIZE;
		int x = chunk.pos.x * 4;
		int z = chunk.pos.z * 4;
		double[] densityMapArray = new double[xSize * ySize * zSize];

		double depthBaseSize = terrainHeight / 16.0 + 0.5;
		double upperLimitScale = 512.0;

		double[] scaleArray = this.scaleNoise.getRegion(null, x, z, xSize, zSize, 1.121, 1.121);
		double[] depthArray = this.depthNoise.getRegion(null, x, z, xSize, zSize, 200.0, 200.0);
		double[] mainNoiseArray = this.mainNoise.getRegion(null, x, 0, z, xSize, ySize, zSize,
			8.555150000000001, 4.277575000000001, 8.555150000000001);
		double[] minLimitArray = this.minLimitNoise.getRegion(null, x, 0, z, xSize, ySize, zSize,
			684.412, 684.412, 684.412);
		double[] maxLimitArray = this.maxLimitNoise.getRegion(null, x, 0, z, xSize, ySize, zSize,
			684.412, 684.412, 684.412);

		float[] blendedRootHeight = new float[xSize * zSize];
		float[] blendedRelief = new float[xSize * zSize];
		shapeAt(chunk, blendedRootHeight, blendedRelief);

		int mainIndex = 0;
		int depthScaleIndex = 0;
		int xSizeScale = 16 / xSize;

		for (int dx = 0; dx < xSize; dx++) {
			int ix = dx * xSizeScale + xSizeScale / 2;

			for (int dz = 0; dz < zSize; dz++) {
				int iz = dz * xSizeScale + xSizeScale / 2;
				double temperature = chunk.temperature[ix * 16 + iz];
				double humidity = chunk.humidity[ix * 16 + iz] * temperature;
				humidity = 1.0 - humidity;
				humidity *= humidity;
				humidity *= humidity;
				humidity = 1.0 - humidity;
				double scale = (scaleArray[depthScaleIndex] + 256.0) / 512.0;
				scale *= humidity;
				if (scale > 1.0) {
					scale = 1.0;
				}

				double depth = BOPLandform.landform(depthArray[depthScaleIndex]);
				if (depth < 0.0) {
					scale = 0.0;
				}

				if (scale < 0.0) {
					scale = 0.0;
				}

				scale += 0.5;

				scale *= reliefMultiplier(blendedRelief[dx * zSize + dz]);

				depth = depth * LANDFORM_GAIN
					+ (blendedRootHeight[dx * zSize + dz] * 4.0
						- rootHeightDatum(depthBaseSize)) / 8.0;

				depth = depth * (depthBaseSize * 2.0) / 16.0;
				double offsetY = depthBaseSize + depth * 4.0;
				depthScaleIndex++;

				for (int dy = 0; dy < ySize; dy++) {
					double densityOffset = (dy - offsetY) * 12.0 / scale;
					if (densityOffset < 0.0) {
						densityOffset *= 4.0;
					}

					double minDensity = minLimitArray[mainIndex] / upperLimitScale;
					double maxDensity = maxLimitArray[mainIndex] / 512.0;
					double mainDensity = (mainNoiseArray[mainIndex] / 10.0 + 1.0) / 2.0;
					double density;
					if (mainDensity < 0.0) {
						density = minDensity;
					} else if (mainDensity > 1.0) {
						density = maxDensity;
					} else {
						density = minDensity + (maxDensity - minDensity) * mainDensity;
					}

					density -= densityOffset;
					if (dy > ySize - 4) {
						double densityMod = (dy - (ySize - 4)) / 3.0F;
						density = density * (1.0 - densityMod) + -10.0 * densityMod;
					}

					densityMapArray[mainIndex] = density;
					mainIndex++;
				}
			}
		}

		modifyDensityMapSmoothingRivers(chunk.pos.x, chunk.pos.z, terrainHeight, densityMapArray);
		modifyDensityMapCarvingRivers(chunk.pos.x, chunk.pos.z, terrainHeight, densityMapArray);
		return densityMapArray;
	}

	private void shapeAt(@NotNull Chunk chunk, @NotNull float[] rootHeightOut, @NotNull float[] reliefOut) {

		Biome[] neighbourhood = BiomeProviderBOP.sampleGrid(this.world.getBiomeProvider(),
			chunk.pos.x * 16 - BLEND_RADIUS * 4, chunk.pos.z * 16 - BLEND_RADIUS * 4,
			BLEND_SIZE, BLEND_SIZE, 4);

		float[] rootHeights = new float[neighbourhood.length];
		float[] reliefs = new float[neighbourhood.length];
		for (int i = 0; i < neighbourhood.length; i++) {
			rootHeights[i] = rootHeightOf(neighbourhood[i]);
			reliefs[i] = reliefOf(neighbourhood[i]);
		}

		blendGrid(rootHeights, reliefs, BLEND_SIZE, BLEND_SIZE, rootHeightOut, reliefOut);
	}

	public static void blendGrid(@NotNull float[] rootHeights, @NotNull float[] reliefs,
	                             int inWidth, int inHeight,
	                             @NotNull float[] rootHeightOut, @NotNull float[] reliefOut) {
		int outWidth = inWidth - BLEND_RADIUS * 2;
		int outHeight = inHeight - BLEND_RADIUS * 2;

		if (outWidth < 1 || outHeight < 1) {
			throw new IllegalArgumentException("Blend input is " + inWidth + "x" + inHeight
				+ ", too small for a radius of " + BLEND_RADIUS + "; it needs at least "
				+ (BLEND_RADIUS * 2 + 1) + " on each axis.");
		}
		int weightsWidth = BLEND_RADIUS * 2 + 1;

		for (int dx = 0; dx < outWidth; dx++) {
			for (int dz = 0; dz < outHeight; dz++) {
				float centreRootHeight = rootHeights[(dx + BLEND_RADIUS) * inHeight + (dz + BLEND_RADIUS)];
				float rootAcc = 0.0f;
				float reliefAcc = 0.0f;
				float weightAcc = 0.0f;

				for (int ox = -BLEND_RADIUS; ox <= BLEND_RADIUS; ox++) {
					for (int oz = -BLEND_RADIUS; oz <= BLEND_RADIUS; oz++) {
						int index = (dx + BLEND_RADIUS + ox) * inHeight + (dz + BLEND_RADIUS + oz);
						float rootHeight = rootHeights[index];
						float weight = BLEND_WEIGHTS[(ox + BLEND_RADIUS) + (oz + BLEND_RADIUS) * weightsWidth]
							/ (rootHeight + 2.0f);
						if (rootHeight > centreRootHeight) {
							weight /= 2.0f;
						}
						rootAcc += rootHeight * weight;
						reliefAcc += reliefs[index] * weight;
						weightAcc += weight;
					}
				}

				rootHeightOut[dx * outHeight + dz] = rootAcc / weightAcc;
				reliefOut[dx * outHeight + dz] = reliefAcc / weightAcc;
			}
		}
	}

	private static float rootHeightOf(Biome biome) {
		return biome instanceof BiomeGenBase bop ? bop.rootHeight : 0.1f;
	}

	private static float reliefOf(Biome biome) {
		return biome instanceof BiomeGenBase bop ? bop.heightVariation : 0.2f;
	}

	@NotNull
	public int[] waterProfile(int gridX, int gridZ, int size, @NotNull int[] depths) {
		int terrainHeight = this.world.getWorldType().getMaxY(this.world) + 1
			- this.world.getWorldType().getMinY(this.world);
		double depthBaseSize = terrainHeight / 16.0 + 0.5;

		int[] yIndex = new int[depths.length];
		int minYIndex = Integer.MAX_VALUE;
		int maxYIndex = Integer.MIN_VALUE;
		for (int i = 0; i < depths.length; i++) {
			yIndex[i] = depths[i] / 8;
			minYIndex = Math.min(minYIndex, yIndex[i]);
			maxYIndex = Math.max(maxYIndex, yIndex[i]);
		}
		int ySize = maxYIndex - minYIndex + 1;

		double[] scaleArray = this.scaleNoise.getRegion(null, gridX, gridZ, size, size, 1.121, 1.121);
		double[] depthArray = this.depthNoise.getRegion(null, gridX, gridZ, size, size, 200.0, 200.0);
		double[] mainNoiseArray = this.mainNoise.getRegion(null, gridX, minYIndex, gridZ, size, ySize, size,
			8.555150000000001, 4.277575000000001, 8.555150000000001);
		double[] minLimitArray = this.minLimitNoise.getRegion(null, gridX, minYIndex, gridZ, size, ySize, size,
			684.412, 684.412, 684.412);
		double[] maxLimitArray = this.maxLimitNoise.getRegion(null, gridX, minYIndex, gridZ, size, ySize, size,
			684.412, 684.412, 684.412);

		Biome[] neighbourhood = BiomeProviderBOP.sampleGrid(this.world.getBiomeProvider(),
			(gridX - BLEND_RADIUS) * 4, (gridZ - BLEND_RADIUS) * 4,
			size + BLEND_RADIUS * 2, size + BLEND_RADIUS * 2, 4);
		float[] rootHeights = new float[neighbourhood.length];
		float[] reliefs = new float[neighbourhood.length];
		for (int i = 0; i < neighbourhood.length; i++) {
			rootHeights[i] = rootHeightOf(neighbourhood[i]);
			reliefs[i] = reliefOf(neighbourhood[i]);
		}
		float[] blendedRootHeight = new float[size * size];
		float[] blendedRelief = new float[size * size];
		blendGrid(rootHeights, reliefs, size + BLEND_RADIUS * 2, size + BLEND_RADIUS * 2,
			blendedRootHeight, blendedRelief);

		double[][] climate = BiomeProviderBOP.sampleClimateGrid(
			this.world.getBiomeProvider(), gridX * 4, gridZ * 4, size, size, 4);
		double[] temperatures = climate[0];
		double[] humidities = climate[1];

		int[] out = new int[size * size];
		for (int dx = 0; dx < size; dx++) {
			for (int dz = 0; dz < size; dz++) {
				int column = dx * size + dz;

				double temperature = temperatures[column];
				double humidity = humidities[column] * temperature;
				humidity = 1.0 - humidity;
				humidity *= humidity;
				humidity *= humidity;
				humidity = 1.0 - humidity;
				double scale = (scaleArray[column] + 256.0) / 512.0;
				scale *= humidity;
				if (scale > 1.0) {
					scale = 1.0;
				}

				double depth = BOPLandform.landform(depthArray[column]);
				if (depth < 0.0) {
					scale = 0.0;
				}

				if (scale < 0.0) {
					scale = 0.0;
				}

				scale += 0.5;
				scale *= reliefMultiplier(blendedRelief[column]);
				depth = depth * LANDFORM_GAIN
					+ (blendedRootHeight[column] * 4.0 - rootHeightDatum(depthBaseSize)) / 8.0;
				depth = depth * (depthBaseSize * 2.0) / 16.0;
				double offsetY = depthBaseSize + depth * 4.0;

				int open = 0;
				for (int i = depths.length - 1; i >= 0; i--) {
					int dy = yIndex[i] - minYIndex;
					int index = dx * ySize * size + dz * ySize + dy;

					double densityOffset = (yIndex[i] - offsetY) * 12.0 / scale;
					if (densityOffset < 0.0) {
						densityOffset *= 4.0;
					}

					double minDensity = minLimitArray[index] / 512.0;
					double maxDensity = maxLimitArray[index] / 512.0;
					double mainDensity = (mainNoiseArray[index] / 10.0 + 1.0) / 2.0;
					double density;
					if (mainDensity < 0.0) {
						density = minDensity;
					} else if (mainDensity > 1.0) {
						density = maxDensity;
					} else {
						density = minDensity + (maxDensity - minDensity) * mainDensity;
					}

					density -= densityOffset;
					if (density > 0.0) {
						break;
					}
					open++;
				}
				out[column] = open;
			}
		}

		return out;
	}

	public static double baseSurfaceY(float rootHeight, int worldHeight) {
		double depthBaseSize = worldHeight / 16.0 + 0.5;
		double depth = (rootHeight * 4.0 - rootHeightDatum(depthBaseSize)) / 8.0 *
			(depthBaseSize * 2.0) / 16.0;
		return (depthBaseSize + depth * 4.0) * 8.0;
	}

	public static double reliefMultiplier(double heightVariation) {
		double ratio = (heightVariation * 0.9 + 0.1) / BASELINE_RELIEF;
		if (ratio <= 1.0) {
			return ratio;
		}
		return Math.min(Math.pow(ratio, RELIEF_RESPONSE), MAX_RELIEF_MULTIPLIER);
	}

	public static int blendRadius() {
		return BLEND_RADIUS;
	}

	private void modifyDensityMapSmoothingRivers(int chunkX, int chunkZ, int terrainHeight,
	                                            @NotNull double[] densityMapArray) {
		int xSize = GRID_SIZE;
		int ySize = terrainHeight / 8 + 1;
		int zSize = GRID_SIZE;
		int x = chunkX * 4;
		int z = chunkZ * 4;
		double riverLevel = 0.0;
		double[] largeSmoothing = this.smoothingRiverNoise.getRegion(null, x, z, xSize, zSize, 1.0 / (1000.0 / xSize), 1.0 / (1000.0 / zSize));
		double[] mediumSmoothing = this.smoothingRiverNoise.getRegion(null, x, z, xSize, zSize, 1.0 / (50.0 / xSize), 1.0 / (50.0 / zSize));
		double[] smallSmoothing = this.smoothingRiverNoise.getRegion(null, x, z, xSize, zSize, 1.0 / (10.0 / xSize), 1.0 / (10.0 / zSize));
		double[] riverRadiuses = new double[]{0.006, 0.024, 0.036};
		double[] riverMults = new double[]{20.0, 60.0, 60.0};

		for (int dx = 0; dx < xSize; dx++) {
			for (int dz = 0; dz < zSize; dz++) {
				double sample = largeSmoothing[dx * zSize + dz]
					+ mediumSmoothing[dx * zSize + dz] * 0.04
					+ smallSmoothing[dx * zSize + dz] * 0.004;
				double finalSample = 0.0;

				for (int i = 0; i < riverRadiuses.length; i++) {
					if (!(sample < riverLevel - riverRadiuses[i]) && !(sample > riverLevel + riverRadiuses[i])) {
						double modSample = sample - riverLevel;
						modSample /= riverRadiuses[i];
						modSample = Math.abs(modSample);
						modSample = -modSample;
						modSample = ++modSample * riverMults[i];
						finalSample += modSample;
					}
				}

				for (int dy = 0; dy < ySize; dy++) {
					int index = dx * ySize * zSize + dz * ySize + dy;
					if (dy < ySize / 2) {
						densityMapArray[index] -= finalSample * (dy / (ySize / 2.0)) * (dy / (ySize / 2.0));
					} else {
						densityMapArray[index] -= finalSample;
					}
				}
			}
		}
	}

	private void modifyDensityMapCarvingRivers(int chunkX, int chunkZ, int terrainHeight,
	                                           @NotNull double[] densityMapArray) {
		int xSize = GRID_SIZE;
		int ySize = terrainHeight / 8 + 1;
		int zSize = GRID_SIZE;
		int x = chunkX * 4;
		int z = chunkZ * 4;
		int maxUndergroundRiverHeight = 2;
		double riverLevel = 0.0;
		double[] largeCarving = this.carvingRiverNoise.getRegion(null, x, z, xSize, zSize, 1.0 / (1000.0 / xSize), 1.0 / (1000.0 / zSize));
		double[] mediumCarving = this.carvingRiverNoise.getRegion(null, x, z, xSize, zSize, 1.0 / (50.0 / xSize), 1.0 / (50.0 / zSize));
		double[] smallCarving = this.carvingRiverNoise.getRegion(null, x, z, xSize, zSize, 1.0 / (10.0 / xSize), 1.0 / (10.0 / zSize));
		double[] riverRadiuses = new double[]{0.012, 0.018, 0.024};
		double[] riverMults = new double[]{20.0, 60.0, 60.0};

		for (int dx = 0; dx < xSize; dx++) {
			for (int dz = 0; dz < zSize; dz++) {
				double sample = largeCarving[dx * zSize + dz]
					+ mediumCarving[dx * zSize + dz] * 0.04
					+ smallCarving[dx * zSize + dz] * 0.004;
				double finalSample = 0.0;

				for (int i = 0; i < riverRadiuses.length; i++) {
					if (!(sample < riverLevel - riverRadiuses[i]) && !(sample > riverLevel + riverRadiuses[i])) {
						double heightFactor = sample - riverLevel;
						heightFactor /= riverRadiuses[i];
						heightFactor = Math.abs(heightFactor);
						heightFactor = -heightFactor;
						heightFactor = ++heightFactor * riverMults[i];
						finalSample += heightFactor;
					}
				}

				if (!(finalSample <= 0.0)) {
					for (int dy = ySize / 2 - maxUndergroundRiverHeight; dy < ySize / 2 + maxUndergroundRiverHeight; dy++) {
						if (dy >= 0) {
							double heightFactor = dy;
							heightFactor -= ySize / 2;
							heightFactor /= maxUndergroundRiverHeight;
							heightFactor = Math.abs(heightFactor);
							heightFactor = -heightFactor;
							heightFactor++;
							densityMapArray[dx * ySize * zSize + dz * ySize + dy] -= finalSample * heightFactor;
						}
					}
				}
			}
		}
	}
}
