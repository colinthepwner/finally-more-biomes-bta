package com.betteroplenty.mixin;

import net.minecraft.core.world.biome.data.BiomeRangeLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = BiomeRangeLookup.class, remap = false)
public abstract class BiomeRangeLookupMixin {

	@ModifyArg(
		method = "getBiome(DDDD)Lnet/minecraft/core/world/biome/Biome;",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/core/data/registry/Registry;getItemByNumericId(I)Ljava/lang/Object;"),
		index = 0)
	private int betteroplenty$readBiomeIdUnsigned(int biomeId) {
		return biomeId & 0xFF;
	}
}
