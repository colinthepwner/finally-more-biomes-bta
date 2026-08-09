package com.betteroplenty.client;

import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

public class SpikeCustomModel<T extends BlockLogic> extends BlockModelStandard<T> {
	private static final AABBdc BASE = new AABBd(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
	private static final AABBdc POST = new AABBd(0.25, 0.5, 0.25, 0.75, 1.0, 0.75);

	public SpikeCustomModel(Block<T> block) {
		super(block);

		this.itemRenderBounds = BASE;
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos) {
		boolean base = renderBlocks.renderStandardBlock(tessellator, worldSource, this, BASE, tilePos);
		boolean post = renderBlocks.renderStandardBlock(tessellator, worldSource, this, POST, tilePos);
		return base || post;
	}

	@Override
	public void renderStandalone(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		int tint = this.getStandaloneTintColor(metadata);
		tessellator.offsetTranslation(-0.5, -0.5, -0.5);
		this.renderBlockWithBounds(tessellator, BASE, metadata, lightIndex, tint);
		this.renderBlockWithBounds(tessellator, POST, metadata, lightIndex, tint);
		tessellator.offsetTranslation(0.5, 0.5, 0.5);
	}
}
