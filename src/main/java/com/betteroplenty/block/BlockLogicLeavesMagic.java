package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicLeavesBase;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BlockLogicLeavesMagic extends BlockLogicLeavesBase {

	private static final int EMIT_CHANCE = 5;

	public BlockLogicLeavesMagic(@NotNull Block<?> block, @NotNull Material material,
								 @NotNull Block<?> sapling) {
		super(block, material, sapling);
	}

	@Override
	public void animationTick(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Random rand) {
		super.animationTick(world, tilePos, rand);

		if (rand.nextInt(EMIT_CHANCE) != 0) {
			return;
		}

		if (world.getBlockType(tilePos.down(new TilePos())) == this.block) {
			return;
		}

		world.spawnParticle("magictree",
			tilePos.x() + rand.nextFloat(),
			tilePos.y() + 0.9F,
			tilePos.z() + rand.nextFloat(),
			0.0, 0.0, 0.0, 0, false);
	}
}
