package com.betteroplenty.mixin.client;

import com.betteroplenty.client.BiomeBlendBOP;
import com.betteroplenty.world.WorldTypeBOP;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.client.world.WorldClient;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import com.betteroplenty.world.promised.WorldTypePromisedLand;
import net.minecraft.core.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = WorldClient.class, remap = false)
public abstract class SkyColorMixin {

	@Redirect(
		method = "getSkyColor",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/biome/Biome;getSkyColor(F)I"))
	private int betteroplenty$blendSkyColor(Biome biome, float temperature,
	                                        ICamera camera, float partialTick) {
		World world = (World) (Object) this;
		boolean isBOP = WorldTypeBOP.BOP != null && world.getWorldType() == WorldTypeBOP.BOP;
		boolean isPromised = WorldTypePromisedLand.PROMISED_LAND != null && world.getWorldType() == WorldTypePromisedLand.PROMISED_LAND;
		if (!isBOP && !isPromised) {
			return biome.getSkyColor(temperature);
		}
		return BiomeBlendBOP.blendSky(world, camera.getTilePos(partialTick), temperature);
	}
}
