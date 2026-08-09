package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFlower;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class BlockLogicFlowerBOP extends BlockLogicFlower {

	@NotNull
	private final Predicate<Block<?>> soil;

	private final boolean needsLight;

	public BlockLogicFlowerBOP(@NotNull Block<?> block, @NotNull Predicate<Block<?>> soil,
							   boolean needsLight, double height, double width) {
		super(block);
		this.soil = soil;
		this.needsLight = needsLight;
		this.setBlockBounds(0.5 - width, 0.0, 0.5 - width, 0.5 + width, height, 0.5 + width);
	}

	public BlockLogicFlowerBOP(@NotNull Block<?> block, @NotNull Predicate<Block<?>> soil) {
		this(block, soil, true, 0.8, 0.4);
	}

	@NotNull
	public BlockLogicFlowerBOP withBounds(double minX, double minY, double minZ,
										  double maxX, double maxY, double maxZ) {
		this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
		return this;
	}

	@Override
	public boolean mayPlaceOn(@NotNull Block<?> block) {
		return block != null && this.soil.test(block);
	}

	@Override
	public boolean canStay(@NotNull World world, @NotNull TilePosc tilePos) {
		if (this.needsLight) {
			return super.canStay(world, tilePos);
		}
		return this.mayPlaceOn(world.getBlockType(tilePos.down(new TilePos())));
	}
}
