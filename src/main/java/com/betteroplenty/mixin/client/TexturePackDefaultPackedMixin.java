package com.betteroplenty.mixin.client;

import com.betteroplenty.res.ObfResources;
import net.minecraft.client.render.texturepack.TexturePackDefault;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TexturePackDefault.class, remap = false)
public abstract class TexturePackDefaultPackedMixin {

	@Inject(method = "hasFile", at = @At("HEAD"), cancellable = true)
	private void betteroplenty$packedHasFile(String file, CallbackInfoReturnable<Boolean> cir) {
		if (ObfResources.has(file)) {
			cir.setReturnValue(true);
		}
	}
}
