package com.betteroplenty.mixin.client;

import java.io.InputStream;

import com.betteroplenty.res.ObfResources;
import net.minecraft.client.render.texturepack.TexturePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TexturePack.class, remap = false)
public abstract class TexturePackPackedMixin {

	@Inject(method = "getResourceAsStream", at = @At("HEAD"), cancellable = true)
	private void betteroplenty$packedResource(String path, CallbackInfoReturnable<InputStream> cir) {
		if (!ObfResources.ACTIVE) {
			return;
		}
		InputStream packed = ObfResources.open(path);
		if (packed != null) {
			cir.setReturnValue(packed);
		}
	}
}
