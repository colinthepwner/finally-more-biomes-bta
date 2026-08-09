package com.betteroplenty.client;

import net.minecraft.client.render.block.model.BlockModelCrossedSquares;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public class BlockModelWaterReed<T extends BlockLogic> extends BlockModelCrossedSquares<T> {

	private static final int MAX_DEPTH = 8;

	public BlockModelWaterReed(Block<T> block) {
		super(block);
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource,
	                      @NotNull TilePosc tilePos) {
		boolean drawn = super.render(tessellator, worldSource, tilePos);

		TilePos probe = new TilePos();
		for (int depth = 1; depth <= MAX_DEPTH; depth++) {
			if (!isWater(worldSource.getBlockType(tilePos.add(0, -depth, 0, probe)))) {
				break;
			}

			tessellator.offsetTranslation(0.0, -depth, 0.0);
			try {
				drawn |= super.render(tessellator, worldSource, tilePos);
			} finally {

				tessellator.offsetTranslation(0.0, depth, 0.0);
			}
		}

		return drawn;
	}

	private static boolean isWater(Block<?> block) {
		return block == Blocks.FLUID_WATER_STILL || block == Blocks.FLUID_WATER_FLOWING;
	}
}
