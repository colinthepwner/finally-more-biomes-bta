package com.betteroplenty.block;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.item.BOPItems;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFlower;
import net.minecraft.core.block.BlockLogicTallGrass;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.sound.BlockSounds;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.util.function.Supplier;

public final class BOPBlocks {
	private BOPBlocks() {}

	public static Block<BlockLogic> SPIKE_CUBE;
	public static Block<BlockLogicFlower> SPIKE_PLANT;
	public static Block<BlockLogic> SPIKE_CUSTOM;

	public static Block<BlockLogicFlower> LAVENDER;

	public static Block<BlockLogicTallGrass> WHEAT_GRASS;
	public static Block<BlockLogic> AMETHYST_ORE;
	public static Block<BlockLogic> AMETHYST_BLOCK;

	public static Block<BlockLogic> RUBY_ORE;
	public static Block<BlockLogic> RUBY_BLOCK;
	public static Block<BlockLogic> PERIDOT_ORE;
	public static Block<BlockLogic> PERIDOT_BLOCK;
	public static Block<BlockLogic> TOPAZ_ORE;
	public static Block<BlockLogic> TOPAZ_BLOCK;
	public static Block<BlockLogic> TANZANITE_ORE;
	public static Block<BlockLogic> TANZANITE_BLOCK;
	public static Block<BlockLogic> MALACHITE_ORE;
	public static Block<BlockLogic> MALACHITE_BLOCK;
	public static Block<BlockLogic> SAPPHIRE_ORE;
	public static Block<BlockLogic> SAPPHIRE_BLOCK;

	public static Block<BlockLogic> HARD_ICE;
	public static Block<BlockLogic> CRAG_ROCK;

	public static Block<BlockLogicQuicksand> QUICKSAND;

	public static Block<BlockLogicRedRock> RED_ROCK;
	public static Block<BlockLogic> RED_COBBLE;
	public static Block<BlockLogic> RED_BRICK;

	public static Block<BlockLogic> HARD_SAND;
	public static Block<BlockLogic> HARD_DIRT;

	public static Block<BlockLogic> LONG_GRASS;

	public static Block<net.minecraft.core.block.BlockLogicLog> BIG_FLOWER_STEM;

	public static Block<BlockLogicBOPPetals> BIG_FLOWER_RED;

	public static Block<BlockLogicBOPPetals> BIG_FLOWER_YELLOW;

	private static CreativeInventoryPlacement after(java.util.function.Supplier<net.minecraft.core.item.IItemConvertible> neighbour) {
		return new CreativeInventoryPlacement.After(neighbour);
	}

