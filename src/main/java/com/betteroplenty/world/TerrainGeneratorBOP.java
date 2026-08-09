package com.betteroplenty.world;

import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;
import net.minecraft.core.world.generate.chunk.perlin.overworld.TerrainGeneratorOverworld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TerrainGeneratorBOP extends TerrainGeneratorOverworld {

	@Nullable private final BOPCoastline coastline;

	public TerrainGeneratorBOP(@NotNull World world) {
		this(world, new DensityGeneratorBOP(world), true);
	}

	public TerrainGeneratorBOP(@NotNull World world, boolean applyCoastline) {
		this(world, new DensityGeneratorBOP(world), applyCoastline);
	}

	private TerrainGeneratorBOP(@NotNull World world, @NotNull DensityGeneratorBOP density,
	                            boolean applyCoastline) {
		super(world, density);
		this.coastline = applyCoastline ? new BOPCoastline(world, density) : null;
	}

	@NotNull
	@Override
	public ChunkGeneratorResult generateTerrain(@NotNull Chunk chunk, @NotNull double[] densityMap) {
		ChunkGeneratorResult result = super.generateTerrain(chunk, densityMap);
		if (this.coastline != null) {
			this.coastline.apply(chunk);
		}
		return result;
	}
}
