package com.betteroplenty.block;

import biomesoplenty.worldgen.tree.WorldGenApple;
import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

public final class BOPOrchard {
	private BOPOrchard() {}

	public static Block<BlockLogicAppleLeaves> APPLE_LEAVES;

	public static Block<BlockLogicSaplingBOP> APPLE_SAPLING;

	public static void register() {
		BlockBuilder builder = new BlockBuilder(BetterOPlenty.MOD_ID);

		APPLE_SAPLING = builder.clone()
			.setBlockSound(net.minecraft.core.sound.BlockSounds.GRASS)
			.setHardness(0.0f)
			.setTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLANTABLE_IN_JAR)
			.setCreativeInventoryPlacement(new CreativeInventoryPlacement.After(() -> Blocks.SAPLING_OAK))
			.build("apple_sapling", 1976,
				block -> new BlockLogicSaplingBOP(block, "apple", random -> new WorldGenApple(false)));

		Block<BlockLogicSaplingBOP> sapling = APPLE_SAPLING;
		APPLE_LEAVES = builder.clone()
			.setBlockSound(net.minecraft.core.sound.BlockSounds.GRASS)
			.setHardness(0.2f)
			.setLightOpacity(1)
			.setFlammability(30, 60)

			.setTags(BlockTags.SHEARS_DO_SILK_TOUCH, BlockTags.MINEABLE_BY_AXE,
				BlockTags.MINEABLE_BY_HOE, BlockTags.MINEABLE_BY_SWORD, BlockTags.MINEABLE_BY_SHEARS)
			.setCreativeInventoryPlacement(new CreativeInventoryPlacement.After(() -> Blocks.LEAVES_CACAO))
			.build("apple_leaves", 1975,
				block -> new BlockLogicAppleLeaves(block, () -> sapling));

		BetterOPlenty.LOGGER.info("Registered the BOP orchard's {} blocks (apple leaves and sapling).", 2);
	}
}
