package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public final class BOPWaterloggable {
	private BOPWaterloggable() {
	}

	private static boolean isWaterloggable(Block<?> block) {
		return block != null
			&& (block == Blocks.TALLGRASS
			|| block == BOPPlants.HIGH_GRASS
			|| block == BOPPlants.HIGH_GRASS_TOP);
	}

	public static boolean isWaterloggedPlant(@NotNull WorldSource source, @NotNull TilePosc pos) {
		Block<?> here = source.getBlockType(pos);
		if (here == BOPPlants.REED) {
			return source.getBlockType(new TilePos(pos.x(), pos.y() - 1, pos.z()))
				.hasTag(BlockTags.IS_WATER);
		}
		if (!isWaterloggable(here)) {
			return false;
		}
		TilePos above = new TilePos(pos.x(), pos.y() + 1, pos.z());
		if (source.getBlockMaterial(above) == Materials.WATER) {
			return true;
		}

		if (isWaterloggable(source.getBlockType(above))) {
			return source.getBlockMaterial(new TilePos(pos.x(), pos.y() + 2, pos.z())) == Materials.WATER;
		}
		return false;
	}
}
