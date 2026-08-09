package com.betteroplenty.mixin.client;

import java.io.File;

import com.betteroplenty.asset.BOPAssetBridge;
import net.minecraft.client.render.texturepack.TexturePackList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TexturePackList.class, remap = false)
public abstract class TexturePackListBridgeMixin {

	@Inject(method = "init", at = @At("TAIL"))
	private void betteroplenty$bridgeAssets(CallbackInfo ci) {
		try {

			com.betteroplenty.BetterOPlenty.LOGGER.info("Asset bridge: starting.");
			File gameDir = BOPAssetBridge.gameDir();
			if (gameDir == null) return;
			File packDir = BOPAssetBridge.run(gameDir);
			if (packDir != null) {
				BOPAssetBridge.enablePack((TexturePackList) (Object) this, packDir);
			}
			BOPAssetBridge.logSummary();
		} catch (Throwable t) {

			com.betteroplenty.BetterOPlenty.LOGGER.error(
				"Asset bridge failed; the mod will run without BOP's art.", t);
		}
	}
}
