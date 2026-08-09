package com.betteroplenty.client;

import com.betteroplenty.block.BlockLogicGrave;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

public class BlockModelGrave extends BlockModelStandard<BlockLogicGrave> {

	private static final double P = 0.0625;

	private static final int[][] LOWER_X = {
		{5, 0, 5, 11, 3, 11},
		{6, 3, 6, 10, 11, 10},
		{0, 11, 5, 16, 14, 11},
	};

	private static final int[][] LOWER_Z = {
		{5, 0, 5, 11, 3, 11},
		{6, 3, 6, 10, 11, 10},
		{5, 11, 0, 11, 14, 16},
	};

	private static final int[][] UPPER_X = {
		{13, -2, 5, 16, 8, 11},
		{0, -2, 5, 3, 8, 11},
		{6, -3, 7, 10, 14, 9},
		{-4, 1, 7, 6, 5, 9},
		{10, 1, 7, 20, 5, 9},
		{0, 8, 5, 16, 11, 11},
	};

	private static final int[][] UPPER_Z = {
		{5, -2, 13, 11, 8, 16},
		{5, -2, 0, 11, 8, 3},
		{7, -3, 6, 9, 14, 10},
		{7, 1, -4, 9, 5, 6},
		{7, 1, 10, 9, 5, 20},
		{5, 8, 0, 11, 11, 16},
	};

	private final boolean upper;

	public BlockModelGrave(Block<BlockLogicGrave> block) {
		super(block);
		this.upper = block.getLogic().isUpper();
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource,
						  @NotNull TilePosc tilePos) {
		boolean drew = false;
		for (int[] box : boxes(BlockLogicGrave.alongZ(worldSource.getBlockData(tilePos)))) {
			drew |= renderBlocks.renderStandardBlock(tessellator, worldSource, this, aabb(box), tilePos);
		}
		return drew;
	}

	@Override
	public void renderStandalone(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		int color = this.getStandaloneTintColor(metadata);
		tessellator.offsetTranslation(-0.5, -0.5, -0.5);
		for (int[] box : LOWER_X) {
			this.renderBlockWithBounds(tessellator, aabb(box), metadata, lightIndex, color);
		}
		tessellator.offsetTranslation(0.5, 0.5, 0.5);
	}

	private int[][] boxes(boolean alongZ) {
		if (this.upper) {
			return alongZ ? UPPER_Z : UPPER_X;
		}
		return alongZ ? LOWER_Z : LOWER_X;
	}

	@NotNull
	private static AABBdc aabb(int[] box) {
		return new AABBd(box[0] * P, box[1] * P, box[2] * P, box[3] * P, box[4] * P, box[5] * P);
	}
}
