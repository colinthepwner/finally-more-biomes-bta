package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import com.betteroplenty.item.BOPItems;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

public class BlockLogicFlesh extends BlockLogic {

	private static final double SINK = 0.125;

	private static final int CHUNKS_MAX_EXCLUSIVE = 5;

	private static final double DRAG = 0.9;

	public BlockLogicFlesh(@NotNull Block<?> block) {
		super(block, Materials.SPONGE);
	}

	@Nullable
	@Override
	public AABBdc getCollisionAABB(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		return new AABBd(tilePos.x(), tilePos.y(), tilePos.z(),
			tilePos.x() + 1.0D, tilePos.y() + 1.0D - SINK, tilePos.z() + 1.0D);
	}

	@Override
	public void onEntityCollision(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Entity entity) {
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
			default -> {
				int chunks = world.rand.nextInt(CHUNKS_MAX_EXCLUSIVE);
				yield chunks == 0
					? new ItemStack[0]
					: new ItemStack[]{new ItemStack(BOPItems.FLESH_CHUNK, chunks)};
			}
		};
	}
}
