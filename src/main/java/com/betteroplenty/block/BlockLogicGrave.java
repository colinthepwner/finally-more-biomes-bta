package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicAxisAligned;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import net.minecraft.core.block.entity.TileEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

import java.util.function.Supplier;

public class BlockLogicGrave extends BlockLogicAxisAligned {

	public static final double THIN = 0.3125;

	public static final double THICK = 0.6875;

	public static final double TOP = 0.875;

	private final boolean upper;

	@NotNull private final Supplier<Block<?>> other;

	private BlockLogicGrave(@NotNull Block<?> block, boolean upper, @NotNull Supplier<Block<?>> other) {

		super(block, Materials.STONE);
		this.upper = upper;
		this.other = other;
	}

	@NotNull
	public static BlockLogicGrave lower(@NotNull Block<?> block, @NotNull Supplier<Block<?>> top) {
		return new BlockLogicGrave(block, false, top);
	}

	@NotNull
	public static BlockLogicGrave upper(@NotNull Block<?> block, @NotNull Supplier<Block<?>> bottom) {
		return new BlockLogicGrave(block, true, bottom);
	}

	public boolean isUpper() {
		return this.upper;
	}

	public static boolean alongZ(int data) {
		return metaToAxis(data & MASK_DIRECTION) == Axis.Z;
	}

	@NotNull
	@Override
	public AABBdc getBoundsFromState(@NotNull WorldSource source, @NotNull TilePosc tilePos) {

		return alongZ(source.getBlockData(tilePos))
			? new AABBd(THIN, 0.0, 0.0, THICK, TOP, 1.0)
			: new AABBd(0.0, 0.0, THIN, 1.0, TOP, THICK);
	}

	@Override
	public void onNeighborChanged(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Block<?> block) {

		if (block != this.block && block != this.other.get()) {
			return;
		}

		TilePos query = new TilePos();
		TilePosc mine = this.upper ? tilePos.down(query) : tilePos.up(query);

		if (world.getBlockType(mine) != this.other.get()) {

			this.dropWithCause(world, EnumDropCause.WORLD, tilePos, world.getBlockData(tilePos), null, null);
			world.setBlockTypeNotify(tilePos, Blocks.AIR);
		}
	}

	@Nullable
	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, int data,
									  @Nullable TileEntity tileEntity) {
		if (this.upper) {
			return new ItemStack[0];
		}
		return dropCause != EnumDropCause.IMPROPER_TOOL
			? new ItemStack[]{new ItemStack(this.block)}
			: null;
	}

	@Override
	public boolean isSolidRender() {
		return false;
	}

	@Override
	public boolean isCubeShaped() {
		return false;
	}
}
