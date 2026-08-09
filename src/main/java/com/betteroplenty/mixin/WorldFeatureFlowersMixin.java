package com.betteroplenty.mixin;

import com.betteroplenty.world.BOPSuppression;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeatureFlowers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(value = WorldFeatureFlowers.class, remap = false)
public abstract class WorldFeatureFlowersMixin {

	@Shadow @Final private int plantBlockId;

	@Inject(method = "place(Lnet/minecraft/core/world/World;Ljava/util/Random;III)Z",
		at = @At("HEAD"), cancellable = true)
	private void betteroplenty$suppressInBOPBiomes(
		World world, Random random, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
		if ((this.plantBlockId == Blocks.FLOWER_RED.id()
			|| this.plantBlockId == Blocks.MUSHROOM_BROWN.id()
			|| this.plantBlockId == Blocks.MUSHROOM_RED.id())
			&& BOPSuppression.suppressAt(world, x, y, z)) {
			cir.setReturnValue(false);
		}
	}
}
