package com.betteroplenty.mixin;

import com.betteroplenty.world.BOPBiomeIdRemap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.world.chunk.ChunkSection;
import net.minecraft.core.world.chunk.reader.ChunkReaderVersion1;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ChunkReaderVersion1.class, remap = false)
public abstract class ChunkReaderVersion1Mixin {

	@Shadow @Final private byte[] biome;

	@Overwrite
	public byte[] getBiomeMap(int ySection, @NotNull Int2ObjectMap<String> biomeRegistry) {
		if (this.biome == null
			|| ChunkReaderVersion1.makeBiomeIndex(15, ySection * 16 + 15, 15) >= this.biome.length) {

			return new byte[512];
		}

		byte[] stored = new byte[512];
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 16; y++) {
					stored[ChunkSection.makeBiomeIndex(x, y, z)] =
						this.biome[ChunkReaderVersion1.makeBiomeIndex(x, ySection * 16 + y, z)];
				}
			}
		}
		return BOPBiomeIdRemap.remapSection(stored, biomeRegistry);
	}
}
