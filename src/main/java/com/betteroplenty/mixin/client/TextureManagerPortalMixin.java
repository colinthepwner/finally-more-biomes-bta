package com.betteroplenty.mixin.client;

import com.betteroplenty.client.DynamicTexturePromisedPortal;
import java.util.Collection;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.dynamictexture.DynamicTexture;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TextureManager.class, remap = false)
public abstract class TextureManagerPortalMixin {

	@Shadow
	protected abstract void addDynamicTexture(DynamicTexture texture);

	@Inject(method = "initDynamicTextures", at = @At("TAIL"))
	private void betteroplenty$animatePromisedPortal(Collection<? super Throwable> errors,
													 CallbackInfo ci) {
		this.addDynamicTexture(new DynamicTexturePromisedPortal(
			TextureRegistry.getTexture("betteroplenty:block/portal")));
	}
}
