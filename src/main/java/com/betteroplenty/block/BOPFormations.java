package com.betteroplenty.block;

import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicSupplier;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BOPFormations {
	private BOPFormations() {}

	public static Block<BlockLogicBOPGroundCover> STALAGMITE;

	public static Block<BlockLogicBOPGroundCover> STALACTITE;

	private static final List<Block<?>> CROSS_MODELS = new ArrayList<>();
	private static final List<String> CROSS_TEXTURES = new ArrayList<>();

	@NotNull public static List<Block<?>> crossModels() { return CROSS_MODELS; }
	@NotNull public static List<String> crossTextures() { return CROSS_TEXTURES; }

	public static boolean growsOnStone(@Nullable Block<?> block) {
		return block == Blocks.STONE;
	}

	public static void register() {

		BlockBuilder formation = new BlockBuilder(BetterOPlenty.MOD_ID)
			.setHardness(0.0f)
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLACE_OVERWRITES);

		STALAGMITE = build(formation, "stalagmite", 2038, "stalagmite",
			block -> new BlockLogicBOPGroundCover(block)
				.withSoil(BOPFormations::growsOnStone)
				.withoutLight());

		STALACTITE = build(formation, "stalactite", 2039, "stalactite",
			block -> new BlockLogicBOPGroundCover(block)
				.withSoil(BOPFormations::growsOnStone)
				.hangingFromAbove());

		BetterOPlenty.LOGGER.info(
			"Registered {} BOP stone-formation blocks (ids 2038-2039); both counters run on upstream's "
				+ "defaults of 3 stalagmites and 6 stalactites per chunk in EVERY biome.",
			CROSS_MODELS.size());
	}

	@NotNull
	private static CreativeInventoryPlacement afterStone() {
		return new CreativeInventoryPlacement.After(() -> Blocks.STONE);
	}

	private static <T extends BlockLogic> Block<T> build(
			@NotNull BlockBuilder builder, @NotNull String name, int id, @NotNull String texture,
			@NotNull BlockLogicSupplier<T> logic) {

		Block<T> block = builder.clone()
			.setCreativeInventoryPlacement(afterStone())
			.build(name, id, logic);

		CROSS_MODELS.add(block);
		CROSS_TEXTURES.add("betteroplenty:block/" + texture);
		return block;
	}

	@NotNull
	public static List<Block<?>> all() {
		return Arrays.asList(STALAGMITE, STALACTITE);
	}
}
