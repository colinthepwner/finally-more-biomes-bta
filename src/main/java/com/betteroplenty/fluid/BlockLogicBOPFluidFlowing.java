package com.betteroplenty.fluid;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFluidFlowing;
import net.minecraft.core.block.Fluid;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

public class BlockLogicBOPFluidFlowing extends BlockLogicFluidFlowing {
	@NotNull
	private final BOPFluidContact contact;

	public BlockLogicBOPFluidFlowing(@NotNull Block<?> block, @NotNull Material material, @NotNull Fluid fluid,
									 @NotNull Block<?> blockStill, @NotNull BOPFluidContact contact) {
		super(block, material, fluid, blockStill);
		this.contact = contact;
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
