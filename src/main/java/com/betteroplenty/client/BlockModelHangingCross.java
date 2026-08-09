package com.betteroplenty.client;

import net.minecraft.client.render.block.model.BlockModelCrossedSquares;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public class BlockModelHangingCross<T extends BlockLogic> extends BlockModelCrossedSquares<T> {

	private final double drop;

	public BlockModelHangingCross(Block<T> block, double drop) {
		super(block);
		this.drop = drop;
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource,
	                      @NotNull TilePosc tilePos) {
		tessellator.offsetTranslation(0.0, this.drop, 0.0);
		try {
			return super.render(tessellator, worldSource, tilePos);
		} finally {

			tessellator.offsetTranslation(0.0, -this.drop, 0.0);
		}
	}
}
