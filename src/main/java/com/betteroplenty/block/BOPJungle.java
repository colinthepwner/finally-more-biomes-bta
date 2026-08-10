package com.betteroplenty.block;

import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.Items;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSounds;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

public final class BOPJungle {
	private BOPJungle() {}

	public static Block<BlockLogic> MYCELIUM;

	public static Block<BlockLogicBamboo> BAMBOO;

	public static Block<BlockLogic> THATCHING;

	public static void register() {
		BlockBuilder builder = new BlockBuilder(BetterOPlenty.MOD_ID);

		MYCELIUM = builder.clone()
			.setHardness(0.6f)
			.setBlockSound(BlockSounds.GRASS)
			.setTags(BlockTags.MINEABLE_BY_SHOVEL, BlockTags.PASSIVE_MOBS_SPAWN,
				BlockTags.CAVES_CUT_THROUGH, BlockTags.CAVE_GEN_REPLACES_SURFACE)
			.setCreativeInventoryPlacement(after(() -> Blocks.GRASS))
			.build("mycelium", 2005, block -> new BlockLogicMycelium(block, Blocks.DIRT));

		BAMBOO = builder.clone()
			.setHardness(0.2f)
			.setBlockSound(BlockSounds.WOOD)
			.setFlammability(5, 5)
			.setTags(BlockTags.MINEABLE_BY_AXE, BlockTags.BROKEN_BY_FLUIDS)

			.setCreativeInventoryPlacement(after(() -> Items.SUGARCANE))
			.build("bamboo", 2006, BlockLogicBamboo::new);

		THATCHING = builder.clone()
			.setHardness(2.0f)
			.setResistance(5.0f)
			.setBlockSound(BlockSounds.WOOD)
			.setFlammability(5, 20)
			.setTags(BlockTags.MINEABLE_BY_AXE, BlockTags.FENCES_CONNECT)
			.setCreativeInventoryPlacement(after(() -> Items.SUGARCANE))
			.build("thatching", 2009, block -> new BlockLogic(block, Materials.WOOD));

		BetterOPlenty.LOGGER.info(
			"Registered {} BOP jungle/tropics blocks (mycelium, bamboo, bamboo thatching).", 3);
	}

	private static CreativeInventoryPlacement after(
			@NotNull java.util.function.Supplier<net.minecraft.core.item.IItemConvertible> neighbour) {
		return new CreativeInventoryPlacement.After(neighbour);
	}
}
