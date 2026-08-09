package com.betteroplenty.fluid;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFluidStill;
import net.minecraft.core.block.Fluid;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import java.util.function.Supplier;

public class BlockLogicBOPFluidStill extends BlockLogicFluidStill {
	@NotNull
	private final Supplier<Block<?>> flowing;
	@NotNull
	private final BOPFluidContact contact;

	public BlockLogicBOPFluidStill(@NotNull Block<?> block, @NotNull Material material, @NotNull Fluid fluid,
								   @NotNull Supplier<Block<?>> flowing, @NotNull BOPFluidContact contact) {

		super(block, material, fluid, block);
		this.flowing = flowing;
		this.contact = contact;
	}

	@Override
	public void onNeighborChanged(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Block<?> block) {
		this.checkForHarden(world, tilePos);

		if (world.getBlockType(tilePos) == this.block) {
			Block<?> flowingBlock = this.flowing.get();
			int meta = world.getBlockData(tilePos);
			world.setBlockTypeData(tilePos, flowingBlock, meta);
			world.markBlocksDirty(tilePos, tilePos);
			world.scheduleBlockUpdate(tilePos, flowingBlock, this.tickDelay(world, tilePos));
		}
	}

	private void checkForHarden(@NotNull World world, @NotNull TilePosc tilePos) {
		if (world.getBlockType(tilePos) != this.block) {
			return;
		}
		TilePos queryPos = new TilePos();
		for (Direction direction : CHECK_HARDEN_DIRECTIONS) {
			if (this.fluid.checkForHarden(this, world, tilePos, world.getBlockMaterial(tilePos.add(direction, queryPos)))) {
				return;
			}
		}
	}

	@Override
	public void onEntityInside(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Entity entity,
							   @NotNull Vector3d velocityDirection) {
		super.onEntityInside(world, tilePos, entity, velocityDirection);
		if (!world.isClientSide) {
			this.contact.apply(world, tilePos, entity);
		}
	}
}
