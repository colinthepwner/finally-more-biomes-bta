package com.betteroplenty.item;

import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.item.ItemSeeds;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.item.Items;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tag.ItemTags;
import net.minecraft.core.item.tool.ItemToolAxe;
import net.minecraft.core.item.tool.ItemToolHoe;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.item.tool.ItemToolShovel;
import net.minecraft.core.item.tool.ItemToolSword;
import net.minecraft.core.enums.HumanArmorShape;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.util.function.Supplier;

public final class BOPItems {
	private BOPItems() {}

	public static Item AMETHYST;

	public static Item RUBY;
	public static Item PERIDOT;
	public static Item TOPAZ;
	public static Item TANZANITE;
	public static Item MALACHITE;
	public static Item SAPPHIRE;

	public static Item GEM_STAR;

	public static Item ANCIENT_STAFF;
	public static Item ANCIENT_STAFF_HANDLE;
	public static Item ANCIENT_STAFF_POLE;
	public static Item ANCIENT_STAFF_TOPPER;

	public static Item ANCIENT_STAFF_DEPLETED;

	public static Item AMETHYST_PICKAXE;
	public static Item AMETHYST_SHOVEL;
	public static Item AMETHYST_AXE;
	public static Item AMETHYST_SWORD;
	public static Item AMETHYST_HOE;
	public static Item AMETHYST_HELMET;
	public static Item AMETHYST_CHESTPLATE;
	public static Item AMETHYST_LEGGINGS;
	public static Item AMETHYST_BOOTS;

	public static Item MUDBALL;

	public static Item DART_BLOWER;

	public static Item DART;

	public static Item MUD_PICKAXE;
	public static Item MUD_SHOVEL;
	public static Item MUD_AXE;
	public static Item MUD_SWORD;
	public static Item MUD_HOE;
	public static Item MUD_HELMET;
	public static Item MUD_CHESTPLATE;
	public static Item MUD_LEGGINGS;
	public static Item MUD_BOOTS;

	public static Item WOOD_SCYTHE;
	public static Item STONE_SCYTHE;
	public static Item IRON_SCYTHE;
	public static Item GOLD_SCYTHE;
	public static Item DIAMOND_SCYTHE;
	public static Item MUD_SCYTHE;
	public static Item AMETHYST_SCYTHE;

	public static Item STEEL_SCYTHE;
	public static Item BOP_RECORD;
	public static Item BOP_RECORD_MUD;

	public static Item WADING_BOOTS;

	public static Item FLIPPERS;

	public static Item MUD_BRICK;

	public static Item ASH;

	public static Item EMPTY_HONEYCOMB;

	public static Item FLESH_CHUNK;

	public static Item CRYSTAL_SHARD;

	public static Item GHASTLY_SOUL;

	public static Item PIXIE_DUST;

	public static Item JAR_HONEY;
	public static Item JAR_POISON;
	public static Item JAR_PIXIE;

	public static Item TURNIP_SEEDS;

	private static CreativeInventoryPlacement after(Supplier<IItemConvertible> neighbour) {
		return new CreativeInventoryPlacement.After(neighbour);
	}

