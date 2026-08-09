package com.betteroplenty.mixin.client;

import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.FogManager;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FogManager.class, remap = false)
public abstract class FogClosenessMixin {

	@Shadow
	@Final
	public Minecraft mc;

	@Unique
	private float betteroplenty$closeness = 1.0F;

	@Inject(method = "setupFog", at = @At("RETURN"))
	private void betteroplenty$applyBiomeCloseness(
		int fogMode, float farPlaneDistance, float partialTick,
		@NotNull FogManager.FogState dest,
		CallbackInfoReturnable<FogManager.FogState> cir
	) {

		if (dest.fogMode != FogManager.FogState.Mode.LINEAR) {
			return;
		}
		if (this.mc == null || this.mc.currentWorld == null || this.mc.activeCamera == null) {
			return;
		}

		float target = 1.0F;
		Biome biome = this.mc.currentWorld.getBlockBiome(this.mc.activeCamera.getTilePos());
		if (biome instanceof BiomeGenBase bop) {
			target = bop.getFogCloseness();
		}

		target = MathHelper.clamp(target, 0.01F, 1.0F);

		final float rate = 5.0E-4F;
		float delta = MathHelper.clamp(target - this.betteroplenty$closeness, -rate, rate);
		this.betteroplenty$closeness = MathHelper.clamp(
			this.betteroplenty$closeness + delta, 0.01F, 1.0F);

		if (this.betteroplenty$closeness >= 1.0F) {
			return;
		}

		dest.fogStart *= this.betteroplenty$closeness;
		dest.fogEnd *= this.betteroplenty$closeness;
	}
}
