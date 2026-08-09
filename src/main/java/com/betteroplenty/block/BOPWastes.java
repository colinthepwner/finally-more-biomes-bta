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

public final class BOPWastes {
	private BOPWastes() {}

	public static Block<BlockLogicAshBOP> ASH;

	public static Block<BlockLogic> ASH_STONE;

	public static Block<BlockLogic> DRIED_DIRT;

	public static Block<BlockLogicSmolderingGrass> SMOLDERING_GRASS;

	public static void register() {
		BlockBuilder builder = new BlockBuilder(BetterOPlenty.MOD_ID);

		ASH = builder.clone()
			.setHardness(0.4f)
			.setBlockSound(BlockSounds.SAND)
			.setTags(BlockTags.MINEABLE_BY_SHOVEL, BlockTags.INFINITE_BURN,
				BlockTags.CAVES_CUT_THROUGH, BlockTags.CAVE_GEN_REPLACES_SURFACE)
			.setCreativeInventoryPlacement(after(() -> Blocks.BLOCK_ASH))
			.build("ash", 1990, BlockLogicAshBOP::new);

		ASH_STONE = builder.clone()
			.setHardness(1.0f)
			.setResistance(10.0f)
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE, BlockTags.CAVES_CUT_THROUGH,
				BlockTags.CAVE_GEN_REPLACES_SURFACE)
			.setCreativeInventoryPlacement(after(() -> Blocks.BASALT))
			.build("ash_stone", 1991, block -> new BlockLogic(block, Materials.STONE));

		DRIED_DIRT = builder.clone()
			.setHardness(0.1f)
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE, BlockTags.CAVES_CUT_THROUGH,
				BlockTags.CAVE_GEN_REPLACES_SURFACE)
			.setCreativeInventoryPlacement(after(() -> Blocks.DIRT_SCORCHED))
			.build("dried_dirt", 1992, block -> new BlockLogic(block, Materials.STONE));

		SMOLDERING_GRASS = builder.clone()
			.setHardness(0.6f)
			.setBlockSound(BlockSounds.GRASS)
			.setTags(BlockTags.MINEABLE_BY_SHOVEL, BlockTags.INFINITE_BURN)
			.setCreativeInventoryPlacement(after(() -> Blocks.DIRT_SCORCHED))
			.build("smoldering_grass", 1993, BlockLogicSmolderingGrass::new);

		BetterOPlenty.LOGGER.info("Registered {} BOP waste surfaces (the Deadlands' ash and burnt "
			+ "turf, the Volcano's ash stone, the Wasteland's dried dirt).", 4);
	}

	private static CreativeInventoryPlacement after(
			@NotNull java.util.function.Supplier<net.minecraft.core.item.IItemConvertible> neighbour) {
		return new CreativeInventoryPlacement.After(neighbour);
	}
}
