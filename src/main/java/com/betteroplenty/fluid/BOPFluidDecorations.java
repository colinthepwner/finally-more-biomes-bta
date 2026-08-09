package com.betteroplenty.fluid;

import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.PositionSelector;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureLake;
import net.minecraft.core.world.pos.TilePos;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Supplier;

public final class BOPFluidDecorations {
	private BOPFluidDecorations() {}

	public static final class NestedDepth implements PositionSelector {

		private static final int UPSTREAM_SEA = 64;

		private final int range;

		public NestedDepth(int range) {
			this.range = range;
		}

		private int scaled(@NotNull World world, int upstreamValue) {
			int oceanY = world.getWorldType().getOceanY();
			if (oceanY <= 0) {
				return upstreamValue;
			}
			return Math.max(1, upstreamValue * oceanY / UPSTREAM_SEA);
		}

		@NotNull
		@Override
		public TilePos getValue(@NotNull World world, @NotNull Chunk chunk, @NotNull Random random,
								int minY, int maxY, int rangeY) {
			int x = chunk.pos.x() * 16 + random.nextInt(16) + 8;
			int z = chunk.pos.z() * 16 + random.nextInt(16) + 8;
			int span = scaled(world, this.range);
			int lift = scaled(world, 8);
			int y = random.nextInt(random.nextInt(random.nextInt(span) + lift) + lift);
			return new TilePos(x, y, z);
		}
	}

	public static final PositionSelector BIOME_POOL_DEPTH = new NestedDepth(112);

	public static final PositionSelector UNDERGROUND_POCKET_DEPTH = new NestedDepth(32);

	public static final int UNDERGROUND_TRIES = 5;

	public static final int UNDERGROUND_SPRING_WATER_CHANCE = 96;

	public static final int UNDERGROUND_LIQUID_POISON_CHANCE = 32;

	public static Supplier<WorldFeatureInterface> springWaterLake() {
		return () -> new WorldFeatureLake(BOPFluids.SPRING_WATER_STILL.id());
	}

	public static Supplier<WorldFeatureInterface> liquidPoisonLake() {
		return () -> new WorldFeatureLake(BOPFluids.LIQUID_POISON_STILL.id());
	}

	public static final int UNDERGROUND_ALWAYS = 1;
}
