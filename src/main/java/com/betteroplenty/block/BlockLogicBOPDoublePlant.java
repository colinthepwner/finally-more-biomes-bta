package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BlockLogicBOPDoublePlant extends BlockLogicBOPGroundCover {

	private final boolean upper;

	@NotNull private final Supplier<Block<?>> other;

	@Nullable private final Supplier<Block<?>> whenOrphaned;

	private BlockLogicBOPDoublePlant(@NotNull Block<?> block, boolean upper,
									 @NotNull Supplier<Block<?>> other,
									 @Nullable Supplier<Block<?>> whenOrphaned) {
		super(block);
		this.upper = upper;
		this.other = other;
		this.whenOrphaned = whenOrphaned;
	}

	@NotNull
	public static BlockLogicBOPDoublePlant lower(@NotNull Block<?> block,
												 @NotNull Supplier<Block<?>> top,
												 @Nullable Supplier<Block<?>> whenOrphaned) {
		return new BlockLogicBOPDoublePlant(block, false, top, whenOrphaned);
	}

	@NotNull
	public static BlockLogicBOPDoublePlant upper(@NotNull Block<?> block,
												 @NotNull Supplier<Block<?>> bottom) {
		return new BlockLogicBOPDoublePlant(block, true, bottom, null);
	}

	@Override
	protected boolean mayPlaceOn(@NotNull Block<?> block) {
		return this.upper ? block == this.other.get() : super.mayPlaceOn(block);
	}

	@Override
	public void onNeighborChanged(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Block<?> block) {
		if (world.isClientSide) {
			return;
		}

		TilePos query = new TilePos();

		if (this.upper) {

			if (world.getBlockType(tilePos.down(query)) != this.other.get()) {
				this.dropWithCause(world, EnumDropCause.WORLD, tilePos, world.getBlockData(tilePos), null, null);
				world.setBlockTypeNotify(tilePos, Blocks.AIR);
			}
			return;
		}

		if (world.getBlockType(tilePos.up(query)) != this.other.get()) {
			Block<?> replacement = this.whenOrphaned == null ? Blocks.AIR : this.whenOrphaned.get();
			world.setBlockTypeNotify(tilePos, replacement);
			return;
		}

		if (!this.canStay(world, tilePos)) {

			this.dropWithCause(world, EnumDropCause.WORLD, tilePos, world.getBlockData(tilePos), null, null);
			world.setBlockTypeNotify(tilePos, Blocks.AIR);
		}
	}
}
