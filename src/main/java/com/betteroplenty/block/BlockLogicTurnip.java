package com.betteroplenty.block;

import com.betteroplenty.item.BOPFoods;
import com.betteroplenty.item.BOPItems;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicCropsWheat;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockLogicTurnip extends BlockLogicCropsWheat {

	public BlockLogicTurnip(@NotNull Block<?> block) {
		super(block);
	}

	@NotNull
	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause,
									  int data, @Nullable TileEntity tileEntity) {
		if (data != MAX_GROWTH_STATE) {
			return new ItemStack[]{new ItemStack(BOPItems.TURNIP_SEEDS)};
		}
		return new ItemStack[]{
			new ItemStack(BOPItems.TURNIP_SEEDS, world.rand.nextInt(3) + 1),
			new ItemStack(BOPFoods.TURNIP),
		};
	}
}
