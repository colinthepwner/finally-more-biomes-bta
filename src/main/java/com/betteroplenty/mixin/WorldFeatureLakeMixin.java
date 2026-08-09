package com.betteroplenty.mixin;

import com.betteroplenty.world.BOPSuppression;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeatureLake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(value = WorldFeatureLake.class, remap = false)
public abstract class WorldFeatureLakeMixin {

	@Inject(method = "place(Lnet/minecraft/core/world/World;Ljava/util/Random;III)Z",
		at = @At("HEAD"), cancellable = true)
	private void betteroplenty$suppressInBOPBiomes(
		World world, Random random, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
		if (BOPSuppression.suppressAt(world, x, y, z)) {
			cir.setReturnValue(false);
		}
	}
}
