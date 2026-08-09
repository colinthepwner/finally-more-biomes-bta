package com.betteroplenty.mixin;

import com.betteroplenty.world.BOPBiomeIdRemap;
import com.mojang.nbt.tags.CompoundTag;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.world.chunk.reader.ChunkReaderVersion2;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ChunkReaderVersion2.class, remap = false)
public abstract class ChunkReaderVersion2Mixin {

	@Shadow
	private CompoundTag findSection(int y) {
		throw new AssertionError("@Shadow");
	}

	@Overwrite
	public byte[] getBiomeMap(int ySection, @NotNull Int2ObjectMap<String> biomeRegistry) {
		CompoundTag sectionTag = this.findSection(ySection);
		byte[] stored = sectionTag == null ? null : sectionTag.getByteArrayOrDefault("BiomeMap", null);
		return BOPBiomeIdRemap.remapSection(stored, biomeRegistry);
	}
}
