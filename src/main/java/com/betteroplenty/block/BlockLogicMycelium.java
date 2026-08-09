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

public class BlockLogicMycelium extends BlockLogic {

	@NotNull
	private final Block<?> dirt;

	public BlockLogicMycelium(@NotNull Block<?> block, @NotNull Block<?> dirt) {
		super(block, Materials.GRASS);
		this.dirt = dirt;
	}

	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, int data,
	                                  @Nullable TileEntity tileEntity) {
		return switch (dropCause) {
			case SILK_TOUCH, PICK_BLOCK -> new ItemStack[]{new ItemStack(this.block)};
			default -> new ItemStack[]{new ItemStack(this.dirt)};
		};
	}
}
