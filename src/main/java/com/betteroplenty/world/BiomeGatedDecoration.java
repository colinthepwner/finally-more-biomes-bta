package com.betteroplenty.world;

import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkDecoration;
import net.minecraft.core.world.generate.chunk.ChunkDecorationBuilder;
import net.minecraft.core.world.generate.chunk.PlaceableFeature;
import net.minecraft.core.world.generate.chunk.PlacementMethod;
import net.minecraft.core.world.generate.chunk.PlacementMethods;
import net.minecraft.core.world.generate.chunk.PositionSelector;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.pos.TilePos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public final class BiomeGatedDecoration implements ChunkDecoration {

	@NotNull
	private final WorldFeatureInterface worldFeature;

	@NotNull
	private final Biome[] biomeMask;

	@NotNull
	private final PositionSelector positionSelector;

	@NotNull
	private final PlacementMethod placementMethod;

	@NotNull
	private final ChunkBiomeWindow window;

	@Nullable
	private GatedPlaceable cachedPlaceable;
	private int cachedMinY;
	private int cachedMaxY;
	private int cachedRangeY;

	private static boolean loggedReentry;

	public BiomeGatedDecoration(
		@NotNull WorldFeatureInterface worldFeature,
		@NotNull Biome[] biomeMask,
		@NotNull PositionSelector positionSelector,
		@NotNull PlacementMethod placementMethod,
		@NotNull ChunkBiomeWindow window
	) {
		this.worldFeature = worldFeature;
		this.biomeMask = biomeMask;
		this.positionSelector = positionSelector;
		this.placementMethod = placementMethod;
		this.window = window;
	}

	@NotNull
	public static BiomeGatedDecoration of(
		@NotNull WorldFeatureInterface feature,
		@NotNull Biome[] mask,
		@NotNull PositionSelector selector,
		@NotNull PlacementMethod method,
		@NotNull ChunkBiomeWindow window
	) {
		return new BiomeGatedDecoration(feature, mask, selector, method, window);
	}

	private static int checkY(@NotNull World world, int minY, int maxY) {
		int y = minY + (maxY - minY) / 2;
		return Math.max(world.getWorldType().getMinY(world),
			Math.min(world.getWorldType().getMaxY(world) - 1, y));
	}

	@Override
	public void placeDecoration(@NotNull World world, @NotNull Chunk chunk, int worldX, int worldZ,
	                            int minY, int maxY, int rangeY, @NotNull Random rand) {
		int checkY = checkY(world, minY, maxY);

		if (this.biomeMask.length != 0) {
			this.window.moveTo(worldX, worldZ, checkY);

			if (!this.window.containsAny(this.biomeMask)) {
				return;
			}
		}

		if (this.cachedPlaceable == null || minY != this.cachedMinY || maxY != this.cachedMaxY
			|| rangeY != this.cachedRangeY) {
			this.cachedPlaceable = new GatedPlaceable(minY, maxY, rangeY);
			this.cachedMinY = minY;
			this.cachedMaxY = maxY;
			this.cachedRangeY = rangeY;
		}

		this.placementMethod.placeFeature(this.cachedPlaceable, world, chunk, rand);
	}

	private boolean maskContains(@NotNull Biome biome) {
		Biome[] mask = this.biomeMask;
		for (int i = 0, n = mask.length; i < n; i++) {
			if (mask[i] == biome) {
				return true;
			}
		}
		return false;
	}

	private final class GatedPlaceable extends PlaceableFeature {
		private final int minY;
		private final int maxY;
		private final int rangeY;

		private GatedPlaceable(int minY, int maxY, int rangeY) {
			super(BiomeGatedDecoration.this.worldFeature, BiomeGatedDecoration.this.positionSelector,
				minY, maxY, rangeY);
			this.minY = minY;
			this.maxY = maxY;
			this.rangeY = rangeY;
		}

		@Override
		public void placeFeature(@NotNull World world, @NotNull Chunk chunk, @NotNull Random random) {
			TilePos pos = BiomeGatedDecoration.this.positionSelector
				.getValue(world, chunk, random, this.minY, this.maxY, this.rangeY);

			if (BiomeGatedDecoration.this.biomeMask.length != 0) {

				ChunkBiomeWindow window = BiomeGatedDecoration.this.window;
				int wasX = window.originX();
				int wasZ = window.originZ();
				if (window.moveTo(chunk.pos.x() * 16, chunk.pos.z() * 16,
					checkY(world, this.minY, this.maxY)) && !loggedReentry) {
					loggedReentry = true;
					BetterOPlenty.LOGGER.info(
						"Decoration re-entered: the shared biome window was at ({}, {}) while chunk "
							+ "({}, {}) was being decorated, so it was re-aimed. Expected and handled "
							+ "-- a feature's block update populated a neighbouring chunk mid-pass. "
							+ "Logged once; see ChunkBiomeWindow.",
						wasX, wasZ, chunk.pos.x() * 16, chunk.pos.z() * 16);
				}
				if (!BiomeGatedDecoration.this.maskContains(
					BiomeGatedDecoration.this.window.biomeAt(pos.x(), pos.z()))) {
					return;
				}
			}

			if (BiomeGatedDecoration.this.worldFeature instanceof WorldFeature) {
				((WorldFeature) BiomeGatedDecoration.this.worldFeature).init(1.0, 1.0, 1.0);
			}
			BiomeGatedDecoration.this.worldFeature.place(world, random, pos);
		}
	}
}
