package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public final class BOPSoils {
	private BOPSoils() {}

	public static final Predicate<Block<?>> GROWS_FLOWERS = block -> block.hasTag(BlockTags.GROWS_FLOWERS);

	public static final Predicate<Block<?>> ARID =
		block -> (block.hasTag(BlockTags.GROWS_CACTI) && block != Blocks.CACTUS)
			|| block == BOPBlocks.HARD_DIRT || block == BOPBlocks.RED_ROCK;

	public static final Predicate<Block<?>> STONE =
		block -> block == Blocks.STONE || block == Blocks.BASALT
			|| block == Blocks.LIMESTONE || block == Blocks.GRANITE;

	public static final Predicate<Block<?>> NETHERRACK = block -> block == Blocks.NETHERRACK;

	public static final Predicate<Block<?>> MYCELIUM = block -> block == BOPJungle.MYCELIUM;

	public static final Predicate<Block<?>> MUSHROOM_FIELD = GROWS_FLOWERS.or(MYCELIUM);

	public static final Predicate<Block<?>> MUSHROOM_CAVE =
		GROWS_FLOWERS.or(MYCELIUM).or(STONE).or(NETHERRACK);

	public static final Predicate<Block<?>> MUSHROOM_TOADSTOOL =
		GROWS_FLOWERS.or(MYCELIUM).or(NETHERRACK);

	public static final Predicate<Block<?>> FLOATING_LEAF = block -> block == Blocks.ALGAE;

	public static final Predicate<Block<?>> SUNFLOWER_STALK = block -> block == BOPFlowers.SUNFLOWER;
}
