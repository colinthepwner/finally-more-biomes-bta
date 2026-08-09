package com.betteroplenty.block;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.item.ItemBlockDandelion;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicSupplier;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.sound.BlockSounds;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.util.List;
import java.util.function.Supplier;

public final class BOPFlowers {
	private BOPFlowers() {}

	public static final double LILY_FLOWER_DROP = -0.97;

	public static Block<BlockLogicFlowerBOP> CLOVER;

	public static Block<BlockLogicFlowerBOP> SWAMP_FLOWER;

	public static Block<BlockLogicDeathbloom> DEATHBLOOM;

	public static Block<BlockLogicFlowerBOP> GLOW_FLOWER;

	public static Block<BlockLogicFlowerBOP> HYDRANGEA;

	public static Block<BlockLogicFlowerBOP> COSMOS;

	public static Block<BlockLogicFlowerBOP> DAFFODIL;

	public static Block<BlockLogicFlowerBOP> WILDFLOWER;

	public static Block<BlockLogicFlowerBOP> VIOLET;

	public static Block<BlockLogicFlowerBOP> ANEMONE;

	public static Block<BlockLogicFlowerBOP> LILY_FLOWER;

	public static Block<BlockLogicFlowerBOP> RAINBOW_FLOWER;

	public static Block<BlockLogicFlowerBOP> BROMELIAD;

	public static Block<BlockLogicSunflower> SUNFLOWER;

	public static Block<BlockLogicSunflowerTop> SUNFLOWER_TOP;

	public static Block<BlockLogicFlowerBOP> DANDELION;

	public static Block<BlockLogicFlowerBOP> HIBISCUS;

	public static Block<BlockLogicFlowerBOP> LILY_OF_THE_VALLEY;

	public static Block<BlockLogicBurningBlossom> BURNING_BLOSSOM;

	public static Block<BlockLogicFlowerBOP> GOLDENROD;

	public static Block<BlockLogicFlowerBOP> BLUEBELLS;

	public static Block<BlockLogicFlowerBOP> MINERS_DELIGHT;

	public static Block<BlockLogicFlowerBOP> ICY_IRIS;

	public static Block<BlockLogicMushroomBOP> TOADSTOOL;

	public static Block<BlockLogicMushroomBOP> PORTOBELLO;

	public static Block<BlockLogicMushroomBOP> BLUE_MILK_CAP;

	public static Block<BlockLogicMushroomBOP> GLOWSHROOM;

	public static Block<BlockLogicMushroomBOP> FLAT_MUSHROOM;

