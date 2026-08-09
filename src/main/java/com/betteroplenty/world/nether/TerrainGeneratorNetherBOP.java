package com.betteroplenty.world.nether;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.perlin.nether.TerrainGeneratorNether;
import org.jetbrains.annotations.NotNull;

public class TerrainGeneratorNetherBOP extends TerrainGeneratorNether {

	private final int oceanY;
	private final int airId;
	private final int lavaId;

	public TerrainGeneratorNetherBOP(@NotNull World world) {
		super(world);
		this.oceanY = world.getWorldType().getOceanY();
		this.airId = Blocks.AIR.id();
		this.lavaId = Blocks.FLUID_LAVA_STILL.id();
	}

	@Override
	protected int getBlockAt(@NotNull Chunk chunk, int x, int y, int z, double density) {
		int id = super.getBlockAt(chunk, x, y, z, density);

		if (id == this.airId && y < this.oceanY) {

			Biome biome = chunk.getBlockBiome(x, this.oceanY, z);
			if (BOPNetherClimate.isBOPNether(biome)) {
				return this.lavaId;
			}
		}

		return id;
	}
}
