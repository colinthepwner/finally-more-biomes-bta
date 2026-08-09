package com.betteroplenty.block;

import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSounds;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

public final class BOPTerracotta {
	private BOPTerracotta() {}

	public static Block<BlockLogic> HARDENED_CLAY;

	public static Block<BlockLogic> STAINED_CLAY_ORANGE;

	public static Block<BlockLogic> STAINED_CLAY_RED;

	public static void register() {
		BlockBuilder terracotta = new BlockBuilder(BetterOPlenty.MOD_ID)
			.setHardness(1.25f)
			.setResistance(7.0f)
			.setBlockSound(BlockSounds.STONE)
			.setCreativeInventoryPlacement(after(() -> Blocks.BLOCK_CLAY));

		HARDENED_CLAY = terracotta.clone()
			.setTags(BlockTags.MINEABLE_BY_PICKAXE, BlockTags.CAVES_CUT_THROUGH,
				BlockTags.CAVE_GEN_REPLACES_SURFACE)
			.build("hardened_clay", 1980, block -> new BlockLogic(block, Materials.STONE));

		BlockBuilder stained = terracotta.clone().setTags(BlockTags.MINEABLE_BY_PICKAXE);

		STAINED_CLAY_ORANGE = stained.clone()
			.build("stained_clay_orange", 1981, block -> new BlockLogic(block, Materials.STONE));

		STAINED_CLAY_RED = stained.clone()
			.build("stained_clay_red", 1982, block -> new BlockLogic(block, Materials.STONE));

		BetterOPlenty.LOGGER.info("Registered the BOP Badlands terracotta family ({} blocks: "
			+ "hardened clay and the two stained metas it bands itself with).", 3);
	}

	private static CreativeInventoryPlacement after(
			@NotNull java.util.function.Supplier<net.minecraft.core.item.IItemConvertible> neighbour) {
		return new CreativeInventoryPlacement.After(neighbour);
	}
}
