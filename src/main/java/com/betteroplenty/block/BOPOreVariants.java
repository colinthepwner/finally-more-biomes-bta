package com.betteroplenty.block;

import com.betteroplenty.BOPIdManifest;
import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.item.BOPItems;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.Item;
import net.minecraft.core.sound.BlockSounds;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class BOPOreVariants {
	private BOPOreVariants() {}

	private static final int FIRST_ID = 2010;

	private static final Map<String, Supplier<Block<?>>> HOSTS = new LinkedHashMap<>();

	static {
		HOSTS.put("basalt", () -> Blocks.BASALT);
		HOSTS.put("granite", () -> Blocks.GRANITE);
		HOSTS.put("limestone", () -> Blocks.LIMESTONE);
		HOSTS.put("permafrost", () -> Blocks.PERMAFROST);
	}

	private static final Map<String, String[]> GEN_HOSTS = new LinkedHashMap<>();

	static {

		GEN_HOSTS.put("ruby", new String[] {"basalt", "granite"});
		GEN_HOSTS.put("sapphire", new String[] {"basalt", "granite"});

		GEN_HOSTS.put("peridot", new String[] {"basalt"});

		GEN_HOSTS.put("topaz", new String[] {"granite"});

		GEN_HOSTS.put("malachite", new String[] {"limestone"});

		GEN_HOSTS.put("tanzanite", new String[] {"limestone"});
	}

	private static final Map<Integer, Map<Integer, Block<?>>> BY_STONE_ORE = new HashMap<>();

	private static final Map<String, Block<?>> BY_KEY = new LinkedHashMap<>();

	private static final Map<Integer, int[]> GEN_BY_STONE_ORE = new HashMap<>();

	private static final Set<String> SHALLOW_ROCKS = new HashSet<>(Arrays.asList("granite", "limestone"));

	private static final Set<Integer> NEEDS_SHALLOW = new HashSet<>();

	public static final int SHALLOW_BAND_MIN = 48;
	public static final int SHALLOW_BAND_RANGE = 64;

	private static final int SHALLOW_SHARE = 3;

	public static int shallowShare() {
		return SHALLOW_SHARE;
	}

	public static boolean needsShallowBand(int stoneOreId) {
		return NEEDS_SHALLOW.contains(stoneOreId);
	}

	public static void register() {
		BlockBuilder builder = new BlockBuilder(BetterOPlenty.MOD_ID);
		int id = FIRST_ID;
		int count = 0;
		List<Integer> variantIds = new ArrayList<>();

		Object[][] gems = {
			{"amethyst", BOPBlocks.AMETHYST_ORE, (Supplier<Item>) () -> BOPItems.AMETHYST},
			{"ruby", BOPBlocks.RUBY_ORE, (Supplier<Item>) () -> BOPItems.RUBY},
			{"peridot", BOPBlocks.PERIDOT_ORE, (Supplier<Item>) () -> BOPItems.PERIDOT},
			{"topaz", BOPBlocks.TOPAZ_ORE, (Supplier<Item>) () -> BOPItems.TOPAZ},
			{"tanzanite", BOPBlocks.TANZANITE_ORE, (Supplier<Item>) () -> BOPItems.TANZANITE},
			{"malachite", BOPBlocks.MALACHITE_ORE, (Supplier<Item>) () -> BOPItems.MALACHITE},
			{"sapphire", BOPBlocks.SAPPHIRE_ORE, (Supplier<Item>) () -> BOPItems.SAPPHIRE},
		};

		for (Object[] gem : gems) {
			String key = (String) gem[0];
			@SuppressWarnings("unchecked")
			Block<?> stoneHosted = (Block<?>) gem[1];
			@SuppressWarnings("unchecked")
			Supplier<Item> drop = (Supplier<Item>) gem[2];

			List<Integer> genHosts = new ArrayList<>();
			genHosts.add(Blocks.STONE.id());
			Map<Integer, Block<?>> byHost = new HashMap<>();

			byHost.put(Blocks.STONE.id(), stoneHosted);

			String[] hostRocks = GEN_HOSTS.getOrDefault(key, new String[0]);
			Set<String> generates = new HashSet<>(Arrays.asList(hostRocks));

			for (String rock : generates) {
				if (SHALLOW_ROCKS.contains(rock)) {
					NEEDS_SHALLOW.add(stoneHosted.id());
				}
			}

			for (Map.Entry<String, Supplier<Block<?>>> host : HOSTS.entrySet()) {

				Block<BlockLogic> variant = builder.clone()
					.setHardness(3.0f)
					.setResistance(5.0f)
					.setBlockSound(BlockSounds.STONE)
					.setTags(BlockTags.MINEABLE_BY_PICKAXE)

					.setCreativeInventoryPlacement(
						new CreativeInventoryPlacement.After(() -> Blocks.ORE_DIAMOND_STONE))
					.build(key + "_ore_" + host.getKey(), id++,
						block -> new BlockLogicOreGem(block, Materials.STONE, drop));
				BOPBlocks.pickaxeLevel(variant, 2);
				variantIds.add(variant.id());
				byHost.put(host.getValue().get().id(), variant);
				BY_KEY.put(key + "_" + host.getKey(), variant);
				if (generates.contains(host.getKey())) {
					genHosts.add(host.getValue().get().id());
				}
				count++;
			}
			BY_STONE_ORE.put(stoneHosted.id(), byHost);
			GEN_BY_STONE_ORE.put(stoneHosted.id(), genHosts.stream().mapToInt(Integer::intValue).toArray());
		}

		BetterOPlenty.LOGGER.info(
			"Registered {} gem-ore variants across {} host rocks, ids {}; BOP gems now generate "
				+ "in basalt, granite, limestone and permafrost as well as stone.",
			count, HOSTS.size(),
			BOPIdManifest.span(variantIds.stream().mapToInt(Integer::intValue).toArray()));
	}

	public static Block<?> variant(String gemKey, String hostKey) {
		return BY_KEY.get(gemKey + "_" + hostKey);
	}

	public static int[] hostsFor(int stoneOreId) {
		int[] hosts = GEN_BY_STONE_ORE.get(stoneOreId);
		return hosts == null ? null : hosts.clone();
	}

	public static int variantFor(int stoneOreId, int hostId) {
		Map<Integer, Block<?>> byHost = BY_STONE_ORE.get(stoneOreId);
		if (byHost == null) {
			return stoneOreId;
		}
		Block<?> variant = byHost.get(hostId);
		return variant == null ? stoneOreId : variant.id();
	}
}
