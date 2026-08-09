package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockLogicSunflowerTop extends BlockLogicFlowerBOP {

	public BlockLogicSunflowerTop(@NotNull Block<?> block) {

		super(block, BOPSoils.SUNFLOWER_STALK);
	}

	@NotNull
	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, int data,
									  @Nullable TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(BOPFlowers.SUNFLOWER)};
	}

}
