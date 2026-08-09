package com.betteroplenty.world;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.world.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class BOPBiomeIdRemap {
	private BOPBiomeIdRemap() {}

	private static final Logger LOGGER = LoggerFactory.getLogger("betteroplenty");

	public static final int UNGENERATED = 0xFF;

	private static final Set<String> REPORTED = ConcurrentHashMap.newKeySet();
	private static final AtomicLong RESCUED = new AtomicLong();
	private static final int MAX_REPORTS = 32;

	@NotNull
	public static byte[] remapSection(byte @Nullable [] stored,
	                                  @Nullable Int2ObjectMap<String> savedKeys) {
		if (stored == null) {
			byte[] empty = new byte[512];
			Arrays.fill(empty, (byte) UNGENERATED);
			return empty;
		}

		byte[] out = new byte[stored.length];
		int rescued = 0;
		int firstLost = -1;
		for (int i = 0; i < stored.length; i++) {
			int storedId = stored[i] & 0xFF;
			int id = remapId(storedId, savedKeys);
			out[i] = (byte) id;
			if (id == UNGENERATED) {
				if (storedId != UNGENERATED && firstLost < 0) {
					firstLost = storedId;
				}
			} else if (storedId >= 128 && id != storedId) {

				rescued++;
			}
		}

		if (rescued > 0 && RESCUED.getAndAdd(rescued) == 0) {
			LOGGER.info(
				"Remapping saved biome columns whose id is past 127 -- BTA's chunk readers skip "
					+ "those, so after the roster changed they would have loaded as a different "
					+ "biome. {} column(s) in the first affected section.", rescued);
		}
		if (firstLost >= 0 && savedKeys != null) {
			report(firstLost, savedKeys);
		}
		return out;
	}

	public static int remapId(int storedId, @Nullable Int2ObjectMap<String> savedKeys) {
		if (storedId == UNGENERATED || savedKeys == null) {

			return UNGENERATED;
		}
		String key = savedKeys.get(storedId);
		if (key == null) {
			return UNGENERATED;
		}
		Biome biome = Registries.BIOMES.getItem(key);
		if (biome == null) {
			return UNGENERATED;
		}
		int id = Registries.BIOMES.getNumericIdOfItem(biome);
		if (id < 0 || id > BOPBiomes.BIOME_ID_CEILING) {

			return UNGENERATED;
		}
		return id;
	}

	public static long rescuedColumns() {
		return RESCUED.get();
	}

	private static void report(int storedId, @NotNull Int2ObjectMap<String> savedKeys) {
		String key = savedKeys.get(storedId);
		String cause = key == null
			? "id " + storedId + " has no entry in the chunk's saved biome map"
			: "'" + key + "' is not registered in this session";
		if (REPORTED.size() < MAX_REPORTS && REPORTED.add(cause)) {
			LOGGER.warn("Saved chunk names a biome that cannot be resolved: {}. Those columns load "
				+ "as ungenerated. Reported once per distinct cause.", cause);
		}
	}
}
