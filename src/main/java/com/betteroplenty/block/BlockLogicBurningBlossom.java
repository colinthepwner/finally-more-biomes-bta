package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.ItemToolShears;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlockLogicBurningBlossom extends BlockLogicFlowerBOP {

	public static final int CONTACT_FIRE_TICKS = 20;

	public static final int HARVEST_FIRE_TICKS = 100;

	public BlockLogicBurningBlossom(@NotNull Block<?> block) {

		super(block, BOPSoils.NETHERRACK, false, 0.8, 0.4);
	}

	@Override
	public void onEntityCollision(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Entity entity) {
		if (BlockLogicDeathbloom.isProtected(entity)) {
			return;
		}
		ignite(entity, CONTACT_FIRE_TICKS);
	}

	@Override
	public void onHarvest(@NotNull World world, @NotNull Player player, @NotNull TilePosc tilePos,
						  int data, @Nullable TileEntity tileEntity) {
		super.onHarvest(world, player, tilePos, data, tileEntity);

		ItemStack held = player.getHeldItem();
		if (held != null && held.getItem() instanceof ItemToolShears) {
			return;
		}
		ignite(player, HARVEST_FIRE_TICKS);
	}

	@Override
	public void animationTick(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Random rand) {
		super.animationTick(world, tilePos, rand);

		if (rand.nextInt(2) == 0) {
			world.spawnParticle("smoke",
				tilePos.x() + rand.nextFloat(), tilePos.y() + rand.nextFloat(), tilePos.z() + rand.nextFloat(),
				0.0, 0.0, 0.0, 0, false);
		}
		if (rand.nextInt(4) == 0) {
			world.spawnParticle("flame",
				tilePos.x() + rand.nextFloat(), tilePos.y() + rand.nextFloat(), tilePos.z() + rand.nextFloat(),
				0.0, 0.0, 0.0, 0, false);
		}
	}

	private static void ignite(@NotNull Entity entity, int ticks) {
		if (entity.remainingFireTicks < ticks) {
			entity.remainingFireTicks = ticks;
			entity.maxFireTicks = ticks;
			entity.activeFireBlock = Blocks.FIRE;
		}
	}
}
