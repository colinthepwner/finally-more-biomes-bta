package com.betteroplenty.client;

import net.minecraft.client.render.block.model.BlockModelCrossedSquares;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockModelOverlayCross<T extends BlockLogic> extends BlockModelCrossedSquares<T> {

	private static final double HALF_WIDTH = 0.45;

	private final String overlayKey;
	@Nullable private final String itemIconKey;

	@Nullable private IconCoordinate overlay;
	@Nullable private IconCoordinate itemIcon;

	public BlockModelOverlayCross(@NotNull Block<T> block, @NotNull String overlayKey,
	                              @Nullable String itemIconKey) {
		super(block);
		this.overlayKey = overlayKey;
		this.itemIconKey = itemIconKey;
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource,
	                      @NotNull TilePosc tilePos) {
		boolean drawn = super.render(tessellator, worldSource, tilePos);

		if (renderBlocks.overrideBlockTexture != null) {
			return drawn;
		}

		drawOverlay(tessellator, tilePos.x(), tilePos.y(), tilePos.z());
		return drawn;
	}

	private void drawOverlay(@NotNull TessellatorGeneral tessellator, double xd, double yd, double zd) {
		IconCoordinate icon = overlay();
		double minU = icon.getIconUMin();
		double maxU = icon.getIconUMax();
		double minV = icon.getIconVMin();
		double maxV = icon.getIconVMax();

		tessellator.setColorOpaque3f(1.0F, 1.0F, 1.0F);

		double minX = xd + 0.5 - HALF_WIDTH;
		double maxX = xd + 0.5 + HALF_WIDTH;
		double minZ = zd + 0.5 - HALF_WIDTH;
		double maxZ = zd + 0.5 + HALF_WIDTH;

		tessellator.addVertexWithUV(minX, yd + 1.0, minZ, minU, minV);
		tessellator.addVertexWithUV(minX, yd, minZ, minU, maxV);
		tessellator.addVertexWithUV(maxX, yd, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, yd + 1.0, maxZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, yd + 1.0, maxZ, minU, minV);
		tessellator.addVertexWithUV(maxX, yd, maxZ, minU, maxV);
		tessellator.addVertexWithUV(minX, yd, minZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, yd + 1.0, minZ, maxU, minV);
		tessellator.addVertexWithUV(minX, yd + 1.0, maxZ, minU, minV);
		tessellator.addVertexWithUV(minX, yd, maxZ, minU, maxV);
		tessellator.addVertexWithUV(maxX, yd, minZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, yd + 1.0, minZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, yd + 1.0, minZ, minU, minV);
		tessellator.addVertexWithUV(maxX, yd, minZ, minU, maxV);
		tessellator.addVertexWithUV(minX, yd, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, yd + 1.0, maxZ, maxU, minV);
	}

	@NotNull
	@Override
	public IconCoordinate getOverlayTexture(int meta) {
		if (itemIconKey == null) {
			return super.getOverlayTexture(meta);
		}
		if (itemIcon == null) {
			itemIcon = TextureRegistry.getTexture(itemIconKey);
		}
		return itemIcon;
	}

	@NotNull
	private IconCoordinate overlay() {
		if (overlay == null) {
			overlay = TextureRegistry.getTexture(overlayKey);
		}
		return overlay;
	}
}
