package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

import java.util.function.Supplier;

public class BlockLogicOreGem extends BlockLogic {

	private final Supplier<Item> gem;

	public BlockLogicOreGem(Block<?> block, Material material, Supplier<Item> gem) {
		super(block, material);
		this.gem = gem;
	}

	@Override
	public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return switch (dropCause) {
			case SILK_TOUCH, PICK_BLOCK -> new ItemStack[]{new ItemStack(this.block)};
			case EXPLOSION, PROPER_TOOL, PISTON_CRUSH -> new ItemStack[]{new ItemStack(this.gem.get())};
			default -> null;
		};
	}
}
