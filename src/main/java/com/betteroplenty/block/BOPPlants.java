package com.betteroplenty.block;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.fluid.BOPDamageTypes;
import com.betteroplenty.fluid.BOPFluidContact;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicSupplier;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.sound.BlockSounds;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.util.ArrayList;
import java.util.List;

public final class BOPPlants {
	private BOPPlants() {}

	public static Block<BlockLogicBOPDoublePlant> HIGH_GRASS;

	public static Block<BlockLogicBOPDoublePlant> HIGH_GRASS_TOP;

	public static Block<BlockLogicBOPGroundCover> BUSH;

	public static Block<BlockLogicBOPGroundCover> SPROUT;

	public static Block<BlockLogicBOPGroundCover> POISON_IVY;

	public static Block<BlockLogicBOPGroundCover> BERRY_BUSH;

	public static Block<BlockLogicBOPGroundCover> SHRUB;

	public static Block<BlockLogicBOPGroundCover> DAMP_GRASS;

	public static Block<BlockLogicBOPGroundCover> KORU;

	public static Block<BlockLogicBOPGroundCover> CLOVER_PATCH;

	public static Block<BlockLogicBOPGroundCover> SHORT_GRASS;

	public static Block<BlockLogicBOPGroundCover> MEDIUM_GRASS;

	public static Block<BlockLogicBOPGroundCover> DEAD_GRASS;

	public static Block<BlockLogicBOPGroundCover> DESERT_GRASS;

	public static Block<BlockLogicBOPGroundCover> DESERT_SPROUTS;

	public static Block<BlockLogicBOPGroundCover> DUNE_GRASS;

	public static Block<BlockLogicBOPGroundCover> THORN;

	public static Block<BlockLogicBOPGroundCover> BARLEY;

	public static Block<BlockLogicBOPGroundCover> CATTAIL;

	public static Block<BlockLogicBOPGroundCover> RIVER_CANE;

	public static Block<BlockLogicBOPDoublePlant> CATTAIL_TOP;

	public static Block<BlockLogicBOPDoublePlant> CATTAIL_BOTTOM;

	public static Block<BlockLogicBOPGroundCover> WILD_CARROT;

	public static Block<BlockLogicBOPGroundCover> TINY_CACTUS;

	public static Block<BlockLogicBOPGroundCover> REED;

	public static Block<BlockLogicBOPGroundCover> ROOT;

	public static Block<BlockLogicBOPHangingMoss> MOSS;

	public static Block<BlockLogicBOPHangingMoss> TREE_MOSS;

	public static Block<BlockLogicBOPHangingMoss> WILLOW_DRAPE;

	public static Block<BlockLogicBOPHangingMoss> IVY;

	private static final List<Block<?>> GRASS_TINTED = new ArrayList<>();
	private static final List<Block<?>> FOLIAGE_TINTED = new ArrayList<>();

	private static final List<Block<?>> CROSS_MODELS = new ArrayList<>();
	private static final List<String> CROSS_TEXTURES = new ArrayList<>();

	private static final List<String> CROSS_MODEL_KEYS = new ArrayList<>();

	private static final List<Block<?>> CROP_MODELS = new ArrayList<>();
	private static final List<String> CROP_MODEL_KEYS = new ArrayList<>();

	private static final List<Block<?>> SHEET_MODELS = new ArrayList<>();
	private static final List<String> SHEET_TEXTURES = new ArrayList<>();

	@NotNull public static List<Block<?>> grassTinted()   { return GRASS_TINTED; }
	@NotNull public static List<Block<?>> foliageTinted() { return FOLIAGE_TINTED; }
	@NotNull public static List<Block<?>> crossModels()   { return CROSS_MODELS; }
	@NotNull public static List<String> crossTextures()   { return CROSS_TEXTURES; }
	@NotNull public static List<String> crossModelKeys()  { return CROSS_MODEL_KEYS; }
	@NotNull public static List<Block<?>> cropModels()    { return CROP_MODELS; }
	@NotNull public static List<String> cropModelKeys()   { return CROP_MODEL_KEYS; }
	@NotNull public static List<Block<?>> sheetModels()   { return SHEET_MODELS; }
	@NotNull public static List<String> sheetTextures()   { return SHEET_TEXTURES; }

