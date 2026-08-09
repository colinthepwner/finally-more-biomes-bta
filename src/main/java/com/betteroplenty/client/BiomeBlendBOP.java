package com.betteroplenty.client;

import com.betteroplenty.world.BiomeProviderBOP;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public final class BiomeBlendBOP {
	private BiomeBlendBOP() {}

	public static final boolean PERF_CONTROL = Boolean.getBoolean("betteroplenty.perfControl");

	public static final int RADIUS = 8;

	public static final int STEP = 4;

	private static final int SPAN = RADIUS * 2 / STEP + 1;

	public interface SampleColor {
		int colorAt(@NotNull WorldSource source, @NotNull Biome biome, @NotNull TilePosc tilePos);
	}

	public static int blend(@NotNull WorldSource source, @NotNull TilePosc centre,
	                        @NotNull SampleColor sampler, @NotNull TilePos probe) {
		probe.x = centre.x();
		probe.y = centre.y();
		probe.z = centre.z();

		Biome centerBiome = source.getBlockBiome(probe);
		if (centerBiome != null) {
			probe.x = centre.x() - RADIUS;
			probe.z = centre.z() - RADIUS;
			Biome b1 = source.getBlockBiome(probe);

			probe.x = centre.x() + RADIUS;
			probe.z = centre.z() - RADIUS;
			Biome b2 = source.getBlockBiome(probe);

			probe.x = centre.x() - RADIUS;
			probe.z = centre.z() + RADIUS;
			Biome b3 = source.getBlockBiome(probe);

			probe.x = centre.x() + RADIUS;
			probe.z = centre.z() + RADIUS;
			Biome b4 = source.getBlockBiome(probe);

			if (b1 == centerBiome && b2 == centerBiome && b3 == centerBiome && b4 == centerBiome) {
				probe.x = centre.x();
				probe.z = centre.z();
				return sampler.colorAt(source, centerBiome, probe);
			}
		}

		double totalWeight = 0.0;
		double r = 0.0;
		double g = 0.0;
		double b = 0.0;

		for (int i = 0; i < KERNEL_DX.length; i++) {
			probe.x = centre.x() + KERNEL_DX[i];
			probe.z = centre.z() + KERNEL_DZ[i];
			Biome biome = source.getBlockBiome(probe);
			if (biome == null) {
				continue;
			}
			int color = sampler.colorAt(source, biome, probe);
			if (color == -1) {
				continue;
			}

			double weight = KERNEL_WEIGHT[i];
			totalWeight += weight;
			r += (color >> 16 & 0xFF) * weight;
			g += (color >> 8 & 0xFF) * weight;
			b += (color & 0xFF) * weight;
		}

		return totalWeight <= 0.0 ? -1 : pack(r / totalWeight, g / totalWeight, b / totalWeight);
	}

	private static final int[] KERNEL_DX;
	private static final int[] KERNEL_DZ;
	private static final double[] KERNEL_WEIGHT;

	static {
		int[] dxs = new int[SPAN * SPAN];
		int[] dzs = new int[SPAN * SPAN];
		double[] weights = new double[SPAN * SPAN];
		int kept = 0;
		for (int ix = 0; ix < SPAN; ix++) {
			int dx = ix * STEP - RADIUS;
			for (int iz = 0; iz < SPAN; iz++) {
				int dz = iz * STEP - RADIUS;
				double weight = kernel(dx, dz);
				if (weight <= 0.0) {
					continue;
				}
				dxs[kept] = dx;
				dzs[kept] = dz;
				weights[kept] = weight;
				kept++;
			}
		}
		KERNEL_DX = Arrays.copyOf(dxs, kept);
		KERNEL_DZ = Arrays.copyOf(dzs, kept);
		KERNEL_WEIGHT = Arrays.copyOf(weights, kept);
	}

	public static final int SKY_RADIUS = 64;

	public static final int SKY_STEP = 16;

	private static final int SKY_SPAN = SKY_RADIUS * 2 / SKY_STEP + 1;

	public static int blendSky(@NotNull World world, @NotNull TilePosc centre, float temperature) {
		int half = SKY_SPAN / 2 * SKY_STEP;
		int originX = Math.floorDiv(centre.x(), SKY_STEP) * SKY_STEP - half;
		int originZ = Math.floorDiv(centre.z(), SKY_STEP) * SKY_STEP - half;

		Object provider = world.getBiomeProvider();
		BiomeProviderBOP.Blend blend = cachedSkyGrid(provider, originX, originZ);
		if (blend == null) {
			blend = BiomeProviderBOP.sampleBlendGrid(
				world.getBiomeProvider(), originX, originZ, SKY_SPAN, SKY_SPAN, SKY_STEP);
			skyGridProvider = provider;
			skyGridOriginX = originX;
			skyGridOriginZ = originZ;
			skyGrid = blend;
		}

		double totalWeight = 0.0;
		double r = 0.0;
		double g = 0.0;
		double b = 0.0;

		for (int ix = 0; ix < SKY_SPAN; ix++) {

			int dx = originX + ix * SKY_STEP - centre.x();
			for (int iz = 0; iz < SKY_SPAN; iz++) {
				int dz = originZ + iz * SKY_STEP - centre.z();
				double weight = skyKernel(dx, dz);
				if (weight <= 0.0) {
					continue;
				}

				Biome biome = blend.biomes[ix * SKY_SPAN + iz];
				if (biome == null) {
					continue;
				}

				weight *= BLEND_FLOOR + (1.0 - BLEND_FLOOR) * blend.weights[ix * SKY_SPAN + iz];

				int color = biome.getSkyColor(temperature);
				totalWeight += weight;
				r += (color >> 16 & 0xFF) * weight;
				g += (color >> 8 & 0xFF) * weight;
				b += (color & 0xFF) * weight;
			}
		}

		if (totalWeight <= 0.0) {
			Biome here = world.getBiomeProvider().getBiome(centre.x(), centre.y(), centre.z());
			return here == null ? 0xFFFFFF : here.getSkyColor(temperature);
		}
		return pack(r / totalWeight, g / totalWeight, b / totalWeight);
	}

	private static final double BLEND_FLOOR = 0.15;

	private static Object skyGridProvider;
	private static int skyGridOriginX;
	private static int skyGridOriginZ;
	private static BiomeProviderBOP.Blend skyGrid;

	@Nullable
	private static BiomeProviderBOP.Blend cachedSkyGrid(@NotNull Object provider, int originX, int originZ) {
		if (PERF_CONTROL) {
			return null;
		}
		return skyGrid != null && skyGridProvider == provider
			&& skyGridOriginX == originX && skyGridOriginZ == originZ
			? skyGrid
			: null;
	}

	private static double falloff(double distance, double radius) {
		double t = distance / radius;
		if (t >= 1.0) {
			return 0.0;
		}
		double u = 1.0 - t * t;
		return u * u;
	}

	private static double kernel(int dx, int dz) {
		return falloff(Math.sqrt((double) dx * dx + (double) dz * dz), RADIUS + 1.0);
	}

	private static double skyKernel(int dx, int dz) {
		return falloff(Math.sqrt((double) dx * dx + (double) dz * dz), SKY_RADIUS + 1.0);
	}

	private static int pack(double r, double g, double b) {
		int red = clampChannel(r);
		int green = clampChannel(g);
		int blue = clampChannel(b);
		return red << 16 | green << 8 | blue;
	}

	private static int clampChannel(double value) {
		int rounded = (int) Math.round(value);
		return rounded < 0 ? 0 : Math.min(rounded, 255);
	}
}
