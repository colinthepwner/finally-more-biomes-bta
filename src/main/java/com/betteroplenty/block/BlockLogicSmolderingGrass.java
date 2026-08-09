package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

import java.util.Random;

public class BlockLogicSmolderingGrass extends BlockLogic {

	public static final int CONTACT_FIRE_TICKS = 40;

	public static final double SINK = 0.02D;

	public BlockLogicSmolderingGrass(@NotNull Block<?> block) {
		super(block, Materials.GRASS);
	}

	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause,
									  int data, @Nullable TileEntity tileEntity) {
		return switch (dropCause) {
			case PISTON_CRUSH, WORLD, EXPLOSION, PROPER_TOOL ->
				new ItemStack[]{new ItemStack(Blocks.DIRT)};
			case PICK_BLOCK, SILK_TOUCH -> new ItemStack[]{new ItemStack(this)};
			default -> null;
		};
	}

	@Override
	public AABBdc getCollisionAABB(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		return new AABBd(tilePos.x(), tilePos.y(), tilePos.z(),
			tilePos.x() + 1.0D, tilePos.y() + 1.0D - SINK, tilePos.z() + 1.0D);
	}

	@Override
	public void animationTick(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Random rand) {
		super.animationTick(world, tilePos, rand);

		if (rand.nextInt(4) == 0) {
			world.spawnParticle("smoke",
				tilePos.x() + rand.nextFloat(), tilePos.y() + 1.1F, tilePos.z() + rand.nextFloat(),
				0.0D, 0.0D, 0.0D, 0, false);
		}
		if (rand.nextInt(6) == 0) {
			world.spawnParticle("flame",
				tilePos.x() + rand.nextFloat(), tilePos.y() + 1.1F, tilePos.z() + rand.nextFloat(),
				0.0D, 0.0D, 0.0D, 0, false);
		}
	}

	@Override
	public void onEntityCollision(@NotNull World world, @NotNull TilePosc tilePos,
								  @NotNull Entity entity) {
		if (entity.remainingFireTicks < CONTACT_FIRE_TICKS) {
			entity.remainingFireTicks = CONTACT_FIRE_TICKS;
			entity.maxFireTicks = CONTACT_FIRE_TICKS;
			entity.activeFireBlock = Blocks.FIRE;
		}
	}
}
