package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockLogicSunflower extends BlockLogicFlowerBOP {

	public BlockLogicSunflower(@NotNull Block<?> block) {

		super(block, BOPSoils.GROWS_FLOWERS);
	}

	@Override
	public boolean canPlaceAt(@NotNull World world, @NotNull TilePosc tilePos) {
		return super.canPlaceAt(world, tilePos) && world.isAirBlock(tilePos.up(new TilePos()));
	}

	@Override
	public void onPlacedOnSide(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side,
							   double xHit, double yHit) {
		TilePos above = tilePos.up(new TilePos());
		if (world.isAirBlock(above)) {
			world.setBlockTypeNotify(above, BOPFlowers.SUNFLOWER_TOP);
		}
	}

	@Override
	public void onNeighborChanged(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Block<?> block) {
		super.onNeighborChanged(world, tilePos, block);

		if (world.getBlockType(tilePos) != this.block) {
			return;
		}
		if (world.getBlockType(tilePos.up(new TilePos())) != BOPFlowers.SUNFLOWER_TOP) {
			world.setBlockTypeNotify(tilePos, Blocks.AIR);
		}
	}

	@NotNull
	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, int data,
									  @Nullable TileEntity tileEntity) {
		return switch (dropCause) {
			case SILK_TOUCH, PICK_BLOCK -> new ItemStack[]{new ItemStack(this.block)};
			default -> new ItemStack[0];
		};
	}
}
