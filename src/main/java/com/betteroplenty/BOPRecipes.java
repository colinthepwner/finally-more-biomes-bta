package com.betteroplenty;

import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPBones;
import com.betteroplenty.block.BOPFlowers;
import com.betteroplenty.block.BOPHive;
import com.betteroplenty.block.BOPJungle;
import com.betteroplenty.block.BOPNether;
import com.betteroplenty.item.BOPFoods;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.block.BOPWastes;
import com.betteroplenty.block.BOPWoodSet;
import com.betteroplenty.item.BOPItems;
import com.betteroplenty.item.BOPFlowerBands;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.DyeColor;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.RecipeBuilder;

import java.util.ArrayList;
import java.util.List;

public final class BOPRecipes {
	private BOPRecipes() {}

	public static void register() {
		RecipeBuilder.initNameSpace(BetterOPlenty.MOD_ID);

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("CCC", "C C", "CCC")
			.addInput('C', BOPFlowers.CLOVER)
			.create("dull_flower_band", track(new ItemStack(BOPFlowerBands.DULL, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("CDC", "D D", "CDC")
			.addInput('C', BOPFlowers.CLOVER)
			.addInput('D', BOPFlowers.COSMOS)
			.create("plain_flower_band", track(new ItemStack(BOPFlowerBands.PLAIN, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("CDC", "V V", "CDC")
			.addInput('C', BOPFlowers.CLOVER)
			.addInput('D', BOPFlowers.COSMOS)
			.addInput('V', BOPFlowers.VIOLET)
			.create("lush_flower_band", track(new ItemStack(BOPFlowerBands.LUSH, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("CDT", "V V", "TDC")
			.addInput('C', BOPFlowers.CLOVER)
			.addInput('D', BOPFlowers.COSMOS)
			.addInput('V', BOPFlowers.VIOLET)
			.addInput('T', BOPFlowers.DAFFODIL)
			.create("exotic_flower_band", track(new ItemStack(BOPFlowerBands.EXOTIC, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("AAA", "AAA", "AAA")
			.addInput('A', BOPItems.AMETHYST)
			.create("amethyst_block", track(new ItemStack(BOPBlocks.AMETHYST_BLOCK, 1)));

		RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
			.addInput(BOPBlocks.AMETHYST_BLOCK)
			.create("amethyst_from_block", track(new ItemStack(BOPItems.AMETHYST, 9)));

		RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
			.addInput(BOPBlocks.LAVENDER)
			.create("purple_dye_from_lavender",
				track(new ItemStack(Items.DYE, 2, DyeColor.PURPLE.itemMeta)));

		registerPlantDyes();
		registerFoods();
		registerGemStorage();
		registerGemStar();

		registerAncientStaff();
		registerAmethystGear();
		registerMudGear();
		registerWoodSets();
		registerBamboo();
		registerRedRock();
		registerWastes();
		registerNether();
		registerProjectiles();
		registerScythes();
		registerBones();
		registerMiscItems();

		invalidateRecipeCache();

		BetterOPlenty.LOGGER.info("Registered {} BOP recipes.", OUTPUTS.size());

		BOPAudit.auditRecipes();
		BOPAudit.auditLogSmelting();
	}

	private static void registerFoods() {

		RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
			.addInput(BOPFlowers.SUNFLOWER)
			.create("sunflower_seeds_from_sunflower",
				track(new ItemStack(BOPFoods.SUNFLOWER_SEEDS, 4)));

		RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
			.addInput(BOPFlowers.TOADSTOOL)
			.create("shroom_powder_from_toadstool",
				track(new ItemStack(BOPFoods.SHROOM_POWDER, 2)));

		RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
			.addInput(Items.BOWL)
			.addInput(BOPFlowers.TOADSTOOL)
			.addInput(BOPFlowers.PORTOBELLO)
			.addInput(BOPFlowers.BLUE_MILK_CAP)
			.create("salad_shroom", track(new ItemStack(BOPFoods.SALAD_SHROOM, 1)));

		RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
			.addInput(Items.BOWL)
			.addInput(BOPFoods.BERRIES)
			.addInput(Items.FOOD_APPLE)
			.addInput(Items.FOOD_CHERRY)
			.create("salad_fruit", track(new ItemStack(BOPFoods.SALAD_FRUIT, 1)));

		RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
			.addInput(Items.BOWL)
			.addInput(BOPFoods.WILD_CARROTS)
			.addInput(BOPFoods.TURNIP)
			.create("salad_veggie", track(new ItemStack(BOPFoods.SALAD_VEGGIE, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("HH", "HH")
			.addInput('H', BOPItems.EMPTY_HONEYCOMB)
			.create("hive_honeycomb", track(new ItemStack(BOPHive.HONEYCOMB, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("HH", "HH")
			.addInput('H', BOPFoods.FILLED_HONEYCOMB)
			.create("hive_honeycomb_filled", track(new ItemStack(BOPHive.HONEYCOMB_FILLED, 1)));

		BetterOPlenty.LOGGER.info(
			"Registered 7 BOP food recipes (5 verbatim, 2 with a forced substitute); ambrosia "
				+ "stays uncraftable -- 2 of its 9 ingredients do not exist (the filled jar, and a "
				+ "potion, which is a settled cut).");
	}

	private static void registerPlantDyes() {

		dye("cyan_dye_from_swamp_flower", BOPFlowers.SWAMP_FLOWER, DyeColor.CYAN);
		dye("magenta_dye_from_wildflower", BOPFlowers.WILDFLOWER, DyeColor.MAGENTA);
		dye("orange_dye_from_cosmos", BOPFlowers.COSMOS, DyeColor.ORANGE);
		dye("pink_dye_from_daffodil", BOPFlowers.DAFFODIL, DyeColor.PINK);

		dye("silver_dye_from_dandelion", BOPFlowers.DANDELION, DyeColor.SILVER);
		dye("light_blue_dye_from_hydrangea", BOPFlowers.HYDRANGEA, DyeColor.LIGHT_BLUE);
		dye("purple_dye_from_violet", BOPFlowers.VIOLET, DyeColor.PURPLE);
		dye("white_dye_from_anemone", BOPFlowers.ANEMONE, DyeColor.WHITE);
		dye("black_dye_from_deathbloom", BOPFlowers.DEATHBLOOM, DyeColor.BLACK);

		dye("pink_dye_from_hibiscus", BOPFlowers.HIBISCUS, DyeColor.PINK);
		dye("white_dye_from_lily_of_the_valley", BOPFlowers.LILY_OF_THE_VALLEY, DyeColor.WHITE);
		dye("orange_dye_from_burning_blossom", BOPFlowers.BURNING_BLOSSOM, DyeColor.ORANGE);
		dye("yellow_dye_from_goldenrod", BOPFlowers.GOLDENROD, DyeColor.YELLOW);
		dye("blue_dye_from_bluebells", BOPFlowers.BLUEBELLS, DyeColor.BLUE);
		dye("light_blue_dye_from_icy_iris", BOPFlowers.ICY_IRIS, DyeColor.LIGHT_BLUE);

		dye("brown_dye_from_cattail", BOPPlants.CATTAIL, DyeColor.BROWN);
		dye("green_dye_from_moss", BOPPlants.MOSS, DyeColor.GREEN);

		dye("lime_dye_from_glowshroom", BOPFlowers.GLOWSHROOM, DyeColor.LIME);
		dye("brown_dye_from_flat_mushroom", BOPFlowers.FLAT_MUSHROOM, DyeColor.BROWN);
		dye("blue_dye_from_blue_milk_cap", BOPFlowers.BLUE_MILK_CAP, DyeColor.BLUE);

		BetterOPlenty.LOGGER.info("Registered 20 BOP plant-to-dye recipes (21 with lavender's).");
	}

	private static void dye(@NotNull String name, @NotNull Block<?> plant, @NotNull DyeColor color) {
		RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
			.addInput(plant)
			.create(name, track(new ItemStack(Items.DYE, 2, color.itemMeta)));
	}

	private static final List<ItemStack> OUTPUTS = new ArrayList<>();

	@NotNull
	public static List<ItemStack> registeredOutputs() {
		return OUTPUTS;
	}

	private static ItemStack track(@NotNull ItemStack output) {
		OUTPUTS.add(output);
		return output;
	}

	private static void invalidateRecipeCache() {
		if (Registries.RECIPES == null) {
			BetterOPlenty.LOGGER.error("Recipe registry does not exist yet -- BOP recipes will not "
				+ "be craftable. Is register() being called before AFTER_GAME_START?");
			return;
		}
		Registries.RECIPES.invalidateCaches();
	}

	private static void registerGemStorage() {
		Object[][] gems = {
			{"ruby", BOPItems.RUBY, BOPBlocks.RUBY_BLOCK},
			{"peridot", BOPItems.PERIDOT, BOPBlocks.PERIDOT_BLOCK},
			{"topaz", BOPItems.TOPAZ, BOPBlocks.TOPAZ_BLOCK},
			{"tanzanite", BOPItems.TANZANITE, BOPBlocks.TANZANITE_BLOCK},
			{"malachite", BOPItems.MALACHITE, BOPBlocks.MALACHITE_BLOCK},
			{"sapphire", BOPItems.SAPPHIRE, BOPBlocks.SAPPHIRE_BLOCK},
		};

		for (Object[] gem : gems) {
			String key = (String) gem[0];
			IItemConvertible item = (IItemConvertible) gem[1];
			IItemConvertible block = (IItemConvertible) gem[2];

			RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
				.setShape("AAA", "AAA", "AAA")
				.addInput('A', item)
				.create(key + "_block", track(new ItemStack(block, 1)));

			RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
				.addInput(block)
				.create(key + "_from_block", track(new ItemStack(item, 9)));
		}

		BetterOPlenty.LOGGER.info("Registered {} BOP gem storage recipes ({} gems, both directions).",
			gems.length * 2, gems.length);
	}

	private static void registerGemStar() {

		final IItemConvertible[] gems = {
			BOPItems.RUBY, BOPItems.PERIDOT, BOPItems.TOPAZ,
			BOPItems.TANZANITE, BOPItems.MALACHITE, BOPItems.SAPPHIRE,
		};
		final String[] names = {"ruby", "peridot", "topaz", "tanzanite", "malachite", "sapphire"};

		RecipeBuilder.addItemsToGroup(BetterOPlenty.MOD_ID, "gems", (Object[]) gems);

		track(new ItemStack(BOPItems.GEM_STAR, 1));

		for (int omit = 0; omit < gems.length; omit++) {
			var recipe = RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID);
			for (int i = 0; i < gems.length; i++) {
				if (i != omit) {
					recipe.addInput(gems[i]);
				}
			}

			recipe.create("gem_star_without_" + names[omit], new ItemStack(BOPItems.GEM_STAR, 1));
		}

		BetterOPlenty.LOGGER.info(
			"Registered the Gem Star ({} shapeless recipes, any {} of {} distinct gems) and the "
				+ "betteroplenty:gems group ({} members).",
			gems.length, gems.length - 1, gems.length, gems.length);
	}

	private static void registerAncientStaff() {

		final IItemConvertible endStone = Blocks.BLOCK_QUARTZ;

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("ESE", "ETE", " E ")
			.addInput('E', endStone)
			.addInput('S', BOPItems.SAPPHIRE)
			.addInput('T', BOPItems.TANZANITE)
			.create("ancient_staff_handle", track(new ItemStack(BOPItems.ANCIENT_STAFF_HANDLE, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("EPE", "EEE", "EAE")
			.addInput('E', endStone)
			.addInput('P', BOPItems.PERIDOT)
			.addInput('A', BOPItems.MALACHITE)
			.create("ancient_staff_pole", track(new ItemStack(BOPItems.ANCIENT_STAFF_POLE, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape(" N ", "ERE", "ETE")
			.addInput('N', BOPItems.GEM_STAR)
			.addInput('E', endStone)
			.addInput('R', BOPItems.RUBY)
			.addInput('T', BOPItems.TOPAZ)
			.create("ancient_staff_topper", track(new ItemStack(BOPItems.ANCIENT_STAFF_TOPPER, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("T", "P", "H")
			.addInput('T', BOPItems.ANCIENT_STAFF_TOPPER)
			.addInput('P', BOPItems.ANCIENT_STAFF_POLE)
			.addInput('H', BOPItems.ANCIENT_STAFF_HANDLE)
			.create("ancient_staff", track(new ItemStack(BOPItems.ANCIENT_STAFF, 1)));

		RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
			.addInput(BOPItems.ANCIENT_STAFF_DEPLETED)
			.addInput(BOPItems.GEM_STAR)
			.create("ancient_staff_recharge", new ItemStack(BOPItems.ANCIENT_STAFF, 1));

		BetterOPlenty.LOGGER.info(
			"Registered the Ancient Staff (3 parts, the assembly and the Gem Star recharge); "
				+ "End Stone reads as quartz.");
	}

	private static void registerAmethystGear() {
		final char gem = 'A';
		final char rod = 'X';

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("AAA", " X ", " X ")
			.addInput(gem, BOPItems.AMETHYST).addInput(rod, Items.INGOT_IRON)
			.create("amethyst_pickaxe", track(new ItemStack(BOPItems.AMETHYST_PICKAXE, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("A", "X", "X")
			.addInput(gem, BOPItems.AMETHYST).addInput(rod, Items.INGOT_IRON)
			.create("amethyst_shovel", track(new ItemStack(BOPItems.AMETHYST_SHOVEL, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("AA", "AX", " X")
			.addInput(gem, BOPItems.AMETHYST).addInput(rod, Items.INGOT_IRON)
			.create("amethyst_axe", track(new ItemStack(BOPItems.AMETHYST_AXE, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("A", "A", "X")
			.addInput(gem, BOPItems.AMETHYST).addInput(rod, Items.INGOT_IRON)
			.create("amethyst_sword", track(new ItemStack(BOPItems.AMETHYST_SWORD, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("AA", " X", " X")
			.addInput(gem, BOPItems.AMETHYST).addInput(rod, Items.INGOT_IRON)
			.create("amethyst_hoe", track(new ItemStack(BOPItems.AMETHYST_HOE, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("AAA", "A A")
			.addInput(gem, BOPItems.AMETHYST)
			.create("amethyst_helmet", track(new ItemStack(BOPItems.AMETHYST_HELMET, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("A A", "AAA", "AAA")
			.addInput(gem, BOPItems.AMETHYST)
			.create("amethyst_chestplate", track(new ItemStack(BOPItems.AMETHYST_CHESTPLATE, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("AAA", "A A", "A A")
			.addInput(gem, BOPItems.AMETHYST)
			.create("amethyst_leggings", track(new ItemStack(BOPItems.AMETHYST_LEGGINGS, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("A A", "A A")
			.addInput(gem, BOPItems.AMETHYST)
			.create("amethyst_boots", track(new ItemStack(BOPItems.AMETHYST_BOOTS, 1)));

		BetterOPlenty.LOGGER.info("Registered 9 amethyst gear recipes (5 tools, 4 armour).");
	}

	private static void registerMudGear() {
		final char mud = 'M';
		final char rod = 'X';

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("MMM", " X ", " X ")
			.addInput(mud, BOPItems.MUDBALL).addInput(rod, Items.STICK)
			.create("mud_pickaxe", track(new ItemStack(BOPItems.MUD_PICKAXE, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("M", "X", "X")
			.addInput(mud, BOPItems.MUDBALL).addInput(rod, Items.STICK)
			.create("mud_shovel", track(new ItemStack(BOPItems.MUD_SHOVEL, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("MM", "MX", " X")
			.addInput(mud, BOPItems.MUDBALL).addInput(rod, Items.STICK)
			.create("mud_axe", track(new ItemStack(BOPItems.MUD_AXE, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("M", "M", "X")
			.addInput(mud, BOPItems.MUDBALL).addInput(rod, Items.STICK)
			.create("mud_sword", track(new ItemStack(BOPItems.MUD_SWORD, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("MM", " X", " X")
			.addInput(mud, BOPItems.MUDBALL).addInput(rod, Items.STICK)
			.create("mud_hoe", track(new ItemStack(BOPItems.MUD_HOE, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("MMM", "M M")
			.addInput(mud, BOPItems.MUDBALL)
			.create("mud_helmet", track(new ItemStack(BOPItems.MUD_HELMET, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("M M", "MMM", "MMM")
			.addInput(mud, BOPItems.MUDBALL)
			.create("mud_chestplate", track(new ItemStack(BOPItems.MUD_CHESTPLATE, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("MMM", "M M", "M M")
			.addInput(mud, BOPItems.MUDBALL)
			.create("mud_leggings", track(new ItemStack(BOPItems.MUD_LEGGINGS, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("M M", "M M")
			.addInput(mud, BOPItems.MUDBALL)
			.create("mud_boots", track(new ItemStack(BOPItems.MUD_BOOTS, 1)));

		BetterOPlenty.LOGGER.info("Registered 9 mud gear recipes (5 tools, 4 armour).");
	}

	private static void registerWoodSets() {
		int recipes = 0;
		for (BOPWoodSet set : BOPWoodSet.registered()) {

			if (set.log == null) {
				continue;
			}
			RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
				.addInput(set.log)
				.create(set.key + "_planks", track(set.planks(4)));
			recipes++;
		}

		BetterOPlenty.LOGGER.info("Registered log-to-planks for {} of {} BOP wood set(s).",
			recipes, BOPWoodSet.registered().size());
	}

	private static void registerBamboo() {

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("AA", "AA")
			.addInput('A', BOPJungle.BAMBOO)
			.create("bamboo_thatching", track(new ItemStack(BOPJungle.THATCHING, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape(" A", "A ")
			.addInput('A', BOPJungle.THATCHING)
			.create("bamboo_from_thatching", track(new ItemStack(BOPJungle.BAMBOO, 8)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("A ", " A")
			.addInput('A', BOPJungle.THATCHING)
			.create("bamboo_from_thatching_mirrored", track(new ItemStack(BOPJungle.BAMBOO, 8)));

		BetterOPlenty.LOGGER.info("Registered {} BOP bamboo recipes (thatching, both directions).", 3);
	}

	private static void registerRedRock() {
		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("RR", "RR")
			.addInput('R', BOPBlocks.RED_ROCK)
			.create("red_brick", track(new ItemStack(BOPBlocks.RED_BRICK, 4)));

		RecipeBuilder.Furnace(BetterOPlenty.MOD_ID)
			.setInput(BOPBlocks.RED_COBBLE)
			.create("red_rock_from_cobble", new ItemStack(BOPBlocks.RED_ROCK, 1));

		BetterOPlenty.LOGGER.info("Registered {} BOP red rock recipes (bricks, and cobble back to "
			+ "stone).", 2);
	}

	private static void registerProjectiles() {

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("MM", "MM")
			.addInput('M', BOPItems.MUDBALL)
			.create("mud_from_mudballs", track(new ItemStack(Blocks.MUD, 1)));

		RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
			.addInput(Blocks.MUD)
			.create("mudballs_from_mud", track(new ItemStack(BOPItems.MUDBALL, 4)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("R R", "R R", "R R")
			.addInput('R', BOPPlants.RIVER_CANE)
			.create("dart_blower", track(new ItemStack(BOPItems.DART_BLOWER, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("T", "R", "F")
			.addInput('T', BOPPlants.THORN)
			.addInput('R', BOPPlants.RIVER_CANE)
			.addInput('F', Items.FEATHER_CHICKEN)
			.create("dart", track(new ItemStack(BOPItems.DART, 4)));

		BetterOPlenty.LOGGER.info("Registered {} BOP projectile recipes (mud both ways, the "
			+ "blowgun, its darts).", 4);
	}

	private static void registerScythes() {
		scythe("wood_scythe", BOPItems.WOOD_SCYTHE, "minecraft:planks", Items.STICK);
		scythe("stone_scythe", BOPItems.STONE_SCYTHE, Blocks.COBBLE_STONE, Items.STICK);
		scythe("iron_scythe", BOPItems.IRON_SCYTHE, Items.INGOT_IRON, Items.STICK);
		scythe("gold_scythe", BOPItems.GOLD_SCYTHE, Items.INGOT_GOLD, Items.STICK);
		scythe("diamond_scythe", BOPItems.DIAMOND_SCYTHE, Items.DIAMOND, Items.STICK);
		scythe("mud_scythe", BOPItems.MUD_SCYTHE, BOPItems.MUDBALL, Items.STICK);
		scythe("amethyst_scythe", BOPItems.AMETHYST_SCYTHE, BOPItems.AMETHYST, Items.INGOT_IRON);

		scythe("steel_scythe", BOPItems.STEEL_SCYTHE, Items.INGOT_STEEL, Items.STICK);

		BetterOPlenty.LOGGER.info("Registered {} BOP scythe recipes (8 tiers, both handednesses).", 16);
	}

	private static void scythe(@NotNull String name, @NotNull IItemConvertible out,
							   @NotNull Object head, @NotNull IItemConvertible handle) {
		addScytheShape(name, out, head, handle, " MM", "M S", "  S");
		addScytheShape(name + "_mirrored", out, head, handle, "MM ", "S M", "S  ");
	}

	private static void addScytheShape(@NotNull String name, @NotNull IItemConvertible out,
									   @NotNull Object head, @NotNull IItemConvertible handle,
									   @NotNull String top, @NotNull String middle,
									   @NotNull String bottom) {
		var builder = RecipeBuilder.Shaped(BetterOPlenty.MOD_ID).setShape(top, middle, bottom);
		if (head instanceof String) {
			builder.addInput('M', (String) head);
		} else {
			builder.addInput('M', (IItemConvertible) head);
		}
		builder.addInput('S', handle).create(name, track(new ItemStack(out, 1)));
	}

	private static void registerWastes() {
		RecipeBuilder.Furnace(BetterOPlenty.MOD_ID)
			.setInput(Blocks.DIRT)
			.create("dried_dirt_from_dirt", new ItemStack(BOPWastes.DRIED_DIRT, 1));

		BetterOPlenty.LOGGER.info("Registered {} BOP waste recipe (dirt bakes into dried dirt).", 1);
	}

	private static void registerBones() {
		bonemeal("bonemeal_from_small_bone", BOPBones.SMALL, 3);
		bonemeal("bonemeal_from_medium_bone", BOPBones.MEDIUM, 6);
		bonemeal("bonemeal_from_large_bone", BOPBones.LARGE, 12);

		BetterOPlenty.LOGGER.info(
			"Registered {} BOP bone recipes (bone segments grind to 3, 6 and 12 bonemeal).", 3);
	}

	private static void bonemeal(@NotNull String name, @NotNull IItemConvertible bone, int amount) {
		RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
			.addInput(bone)
			.create(name, track(new ItemStack(Items.DYE, amount, DyeColor.WHITE.itemMeta)));
	}

	private static void registerMiscItems() {

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("AAA", "AAA", "AAA")
			.addInput('A', BOPItems.ASH)
			.create("coal_from_ash", track(new ItemStack(Items.COAL, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("AA", "AA")
			.addInput('A', BOPItems.ASH)
			.create("ash_block_from_ash", track(new ItemStack(BOPWastes.ASH, 1)));

		RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
			.addInput(BOPItems.ASH)
			.create("gray_dye_from_ash",
				track(new ItemStack(Items.DYE, 2, DyeColor.GRAY.itemMeta)));

		RecipeBuilder.Furnace(BetterOPlenty.MOD_ID)
			.setInput(BOPItems.MUDBALL)
			.create("mud_brick_from_mudball", new ItemStack(BOPItems.MUD_BRICK, 1));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("MM", "MM")
			.addInput('M', BOPItems.MUD_BRICK)
			.create("mud_baked_from_mud_bricks", track(new ItemStack(Blocks.MUD_BAKED, 1)));

		RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
			.addInput(BOPPlants.POISON_IVY)
			.addInput(Items.JAR)
			.create("jar_poison_from_poison_ivy", track(new ItemStack(BOPItems.JAR_POISON, 1)));

		BetterOPlenty.LOGGER.info("Registered {} BOP misc-item recipes (ash to coal, to a block and "
			+ "to gray dye; mudball smelts to a brick, four bricks bake to mud; poison ivy and a "
			+ "jar make the poison jar).", 6);
	}

	private static void registerNether() {
		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("SSS", "SNS", "SSS")
			.addInput('S', Items.SEEDS_WHEAT)
			.addInput('N', Blocks.NETHERRACK)
			.create("overgrown_netherrack",
				track(new ItemStack(BOPNether.OVERGROWN_NETHERRACK, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("FF", "FF")
			.addInput('F', BOPItems.FLESH_CHUNK)
			.create("flesh_from_chunks", track(new ItemStack(BOPNether.FLESH, 1)));

		BetterOPlenty.LOGGER.info(
			"Registered {} BOP Nether recipes (seeds sow netherrack into overgrown netherrack; "
				+ "four flesh chunks press back into flesh).", 2);
	}
}
