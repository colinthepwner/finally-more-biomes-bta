package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicAxisAligned;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

public class BlockLogicBOPBones extends BlockLogicAxisAligned {

	public static final double SMALL_INSET = 0.374;

	public static final double MEDIUM_INSET = 0.187;

	private final double inset;

	public BlockLogicBOPBones(@NotNull Block<?> block, double inset) {

		super(block, Materials.STONE);
		this.inset = inset;
	}

	@NotNull
	@Override
	public AABBdc getBoundsFromState(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		Axis axis = metaToAxis(source.getBlockData(tilePos) & MASK_DIRECTION);
		double lo = this.inset;
		double hi = 1.0 - this.inset;

		switch (axis) {

			case Z:
				return new AABBd(lo, lo, 0.0, hi, hi, 1.0);

			case X:
				return new AABBd(0.0, lo, lo, 1.0, hi, hi);

			case Y:
			default:
				return new AABBd(lo, 0.0, lo, hi, 1.0, hi);
		}
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
