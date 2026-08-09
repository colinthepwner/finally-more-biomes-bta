package com.betteroplenty.client;

import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

public class BlockModelBamboo<T extends BlockLogic> extends BlockModelStandard<T> {

	private static final AABBdc FULL_CUBE = new AABBd(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

	private static final double PLANE = 5.0 / 16.0;

	public BlockModelBamboo(Block<T> block) {
		super(block);

		this.itemRenderBounds = new AABBd(PLANE, 0.0, PLANE, 1.0 - PLANE, 1.0, 1.0 - PLANE);
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource,
	                      @NotNull TilePosc tilePos) {
		int meta = worldSource.getBlockData(tilePos);
		int color = BlockColorDispatcher.getInstance().getDispatch(this.block).getWorldColor(worldSource, tilePos, 0);
		float r = (color >> 16 & 0xFF) / 255.0F;
		float g = (color >> 8 & 0xFF) / 255.0F;
		float b = (color & 0xFF) / 255.0F;

		renderBlocks.cache.setupCache(this.block, worldSource, tilePos);
		renderBlocks.enableAO = true;

		boolean rendered = false;

		if (renderBlocks.renderAllFaces
			|| this.shouldSideBeRendered(worldSource, FULL_CUBE, tilePos.add(0, -1, 0, new TilePos()), Side.BOTTOM, meta)) {
			rendered |= drawFace(tessellator, worldSource, tilePos, meta, r, g, b, Side.BOTTOM, 0.0F);
		}
		if (renderBlocks.renderAllFaces
			|| this.shouldSideBeRendered(worldSource, FULL_CUBE, tilePos.add(0, 1, 0, new TilePos()), Side.TOP, meta)) {
			rendered |= drawFace(tessellator, worldSource, tilePos, meta, r, g, b, Side.TOP, 0.0F);
		}

		rendered |= drawFace(tessellator, worldSource, tilePos, meta, r, g, b, Side.NORTH, (float) PLANE);
		rendered |= drawFace(tessellator, worldSource, tilePos, meta, r, g, b, Side.SOUTH, (float) PLANE);
		rendered |= drawFace(tessellator, worldSource, tilePos, meta, r, g, b, Side.WEST, (float) PLANE);
		rendered |= drawFace(tessellator, worldSource, tilePos, meta, r, g, b, Side.EAST, (float) PLANE);

		renderBlocks.enableAO = false;
		return rendered;
	}

	private boolean drawFace(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource,
	                         @NotNull TilePosc tilePos, int meta, float r, float g, float b,
	                         @NotNull Side side, float depth) {
		IconCoordinate tex = this.getBlockTexture(worldSource, tilePos, side);
		if (tex == null || (renderBlocks.renderBitMask >> side.id & 1) != 0) {
			return false;
		}

		boolean colored = this.shouldSideBeColored(worldSource, tilePos, side, meta);
		float fr = colored ? r : 1.0F;
		float fg = colored ? g : 1.0F;
		float fb = colored ? b : 1.0F;

		double x = tilePos.x();
		double y = tilePos.y();
		double z = tilePos.z();

		switch (side) {
			case BOTTOM -> {
				renderBlocks.setupLighting(worldSource, this.block, tilePos, fr, fg, fb, side,
					0, -1, 0, depth, 0, 0, 1, 1.0F, 0.0F, -1, 0, 0, 1.0F, 0.0F);
				renderBlocks.renderBottomFace(tessellator, FULL_CUBE, x, y, z, tex);
			}
			case TOP -> {
				renderBlocks.setupLighting(worldSource, this.block, tilePos, fr, fg, fb, side,
					0, 1, 0, depth, 0, 0, 1, 1.0F, 0.0F, 1, 0, 0, 1.0F, 0.0F);
				renderBlocks.renderTopFace(tessellator, FULL_CUBE, x, y, z, tex);
			}

			case NORTH -> {
				renderBlocks.setupLighting(worldSource, this.block, tilePos, fr, fg, fb, side,
					0, 0, -1, depth, -1, 0, 0, 1.0F, 0.0F, 0, 1, 0, 1.0F, 0.0F);
				renderBlocks.renderNorthFace(tessellator, FULL_CUBE, x, y, z + PLANE, tex);
			}

			case SOUTH -> {
				renderBlocks.setupLighting(worldSource, this.block, tilePos, fr, fg, fb, side,
					0, 0, 1, depth, 0, 1, 0, 1.0F, 0.0F, -1, 0, 0, 1.0F, 0.0F);
				renderBlocks.renderSouthFace(tessellator, FULL_CUBE, x, y, z - PLANE, tex);
			}
			case WEST -> {
				renderBlocks.setupLighting(worldSource, this.block, tilePos, fr, fg, fb, side,
					-1, 0, 0, depth, 0, 0, 1, 1.0F, 0.0F, 0, 1, 0, 1.0F, 0.0F);
				renderBlocks.renderWestFace(tessellator, FULL_CUBE, x + PLANE, y, z, tex);
			}
			case EAST -> {
				renderBlocks.setupLighting(worldSource, this.block, tilePos, fr, fg, fb, side,
					1, 0, 0, depth, 0, 0, 1, 1.0F, 0.0F, 0, -1, 0, 1.0F, 0.0F);
				renderBlocks.renderEastFace(tessellator, FULL_CUBE, x - PLANE, y, z, tex);
			}
			default -> {
				return false;
			}
		}

		return true;
	}

	@Override
	public void renderStandalone(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		tessellator.offsetTranslation(-0.5, -0.5, -0.5);
		tessellator.startDrawingQuads();
		tessellator.setLightmapCoord1i(lightIndex);
		tessellator.setColor1i(this.getStandaloneTintColor(metadata));

		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		renderBlocks.renderBottomFace(tessellator, FULL_CUBE, 0.0, 0.0, 0.0,
			this.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata));
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderBlocks.renderTopFace(tessellator, FULL_CUBE, 0.0, 0.0, 0.0,
			this.getBlockTextureFromSideAndMetadata(Side.TOP, metadata));
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		renderBlocks.renderNorthFace(tessellator, FULL_CUBE, 0.0, 0.0, PLANE,
			this.getBlockTextureFromSideAndMetadata(Side.NORTH, metadata));
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderBlocks.renderSouthFace(tessellator, FULL_CUBE, 0.0, 0.0, -PLANE,
			this.getBlockTextureFromSideAndMetadata(Side.SOUTH, metadata));
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		renderBlocks.renderWestFace(tessellator, FULL_CUBE, PLANE, 0.0, 0.0,
			this.getBlockTextureFromSideAndMetadata(Side.WEST, metadata));
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderBlocks.renderEastFace(tessellator, FULL_CUBE, -PLANE, 0.0, 0.0,
			this.getBlockTextureFromSideAndMetadata(Side.EAST, metadata));

		tessellator.draw();
		tessellator.offsetTranslation(0.5, 0.5, 0.5);
	}
}
