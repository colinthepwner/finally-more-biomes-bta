package com.betteroplenty.block;

import com.betteroplenty.item.BOPItems;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class BlockLogicOreAmethyst extends BlockLogic {

	public BlockLogicOreAmethyst(Block<?> block, net.minecraft.core.block.material.Material material) {
		super(block, material);
	}

	@Override
	public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		switch (dropCause) {
			case SILK_TOUCH:
			case PICK_BLOCK:
				return new ItemStack[]{new ItemStack(this.block)};
			case PROPER_TOOL:
				return new ItemStack[]{new ItemStack(BOPItems.AMETHYST)};
			default:
				return new ItemStack[0];
		}
	}
}
