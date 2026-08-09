package biomesoplenty.worldgen.tree;

import com.betteroplenty.block.BOPWoodSets;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;

import java.util.Random;

public class WorldGenOminous1 extends BOPWorldFeature {

	public WorldGenOminous1(boolean par1) {
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		final int leavesId = BOPWoodSets.DARK.leaves.id();
		final int logId = BOPWoodSets.DARK.log.id();
		final int worldHeight = par1World.getHeightBlocks();

		int var6 = par2Random.nextInt(6) + 14;
		int var7 = 4 + par2Random.nextInt(3);
		int var8 = var6 - var7;
		int var9 = 2 + par2Random.nextInt(2);
		boolean var10 = true;

		if (par4 >= 1 && par4 + var6 + 1 <= worldHeight) {
			int var11;
			int var13;
			int var15;
			int var21;

			for (var11 = par4; var11 <= par4 + 1 + var6 && var10; ++var11) {
				if (var11 - par4 < var7) {
					var21 = 0;
				} else {
					var21 = var9;
				}

				for (var13 = par3 - var21; var13 <= par3 + var21 && var10; ++var13) {
					for (int var14 = par5 - var21; var14 <= par5 + var21 && var10; ++var14) {
						if (var11 >= 0 && var11 < worldHeight) {
							var15 = getBlockId(par1World, var13, var11, var14);

							if (var15 != 0 && var15 != leavesId) {
								var10 = false;
							}
						} else {
							var10 = false;
						}
					}
				}
			}

			if (!var10)
				return false;
			else {
				var11 = getBlockId(par1World, par3, par4 - 1, par5);

				if (Blocks.hasTag(var11, BlockTags.GROWS_TREES) && par4 < worldHeight - var6 - 1) {

					WorldFeatureTree.onTreeGrown(par1World, par3, par4, par5);
					var21 = par2Random.nextInt(2);
					var13 = 1;
					byte var22 = 0;
					int var17;
					int var16;

					for (var15 = 0; var15 <= var8; ++var15) {
						var16 = par4 + var6 - var15;

						for (var17 = par3 - var21; var17 <= par3 + var21; ++var17) {
							int var18 = var17 - par3;

							for (int var19 = par5 - var21; var19 <= par5 + var21; ++var19) {
								int var20 = var19 - par5;

								if ((Math.abs(var18) != var21 || Math.abs(var20) != var21 || var21 <= 0) && !isOpaqueCube(par1World, var17, var16, var19)) {
									setBlock(par1World, var17, var16, var19, leavesId);
								}
							}
						}

						if (var21 >= var13) {
							var21 = var22;
							var22 = 1;
							++var13;

							if (var13 > var9) {
								var13 = var9;
							}
						} else {
							++var21;
						}
					}

					var15 = par2Random.nextInt(3);

					for (var16 = 0; var16 < var6 - var15; ++var16) {
						if (isTrunkPath(par1World, par3, par4 + var16, par5, leavesId)) {
							setBlock(par1World, par3, par4 + var16, par5, logId);
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
