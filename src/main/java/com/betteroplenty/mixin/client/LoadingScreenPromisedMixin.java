package com.betteroplenty.mixin.client;

import com.betteroplenty.world.promised.DimensionPromisedLand;
import net.minecraft.client.render.LoadingScreenRenderer;
import net.minecraft.core.world.Dimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LoadingScreenRenderer.class, remap = false)
public abstract class LoadingScreenPromisedMixin {

	@Shadow
	private String backgroundPath;

	@Inject(method = "updateLoadingBackground", at = @At("TAIL"))
	private void betteroplenty$promisedLandBackground(Dimension dimension, CallbackInfo ci) {

		if (dimension != null && dimension == DimensionPromisedLand.PROMISED_LAND) {
			this.backgroundPath = "/assets/betteroplenty/textures/gui/background-loading-promised.png";
		}
	}
}
