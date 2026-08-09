package biomesoplenty.worldgen.tree;

import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.block.BOPWoodSets;
import com.betteroplenty.block.BlockLogicBOPHangingMoss;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import net.minecraft.core.world.pos.TilePos;

import java.util.Random;

public class WorldGenBayou2 extends BOPWorldFeature {

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		final int leavesId = BOPWoodSets.WILLOW.leaves.id();
		final int logId = BOPWoodSets.WILLOW.log.id();
		final int waterStill = Blocks.FLUID_WATER_STILL.id();
		final int waterMoving = Blocks.FLUID_WATER_FLOWING.id();
		final int grassId = Blocks.GRASS.id();
		final int worldHeight = par1World.getHeightBlocks();

		int var6;

		for (var6 = par2Random.nextInt(5) + 8; getBlockMaterial(par1World, par3, par4 - 1, par5) == Materials.WATER; --par4) {
			;
		}

		boolean var7 = true;

		if (par4 >= 1 && par4 + var6 + 1 <= worldHeight) {
			int var8;
			int var81;
			int var82;
			int var83;
			int var84;
			int var10;
			int var11;
			int var12;

			for (var8 = par4; var8 <= par4 + 1 + var6; ++var8) {
				byte var9 = 1;

				if (var8 == par4) {
					var9 = 0;
				}

				if (var8 >= par4 + 1 + var6 - 2) {
					var9 = 3;
				}

				for (var10 = par3 - var9; var10 <= par3 + var9 && var7; ++var10) {
					for (var11 = par5 - var9; var11 <= par5 + var9 && var7; ++var11) {
						if (var8 >= 0 && var8 < worldHeight) {
							var12 = getBlockId(par1World, var10, var8, var11);

							if (var12 != 0 && var12 != leavesId) {

								if (var12 != waterStill && var12 != waterMoving) {
									var7 = false;
								} else if (var8 > par4) {
									var7 = false;
								}
							}
						} else {
							var7 = false;
						}
					}
				}
			}

			if (!var7)
				return false;
			else {
				var8 = getBlockId(par1World, par3, par4 - 1, par5);
				var81 = getBlockId(par1World, par3 + 1, par4 - 1, par5);
				var82 = getBlockId(par1World, par3, par4 - 1, par5 - 1);
				var83 = getBlockId(par1World, par3, par4 - 1, par5 + 1);

				var84 = getBlockId(par1World, par3 - 1, par4 - 1, par5 - 1);

				if (var81 != grassId)
					return false;

				if (var82 != grassId)
					return false;

				if (var83 != grassId)
					return false;

				if (var84 != grassId)
					return false;

				if (Blocks.hasTag(var8, BlockTags.GROWS_TREES) && par4 < worldHeight - var6 - 1) {

					WorldFeatureTree.onTreeGrown(par1World, par3, par4, par5);

					for (int course = 0; course <= 1; ++course) {
						setBlockAndMetadata(par1World, par3 - 1, par4 + course, par5, logId, 0);
						setBlockAndMetadata(par1World, par3 + 1, par4 + course, par5, logId, 0);
						setBlockAndMetadata(par1World, par3, par4 + course, par5 - 1, logId, 0);
						setBlockAndMetadata(par1World, par3, par4 + course, par5 + 1, logId, 0);
					}

					int var13;
					int var16;

					for (var16 = par4 - 3 + var6; var16 <= par4 + var6; ++var16) {
						var10 = var16 - (par4 + var6);
						var11 = 2 - var10 / 2;

						for (var12 = par3 - var11; var12 <= par3 + var11; ++var12) {
							var13 = var12 - par3;

							for (int var14 = par5 - var11; var14 <= par5 + var11; ++var14) {
								int var15 = var14 - par5;

								if ((Math.abs(var13) != var11 || Math.abs(var15) != var11 || par2Random.nextInt(2) != 0 && var10 != 0) && !isOpaqueCube(par1World, var12, var16, var14)) {
									setBlockAndMetadata(par1World, var12, var16, var14, leavesId, 0);
								}
							}
						}
					}

					for (var16 = 0; var16 < var6; ++var16) {
						var10 = getBlockId(par1World, par3, par4 + var16, par5);

						if (isTrunkPath(par1World, par3, par4 + var16, par5, leavesId) || var10 == waterMoving || var10 == waterStill) {
							setBlockAndMetadata(par1World, par3, par4 + var16, par5, logId, 0);
						}
					}

					for (var16 = par4 - 3 + var6; var16 <= par4 + var6; ++var16) {
						var10 = var16 - (par4 + var6);
						var11 = 2 - var10 / 2;

						for (var12 = par3 - var11; var12 <= par3 + var11; ++var12) {
							for (var13 = par5 - var11; var13 <= par5 + var11; ++var13) {
								if (getBlockId(par1World, var12, var16, var13) == leavesId) {
									if (par2Random.nextInt(4) == 0 && getBlockId(par1World, var12 - 1, var16, var13) == 0) {

										this.generateVines(par1World, var12 - 1, var16, var13, Side.WEST);
									}

									if (par2Random.nextInt(4) == 0 && getBlockId(par1World, var12 + 1, var16, var13) == 0) {
										this.generateVines(par1World, var12 + 1, var16, var13, Side.EAST);
									}

									if (par2Random.nextInt(4) == 0 && getBlockId(par1World, var12, var16, var13 - 1) == 0) {
										this.generateVines(par1World, var12, var16, var13 - 1, Side.NORTH);
									}

									if (par2Random.nextInt(4) == 0 && getBlockId(par1World, var12, var16, var13 + 1) == 0) {
										this.generateVines(par1World, var12, var16, var13 + 1, Side.SOUTH);
									}
								}
							}
						}
					}

					return true;
				} else
					return false;
			}
		} else
			return false;
	}

	private void generateVines(World par1World, int par2, int par3, int par4, Side par5) {
		final Block<?> drape = BOPPlants.WILLOW_DRAPE;
		final int facing = BlockLogicBOPHangingMoss.attachmentData(par5);
		int var6 = 4;

		while (true) {
			if (!drape.getLogic().canPlaceOnSide(par1World, new TilePos(par2, par3, par4), par5))
				return;

			setBlockAndMetadata(par1World, par2, par3, par4, drape.id(), facing);
			--par3;

			if (getBlockId(par1World, par2, par3, par4) != 0 || var6 <= 0)
				return;

			--var6;
		}
	}
}