	public static void register() {
		ItemBuilder builder = new ItemBuilder(BetterOPlenty.MOD_ID);

		AMETHYST = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.DIAMOND))
			.build(new Item("amethyst", "betteroplenty:item/amethyst", 18000));

		RUBY = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.DIAMOND))
			.build(new Item("ruby", "betteroplenty:item/ruby", 18200));
		PERIDOT = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.DIAMOND))
			.build(new Item("peridot", "betteroplenty:item/peridot", 18201));
		TOPAZ = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.DIAMOND))
			.build(new Item("topaz", "betteroplenty:item/topaz", 18202));
		TANZANITE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.DIAMOND))
			.build(new Item("tanzanite", "betteroplenty:item/tanzanite", 18203));
		MALACHITE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.DIAMOND))
			.build(new Item("malachite", "betteroplenty:item/malachite", 18204));
		SAPPHIRE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.DIAMOND))
			.build(new Item("sapphire", "betteroplenty:item/sapphire", 18205));

		GEM_STAR = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.DIAMOND))
			.build(new Item("gem_star", "betteroplenty:item/gemstar", 18206));

		ANCIENT_STAFF_HANDLE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.DIAMOND))
			.build(new Item("ancient_staff_handle", "betteroplenty:item/staffhandle", 18207)
				.setMaxStackSize(1));
		ANCIENT_STAFF_POLE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.DIAMOND))
			.build(new Item("ancient_staff_pole", "betteroplenty:item/staffpole", 18208)
				.setMaxStackSize(1));
		ANCIENT_STAFF_TOPPER = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.DIAMOND))
			.build(new Item("ancient_staff_topper", "betteroplenty:item/stafftopper", 18209)
				.setMaxStackSize(1));
		ANCIENT_STAFF = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.DIAMOND))
			.build(new ItemAncientStaff(
				"ancient_staff", "betteroplenty:item/ancientstaff", 18210));
		ANCIENT_STAFF_DEPLETED = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.DIAMOND))
			.build(new Item(
				"ancient_staff_depleted", "betteroplenty:item/ancientstaffbroken", 18211)
				.setMaxStackSize(1));

		AMETHYST_PICKAXE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_PICKAXE_IRON))
			.build(new ItemToolPickaxe(
				"amethyst_pickaxe", "betteroplenty:item/amethystpickaxe", 18001, BOPMaterials.AMETHYST_TOOL));
		AMETHYST_SHOVEL = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_SHOVEL_IRON))
			.build(new ItemToolShovel(
				"amethyst_shovel", "betteroplenty:item/amethystshovel", 18002, BOPMaterials.AMETHYST_TOOL));
		AMETHYST_AXE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_AXE_IRON))
			.build(new ItemToolAxe(
				"amethyst_axe", "betteroplenty:item/amethystaxe", 18003, BOPMaterials.AMETHYST_TOOL));

		AMETHYST_SWORD = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_SWORD_IRON))
			.build(new ItemToolSword(
				"amethyst_sword", "betteroplenty:item/amethystsword", 18004, BOPMaterials.AMETHYST_TOOL)
				.withTags(ItemTags.PREVENT_CREATIVE_MINING));
		AMETHYST_HOE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_HOE_IRON))
			.build(new ItemToolHoe(
				"amethyst_hoe", "betteroplenty:item/amethysthoe", 18005, BOPMaterials.AMETHYST_TOOL));

		AMETHYST_HELMET = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.ARMOR_HELMET_IRON))
			.build(new ItemArmor<>(
				"amethyst_helmet", "betteroplenty:item/amethysthelmet", 18006,
				BOPMaterials.AMETHYST_ARMOR, HumanArmorShape.HEAD));
		AMETHYST_CHESTPLATE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.ARMOR_CHESTPLATE_IRON))
			.build(new ItemArmor<>(
				"amethyst_chestplate", "betteroplenty:item/amethystchestplate", 18007,
				BOPMaterials.AMETHYST_ARMOR, HumanArmorShape.CHEST));
		AMETHYST_LEGGINGS = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.ARMOR_LEGGINGS_IRON))
			.build(new ItemArmor<>(
				"amethyst_leggings", "betteroplenty:item/amethystleggings", 18008,
				BOPMaterials.AMETHYST_ARMOR, HumanArmorShape.LEGS));
		AMETHYST_BOOTS = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.ARMOR_BOOTS_IRON))
			.build(new ItemArmor<>(
				"amethyst_boots", "betteroplenty:item/amethystboots", 18009,
				BOPMaterials.AMETHYST_ARMOR, HumanArmorShape.BOOTS));

		MUDBALL = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.AMMO_SNOWBALL))
			.build(new ItemBOPMudball("mudball", "betteroplenty:item/mudball", 18300));
		DART_BLOWER = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_BOW))
			.build(new ItemDartBlower("dart_blower", "betteroplenty:item/dartblower", 18301));
		DART = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.AMMO_ARROW))
			.build(new ItemDart("dart", "betteroplenty:item/dart", 18302));

		registerRecords(builder);
		registerFlowerBands(builder);
		registerWaterGear(builder);
		registerMudTier(builder);
		registerScythes(builder);
		registerMiscItems(builder);
		registerJars(builder);
		registerTurnipSeeds(builder);

		BetterOPlenty.LOGGER.info(
			"Registered {} BOP items (7 gems, 1 gem star, 5 ancient staff, 10 tools, 8 armour, "
				+ "3 projectile, 8 scythe, 4 flower band, 2 record, 7 misc, 3 jar, 1 turnip seed, "
				+ "2 water gear).", 61);
	}

	private static void registerMiscItems(ItemBuilder builder) {
		MUD_BRICK = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.BRICK_CLAY))
			.build(new Item("mud_brick", "betteroplenty:item/mudbrick", 18900));

		ASH = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.NETHERCOAL))
			.build(new Item("ash", "betteroplenty:item/ash", 18901));
		EMPTY_HONEYCOMB = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.DUST_SUGAR))
			.build(new Item("empty_honeycomb", "betteroplenty:item/emptyhoneycomb", 18902));
		FLESH_CHUNK = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.SLIMEBALL))
			.build(new Item("flesh_chunk", "betteroplenty:item/fleshchunk", 18903));
		CRYSTAL_SHARD = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.QUARTZ))
			.build(new Item("crystal_shard", "betteroplenty:item/crystalshard", 18904));

		GHASTLY_SOUL = builder.clone()
			.setStackSize(1)
			.setCreativeInventoryPlacement(after(() -> Items.BONE))
			.build(new Item("ghastly_soul", "betteroplenty:item/ghastlysoul", 18905));
		PIXIE_DUST = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.DUST_GLOWSTONE))
			.build(new Item("pixie_dust", "betteroplenty:item/pixiedust", 18906));
	}

	private static void registerTurnipSeeds(ItemBuilder builder) {
		TURNIP_SEEDS = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.SEEDS_WHEAT))
			.build(new ItemSeeds("turnip_seeds", "betteroplenty:item/turnipseeds", 31003,
				com.betteroplenty.block.BOPCrops.TURNIP_CROP));
	}

	private static void registerJars(ItemBuilder builder) {
		JAR_HONEY = builder.clone()
			.setStackSize(1)
			.setCreativeInventoryPlacement(after(() -> Items.JAR))
			.build(new Item("jar_honey", "betteroplenty:item/jarhoney", 31000));
		JAR_POISON = builder.clone()
			.setStackSize(1)
			.setCreativeInventoryPlacement(after(() -> Items.JAR))
			.build(new Item("jar_poison", "betteroplenty:item/jarpoison", 31001));
		JAR_PIXIE = builder.clone()
			.setStackSize(1)
			.setCreativeInventoryPlacement(after(() -> Items.JAR))
			.build(new Item("jar_pixie", "betteroplenty:item/jarpixie", 31002));
	}

	private static void registerScythes(ItemBuilder builder) {
		WOOD_SCYTHE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_HOE_WOOD))
			.build(new ItemBOPScythe("wood_scythe", "betteroplenty:item/woodscythe", 18409,
				ToolMaterial.wood, ItemBOPScythe.Tier.PLAIN));
		STONE_SCYTHE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_HOE_STONE))
			.build(new ItemBOPScythe("stone_scythe", "betteroplenty:item/stonescythe", 18410,
				ToolMaterial.stone, ItemBOPScythe.Tier.PLAIN));
		IRON_SCYTHE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_HOE_IRON))
			.build(new ItemBOPScythe("iron_scythe", "betteroplenty:item/ironscythe", 18411,
				ToolMaterial.iron, ItemBOPScythe.Tier.IRON));
		GOLD_SCYTHE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_HOE_GOLD))
			.build(new ItemBOPScythe("gold_scythe", "betteroplenty:item/goldscythe", 18412,
				ToolMaterial.gold, ItemBOPScythe.Tier.IRON));
		DIAMOND_SCYTHE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_HOE_DIAMOND))
			.build(new ItemBOPScythe("diamond_scythe", "betteroplenty:item/diamondscythe", 18413,
				ToolMaterial.diamond, ItemBOPScythe.Tier.DIAMOND));
		MUD_SCYTHE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_HOE_WOOD))
			.build(new ItemBOPScythe("mud_scythe", "betteroplenty:item/mudscythe", 18414,
				BOPMaterials.MUD_TOOL, ItemBOPScythe.Tier.PLAIN));
		AMETHYST_SCYTHE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_HOE_IRON))
			.build(new ItemBOPScythe("amethyst_scythe", "betteroplenty:item/amethystscythe", 18415,
				BOPMaterials.AMETHYST_TOOL, ItemBOPScythe.Tier.AMETHYST));

		STEEL_SCYTHE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_HOE_STEEL))
			.build(new ItemBOPScythe("steel_scythe", "betteroplenty:item/steelscythe", 18416,
				ToolMaterial.steel, ItemBOPScythe.Tier.STEEL));
	}

	private static void registerRecords(ItemBuilder builder) {
		BOP_RECORD = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.RECORD_13))
			.build(new ItemDiscMusic("record_traversia", "betteroplenty:item/boprecord", 18510,
				"betteroplenty:record.traversia", "Biomes O' Plenty"));
		BOP_RECORD_MUD = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.RECORD_13))
			.build(new ItemDiscMusic("record_muddy", "betteroplenty:item/mudrecord", 18511,
				"betteroplenty:record.muddy", "Biomes O' Plenty"));
	}

	private static void registerFlowerBands(ItemBuilder builder) {
		BOPFlowerBands.DULL = band(builder, "dull", 18500, BOPFlowerBands.DULL_MATERIAL);
		BOPFlowerBands.PLAIN = band(builder, "plain", 18501, BOPFlowerBands.PLAIN_MATERIAL);
		BOPFlowerBands.LUSH = band(builder, "lush", 18502, BOPFlowerBands.LUSH_MATERIAL);
		BOPFlowerBands.EXOTIC = band(builder, "exotic", 18503, BOPFlowerBands.EXOTIC_MATERIAL);
	}

	private static Item band(ItemBuilder builder, String tier, int id, ArmorMaterial material) {
		return builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.ARMOR_HELMET_LEATHER))
			.build(new ItemArmor<>(tier + "_flower_band", "betteroplenty:item/" + tier + "flowerband",
				id, material, HumanArmorShape.HEAD));
	}

	private static void registerWaterGear(ItemBuilder builder) {
		WADING_BOOTS = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.ARMOR_BOOTS_ICESKATES))
			.build(new ItemArmor<>("wading_boots", "betteroplenty:item/wadingboots", 18504,
				BOPMaterials.WADING_BOOTS_ARMOR, HumanArmorShape.BOOTS));
		FLIPPERS = builder.clone()
			.setCreativeInventoryPlacement(after(() -> WADING_BOOTS))
			.build(new ItemArmor<>("flippers", "betteroplenty:item/flippers", 18505,
				BOPMaterials.FLIPPERS_ARMOR, HumanArmorShape.BOOTS));
	}

	private static void registerMudTier(ItemBuilder builder) {

		MUD_PICKAXE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_PICKAXE_WOOD))
			.build(new ItemToolPickaxe(
				"mud_pickaxe", "betteroplenty:item/mudpickaxe", 18400, BOPMaterials.MUD_TOOL));
		MUD_SHOVEL = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_SHOVEL_WOOD))
			.build(new ItemToolShovel(
				"mud_shovel", "betteroplenty:item/mudshovel", 18401, BOPMaterials.MUD_TOOL));
		MUD_AXE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_AXE_WOOD))
			.build(new ItemToolAxe(
				"mud_axe", "betteroplenty:item/mudaxe", 18402, BOPMaterials.MUD_TOOL));

		MUD_SWORD = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_SWORD_WOOD))
			.build(new ItemToolSword(
				"mud_sword", "betteroplenty:item/mudsword", 18403, BOPMaterials.MUD_TOOL)
				.withTags(ItemTags.PREVENT_CREATIVE_MINING));
		MUD_HOE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.TOOL_HOE_WOOD))
			.build(new ItemToolHoe(
				"mud_hoe", "betteroplenty:item/mudhoe", 18404, BOPMaterials.MUD_TOOL));

		MUD_HELMET = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.ARMOR_HELMET_LEATHER))
			.build(new ItemArmor<>(
				"mud_helmet", "betteroplenty:item/mudhelmet", 18405,
				BOPMaterials.MUD_ARMOR, HumanArmorShape.HEAD));
		MUD_CHESTPLATE = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.ARMOR_CHESTPLATE_LEATHER))
			.build(new ItemArmor<>(
				"mud_chestplate", "betteroplenty:item/mudchestplate", 18406,
				BOPMaterials.MUD_ARMOR, HumanArmorShape.CHEST));
		MUD_LEGGINGS = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.ARMOR_LEGGINGS_LEATHER))
			.build(new ItemArmor<>(
				"mud_leggings", "betteroplenty:item/mudleggings", 18407,
				BOPMaterials.MUD_ARMOR, HumanArmorShape.LEGS));
		MUD_BOOTS = builder.clone()
			.setCreativeInventoryPlacement(after(() -> Items.ARMOR_BOOTS_LEATHER))
			.build(new ItemArmor<>(
				"mud_boots", "betteroplenty:item/mudboots", 18408,
				BOPMaterials.MUD_ARMOR, HumanArmorShape.BOOTS));
	}
}
