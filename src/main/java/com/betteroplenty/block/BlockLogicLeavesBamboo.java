package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicLeavesBase;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import java.util.function.Supplier;

public class BlockLogicLeavesBamboo extends BlockLogicLeavesBase {

	private static final int SUPPORT_RADIUS = 4;

	@NotNull
	private final Supplier<Block<?>> stalk;

	public BlockLogicLeavesBamboo(@NotNull Block<?> block, @NotNull Material material,
	                              @NotNull Block<?> sapling, @NotNull Supplier<Block<?>> stalk) {
		super(block, material, sapling);
		this.stalk = stalk;
	}

	@Override
	public void updateTick(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Random rand, boolean isRandomTick) {
		if (!world.isClientSide && this.hasStalkInRange(world, tilePos)) {
			return;
		}
		super.updateTick(world, tilePos, rand, isRandomTick);
	}

	private boolean hasStalkInRange(@NotNull World world, @NotNull TilePosc tilePos) {
		Block<?> bamboo = this.stalk.get();
		if (bamboo == null) {
			return false;
		}
		TilePos p = new TilePos();
		for (int dx = -SUPPORT_RADIUS; dx <= SUPPORT_RADIUS; dx++) {
			for (int dy = -SUPPORT_RADIUS; dy <= SUPPORT_RADIUS; dy++) {
				for (int dz = -SUPPORT_RADIUS; dz <= SUPPORT_RADIUS; dz++) {
					p.set(tilePos.x() + dx, tilePos.y() + dy, tilePos.z() + dz);
					if (world.getBlockType(p) == bamboo) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
