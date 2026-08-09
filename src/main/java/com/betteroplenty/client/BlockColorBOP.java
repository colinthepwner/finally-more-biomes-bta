package com.betteroplenty.client;

import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.colorizer.Colorizer;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

import java.util.function.ToIntFunction;

public class BlockColorBOP extends BlockColor implements BiomeBlendBOP.SampleColor {

	public interface BiomeTint extends ToIntFunction<BiomeGenBase> {}

	@NotNull
	private final Colorizer colorizer;
	@NotNull
	private final BiomeTint tint;

	public BlockColorBOP(@NotNull Colorizer colorizer, @NotNull BiomeTint tint) {
		this.colorizer = colorizer;
		this.tint = tint;
	}

	public static int meshGeneration = 1;

	public static void bumpMeshGeneration() {
		meshGeneration++;
	}

	private final long[] memoKey = new long[MEMO_SLOTS];
	private final int[] memoValue = new int[MEMO_SLOTS];
	private final int[] memoStamp = new int[MEMO_SLOTS];

	private static final int MEMO_SLOTS = 512;

	@NotNull
	private final TilePos probe = new TilePos();

	@Override
	public int getWorldColor(@NotNull WorldSource source, @NotNull TilePosc tilePos, int tintIndex) {
		int x = tilePos.x();
		int z = tilePos.z();
		int cell = tilePos.y() >> 3;

		long key = ((long) (x & 0x3FFFFFF) << 37) | ((long) (z & 0x3FFFFFF) << 11) | (cell & 0x7FFL);
		int slot = ((cell & 1) << 8) | ((z & 15) << 4) | (x & 15);

		int generation = meshGeneration;
		if (!BiomeBlendBOP.PERF_CONTROL
			&& this.memoStamp[slot] == generation && this.memoKey[slot] == key) {
			memoHits++;
			return this.memoValue[slot];
		}
		memoMisses++;

		int color = BiomeBlendBOP.blend(source, tilePos, this, this.probe);

		this.memoKey[slot] = key;
		this.memoValue[slot] = color;
		this.memoStamp[slot] = generation;
		return color;
	}

	public static long memoHits;
	public static long memoMisses;

	public static void resetCounters() {
		memoHits = 0;
		memoMisses = 0;
	}

	@Override
	public int colorAt(@NotNull WorldSource source, @NotNull Biome biome, @NotNull TilePosc tilePos) {
		return this.colorOf(source, biome, tilePos);
	}

	private int colorOf(@NotNull WorldSource source, @NotNull Biome biome, @NotNull TilePosc tilePos) {
		double resist = 0.0;
		if (biome instanceof BiomeGenBase bop) {
			resist = bop.seasonResist;
			int base = this.tint.applyAsInt(bop);
			if (base != -1) {
				return applySeason(liftDark(base),
					source.getBlockTemperature(tilePos), source.getBlockHumidity(tilePos), resist);
			}
		}

		double temp = source.getBlockTemperature(tilePos);
		double humid = source.getBlockHumidity(tilePos);
		int seasonal = this.colorizer.getColor(temp, humid);
		if (resist <= 0.0 || seasonal == -1) {
			return seasonal;
		}
		int neutral = this.colorizer.getDefaultColor(temp, humid);
		if (neutral == -1) {
			return seasonal;
		}
		float keep = (float) (1.0 - resist);
		int r = blendChannel(neutral >> 16 & 0xFF, seasonal >> 16 & 0xFF, neutral >> 16 & 0xFF, keep);
		int g = blendChannel(neutral >> 8 & 0xFF, seasonal >> 8 & 0xFF, neutral >> 8 & 0xFF, keep);
		int b = blendChannel(neutral & 0xFF, seasonal & 0xFF, neutral & 0xFF, keep);
		return r << 16 | g << 8 | b;
	}

	@Override
	public int getFallbackColor(int meta, int tintIndex) {

		return this.colorizer.getDefaultColor(1.0, 0.7);
	}

	private static final double MIN_TINT_LUMA = 100.0;

	private static int liftDark(int base) {
		int r = base >> 16 & 0xFF;
		int g = base >> 8 & 0xFF;
		int b = base & 0xFF;

		double luma = 0.299 * r + 0.587 * g + 0.114 * b;
		if (luma >= MIN_TINT_LUMA || luma <= 0.0) {
			return base;
		}

		double scale = MIN_TINT_LUMA / luma;
		return Math.min(255, (int) Math.round(r * scale)) << 16
			| Math.min(255, (int) Math.round(g * scale)) << 8
			| Math.min(255, (int) Math.round(b * scale));
	}

	private int applySeason(int base, double temperature, double humidity, double resist) {
		int seasonal = this.colorizer.getColor(temperature, humidity);
		int neutral = this.colorizer.getDefaultColor(temperature, humidity);
		if (seasonal == -1 || neutral == -1 || seasonal == neutral) {
			return base;
		}

		float strength = (float) (SEASON_STRENGTH * (1.0 - resist));
		if (strength <= 0.0f) {
			return base;
		}

		int r = blendChannel(base >> 16 & 0xFF, seasonal >> 16 & 0xFF, neutral >> 16 & 0xFF, strength);
		int g = blendChannel(base >> 8 & 0xFF, seasonal >> 8 & 0xFF, neutral >> 8 & 0xFF, strength);
		int b = blendChannel(base & 0xFF, seasonal & 0xFF, neutral & 0xFF, strength);
		return r << 16 | g << 8 | b;
	}

	private static final float SEASON_STRENGTH = 0.75f;

	private static int blendChannel(int base, int seasonal, int neutral, float strength) {
		int shifted = Math.round(base + (seasonal - base) * strength);
		return Math.min(255, Math.max(0, shifted));
	}

}
