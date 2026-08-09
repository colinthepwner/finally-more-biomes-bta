package com.betteroplenty.item;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ItemBlockDandelion<T extends BlockLogic> extends ItemBlock<T> {

	private static final int SEEDS = 40;

	private static final float JITTER = 8.0F;

	public ItemBlockDandelion(@NotNull Block<T> block) {
		super(block);
	}

	@Nullable
	@Override
	public ItemStack onUse(@NotNull ItemStack selfStack, @NotNull World world, @NotNull Player player) {
		Random rand = world.rand;

		float yaw = player.yRot / 180.0F * (float) Math.PI;
		float pitch = player.xRot / 180.0F * (float) Math.PI;
		double lookX = -MathHelper.sin(yaw) * MathHelper.cos(pitch);
		double lookY = -MathHelper.sin(pitch);
		double lookZ = MathHelper.cos(yaw) * MathHelper.cos(pitch);

		for (int i = 0; i < SEEDS; i++) {

			float jitter = (rand.nextFloat() - 0.5F) / JITTER;
			world.spawnParticle("dandelion",
				player.x + lookX + jitter,
				player.y + lookY + player.getHeadHeight() + jitter,
				player.z + lookZ + jitter,
				0.0, 0.0, 0.0, 0, false);
		}

		selfStack.consumeItem(player);

		return selfStack;
	}
}
