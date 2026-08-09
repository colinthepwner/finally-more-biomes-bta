package com.betteroplenty.client;

import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;

public class BlockModelCarpet<T extends BlockLogic> extends BlockModelStandard<T> {

	public BlockModelCarpet(Block<T> block) {
		super(block);
		this.withCustomItemBounds(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
	}

	@Override
	public boolean shouldItemRender3d() {
		return false;
	}
}
