package com.betteroplenty.block;

import com.betteroplenty.item.BOPItems;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

import java.util.Random;

public class BlockLogicAshBOP extends BlockLogic {

	public static final double SINK = 0.125D;

	public static final double DRAG = 0.4D;

	public static final int ASH_PER_BLOCK = 4;

	public BlockLogicAshBOP(@NotNull Block<?> block) {
		super(block, Materials.SAND);
	}

	@Override
	public AABBdc getCollisionAABB(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		return new AABBd(tilePos.x(), tilePos.y(), tilePos.z(),
			tilePos.x() + 1.0D, tilePos.y() + 1.0D - SINK, tilePos.z() + 1.0D);
	}

	@Override
	public void animationTick(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Random rand) {
		super.animationTick(world, tilePos, rand);

		if (rand.nextInt(2) == 0) {
			world.spawnParticle("smoke",
				tilePos.x() + rand.nextFloat(), tilePos.y() + 1.1F, tilePos.z() + rand.nextFloat(),
				0.0D, 0.0D, 0.0D, 0, false);
		}
	}

	@Override
	public void onEntityCollision(@NotNull World world, @NotNull TilePosc tilePos,
								  @NotNull Entity entity) {
		if (entity instanceof Player player) {
			ItemStack boots = player.getItemInArmorSlot(HumanArmorShape.BOOTS);
			if (boots != null && boots.itemID == BOPItems.WADING_BOOTS.id) {
				return;
			}
		}

		entity.xd *= DRAG;
		entity.zd *= DRAG;
	}

	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause,
									  int data, TileEntity tileEntity) {
		return switch (dropCause) {
			case SILK_TOUCH, PICK_BLOCK -> new ItemStack[]{new ItemStack(this.block)};
			case IMPROPER_TOOL -> null;
			default -> new ItemStack[]{new ItemStack(BOPItems.ASH, ASH_PER_BLOCK)};
		};
	}
}
