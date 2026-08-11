package com.betteroplenty.mixin.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.betteroplenty.BetterOPlenty;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.texture.stitcher.AtlasStitcher;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TextureManager.class, remap = false)
public abstract class TextureManagerAnimationAuditMixin {

	@Inject(method = "initDynamicTextures", at = @At("TAIL"))
	private void betteroplenty$auditAnimations(Collection<? super Throwable> errors, CallbackInfo ci) {
		int animated = 0;
		List<String> unanimated = new ArrayList<>();

		for (AtlasStitcher atlas : TextureRegistry.atlases) {
			for (IconCoordinate icon : atlas.iconMap.values()) {
				if (!BetterOPlenty.MOD_ID.equals(icon.namespaceId.namespace())) continue;

				if (icon.sourceImageHeight <= icon.sourceImageWidth) continue;
				if (icon.hasMeta("animation")) {
					animated++;
				} else {
					unanimated.add(icon.namespaceId.toString());
				}
			}
		}

		if (animated > 0) {
			BetterOPlenty.LOGGER.info("Animated tiles: {} carrying their frame timing.", animated);
		}
		if (!unanimated.isEmpty()) {

			BetterOPlenty.LOGGER.warn("Animated tiles: {} stacked-frame texture(s) have no animation "
				+ "metadata and will render as a squashed strip. The '.png.mcmeta' has to be in the "
				+ "same pack as the '.png'. Affected: {}", unanimated.size(), unanimated);
		}
	}
}
