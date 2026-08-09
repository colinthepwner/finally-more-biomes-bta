package biomesoplenty.worldgen.tree;

import com.betteroplenty.block.BOPWoodSets;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;

import java.util.Random;

public class WorldGenRedwoodTree extends BOPWorldFeature {

	private final int minTreeHeight;

	public WorldGenRedwoodTree(boolean par1) {
		this(par1, 40, 0, 0, false);
	}

	public WorldGenRedwoodTree(boolean par1, int par2, int par3, int par4, boolean par5) {
		minTreeHeight = par2;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		final int leavesId = BOPWoodSets.REDWOOD.leaves.id();
		final int logId = BOPWoodSets.REDWOOD.log.id();
		final int grassId = Blocks.GRASS.id();
		final int worldHeight = par1World.getHeightBlocks();

		int var6 = par2Random.nextInt(10) + minTreeHeight;
		boolean var7 = true;

		if (par4 >= 1 && par4 + var6 + 1 <= worldHeight) {
			int var8;
			byte var9;
			int var11;
			int var12;
			int var81;
			int var82;
			int var83;
			int var84;
			int var85;
			int var86;
			int var87;
			int var88;
			int var89;
			int var90;
			int var91;

			for (var8 = par4; var8 <= par4 + 1 + var6; ++var8) {
				var9 = 7;

				if (var8 == par4) {
					var9 = 7;
				}

				if (var8 >= par4 + 1 + var6 - 2) {
					var9 = 8;
				}

				for (int var10 = par3 - var9; var10 <= par3 + var9 && var7; ++var10) {
					for (var11 = par5 - var9; var11 <= par5 + var9 && var7; ++var11) {
						if (var8 >= 0 && var8 < worldHeight) {
							var12 = getBlockId(par1World, var10, var8, var11);

							if (var12 != 0
								&& var12 != leavesId
								&& !Blocks.hasTag(var12, BlockTags.GROWS_TREES)
								&& var12 != logId) {
								var7 = false;
							}
						} else {
							var7 = false;
						}
					}
				}
			}

			if (!var7) {
				return false;
			} else {
				var8 = getBlockId(par1World, par3 - 1, par4 - 1, par5);
				var81 = getBlockId(par1World, par3 + 1, par4 - 1, par5);
				var82 = getBlockId(par1World, par3, par4 - 1, par5 - 1);
				var83 = getBlockId(par1World, par3, par4 - 1, par5 + 1);
				var84 = getBlockId(par1World, par3 - 1, par4 - 1, par5 - 1);
				var85 = getBlockId(par1World, par3 + 1, par4 - 1, par5 - 1);
				var86 = getBlockId(par1World, par3 - 1, par4 - 1, par5 + 1);
				var87 = getBlockId(par1World, par3 + 1, par4 - 1, par5 + 1);
				var88 = getBlockId(par1World, par3 - 2, par4 - 1, par5);
				var89 = getBlockId(par1World, par3 + 2, par4 - 1, par5);
				var90 = getBlockId(par1World, par3, par4 - 1, par5 - 2);
				var91 = getBlockId(par1World, par3, par4 - 1, par5 + 2);

				if (var81 != grassId)
					return false;

				if (var82 != grassId)
					return false;

				if (var83 != grassId)
					return false;

				if (var84 != grassId)
					return false;

				if (var85 != grassId)
					return false;

				if (var86 != grassId)
					return false;

				if (var87 != grassId)
					return false;

				if (var88 != grassId)
					return false;

				if (var89 != grassId)
					return false;

				if (var90 != grassId)
					return false;

				if (var91 != grassId)
					return false;

				if (Blocks.hasTag(var8, BlockTags.GROWS_TREES) && par4 < worldHeight - var6 - 1) {

					WorldFeatureTree.onTreeGrown(par1World, par3, par4, par5);
					var9 = 9;
					byte var18 = 0;
					int var13;
					int var14;
					int var15;

					for (var11 = par4 - var9 + var6; var11 <= par4 + var6; ++var11) {
						var12 = var11 - (par4 + var6);
						var13 = var18 + 1 - var12 / 4;

						for (var14 = par3 - var13; var14 <= par3 + var13; ++var14) {
							var15 = var14 - par3;

							for (int var16 = par5 - var13; var16 <= par5 + var13; ++var16) {
								int var17 = var16 - par5;

								if ((Math.abs(var15) != var13 || Math.abs(var17) != var13 || par2Random.nextInt(2) != 0 && var12 != 0)
									&& !isOpaqueCube(par1World, var14, var11, var16)) {

									setBlockAndMetadata(par1World, var14, var11 + 10, var16, leavesId, 0);
									setBlockAndMetadata(par1World, var14, var11 + 6, var16, leavesId, 0);
									setBlockAndMetadata(par1World, var14, var11, var16, leavesId, 0);
								}
							}
						}
					}

					for (var11 = 0; var11 < var6; ++var11) {

						if (isTrunkPath(par1World, par3, par4 + var11, par5, leavesId)) {

							setBlockAndMetadata(par1World, par3, par4 + (var6), par5, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 + (var6 + 1), par5, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 + (var6 + 2), par5, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 + (var6 + 3), par5, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 + (var6 + 4), par5, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 + (var6 + 5), par5, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 + var11, par5, logId, 0);
							setBlockAndMetadata(par1World, par3 - 1, par4 + var11, par5, logId, 0);
							setBlockAndMetadata(par1World, par3 + 1, par4 + var11, par5, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 + var11, par5 - 1, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 + var11, par5 + 1, logId, 0);

							setBlockAndMetadata(par1World, par3 - 1, par4 + (var11 / 2), par5 - 1, logId, 0);
							setBlockAndMetadata(par1World, par3 + 1, par4 + (var11 / 2), par5 - 1, logId, 0);
							setBlockAndMetadata(par1World, par3 - 1, par4 + (var11 / 2), par5 + 1, logId, 0);
							setBlockAndMetadata(par1World, par3 + 1, par4 + (var11 / 2), par5 + 1, logId, 0);

							setBlockAndMetadata(par1World, par3 - 2, par4 + (var11 / 4), par5, logId, 0);
							setBlockAndMetadata(par1World, par3 - 2, par4 + (var11 / 4), par5 - 1, logId, 0);
							setBlockAndMetadata(par1World, par3 - 2, par4 + (var11 / 4), par5 + 1, logId, 0);
							setBlockAndMetadata(par1World, par3 + 2, par4 + (var11 / 4), par5, logId, 0);
							setBlockAndMetadata(par1World, par3 + 2, par4 + (var11 / 4), par5 - 1, logId, 0);
							setBlockAndMetadata(par1World, par3 + 2, par4 + (var11 / 4), par5 + 1, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 + (var11 / 4), par5 - 2, logId, 0);
							setBlockAndMetadata(par1World, par3 - 1, par4 + (var11 / 4), par5 - 2, logId, 0);
							setBlockAndMetadata(par1World, par3 + 1, par4 + (var11 / 4), par5 - 2, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 + (var11 / 4), par5 + 2, logId, 0);
							setBlockAndMetadata(par1World, par3 - 1, par4 + (var11 / 4), par5 + 2, logId, 0);
							setBlockAndMetadata(par1World, par3 + 1, par4 + (var11 / 4), par5 + 2, logId, 0);

							setBlockAndMetadata(par1World, par3 - 2, par4 + (var11 / 6), par5 - 2, logId, 0);
							setBlockAndMetadata(par1World, par3 + 2, par4 + (var11 / 6), par5 + 2, logId, 0);
							setBlockAndMetadata(par1World, par3 + 2, par4 + (var11 / 6), par5 - 2, logId, 0);
							setBlockAndMetadata(par1World, par3 - 2, par4 + (var11 / 6), par5 + 2, logId, 0);
							setBlockAndMetadata(par1World, par3 - 3, par4 + (var11 / 6), par5, logId, 0);
							setBlockAndMetadata(par1World, par3 + 3, par4 + (var11 / 6), par5, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 + (var11 / 6), par5 - 3, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 + (var11 / 6), par5 + 3, logId, 0);

							setBlockAndMetadata(par1World, par3 - 3, par4 + (var11 / 8), par5 - 1, logId, 0);
							setBlockAndMetadata(par1World, par3 - 3, par4 + (var11 / 8), par5 + 1, logId, 0);
							setBlockAndMetadata(par1World, par3 + 3, par4 + (var11 / 8), par5 - 1, logId, 0);
							setBlockAndMetadata(par1World, par3 + 3, par4 + (var11 / 8), par5 + 1, logId, 0);
							setBlockAndMetadata(par1World, par3 - 1, par4 + (var11 / 8), par5 - 3, logId, 0);
							setBlockAndMetadata(par1World, par3 + 1, par4 + (var11 / 8), par5 - 3, logId, 0);
							setBlockAndMetadata(par1World, par3 - 1, par4 + (var11 / 8), par5 + 3, logId, 0);
							setBlockAndMetadata(par1World, par3 + 1, par4 + (var11 / 8), par5 + 3, logId, 0);

						}
					}

					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
}
