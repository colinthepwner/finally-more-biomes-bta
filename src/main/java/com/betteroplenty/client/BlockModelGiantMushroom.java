package com.betteroplenty.client;

import com.betteroplenty.block.BlockLogicGiantMushroom;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockModelGiantMushroom<T extends BlockLogic> extends BlockModelStandard<T> {

	@NotNull private final IconCoordinate skin;
	@NotNull private final IconCoordinate pores;

	private boolean standalone;

	public BlockModelGiantMushroom(@NotNull Block<T> block, @NotNull String skinTexture) {
		super(block);
		this.skin = TextureRegistry.getTexture(skinTexture);
		this.pores = TextureRegistry.getTexture("betteroplenty:block/mushroom_skin_stem");

		this.setAllTextures(skinTexture);
	}

	@Nullable
	@Override
	public IconCoordinate getBlockTextureFromSideAndMetadata(@NotNull Side side, int data) {
		if (this.standalone && data == BlockLogicGiantMushroom.PORES) {
			return this.skin;
		}

		int skinned = BlockLogicGiantMushroom.skinnedFaces(data);

		IconCoordinate faceSkin = data == BlockLogicGiantMushroom.STEM ? this.pores : this.skin;
		return (skinned & bit(side)) != 0 ? faceSkin : this.pores;
	}

	@Override
	public void renderStandalone(@NotNull net.minecraft.client.render.tessellator.TessellatorGeneral tessellator,
								 int metadata, byte lightIndex) {
		this.standalone = true;
		try {
			super.renderStandalone(tessellator, metadata, lightIndex);
		} finally {
			this.standalone = false;
		}
	}

	private static int bit(@NotNull Side side) {
		return switch (side) {
			case TOP -> BlockLogicGiantMushroom.FACE_TOP;
			case BOTTOM -> BlockLogicGiantMushroom.FACE_BOTTOM;
			case NORTH -> BlockLogicGiantMushroom.FACE_NORTH;
			case SOUTH -> BlockLogicGiantMushroom.FACE_SOUTH;
			case WEST -> BlockLogicGiantMushroom.FACE_WEST;
			case EAST -> BlockLogicGiantMushroom.FACE_EAST;
			default -> 0;
		};
	}
}
