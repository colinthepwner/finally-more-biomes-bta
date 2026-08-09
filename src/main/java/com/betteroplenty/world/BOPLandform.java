package com.betteroplenty.world;

import net.minecraft.core.world.noise.FractalNoise2D;
import net.minecraft.core.world.noise.ImprovedPerlinNoise;
import net.minecraft.core.world.noise.Noise2D;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class BOPLandform {

	private static final double BLOCK_SCALE = 50.0;

	@NotNull private final Noise2D depthNoise;

	public BOPLandform(long seed) {
		this.depthNoise = new FractalNoise2D<>(ImprovedPerlinNoise.genOctaves(seed, 16, 58));
	}

	@NotNull
	public double[] region(int x, int z, int xSize, int zSize, int stride) {
		double[] out = this.depthNoise.getRegion(null, x / (double) stride, z / (double) stride,
			xSize, zSize, BLOCK_SCALE * stride, BLOCK_SCALE * stride);
		for (int i = 0; i < out.length; i++) {
			out[i] = landform(out[i]);
		}
		return out;
	}

	public static double landform(double rawDepthNoise) {
		double depth = rawDepthNoise / 8000.0;
		if (depth < 0.0) {
			depth = -depth * 0.3;
		}

		depth = depth * 3.0 - 2.0;
		if (depth < 0.0) {
			depth /= 2.0;
			if (depth < -1.0) {
				depth = -1.0;
			}

			depth /= 1.4;
			depth /= 2.0;
		} else {
			if (depth > 1.0) {
				depth = 1.0;
			}

			depth /= 8.0;
		}
		return depth;
	}

	public static double altitude(double landform) {
		return BOPClimate.LANDFORM.share(landform);
	}

	public static boolean isSea(double landform) {
		return altitude(landform) < BOPClimate.SEA_SHARE;
	}

	public static void probe() {
		long[] seeds = {3633286825302357475L, 1L, 42L, -1234567890123L, 987654321987654321L, 555555555L};
		int patch = 96;
		int patches = 26;
		int knots = 65;

		double[] mean = new double[knots];
		double worstSpread = 0.0;
		double[][] perSeed = new double[seeds.length][];
		double basin = 0.0;

		for (int s = 0; s < seeds.length; s++) {
			BOPLandform field = new BOPLandform(seeds[s]);
			java.util.Random pick = new java.util.Random(7);
			double[] values = new double[patches * patch * patch];
			int at = 0;
			for (int p = 0; p < patches; p++) {
				int ox = (int) ((pick.nextDouble() * 2 - 1) * 2_000_000);
				int oz = (int) ((pick.nextDouble() * 2 - 1) * 2_000_000);
				for (double v : field.region(ox & ~3, oz & ~3, patch, patch, 4)) {
					values[at++] = v;
				}
			}
			Arrays.sort(values);
			perSeed[s] = new double[knots];
			for (int k = 0; k < knots; k++) {
				perSeed[s][k] = values[(int) Math.round((double) k / (knots - 1) * (values.length - 1))];
				mean[k] += perSeed[s][k] / seeds.length;
			}
			int under = 0;
			for (double v : values) {
				if (v < 0.0) under++;
			}
			basin += (double) under / values.length / seeds.length;
		}

		for (int k = 0; k < knots; k++) {
			double lo = Double.MAX_VALUE;
			double hi = -Double.MAX_VALUE;
			for (double[] seedKnots : perSeed) {
				lo = Math.min(lo, seedKnots[k]);
				hi = Math.max(hi, seedKnots[k]);
			}
			worstSpread = Math.max(worstSpread, hi - lo);
		}

		StringBuilder table = new StringBuilder();
		for (int k = 0; k < knots; k++) {
			table.append(k % 8 == 0 ? "\n\t\t" : ", ").append(String.format("%.5f", mean[k]));
		}
		com.betteroplenty.BetterOPlenty.LOGGER.info(
			"Landform probe: {} samples/seed over {} seeds, basin share {}, worst cross-seed knot"
				+ " spread {}. Table:{}", patches * patch * patch, seeds.length,
			String.format("%.4f", basin), String.format("%.5f", worstSpread), table);

		double[] shipped = new double[knots];
		for (int k = 0; k < knots; k++) {
			shipped[k] = BOPClimate.LANDFORM.share(mean[k]) * (knots - 1);
		}
		double worstDrift = 0.0;
		for (int k = 0; k < knots; k++) {
			worstDrift = Math.max(worstDrift, Math.abs(shipped[k] - k) / (knots - 1.0));
		}
		if (worstDrift > 0.02) {
			com.betteroplenty.BetterOPlenty.LOGGER.error(
				"Landform axis is stale: the shipped table maps the measured quantiles up to {}% of"
					+ " the axis away from uniform. The sea is not the size BOPClimate.SEA_SHARE says.",
				String.format("%.1f", 100 * worstDrift));
		} else {
			com.betteroplenty.BetterOPlenty.LOGGER.info(
				"Landform axis is current: worst uniformity error {}% of the axis.",
				String.format("%.2f", 100 * worstDrift));
		}
	}
}
