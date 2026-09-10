package com.betteroplenty.mixin;

import net.minecraft.core.world.biome.data.BiomeRangeMap;
import net.minecraft.core.world.biome.provider.BiomeProviderOverworld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = BiomeProviderOverworld.class, remap = false)
public interface BiomeProviderOverworldAccessor {
	@Accessor("brm")
	static BiomeRangeMap betteroplenty$brm() {
		throw new AssertionError("Mixin accessor was not applied");
	}
}