	public static void register() {

		BlockBuilder plant = new BlockBuilder(BetterOPlenty.MOD_ID)
			.setHardness(0.0f)
			.setBlockSound(BlockSounds.GRASS);

		BlockBuilder flower = plant.clone().setTags(
			BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLANTABLE_IN_JAR,
			BlockTags.SHEARS_DO_SILK_TOUCH, BlockTags.SHEEPS_FAVOURITE_BLOCK);

		BlockBuilder mushroom = plant.clone().setTags(
			BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLANTABLE_IN_JAR,
			BlockTags.SHEARS_DO_SILK_TOUCH, BlockTags.PIGS_FAVOURITE_BLOCK);

		CLOVER = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.TALLGRASS))
			.build("clover", 1700, block ->
				new BlockLogicFlowerBOP(block, BOPSoils.GROWS_FLOWERS, true, 0.015625, 0.5));

		SWAMP_FLOWER = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_LIGHT_BLUE))
			.build("swamp_flower", 1701, defaultFlower());

		DEATHBLOOM = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_PURPLE))
			.build("deathbloom", 1702, BlockLogicDeathbloom::new);

		GLOW_FLOWER = flower.clone()
			.setLuminance(9)
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_LIGHT_BLUE))
			.build("glow_flower", 1703, defaultFlower());

		HYDRANGEA = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_LIGHT_BLUE))
			.build("hydrangea", 1704, defaultFlower());

		COSMOS = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_ORANGE))
			.build("cosmos", 1705, block ->
				new BlockLogicFlowerBOP(block, BOPSoils.GROWS_FLOWERS, true, 0.8, 0.2));

		DAFFODIL = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_PINK))
			.build("daffodil", 1706, block ->
				new BlockLogicFlowerBOP(block, BOPSoils.GROWS_FLOWERS, true, 0.6, 0.2));

		WILDFLOWER = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_PINK))
			.build("wildflower", 1707, defaultFlower());

		VIOLET = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_PURPLE))
			.build("violet", 1708, defaultFlower());

		ANEMONE = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_PINK))
			.build("anemone", 1709, block ->
				new BlockLogicFlowerBOP(block, BOPSoils.GROWS_FLOWERS, true, 0.5, 0.2));

		LILY_FLOWER = flower.clone()
			.addTags(BlockTags.PLACE_OVERWRITES)
			.setCreativeInventoryPlacement(after(() -> Blocks.ALGAE))
			.build("lily_flower", 1710, block ->
				new BlockLogicFlowerBOP(block, BOPSoils.FLOATING_LEAF, true, 0.4, 0.2)
					.withBounds(0.3, LILY_FLOWER_DROP, 0.3, 0.7, 0.4, 0.7));

		RAINBOW_FLOWER = flower.clone()
			.setLuminance(5)
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_PURPLE))
			.build("rainbow_flower", 1711, block ->
				new BlockLogicFlowerBOP(block, BOPSoils.GROWS_FLOWERS, false, 0.4, 0.2));

		BROMELIAD = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.CACTUS))
			.build("bromeliad", 1712, block ->
				new BlockLogicFlowerBOP(block, BOPSoils.ARID, true, 0.8, 0.4));

		SUNFLOWER = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_YELLOW))
			.build("sunflower", 1713, BlockLogicSunflower::new);

		SUNFLOWER_TOP = flower.clone()
			.addTags(BlockTags.NOT_IN_CREATIVE_MENU)
			.build("sunflower_top", 1714, BlockLogicSunflowerTop::new);

		DANDELION = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_YELLOW))

			.setBlockItem(ItemBlockDandelion::new)
			.build("dandelion", 1715, block ->
				new BlockLogicFlowerBOP(block, BOPSoils.GROWS_FLOWERS, true, 0.6, 0.2));

		HIBISCUS = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_PINK))
			.build("hibiscus", 1716, defaultFlower());

		LILY_OF_THE_VALLEY = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_PINK))
			.build("lily_of_the_valley", 1717, defaultFlower());

		BURNING_BLOSSOM = flower.clone()
			.setLuminance(9)
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_ORANGE))
			.build("burning_blossom", 1718, BlockLogicBurningBlossom::new);

		GOLDENROD = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_YELLOW))
			.build("goldenrod", 1720, defaultFlower());

		BLUEBELLS = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_LIGHT_BLUE))
			.build("bluebells", 1721, defaultFlower());

		MINERS_DELIGHT = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.MUSHROOM_BROWN))
			.build("miners_delight", 1722, block ->
				new BlockLogicFlowerBOP(block, BOPSoils.STONE, false, 0.8, 0.4));

		ICY_IRIS = flower.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.FLOWER_LIGHT_BLUE))
			.build("icy_iris", 1723, defaultFlower());

		TOADSTOOL = mushroom.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.MUSHROOM_RED))
			.build("toadstool", 1724, block ->
				new BlockLogicMushroomBOP(block, BOPSoils.MUSHROOM_TOADSTOOL));

		PORTOBELLO = mushroom.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.MUSHROOM_BROWN))
			.build("portobello", 1725, block ->
				new BlockLogicMushroomBOP(block, BOPSoils.MUSHROOM_FIELD));

		BLUE_MILK_CAP = mushroom.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.MUSHROOM_BROWN))
			.build("blue_milk_cap", 1726, block ->
				new BlockLogicMushroomBOP(block, BOPSoils.MUSHROOM_FIELD));

		GLOWSHROOM = mushroom.clone()
			.setLuminance(6)
			.setCreativeInventoryPlacement(after(() -> Blocks.MUSHROOM_BROWN))
			.build("glowshroom", 1727, block ->
				new BlockLogicMushroomBOP(block, BOPSoils.MUSHROOM_CAVE));

		FLAT_MUSHROOM = mushroom.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.MUSHROOM_BROWN))
			.build("flat_mushroom", 1728, block ->
				new BlockLogicMushroomBOP(block, BOPSoils.MUSHROOM_FIELD));

		BetterOPlenty.LOGGER.info(
			"Registered {} BOP flower(s) and {} mushroom(s) ({} blocks; lavender was already at 1403).",
			16 + 7, 5, 28);
	}

	@NotNull
	private static BlockLogicSupplier<BlockLogicFlowerBOP> defaultFlower() {
		return block -> new BlockLogicFlowerBOP(block, BOPSoils.GROWS_FLOWERS);
	}

	@NotNull
	private static CreativeInventoryPlacement after(@NotNull Supplier<IItemConvertible> neighbour) {
		return new CreativeInventoryPlacement.After(neighbour);
	}

	public record Member(@NotNull Block<? extends BlockLogic> block, @NotNull String texture) {

		@NotNull
		public String textureKey() {
			return BetterOPlenty.MOD_ID + ":block/" + this.texture;
		}
	}

	@NotNull
	public static List<Member> registered() {
		return List.of(
			new Member(CLOVER, "clover"),
			new Member(SWAMP_FLOWER, "swampflower"),
			new Member(DEATHBLOOM, "deadbloom"),
			new Member(GLOW_FLOWER, "glowflower"),
			new Member(HYDRANGEA, "hydrangea"),
			new Member(COSMOS, "cosmos"),
			new Member(DAFFODIL, "daffodil"),
			new Member(WILDFLOWER, "wildflower"),
			new Member(VIOLET, "violet"),
			new Member(ANEMONE, "anemone"),
			new Member(LILY_FLOWER, "lilyflower"),
			new Member(RAINBOW_FLOWER, "rainbowflower"),
			new Member(BROMELIAD, "bromeliad"),
			new Member(SUNFLOWER, "sunflowerbottom"),
			new Member(SUNFLOWER_TOP, "sunflowertop"),
			new Member(DANDELION, "dandelion"),
			new Member(HIBISCUS, "hibiscus"),
			new Member(LILY_OF_THE_VALLEY, "lilyofthevalley"),
			new Member(BURNING_BLOSSOM, "burningblossom"),
			new Member(GOLDENROD, "goldenrod"),
			new Member(BLUEBELLS, "bluebells"),
			new Member(MINERS_DELIGHT, "minersdelight"),
			new Member(ICY_IRIS, "icyiris"),
			new Member(TOADSTOOL, "toadstool"),
			new Member(PORTOBELLO, "portobello"),
			new Member(BLUE_MILK_CAP, "bluemilk"),
			new Member(GLOWSHROOM, "glowshroom"),
			new Member(FLAT_MUSHROOM, "flatmushroom"));
	}
}
