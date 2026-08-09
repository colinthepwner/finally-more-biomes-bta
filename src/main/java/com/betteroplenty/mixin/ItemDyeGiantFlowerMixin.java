package com.betteroplenty.mixin;

import biomesoplenty.worldgen.WorldGenGiantFlowerRed;
import biomesoplenty.worldgen.WorldGenGiantFlowerYellow;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemDye;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemDye.class, remap = false)
public abstract class ItemDyeGiantFlowerMixin {

	@Inject(
		method = "onUseOnBlock(Lnet/minecraft/core/item/ItemStack;Lnet/minecraft/core/world/World;"
			+ "Lnet/minecraft/core/entity/player/Player;Lnet/minecraft/core/world/pos/TilePosc;"
			+ "Lnet/minecraft/core/util/helper/Side;DD)Z",
		at = @At("HEAD"),
		cancellable = true
	)
	private void betteroplenty$growGiantFlower(
			ItemStack selfStack, World world, Player player, TilePosc blockPos, Side side,
			double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {

		if (selfStack.getMetadata() != DyeColor.WHITE.itemMeta) {
			return;
		}

		Block<?> block = world.getBlockType(blockPos);
		boolean red = block == Blocks.FLOWER_RED;
		if (!red && block != Blocks.FLOWER_YELLOW) {
			return;
		}

		if (!world.isClientSide) {
			if (world.rand.nextFloat() < 0.45f) {
				int x = blockPos.x();
				int y = blockPos.y() - 1;
				int z = blockPos.z();
				if (red) {
					new WorldGenGiantFlowerRed().generate(world, world.rand, x, y, z);
				} else {
					new WorldGenGiantFlowerYellow().generate(world, world.rand, x, y, z);
				}
			}
			if (player == null || player.getGamemode().hasBlockConsumption()) {
				selfStack.stackSize--;
			}
		}

		cir.setReturnValue(true);
		cir.cancel();
	}
}
