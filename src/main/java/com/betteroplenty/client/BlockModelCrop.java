package com.betteroplenty.client;

import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockModelCrop<T extends BlockLogic> extends BlockModelGeneric<T> {

	private final boolean jittered;

	public BlockModelCrop(@NotNull Block<T> block, @NotNull String modelKey, boolean jittered) {
		super(block, BlockModelDispatcher.loadDataModel(modelKey));
		this.jittered = jittered;

		render3D(false);
	}

	@Override
	public boolean renderAttached(@NotNull TessellatorGeneral tessellator,
	                              @NotNull WorldSource worldSource, @NotNull TilePosc tilePos,
	                              boolean cullFaces, @Nullable IconCoordinate overrideTexture) {
		if (!jittered) {
			return super.renderAttached(tessellator, worldSource, tilePos, cullFaces, overrideTexture);
		}

		long hash = positionHash(tilePos.x(), tilePos.y(), tilePos.z());
		double dx = ((hash >> 16 & 15L) / 15.0F - 0.5D) * 0.125D;
		double dz = ((hash >> 24 & 15L) / 15.0F - 0.5D) * 0.125D;

		return getModel(worldSource, tilePos).renderAttached(
			this, tessellator, worldSource, tilePos, 0, 0, 0, dx, 0.0, dz,
			false, cullFaces, overrideTexture);
	}

	private static long positionHash(int x, int y, int z) {
		long hash = (long) (x * 3129871) ^ (long) z * 116129781L ^ (long) y;
		return hash * hash * 42317861L + hash * 11L;
	}
}
