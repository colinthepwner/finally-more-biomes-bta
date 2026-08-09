package com.betteroplenty.block;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.item.BOPFoods;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicSupplier;
import net.minecraft.core.block.BlockLogicTransparent;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSounds;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BOPHive {
	private BOPHive() {}

	public static Block<BlockLogicHive> HONEYCOMB;

	public static Block<BlockLogicHive> HIVE;

	public static Block<BlockLogicHive> HONEYCOMB_EMPTY;

	public static Block<BlockLogicHive> HONEYCOMB_FILLED;

	public static Block<BlockLogicTransparent> HONEY_BLOCK;

	private static final List<Block<?>> CUBE_MODELS = new ArrayList<>();
	private static final List<String> CUBE_TEXTURES = new ArrayList<>();

	@NotNull public static List<Block<?>> cubeModels() { return CUBE_MODELS; }

	@NotNull public static List<String> cubeTextures() { return CUBE_TEXTURES; }

	public static void register() {

		BlockBuilder hive = new BlockBuilder(BetterOPlenty.MOD_ID)
			.setHardness(0.5f)
			.setBlockSound(BlockSounds.GRASS)
			.setTags(BlockTags.MINEABLE_BY_AXE);

		HONEYCOMB = build(hive, "hive_honeycomb", 2100, "honeycomb",
			block -> new BlockLogicHive(block, BlockLogicHive.Cell.COMB, null));

		HIVE = build(hive, "hive", 2101, "hive",
			block -> new BlockLogicHive(block, BlockLogicHive.Cell.SHELL, null));

		HONEYCOMB_EMPTY = build(hive, "hive_honeycomb_empty", 2102, "honeycombempty",
			block -> new BlockLogicHive(block, BlockLogicHive.Cell.BROOD, null));

		HONEYCOMB_FILLED = build(hive, "hive_honeycomb_filled", 2103, "honeycombfilled",
			block -> new BlockLogicHive(block, BlockLogicHive.Cell.FILLED,
				() -> BOPFoods.FILLED_HONEYCOMB));

		HONEY_BLOCK = new BlockBuilder(BetterOPlenty.MOD_ID)
			.setHardness(0.5f)
			.setBlockSound(BlockSounds.STONE)
			.setCreativeInventoryPlacement(afterGlowstone())
			.build("honey_block", 2104, block -> new BlockLogicTransparent(block, Materials.GLASS));
		CUBE_MODELS.add(HONEY_BLOCK);
		CUBE_TEXTURES.add("betteroplenty:block/honeyblock");

		BetterOPlenty.LOGGER.info(
			"Registered {} BOP hive blocks and the honey block (ids 2100-2104); their only source is "
				+ "WorldGenHive, in BOP's six Nether biomes.", 4);
	}

	@NotNull
	private static CreativeInventoryPlacement afterGlowstone() {
		return new CreativeInventoryPlacement.After(() -> Blocks.GLOWSTONE);
	}

	private static <T extends BlockLogic> Block<T> build(
			@NotNull BlockBuilder builder, @NotNull String name, int id, @NotNull String texture,
			@NotNull BlockLogicSupplier<T> logic) {

		Block<T> block = builder.clone()
			.setCreativeInventoryPlacement(afterGlowstone())
			.build(name, id, logic);

		CUBE_MODELS.add(block);
		CUBE_TEXTURES.add("betteroplenty:block/" + texture);
		return block;
	}

	@NotNull
	public static List<Block<?>> all() {
		return Arrays.asList(HONEYCOMB, HIVE, HONEYCOMB_EMPTY, HONEYCOMB_FILLED, HONEY_BLOCK);
	}
}
