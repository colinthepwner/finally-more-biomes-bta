package com.betteroplenty.block;

import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicSupplier;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BOPCorals {
	private BOPCorals() {}

	public static Block<BlockLogicBOPCoral> KELP_BOTTOM;
	public static Block<BlockLogicBOPCoral> KELP_MIDDLE;
	public static Block<BlockLogicBOPCoral> KELP_TOP;
	public static Block<BlockLogicBOPCoral> KELP_SINGLE;

	public static Block<BlockLogicBOPCoral> CORAL_PINK;
	public static Block<BlockLogicBOPCoral> CORAL_ORANGE;
	public static Block<BlockLogicBOPCoral> CORAL_BLUE;
	public static Block<BlockLogicBOPCoral> CORAL_GLOW;

	private static final List<Block<?>> CROSS_MODELS = new ArrayList<>();
	private static final List<String> CROSS_TEXTURES = new ArrayList<>();

	@NotNull public static List<Block<?>> crossModels() { return CROSS_MODELS; }
	@NotNull public static List<String> crossTextures() { return CROSS_TEXTURES; }

	public static boolean growsOnSeaBed(@Nullable Block<?> block) {
		return seaBed(block);
	}

	private static boolean seaBed(@Nullable Block<?> block) {
		return block == Blocks.DIRT
			|| block == Blocks.SAND
			|| block == Blocks.SPONGE_DRY
			|| block == Blocks.SPONGE_WET
			|| block == Blocks.STONE
			|| block == Blocks.BLOCK_CLAY
			|| isKelp(block)
			|| isCoral(block);
	}

	public static boolean isKelp(@Nullable Block<?> block) {
		return block != null
			&& (block == KELP_BOTTOM || block == KELP_MIDDLE || block == KELP_TOP || block == KELP_SINGLE);
	}

	public static boolean isCoral(@Nullable Block<?> block) {
		return block != null
			&& (block == CORAL_PINK || block == CORAL_ORANGE || block == CORAL_BLUE || block == CORAL_GLOW);
	}

	public static void register() {

		BlockBuilder coral = new BlockBuilder(BetterOPlenty.MOD_ID)
			.setHardness(0.0f)
			.setBlockSound(BlockSounds.GRASS)
			.setTags(BlockTags.PLACE_OVERWRITES, BlockTags.SHEARS_DO_SILK_TOUCH);

		KELP_BOTTOM = build(coral, "kelp_bottom", 1830, "kelpbottom", HIDDEN,
			block -> new BlockLogicBOPCoral(block, Materials.WATER, BOPCorals::seaBed,
				() -> KELP_SINGLE, () -> KELP_SINGLE));

		KELP_MIDDLE = build(coral, "kelp_middle", 1831, "kelpmiddle", HIDDEN,
			block -> new BlockLogicBOPCoral(block, Materials.WATER, BOPCorals::isKelp,
				() -> KELP_TOP, () -> KELP_SINGLE));

		KELP_TOP = build(coral, "kelp_top", 1832, "kelptop", HIDDEN,
			block -> new BlockLogicBOPCoral(block, Materials.WATER, BOPCorals::isKelp,
				null, () -> KELP_SINGLE));

		KELP_SINGLE = build(coral, "kelp", 1833, "kelpsingle", VISIBLE,
			block -> new BlockLogicBOPCoral(block, Materials.WATER, BOPCorals::seaBed, null, null)
				.withRegrowth());

		CORAL_PINK = build(coral, "coral_pink", 1834, "pinkcoral", VISIBLE,
			block -> new BlockLogicBOPCoral(block, Materials.WATER, BOPCorals::seaBed, null, null));

		CORAL_ORANGE = build(coral, "coral_orange", 1835, "orangecoral", VISIBLE,
			block -> new BlockLogicBOPCoral(block, Materials.WATER, BOPCorals::seaBed, null, null));

		CORAL_BLUE = build(coral, "coral_blue", 1836, "bluecoral", VISIBLE,
			block -> new BlockLogicBOPCoral(block, Materials.WATER, BOPCorals::seaBed, null, null));

		CORAL_GLOW = build(coral.clone().setLuminance(10), "coral_glow", 1837, "glowcoral", VISIBLE,
			block -> new BlockLogicBOPCoral(block, Materials.WATER, BOPCorals::seaBed, null, null));

		BetterOPlenty.LOGGER.info("Registered {} BOP coral and kelp blocks (ids 1830-1837).",
			CROSS_MODELS.size());

		BetterOPlenty.LOGGER.warn("Glow coral has no recipe yet: its only upstream use is ambrosia "
			+ "(core/BOPCrafting.java:269), whose nine ingredients include a potion -- cut content -- "
			+ "and six items this port has not got. Deferred with BOP's food surface.");
	}

	private static final boolean VISIBLE = false;
	private static final boolean HIDDEN = true;

	@NotNull
	private static CreativeInventoryPlacement afterAlgae() {
		return new CreativeInventoryPlacement.After(() -> Blocks.ALGAE);
	}

	private static <T extends BlockLogic> Block<T> build(
			@NotNull BlockBuilder builder, @NotNull String name, int id, @NotNull String texture,
			boolean hidden, @NotNull BlockLogicSupplier<T> logic) {

		BlockBuilder configured = builder.clone();
		Block<T> block = hidden
			? configured.addTags(BlockTags.NOT_IN_CREATIVE_MENU).build(name, id, logic)
			: configured.setCreativeInventoryPlacement(afterAlgae()).build(name, id, logic);

		CROSS_MODELS.add(block);
		CROSS_TEXTURES.add("betteroplenty:block/" + texture);
		return block;
	}

	@NotNull
	public static List<Block<?>> all() {
		return Arrays.asList(KELP_BOTTOM, KELP_MIDDLE, KELP_TOP, KELP_SINGLE,
			CORAL_PINK, CORAL_ORANGE, CORAL_BLUE, CORAL_GLOW);
	}
}
