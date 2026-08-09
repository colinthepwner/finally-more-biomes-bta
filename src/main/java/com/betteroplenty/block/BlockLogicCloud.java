package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicTransparent;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

public class BlockLogicCloud extends BlockLogicTransparent {

	private static final double SINK = 0.25;

	private static final double DRAG = 0.8;

	public BlockLogicCloud(@NotNull Block<?> block) {
		super(block, Materials.SPONGE);
	}

	@Nullable
	@Override
	public AABBdc getCollisionAABB(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		return new AABBd(tilePos.x(), tilePos.y(), tilePos.z(),
			tilePos.x() + 1.0, tilePos.y() + 1.0 - SINK, tilePos.z() + 1.0);
	}

	@Override
	public void onEntityCollision(@NotNull World world, @NotNull TilePosc tilePos,
	                              @NotNull Entity entity) {

		entity.fallDistance = 0.0F;

		if (applyDrag(entity)) {
			entity.xd *= DRAG;
			entity.zd *= DRAG;
		}
	}

	private static boolean applyDrag(@NotNull Entity entity) {
		return true;
	}

}
