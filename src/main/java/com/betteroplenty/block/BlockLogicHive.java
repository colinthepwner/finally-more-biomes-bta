package com.betteroplenty.block;

import biomesoplenty.entities.EntityWasp;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BlockLogicHive extends BlockLogic {

	public enum Cell {

		COMB,

		SHELL,

		BROOD,

		FILLED
	}

	@NotNull
	private final Cell cell;

	@Nullable
	private final Supplier<IItemConvertible> drop;

	public BlockLogicHive(@NotNull Block<?> block, @NotNull Cell cell,
						  @Nullable Supplier<IItemConvertible> drop) {

		super(block, Materials.WOOD);
		this.cell = cell;
		this.drop = drop;
	}

	@Nullable
	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, int data,
									  @Nullable TileEntity tileEntity) {
		if (dropCause == EnumDropCause.PICK_BLOCK || dropCause == EnumDropCause.SILK_TOUCH) {
			return new ItemStack[]{new ItemStack(this.block)};
		}

		switch (this.cell) {
			case BROOD:

				return new ItemStack[0];

			case FILLED:

				if (this.drop == null || world.rand.nextInt(2) == 0) {
					return new ItemStack[0];
				}
				return new ItemStack[]{new ItemStack(this.drop.get(), 1)};

			default:
				return super.getBreakResult(world, dropCause, data, tileEntity);
		}
	}

	@Override
	public void onRemoved(@NotNull World world, @NotNull TilePosc tilePos, int data) {
		super.onRemoved(world, tilePos, data);

		if (this.cell != Cell.BROOD) {
			return;
		}

		EntityWasp wasp = new EntityWasp(world);
		wasp.moveTo(tilePos.x() + 0.6, tilePos.y(), tilePos.z() + 0.3, 0.0F, 0.0F);
		world.entityJoinedWorld(wasp);
	}
}
