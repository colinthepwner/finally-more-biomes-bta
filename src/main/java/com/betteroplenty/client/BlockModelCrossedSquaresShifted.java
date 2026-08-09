package com.betteroplenty.client;

import net.minecraft.client.render.block.model.BlockModelCrossedSquares;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public class BlockModelCrossedSquaresShifted<T extends BlockLogic> extends BlockModelCrossedSquares<T> {

	public BlockModelCrossedSquaresShifted(Block<T> block) {
		super(block);
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource,
						  @NotNull TilePosc tilePos) {
		long dRandom = tilePos.x() * 3129871L ^ tilePos.z() * 116129781L ^ tilePos.y();
		dRandom = dRandom * dRandom * 42317861L + dRandom * 11L;
		double xd = ((float) (dRandom >> 16 & 15L) / 15.0F - 0.5) * 0.5;
		double yd = ((float) (dRandom >> 20 & 15L) / 15.0F - 1.0) * 0.2;
		double zd = ((float) (dRandom >> 24 & 15L) / 15.0F - 0.5) * 0.5;

		tessellator.offsetTranslation(xd, yd, zd);
		try {
			return super.render(tessellator, worldSource, tilePos);
		} finally {
			tessellator.offsetTranslation(-xd, -yd, -zd);
		}
	}
}
