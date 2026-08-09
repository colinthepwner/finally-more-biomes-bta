package biomesoplenty.worldgen.tree;

import com.betteroplenty.block.BOPJungle;
import com.betteroplenty.block.BOPWoodSets;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenBambooTree extends BOPWorldFeature {

	private final int minTreeHeight;

	public WorldGenBambooTree(boolean par1) {
		this(par1, 10, 0, 0, false);
	}

	public WorldGenBambooTree(boolean par1, int par2, int par3, int par4, boolean par5) {
		minTreeHeight = par2;

	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		final int leavesId = BOPWoodSets.BAMBOO.leaves.id();
		final int bambooId = BOPJungle.BAMBOO.id();
		final int worldHeight = par1World.getHeightBlocks();

		int var6 = par2Random.nextInt(12) + minTreeHeight;
		boolean var7 = true;

		if (par4 >= 1 && par4 + var6 + 1 <= worldHeight) {
			int var8;
			byte var9;
			int var11;
			int var12;

			for (var8 = par4; var8 <= par4 + 1 + var6; ++var8) {
				var9 = 1;

				if (var8 == par4) {
					var9 = 0;
				}

				if (var8 >= par4 + 1 + var6 - 2) {
					var9 = 2;
				}

				for (int var10 = par3 - var9; var10 <= par3 + var9 && var7; ++var10) {
					for (var11 = par5 - var9; var11 <= par5 + var9 && var7; ++var11) {
						if (var8 >= 0 && var8 < worldHeight) {
							var12 = getBlockId(par1World, var10, var8, var11);

							if (var12 != 0 && var12 != leavesId && var12 != Blocks.GRASS.id()
									&& var12 != Blocks.DIRT.id() && var12 != bambooId) {
								var7 = false;
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

				if (Blocks.hasTag(var8, BlockTags.GROWS_TREES) && par4 < worldHeight - var6 - 1) {
					var9 = 3;
					byte var18 = 0;
					int var13;
					int var14;
					int var15;

					for (var11 = par4 - var9 + var6; var11 <= par4 + var6; ++var11) {
						var12 = var11 - (par4 + var6);
						var13 = var18 + 1 - var12 / 3;

						for (var14 = par3 - var13; var14 <= par3 + var13; ++var14) {
							var15 = var14 - par3;

							for (int var16 = par5 - var13; var16 <= par5 + var13; ++var16) {
								int var17 = var16 - par5;

								if ((Math.abs(var15) != var13 || Math.abs(var17) != var13
										|| par2Random.nextInt(2) != 0 && var12 != 0)
										&& !isOpaqueCube(par1World, var14, var11, var16)) {
									setBlock(par1World, var14, var11, var16, leavesId);
								}
							}
						}
					}

					for (var11 = 0; var11 < var6; ++var11) {
						if (isTrunkPath(par1World, par3, par4 + var11, par5, leavesId)) {
							setBlock(par1World, par3, par4 + var11, par5, bambooId);

							setBlock(par1World, par3 - 1, par4 + (var6 - 4), par5, leavesId);
							setBlock(par1World, par3 + 1, par4 + (var6 - 4), par5, leavesId);
							setBlock(par1World, par3, par4 + (var6 - 4), par5 - 1, leavesId);
							setBlock(par1World, par3, par4 + (var6 - 4), par5 + 1, leavesId);

							setBlock(par1World, par3 - 1, par4 + (var6 - 5), par5 + 1, leavesId);
							setBlock(par1World, par3 - 1, par4 + (var6 - 5), par5 - 1, leavesId);
							setBlock(par1World, par3 + 1, par4 + (var6 - 5), par5 + 1, leavesId);
							setBlock(par1World, par3 + 1, par4 + (var6 - 5), par5 - 1, leavesId);
							setBlock(par1World, par3 - 1, par4 + (var6 - 5), par5, leavesId);
							setBlock(par1World, par3 + 1, par4 + (var6 - 5), par5, leavesId);
							setBlock(par1World, par3, par4 + (var6 - 5), par5 - 1, leavesId);
							setBlock(par1World, par3, par4 + (var6 - 5), par5 + 1, leavesId);

							setBlock(par1World, par3 - 1, par4 + (var6 - 6), par5, leavesId);
							setBlock(par1World, par3 + 1, par4 + (var6 - 6), par5, leavesId);
							setBlock(par1World, par3, par4 + (var6 - 6), par5 - 1, leavesId);
							setBlock(par1World, par3, par4 + (var6 - 6), par5 + 1, leavesId);

							setBlock(par1World, par3 - 1, par4 + (var6 - 7), par5 + 1, leavesId);
							setBlock(par1World, par3 - 1, par4 + (var6 - 7), par5 - 1, leavesId);
							setBlock(par1World, par3 + 1, par4 + (var6 - 7), par5 + 1, leavesId);
							setBlock(par1World, par3 + 1, par4 + (var6 - 7), par5 - 1, leavesId);
							setBlock(par1World, par3 - 1, par4 + (var6 - 7), par5, leavesId);
							setBlock(par1World, par3 + 1, par4 + (var6 - 7), par5, leavesId);
							setBlock(par1World, par3, par4 + (var6 - 7), par5 - 1, leavesId);
							setBlock(par1World, par3, par4 + (var6 - 7), par5 + 1, leavesId);

							setBlock(par1World, par3 - 1, par4 + (var6 - 8), par5, leavesId);
							setBlock(par1World, par3 + 1, par4 + (var6 - 8), par5, leavesId);
							setBlock(par1World, par3, par4 + (var6 - 8), par5 - 1, leavesId);
							setBlock(par1World, par3, par4 + (var6 - 8), par5 + 1, leavesId);

						}
					}

					return true;
				} else
					return false;
			}
		} else
			return false;
	}
}
