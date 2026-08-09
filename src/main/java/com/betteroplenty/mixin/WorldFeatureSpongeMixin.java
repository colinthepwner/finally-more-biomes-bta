package com.betteroplenty.mixin;

import com.betteroplenty.world.BOPSuppression;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeatureSponge;
import net.minecraft.core.world.pos.TilePosc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(value = WorldFeatureSponge.class, remap = false)
public abstract class WorldFeatureSpongeMixin {

	@Inject(method = "place(Lnet/minecraft/core/world/World;Ljava/util/Random;Lnet/minecraft/core/world/pos/TilePosc;)Z",
		at = @At("HEAD"), cancellable = true)
	private void betteroplenty$suppressInBOPBiomes(
		World world, Random random, TilePosc tilePos, CallbackInfoReturnable<Boolean> cir) {
		if (BOPSuppression.suppressAt(world, tilePos.x(), tilePos.y(), tilePos.z())) {
			cir.setReturnValue(false);
		}
	}
}
