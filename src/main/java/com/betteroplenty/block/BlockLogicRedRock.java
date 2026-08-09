package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BlockLogicRedRock extends BlockLogic {

	@NotNull
	private final Supplier<Block<?>> cobble;

	public BlockLogicRedRock(@NotNull Block<?> block, @NotNull Material material,
							 @NotNull Supplier<Block<?>> cobble) {
		super(block, material);
		this.cobble = cobble;
	}

	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause,
									  int data, @Nullable TileEntity tileEntity) {
		return switch (dropCause) {
			case PISTON_CRUSH, WORLD, EXPLOSION, PROPER_TOOL ->
				new ItemStack[]{new ItemStack(this.cobble.get())};
			case PICK_BLOCK, SILK_TOUCH -> new ItemStack[]{new ItemStack(this)};
			default -> null;
		};
	}
}