	public static void register() {
		BlockBuilder builder = new BlockBuilder(BetterOPlenty.MOD_ID);

		BlockBuilder spike = builder.clone().setTags(BlockTags.NOT_IN_CREATIVE_MENU);

		SPIKE_CUBE = spike.clone()
			.setHardness(1.0f)
			.setBlockSound(BlockSounds.STONE)
			.build("spike_cube", 1400, block -> new BlockLogic(block, Materials.STONE));

		SPIKE_PLANT = spike.clone()
			.setHardness(0.0f)
			.setBlockSound(BlockSounds.GRASS)
			.build("spike_plant", 1401, BlockLogicFlower::new);

		SPIKE_CUSTOM = spike.clone()
			.setHardness(1.0f)
			.setBlockSound(BlockSounds.STONE)
			.build("spike_custom", 1402, block -> new BlockLogic(block, Materials.STONE));

		LAVENDER = builder.clone()
			.setHardness(0.0f)
			.setBlockSound(BlockSounds.GRASS)
			.setTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLANTABLE_IN_JAR,
				BlockTags.SHEARS_DO_SILK_TOUCH, BlockTags.SHEEPS_FAVOURITE_BLOCK)
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_PURPLE))
			.build("lavender", 1403, BlockLogicFlower::new);

		WHEAT_GRASS = builder.clone()
			.setHardness(0.0f)
			.setBlockSound(BlockSounds.GRASS)
			.setCreativeInventoryPlacement(after(() -> Blocks.TALLGRASS))
			.build("wheat_grass", 1404, BlockLogicTallGrass::new);

		AMETHYST_ORE = builder.clone()
			.setHardness(3.0f)
			.setResistance(5.0f)
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.setCreativeInventoryPlacement(after(() -> Blocks.ORE_DIAMOND_STONE))
			.build("amethyst_ore", 1405, block -> new BlockLogicOreAmethyst(block, Materials.STONE));

		AMETHYST_BLOCK = builder.clone()
			.setHardness(5.0f)
			.setResistance(10.0f)
			.setBlockSound(BlockSounds.METAL)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.setCreativeInventoryPlacement(after(() -> Blocks.BLOCK_DIAMOND))
			.build("amethyst_block", 1406, block -> new BlockLogic(block, Materials.METAL));

		pickaxeLevel(AMETHYST_ORE, 3);
		pickaxeLevel(AMETHYST_BLOCK, 3);

		registerGems(builder);
		registerTerrain(builder);

		BetterOPlenty.LOGGER.info("Registered {} spike blocks and {} BOP blocks.", 3, 18);

		BOPWoodSets.register();

		BOPFlowers.register();

		BOPPlants.register();

		BOPCorals.register();

		BOPJungle.register();

		registerGarden(builder);

		BOPOrchard.register();

		BOPTerracotta.register();

		BOPWastes.register();

		BOPFormations.register();

		BOPBones.register();
		BOPGraves.register();

		BOPNether.register();

		BOPHive.register();
	}

	private static void registerGarden(BlockBuilder builder) {

		LONG_GRASS = builder.clone()
			.setHardness(0.6f)
			.setBlockSound(BlockSounds.GRASS)
			.setTags(BlockTags.MINEABLE_BY_SHOVEL, BlockTags.GROWS_FLOWERS, BlockTags.GROWS_TREES,
				BlockTags.GROWS_SUGAR_CANE, BlockTags.GROWS_CACTI, BlockTags.PASSIVE_MOBS_SPAWN,
				BlockTags.FIREFLIES_CAN_SPAWN, BlockTags.CAVES_CUT_THROUGH,
				BlockTags.CAVE_GEN_REPLACES_SURFACE)
			.setCreativeInventoryPlacement(after(() -> Blocks.GRASS_RETRO))
			.build("long_grass", 1960,
				block -> new net.minecraft.core.block.BlockLogicGrass(block, Blocks.DIRT));

		BIG_FLOWER_STEM = builder.clone()
			.setHardness(2.0f)
			.setBlockSound(BlockSounds.WOOD)
			.setFlammability(15, 10)
			.setTags(BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_AXE)
			.setCreativeInventoryPlacement(after(() -> Blocks.LOG_PALM))
			.build("big_flower_stem", 1961, net.minecraft.core.block.BlockLogicLog::new);

		BlockBuilder petals = builder.clone()
			.setHardness(0.2f)
			.setBlockSound(BlockSounds.GRASS)
			.setLightOpacity(1)
			.setFlammability(30, 60)
			.setTags(BlockTags.SHEARS_DO_SILK_TOUCH, BlockTags.MINEABLE_BY_AXE,
				BlockTags.MINEABLE_BY_HOE, BlockTags.MINEABLE_BY_SWORD, BlockTags.MINEABLE_BY_SHEARS)
			.setCreativeInventoryPlacement(after(() -> Blocks.LEAVES_PALM));

		BIG_FLOWER_RED = petals.clone()
			.build("big_flower_red", 1962,
				block -> new BlockLogicBOPPetals(block, () -> Blocks.FLOWER_RED));
		BIG_FLOWER_YELLOW = petals.clone()
			.build("big_flower_yellow", 1963,
				block -> new BlockLogicBOPPetals(block, () -> Blocks.FLOWER_YELLOW));

		BetterOPlenty.LOGGER.info("Registered {} Garden blocks (long grass, the giant flower's stem "
			+ "and its two petal canopies).", 4);
	}

	private static void registerGems(BlockBuilder builder) {
		RUBY_ORE = gemOre(builder, "ruby_ore", 1900, () -> BOPItems.RUBY);
		PERIDOT_ORE = gemOre(builder, "peridot_ore", 1902, () -> BOPItems.PERIDOT);
		TOPAZ_ORE = gemOre(builder, "topaz_ore", 1904, () -> BOPItems.TOPAZ);
		TANZANITE_ORE = gemOre(builder, "tanzanite_ore", 1906, () -> BOPItems.TANZANITE);
		MALACHITE_ORE = gemOre(builder, "malachite_ore", 1908, () -> BOPItems.MALACHITE);
		SAPPHIRE_ORE = gemOre(builder, "sapphire_ore", 1910, () -> BOPItems.SAPPHIRE);

		RUBY_BLOCK = gemBlock(builder, "ruby_block", 1901);
		PERIDOT_BLOCK = gemBlock(builder, "peridot_block", 1903);
		TOPAZ_BLOCK = gemBlock(builder, "topaz_block", 1905);
		TANZANITE_BLOCK = gemBlock(builder, "tanzanite_block", 1907);
		MALACHITE_BLOCK = gemBlock(builder, "malachite_block", 1909);
		SAPPHIRE_BLOCK = gemBlock(builder, "sapphire_block", 1911);

		BetterOPlenty.LOGGER.info("Registered {} BOP gem blocks ({} ores, {} storage blocks), "
			+ "all iron-pick gated.", 12, 6, 6);
	}

	private static void registerTerrain(BlockBuilder builder) {
		HARD_ICE = builder.clone()
			.setHardness(0.75f)
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.setCreativeInventoryPlacement(after(() -> Blocks.PERMAICE))
			.build("hard_ice", 1920, block -> new BlockLogic(block, Materials.STONE));

		CRAG_ROCK = builder.clone()
			.setHardness(1.0f)
			.setBlockSound(BlockSounds.GRAVEL)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.setCreativeInventoryPlacement(after(() -> Blocks.BASALT))
			.build("crag_rock", 1921, block -> new BlockLogic(block, Materials.STONE));

		QUICKSAND = builder.clone()
			.setHardness(0.0f)
			.setBlockSound(BlockSounds.SAND)
			.setTags(BlockTags.MINEABLE_BY_SHOVEL)
			.setCreativeInventoryPlacement(after(() -> Blocks.MUD))
			.build("quicksand", 2040, block -> new BlockLogicQuicksand(block, Materials.SAND));

		BetterOPlenty.LOGGER.info("Registered {} BOP terrain blocks (the Glacier's and the Crag's "
			+ "surface stone, and quicksand).", 3);

		registerRedRock(builder);
		registerHardpan(builder);
	}

	private static void registerRedRock(BlockBuilder builder) {
		BlockBuilder redRock = builder.clone()
			.setBlockSound(BlockSounds.STONE)
			.setResistance(10.0f)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE);

		RED_ROCK = redRock.clone()
			.setHardness(1.0f)
			.setCreativeInventoryPlacement(after(() -> Blocks.STONE))
			.build("red_rock", 1970,
				block -> new BlockLogicRedRock(block, Materials.STONE, () -> RED_COBBLE));

		RED_COBBLE = redRock.clone()
			.setHardness(1.6f)
			.setCreativeInventoryPlacement(after(() -> Blocks.COBBLE_STONE))
			.build("red_cobble", 1971, block -> new BlockLogic(block, Materials.STONE));

		RED_BRICK = redRock.clone()
			.setHardness(1.1f)
			.setCreativeInventoryPlacement(after(() -> Blocks.BRICK_STONE))
			.build("red_brick", 1972, block -> new BlockLogic(block, Materials.STONE));

		BetterOPlenty.LOGGER.info("Registered the BOP red rock family ({} blocks: stone, cobble, "
			+ "brick).", 3);
	}

	private static void registerHardpan(BlockBuilder builder) {
		HARD_SAND = builder.clone()
			.setHardness(0.7f)
			.setBlockSound(BlockSounds.SAND)
			.setTags(BlockTags.MINEABLE_BY_SHOVEL)
			.setCreativeInventoryPlacement(after(() -> Blocks.SAND))
			.build("hard_sand", 1973, block -> new BlockLogic(block, Materials.SAND));

		HARD_DIRT = builder.clone()
			.setHardness(0.9f)
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.setCreativeInventoryPlacement(after(() -> Blocks.DIRT))
			.build("hard_dirt", 1974, block -> new BlockLogic(block, Materials.STONE));

		BetterOPlenty.LOGGER.info("Registered {} BOP hardpan blocks (the Outback's sand and the "
			+ "Canyon's dirt).", 2);
	}

	private static Block<BlockLogic> gemOre(BlockBuilder builder, String name, int id,
			Supplier<Item> gem) {
		Block<BlockLogic> ore = builder.clone()
			.setHardness(3.0f)
			.setResistance(5.0f)
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.setCreativeInventoryPlacement(after(() -> Blocks.ORE_DIAMOND_STONE))
			.build(name, id, block -> new BlockLogicOreGem(block, Materials.STONE, gem));

		pickaxeLevel(ore, 2);
		return ore;
	}

	private static Block<BlockLogic> gemBlock(BlockBuilder builder, String name, int id) {
		Block<BlockLogic> storage = builder.clone()
			.setHardness(5.0f)
			.setResistance(10.0f)
			.setBlockSound(BlockSounds.METAL)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.setCreativeInventoryPlacement(after(() -> Blocks.BLOCK_DIAMOND))
			.build(name, id, block -> new BlockLogic(block, Materials.METAL));
		pickaxeLevel(storage, 2);
		return storage;
	}

	static void pickaxeLevel(Block<?> block, int level) {
		ItemToolPickaxe.miningLevels.put(block, level);
	}
}
