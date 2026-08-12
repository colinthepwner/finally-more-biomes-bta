package com.betteroplenty.block;

import com.betteroplenty.BOPIdManifest;
import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSounds;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.util.Arrays;
import java.util.List;

public final class BOPGraves {
	private BOPGraves() {}

	public static Block<BlockLogicGrave> GRAVE;

	public static Block<BlockLogicGrave> GRAVE_TOP;

	public static void register() {

		BlockBuilder grave = new BlockBuilder(BetterOPlenty.MOD_ID)
			.setHardness(5.0f)
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE);

		GRAVE = grave.clone()
			.setCreativeInventoryPlacement(afterBonePile())
			.build("grave", 2123, block -> BlockLogicGrave.lower(block, () -> GRAVE_TOP));

		GRAVE_TOP = grave.clone()
			.addTags(BlockTags.NOT_IN_CREATIVE_MENU)
			.build("grave_top", 2124, block -> BlockLogicGrave.upper(block, () -> GRAVE));

		BetterOPlenty.LOGGER.info(
			"Registered {} BOP grave blocks (ids {}); all six BOP Nether biomes set "
				+ "gravesPerChunk = 1.", all().size(),
			BOPIdManifest.span(GRAVE.id(), GRAVE_TOP.id()));
	}

	@NotNull
	private static CreativeInventoryPlacement afterBonePile() {
		return new CreativeInventoryPlacement.After(() -> Blocks.BONE_PILE);
	}

	@NotNull
	public static List<Block<?>> all() {
		return Arrays.asList(GRAVE, GRAVE_TOP);
	}
}
