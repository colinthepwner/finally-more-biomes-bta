package com.betteroplenty.mixin.client;

import com.betteroplenty.client.BlockColorBOP;
import com.betteroplenty.client.MeshProfile;
import net.minecraft.client.render.terrain.ChunkRendererMultiDraw;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChunkRendererMultiDraw.class, remap = false)
public abstract class ChunkRebuildGenerationMixin {

	@Inject(method = "rebuild(Z)Z", at = @At("HEAD"))
	private void betteroplenty$beginMeshGeneration(boolean priority, CallbackInfoReturnable<Boolean> cir) {
		BlockColorBOP.bumpMeshGeneration();
		MeshProfile.begin();
	}

	@Inject(method = "rebuild(Z)Z", at = @At("RETURN"))
	private void betteroplenty$endMeshGeneration(boolean priority, CallbackInfoReturnable<Boolean> cir) {
		BlockColorBOP.bumpMeshGeneration();
		MeshProfile.end(Boolean.TRUE.equals(cir.getReturnValue()));
	}
}
