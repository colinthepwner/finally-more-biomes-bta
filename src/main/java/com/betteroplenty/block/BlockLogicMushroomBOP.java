package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicMushroom;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class BlockLogicMushroomBOP extends BlockLogicMushroom {

	@NotNull
	private final Predicate<Block<?>> soil;

	public BlockLogicMushroomBOP(@NotNull Block<?> block, @NotNull Predicate<Block<?>> soil) {
		super(block);
		this.soil = soil;
	}

	@Override
	public boolean mayPlaceOn(@NotNull Block<?> block) {
		return block != null && this.soil.test(block);
	}
}
