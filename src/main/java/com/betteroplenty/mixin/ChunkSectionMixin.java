package com.betteroplenty.mixin;

import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.ChunkSection;
import net.minecraft.core.world.pos.ChunkSectionTilePosc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.jetbrains.annotations.Nullable;

@Mixin(value = ChunkSection.class, remap = false)
public abstract class ChunkSectionMixin {

	@Shadow
	public byte[] biome;

	@Overwrite
	@Nullable
	public Biome getBiome(ChunkSectionTilePosc sectionPos) {
		if (!sectionPos.inBounds()) {
			return null;
		}
		int biomeId = this.biome[ChunkSection.makeBiomeIndex(sectionPos)] & 0xFF;
		return biomeId == 0xFF ? null : Registries.BIOMES.getItemByNumericId(biomeId);
	}
}
