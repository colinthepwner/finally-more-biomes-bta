package com.betteroplenty.client;

import java.util.IdentityHashMap;
import java.util.Map;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.block.BOPCorals;
import com.betteroplenty.block.BOPFlowers;
import com.betteroplenty.block.BOPJungle;
import com.betteroplenty.block.BOPPlants;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BOPItemIcons {

	private static final Map<Block<?>, String> KEYS = new IdentityHashMap<>();

	private static final Map<Block<?>, IconCoordinate> ICONS = new IdentityHashMap<>();

	private BOPItemIcons() {
	}

	public static void resolve() {
		KEYS.clear();
		ICONS.clear();

		put(BOPPlants.REED, "item_reed");
		put(BOPPlants.HIGH_GRASS, "item_highgrass");
		put(BOPPlants.HIGH_GRASS_TOP, "item_highgrass");
		put(BOPPlants.BERRY_BUSH, "item_berrybush");
		put(BOPPlants.SHRUB, "item_shrub");
		put(BOPPlants.MOSS, "item_moss");
		put(BOPFlowers.RAINBOW_FLOWER, "item_rainbowflower");
		put(BOPFlowers.SUNFLOWER, "item_sunflower");
		put(BOPCorals.KELP_SINGLE, "item_kelp");
		put(BOPJungle.BAMBOO, "item_bamboo");

		for (Map.Entry<Block<?>, String> entry : KEYS.entrySet()) {
			ICONS.put(entry.getKey(), TextureRegistry.getTexture(entry.getValue()));
		}
		BetterOPlenty.LOGGER.info("Bound {} bespoke inventory sprite(s).", ICONS.size());
	}

	@Nullable
	public static IconCoordinate iconFor(@Nullable Block<?> block) {
		return block == null ? null : ICONS.get(block);
	}

	private static void put(@Nullable Block<?> block, @NotNull String texture) {
		if (block == null) {
			BetterOPlenty.LOGGER.warn("Inventory sprite '{}' has no block to bind to.", texture);
			return;
		}
		KEYS.put(block, "betteroplenty:block/" + texture);
	}
}
