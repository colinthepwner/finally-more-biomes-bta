package com.betteroplenty.compat;

import com.betteroplenty.block.BOPJungle;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicLeavesBase;
import net.minecraft.core.block.BlockLogicLog;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import net.minecraft.core.world.pos.TilePos;

import java.util.Random;

public abstract class BOPWorldFeature extends WorldFeature {

	public abstract boolean generate(World world, Random random, int x, int y, int z);

	@Override
	public boolean place(World world, Random random, int x, int y, int z) {
		return this.generate(world, random, x, y, z);
	}

	protected static boolean setBlock(World world, int x, int y, int z, int blockId) {
		return setBlockAndMetadata(world, x, y, z, blockId, 0);
	}

	protected static boolean setBlock(World world, int x, int y, int z, int blockId, int metadata, int flag) {
		return setBlockAndMetadata(world, x, y, z, blockId, metadata);
	}

	protected static boolean setBlockAndMetadata(World world, int x, int y, int z, int blockId, int metadata) {

		if (DecorationWindow.wouldCascade(world, x, z)
			&& DecorationWindow.deferWrite(world, x, y, z, blockId, metadata)) {
			return true;
		}
		return world.setBlockAndMetadataRaw(x, y, z, blockId, metadata);
	}

	protected static boolean isTrunkPath(World world, int x, int y, int z, int leavesId) {
		int id = getBlockId(world, x, y, z);
		return id == 0 || id == leavesId;
	}

	protected static boolean isTrunkPath(World world, int x, int y, int z) {
		int id = getBlockId(world, x, y, z);
		return id == 0 || isLeaves(id);
	}

	protected static boolean isOpaqueCube(World world, int x, int y, int z) {
		if (DecorationWindow.wouldCascade(world, x, z)) {
			return false;
		}
		Block<?> block = world.getBlockType(new TilePos(x, y, z));
		return block != null && block.isSolidRender();
	}

	protected static boolean isAirBlock(World world, int x, int y, int z) {
		if (DecorationWindow.wouldCascade(world, x, z)) {
			return true;
		}
		return world.isAirBlock(x, y, z);
	}

	protected static int getBlockId(World world, int x, int y, int z) {
		if (DecorationWindow.wouldCascade(world, x, z)) {
			return 0;
		}
		return world.getBlockId(x, y, z);
	}

	protected static int getBlockMetadata(World world, int x, int y, int z) {
		if (DecorationWindow.wouldCascade(world, x, z)) {
			return 0;
		}
		return world.getBlockData(new TilePos(x, y, z));
	}

	protected static Material getBlockMaterial(World world, int x, int y, int z) {
		if (DecorationWindow.wouldCascade(world, x, z)) {
			return Materials.AIR;
		}
		return world.getBlockMaterial(new TilePos(x, y, z));
	}

	protected static Block<?> getBlock(World world, int x, int y, int z) {
		if (DecorationWindow.wouldCascade(world, x, z)) {
			return Blocks.AIR;
		}
		return world.getBlockType(new TilePos(x, y, z));
	}

	protected static int getFullBlockLightValue(World world, int x, int y, int z) {
		if (DecorationWindow.wouldCascade(world, x, z)) {
			return 0;
		}
		return world.getFullBlockLightValue(x, y, z);
	}

	protected static boolean canBlockSeeTheSky(World world, int x, int y, int z) {
		if (DecorationWindow.wouldCascade(world, x, z)) {
			return false;
		}
		return world.canBlockSeeTheSky(x, y, z);
	}

	protected static boolean canPlaceAt(World world, int blockId, int x, int y, int z) {

		if (DecorationWindow.wouldCascade(world, x, z)) {
			return false;
		}
		Block<?> block = Blocks.getBlock(blockId);
		if (block == null) {
			return false;
		}
		if (isVanillaMushroom(blockId) && !vanillaMushroomStayRule(world, x, y, z)) {
			return false;
		}
		return block.getLogic().canPlaceAt(world, new TilePos(x, y, z));
	}

	private static boolean isVanillaMushroom(int blockId) {
		return blockId == Blocks.MUSHROOM_BROWN.id() || blockId == Blocks.MUSHROOM_RED.id();
	}

	private static boolean vanillaMushroomStayRule(World world, int x, int y, int z) {
		Block<?> below = getBlock(world, x, y - 1, z);
		if (below != null && below == BOPJungle.MYCELIUM) {
			return true;
		}
		return getFullBlockLightValue(world, x, y, z) < 13;
	}

	protected static void onTreeGrownAt(World world, int x, int y, int z) {
		WorldFeatureTree.onTreeGrown(world, x, y, z);
	}

	protected static boolean canBlockStay(World world, int blockId, int x, int y, int z) {

		if (DecorationWindow.wouldCascade(world, x, z)) {
			return false;
		}
		Block<?> block = Blocks.getBlock(blockId);
		if (block == null) {
			return false;
		}

		if (isVanillaMushroom(blockId) && !vanillaMushroomStayRule(world, x, y, z)) {
			return false;
		}
		return block.getLogic().canStay(world, new TilePos(x, y, z));
	}

	protected static boolean isLeaves(int blockId) {
		Block<?> block = Blocks.getBlock(blockId);
		return block != null && Block.hasLogicClass(block, BlockLogicLeavesBase.class);
	}

	protected static boolean isWood(int blockId) {
		Block<?> block = Blocks.getBlock(blockId);
		return block != null && Block.hasLogicClass(block, BlockLogicLog.class);
	}
}
