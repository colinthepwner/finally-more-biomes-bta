package com.betteroplenty.mixin;

import com.betteroplenty.world.BOPSuppression;
import net.minecraft.core.world.generate.chunk.perlin.overworld.ChunkDecoratorOverworld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ChunkDecoratorOverworld.class, remap = false)
public abstract class ChunkDecoratorOverworldMixin {

	@Inject(method = "applySnowAndIceForColumn(III)V", at = @At("HEAD"), cancellable = true)
	private void betteroplenty$deferSnowUntilTreesArePlaced(int dx, int dz, int oceanY,
	                                                        CallbackInfo ci) {
		if (BOPSuppression.deferSnow) {
			ci.cancel();
		}
	}
}