	public static void register() {

		BlockBuilder plant = new BlockBuilder(BetterOPlenty.MOD_ID)
			.setHardness(0.0f)
			.setBlockSound(BlockSounds.GRASS)
			.setFlammability(60, 100)
			.setTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLACE_OVERWRITES,
				BlockTags.SHEARS_DO_SILK_TOUCH, BlockTags.SHEEPS_FAVOURITE_BLOCK);

		registerFoliage(plant);
		registerPlants(plant);
		registerMosses(plant);

		BetterOPlenty.LOGGER.info(
			"Registered {} BOP ground-cover blocks ({} crosses, {} crops, {} sheets; "
				+ "{} grass-tinted, {} foliage-tinted).",
			CROSS_MODELS.size() + CROP_MODELS.size() + SHEET_MODELS.size(),
			CROSS_MODELS.size(), CROP_MODELS.size(), SHEET_MODELS.size(),
			GRASS_TINTED.size(), FOLIAGE_TINTED.size());

		BetterOPlenty.LOGGER.warn("Wild carrot and sprout have no hand-harvest drop yet: upstream "
			+ "drops a wild carrot and a carrot-or-potato, and neither item exists in this port. "
			+ "The blocks, their generation and their shear drops are complete. (Berries landed "
			+ "2026-07-29 as a right-click PICK rather than a drop; koru's turnip seed landed with "
			+ "the turnip crop.)");
	}

	private static void registerFoliage(@NotNull BlockBuilder plant) {

		HIGH_GRASS = cross(plant, "high_grass", 1750, "highgrassbottom", VISIBLE,
			block -> BlockLogicBOPDoublePlant.lower(block, () -> HIGH_GRASS_TOP, () -> Blocks.TALLGRASS),
			GRASS_TINT);

		HIGH_GRASS_TOP = cross(plant, "high_grass_top", 1751, "highgrasstop", HIDDEN,
			block -> {
				BlockLogicBOPDoublePlant logic = BlockLogicBOPDoublePlant.upper(block, () -> HIGH_GRASS);
				logic.withDropAs(() -> Blocks.TALLGRASS);
				return logic;
			},
			GRASS_TINT);

		BUSH = cross(plant, "bush", 1752, "bush", VISIBLE,
			BlockLogicBOPGroundCover::new, GRASS_TINT);

		SPROUT = cross(plant, "sprout", 1753, "sprout", VISIBLE,
			BlockLogicBOPGroundCover::new, GRASS_TINT);

		POISON_IVY = cross(plant, "poison_ivy", 1754, "poisonivy", VISIBLE,
			block -> new BlockLogicBOPGroundCover(block).withContact(BOPPlants::poisonIvyContact),
			GRASS_TINT);

		BERRY_BUSH = cross(plant, "berry_bush", 1755, "berrybush", VISIBLE,
			block -> new BlockLogicBOPGroundCover(block)
				.withDropAs(() -> BUSH)
				.withPickable(() -> com.betteroplenty.item.BOPFoods.BERRIES, () -> BUSH),
			GRASS_TINT);

		SHRUB = cross(plant, "shrub", 1756, "shrub", VISIBLE,
			BlockLogicBOPGroundCover::new, FOLIAGE_TINT);

		DAMP_GRASS = cross(plant, "damp_grass", 1757, "dampgrass", VISIBLE,
			BlockLogicBOPGroundCover::new, GRASS_TINT);

		KORU = cross(plant, "koru", 1758, "koru", VISIBLE,
			block -> new BlockLogicBOPGroundCover(block)
				.withHandDrop(64)
				.withDropItem(() -> com.betteroplenty.item.BOPItems.TURNIP_SEEDS),
			GRASS_TINT);

		CLOVER_PATCH = flat(plant, "clover_patch", 1759, "cloverpatch", VISIBLE,
			block -> new BlockLogicBOPGroundCover(block).withBounds(0.0, 0.0, 0.0, 1.0, 0.015625, 1.0),
			GRASS_TINT);

		SHORT_GRASS = cross(plant, "short_grass", 1776, "shortgrass", VISIBLE,
			BlockLogicBOPGroundCover::new, GRASS_TINT);

		MEDIUM_GRASS = cross(plant, "medium_grass", 1777, "mediumgrass", VISIBLE,
			BlockLogicBOPGroundCover::new, GRASS_TINT);
	}

	private static void registerPlants(@NotNull BlockBuilder plant) {

		DEAD_GRASS = cross(plant, "dead_grass", 1760, "deadgrass", VISIBLE,
			block -> new BlockLogicBOPGroundCover(block)
				.withSoil(soil -> soil == Blocks.SAND || soil == BOPWastes.DRIED_DIRT),
			NO_TINT);

		DESERT_GRASS = cross(plant, "desert_grass", 1761, "desertgrass", VISIBLE,
			block -> new BlockLogicBOPGroundCover(block).withSoil(soil -> soil == BOPBlocks.RED_ROCK),
			NO_TINT);

		DESERT_SPROUTS = cross(plant, "desert_sprouts", 1762, "desertsprouts", VISIBLE,
			BlockLogicBOPGroundCover::new, NO_TINT);

		DUNE_GRASS = cross(plant, "dune_grass", 1763, "dunegrass", VISIBLE,
			block -> new BlockLogicBOPGroundCover(block).withSoil(soil -> soil == Blocks.SAND),
			NO_TINT);

		THORN = cross(plant.clone().setTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.SHEARS_DO_SILK_TOUCH),
			"thorn", 1764, "thorn", VISIBLE,
			block -> new BlockLogicBOPGroundCover(block)
				.withSoil(soil -> soil == Blocks.GRASS || soil == Blocks.DIRT || soil == Blocks.SOULSAND)
				.withoutLight()
				.withContact(BOPPlants::spikeContact)
				.withHarvestBite(2),
			NO_TINT);

		BARLEY = crop(plant, "barley", 1765, VISIBLE,
			block -> new BlockLogicBOPGroundCover(block)
				.withSoil(soil -> soil == Blocks.GRASS || soil == Blocks.DIRT)
				.withHandDrop(5)

				.withBounds(0.125, 0.0, 0.125, 0.875, 1.0, 0.875));

		CATTAIL = crop(plant.clone().setTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLACE_OVERWRITES),
			"cattail", 1766, VISIBLE,
			block -> new BlockLogicBOPGroundCover(block)
				.withSoil(soil -> soil == Blocks.GRASS)
				.withWaterBeside()
				.withHandDrop(1)
				.withBounds(0.125, 0.0, 0.125, 0.875, 1.0, 0.875)
				.withTallGrowth(() -> CATTAIL_BOTTOM, () -> CATTAIL_TOP));

		RIVER_CANE = crop(plant.clone().setTags(BlockTags.BROKEN_BY_FLUIDS),
			"river_cane", 1767, VISIBLE,
			block -> new BlockLogicBOPGroundCover(block)
				.withSoil(soil -> soil == Blocks.GRASS)
				.stackingOnItself(() -> RIVER_CANE)
				.withHandDrop(1));

		CATTAIL_TOP = crop(plant.clone().setTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLACE_OVERWRITES),
			"cattail_top", 1768, HIDDEN,
			block -> {
				BlockLogicBOPDoublePlant logic = BlockLogicBOPDoublePlant.upper(block, () -> CATTAIL_BOTTOM);
				logic.withHandDrop(1).withDropAs(() -> CATTAIL);
				return logic;
			});

		CATTAIL_BOTTOM = crop(plant.clone().setTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLACE_OVERWRITES),
			"cattail_bottom", 1769, HIDDEN,
			block -> {
				BlockLogicBOPDoublePlant logic =
					BlockLogicBOPDoublePlant.lower(block, () -> CATTAIL_TOP, null);
				logic.withSoil(soil -> soil == Blocks.GRASS).withWaterBeside();
				return logic;
			});

		WILD_CARROT = crop(plant, "wild_carrot", 1770, VISIBLE,
			block -> new BlockLogicBOPGroundCover(block)
				.withHandDrop(1)
				.withDropItem(() -> com.betteroplenty.item.BOPFoods.WILD_CARROTS));

		TINY_CACTUS = cross(plant, "tiny_cactus", 1771, "cactus", VISIBLE,
			block -> new BlockLogicBOPGroundCover(block)
				.withSoil(soil -> soil == Blocks.SAND || soil == BOPBlocks.RED_ROCK
					|| soil == Blocks.SOULSAND)
				.withContact(BOPPlants::spikeContact)
				.withHandDrop(1)
				.withCactusGrowth(),
			NO_TINT);

		REED = cross(plant, "reed", 1772, "reed", VISIBLE,
			block -> new BlockLogicBOPGroundCover(block)
				.withSoil(soil -> soil == Blocks.FLUID_WATER_STILL)
				.withHandDrop(1),
			NO_TINT);

		ROOT = cross(plant, "root", 1773, "root", VISIBLE,
			block -> new BlockLogicBOPGroundCover(block).hangingFromAbove().withHandDrop(1),
			NO_TINT);
	}

	private static void registerMosses(@NotNull BlockBuilder plant) {

		BlockBuilder moss = plant.clone()
			.setHardness(0.2f)
			.setFlammability(15, 100)
			.setTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.SHEARS_DO_SILK_TOUCH);

		MOSS = moss.clone()
			.setCreativeInventoryPlacement(afterBTAGrasses())
			.build("moss", 1774, block -> BlockLogicBOPHangingMoss.caveMoss(block,
				() -> new Block<?>[]{
					BOPWoodSets.REDWOOD.log, BOPWoodSets.WILLOW.log, BOPWoodSets.DEAD.log
				}));
		sheet(MOSS, "moss");
		FOLIAGE_TINTED.add(MOSS);

		TREE_MOSS = moss.clone()
			.setCreativeInventoryPlacement(afterBTAGrasses())
			.build("tree_moss", 1775, BlockLogicBOPHangingMoss::treeMoss);
		sheet(TREE_MOSS, "treemoss");

		WILLOW_DRAPE = moss.clone()
			.setCreativeInventoryPlacement(afterBTAGrasses())
			.build("willow_drape", 1940, block -> BlockLogicBOPHangingMoss.drape(block, false));
		sheet(WILLOW_DRAPE, "willow");
		FOLIAGE_TINTED.add(WILLOW_DRAPE);

		IVY = moss.clone()
			.setCreativeInventoryPlacement(afterBTAGrasses())
			.build("ivy", 1941, block -> BlockLogicBOPHangingMoss.drape(block, true));
		sheet(IVY, "ivy");
		FOLIAGE_TINTED.add(IVY);
	}

	private static void sheet(@NotNull Block<?> block, @NotNull String texture) {
		SHEET_MODELS.add(block);
		SHEET_TEXTURES.add("betteroplenty:block/" + texture);
	}

	private static void poisonIvyContact(@NotNull World world, @NotNull TilePosc tilePos,
										 @NotNull Entity entity) {
		if (!(entity instanceof Mob mob) || !mob.isAlive() || wearsLeatherLegwear(entity)) {
			return;
		}
		if (!BOPFluidContact.acquire(BOPFluidContact.POISON_LAST_APPLIED, entity,
				BOPFluidContact.DAMAGE_INTERVAL_TICKS)) {
			return;
		}

		if (mob.getHealth() > 1) {
			mob.hurt(null, 1, BOPDamageTypes.POISON);
		}
	}

	private static void spikeContact(@NotNull World world, @NotNull TilePosc tilePos,
									 @NotNull Entity entity) {
		if (!wearsLeatherLegwear(entity)) {
			entity.hurt(null, 1, DamageType.COMBAT);
		}
	}

	private static boolean wearsLeatherLegwear(@NotNull Entity entity) {
		if (!(entity instanceof Player player)) {
			return false;
		}
		ItemStack boots = player.getItemInArmorSlot(HumanArmorShape.BOOTS);
		ItemStack legs = player.getItemInArmorSlot(HumanArmorShape.LEGS);
		return boots != null && boots.getItem() == Items.ARMOR_BOOTS_LEATHER
			&& legs != null && legs.getItem() == Items.ARMOR_LEGGINGS_LEATHER;
	}

	private static final boolean VISIBLE = false;

	private static final boolean HIDDEN = true;

	private static final int NO_TINT = 0;
	private static final int GRASS_TINT = 1;
	private static final int FOLIAGE_TINT = 2;

	@NotNull
	private static CreativeInventoryPlacement afterBTAGrasses() {
		return new CreativeInventoryPlacement.After(() -> Blocks.TALLGRASS_FERN);
	}

	private static <T extends BlockLogic> Block<T> flat(
			@NotNull BlockBuilder builder, @NotNull String name, int id, @NotNull String texture,
			boolean hidden, @NotNull BlockLogicSupplier<T> logic, int tint) {

		BlockBuilder configured = builder.clone();
		Block<T> block = hidden
			? configured.addTags(BlockTags.NOT_IN_CREATIVE_MENU).build(name, id, logic)
			: configured.setCreativeInventoryPlacement(afterBTAGrasses()).build(name, id, logic);

		sheet(block, texture);
		if (tint == GRASS_TINT) {
			GRASS_TINTED.add(block);
		} else if (tint == FOLIAGE_TINT) {
			FOLIAGE_TINTED.add(block);
		}
		return block;
	}

	private static <T extends BlockLogic> Block<T> crop(
			@NotNull BlockBuilder builder, @NotNull String name, int id,
			boolean hidden, @NotNull BlockLogicSupplier<T> logic) {

		BlockBuilder configured = builder.clone();
		Block<T> block = hidden
			? configured.addTags(BlockTags.NOT_IN_CREATIVE_MENU).build(name, id, logic)
			: configured.setCreativeInventoryPlacement(afterBTAGrasses()).build(name, id, logic);

		CROP_MODELS.add(block);
		CROP_MODEL_KEYS.add("betteroplenty:block/crops/" + name);

		return block;
	}

	private static <T extends BlockLogic> Block<T> cross(
			@NotNull BlockBuilder builder, @NotNull String name, int id, @NotNull String texture,
			boolean hidden, @NotNull BlockLogicSupplier<T> logic, int tint) {

		BlockBuilder configured = builder.clone();
		Block<T> block = hidden
			? configured.addTags(BlockTags.NOT_IN_CREATIVE_MENU).build(name, id, logic)
			: configured.setCreativeInventoryPlacement(afterBTAGrasses()).build(name, id, logic);

		CROSS_MODELS.add(block);
		CROSS_TEXTURES.add("betteroplenty:block/" + texture);
		CROSS_MODEL_KEYS.add("betteroplenty:block/cross/" + name);
		if (tint == GRASS_TINT) {
			GRASS_TINTED.add(block);
		} else if (tint == FOLIAGE_TINT) {
			FOLIAGE_TINTED.add(block);
		}
		return block;
	}
}
