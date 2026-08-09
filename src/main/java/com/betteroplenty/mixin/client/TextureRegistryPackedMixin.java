package com.betteroplenty.mixin.client;

import com.betteroplenty.res.ObfResources;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TextureRegistry.class, remap = false)
public abstract class TextureRegistryPackedMixin {

	private static final String ROOT = "/assets/betteroplenty/textures/";

	@Inject(method = "getFilesAndSubFiles", at = @At("HEAD"), cancellable = true)
	private static void betteroplenty$packedFileList(String directory, boolean searchSubDirectories,
	                                                 CallbackInfoReturnable<String[]> cir) {
		if (!ObfResources.ACTIVE || directory == null || !directory.startsWith(ROOT)) {
			return;
		}
		String[] names = ObfResources.list(directory, searchSubDirectories);
		if (names != null) {
			cir.setReturnValue(names);
		}
	}
}
