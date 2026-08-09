package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicSugarcane;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

public class BlockLogicBamboo extends BlockLogicSugarcane {

	private static final double INSET = 4.0 / 16.0;

	public BlockLogicBamboo(@NotNull Block<?> block) {
		super(block);
		this.setBlockBounds(INSET, 0.0, INSET, 1.0 - INSET, 1.0, 1.0 - INSET);
	}

	@Override
	public boolean canPlaceAt(@NotNull World world, @NotNull TilePosc tilePos) {
		Block<?> below = world.getBlockType(tilePos.down(new TilePos()));
		return below == this.block || below == Blocks.GRASS;
	}

	@Override
	public boolean canStay(@NotNull World world, @NotNull TilePosc tilePos) {
		return this.canPlaceAt(world, tilePos);
	}

	@Nullable
	@Override
	public AABBdc getCollisionAABB(@NotNull WorldSource source, @NotNull TilePosc tilePos) {

		return this.getBoundsFromState(source, tilePos)
			.translate(tilePos.x(), tilePos.y(), tilePos.z(), new AABBd());
	}

	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, int data,
	                                  @Nullable TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(this.block)};
	}
}
