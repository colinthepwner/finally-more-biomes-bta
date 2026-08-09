package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BlockLogicBOPPetals extends BlockLogic {

	public static final int DROP_CHANCE = 20;

	private final Supplier<Block<?>> flower;

	public BlockLogicBOPPetals(@NotNull Block<?> block, @NotNull Supplier<Block<?>> flower) {
		super(block, Materials.LEAVES);
		this.flower = flower;
	}

	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause,
	                                  int data, @Nullable TileEntity tileEntity) {

		if (dropCause == EnumDropCause.SILK_TOUCH || dropCause == EnumDropCause.PICK_BLOCK) {
			return new ItemStack[]{new ItemStack(this)};
		}

		if (world.rand.nextInt(DROP_CHANCE) != 0) {
			return new ItemStack[0];
		}
		return new ItemStack[]{new ItemStack(this.flower.get())};
	}
}
