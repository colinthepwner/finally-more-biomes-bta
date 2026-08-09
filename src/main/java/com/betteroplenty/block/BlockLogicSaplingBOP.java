package com.betteroplenty.block;

import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicSaplingBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlockLogicSaplingBOP extends BlockLogicSaplingBase {

	@NotNull
	private final String species;

	@Nullable
	private final BOPWoodSet.TreeFeature tree;

	private boolean warnedPending;

	public BlockLogicSaplingBOP(@NotNull Block<?> block, @NotNull String species,
								@Nullable BOPWoodSet.TreeFeature tree) {
		this(block, species, tree, false);
	}

	public BlockLogicSaplingBOP(@NotNull Block<?> block, @NotNull String species,
								@Nullable BOPWoodSet.TreeFeature tree, boolean growsOnSand) {
		super(block);
		this.species = species;
		this.tree = tree;
		this.canGrowOnSand = growsOnSand;
	}

	@Override
	public void growTree(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Random random) {
		if (this.tree == null) {

			if (!this.warnedPending) {
				this.warnedPending = true;
				BetterOPlenty.LOGGER.warn("The {} sapling cannot grow yet: its tree generator is not "
					+ "ported. The block is registered so the generator can be written against it.",
					this.species);
			}
			return;
		}

		WorldFeature feature = this.tree.get(random);

		world.setBlockType(tilePos, Blocks.AIR);
		if (!feature.place(world, random, tilePos.x(), tilePos.y(), tilePos.z())) {
			world.setBlockType(tilePos, this.block);
		}
	}
}
