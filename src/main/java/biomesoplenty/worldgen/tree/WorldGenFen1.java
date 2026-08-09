package biomesoplenty.worldgen.tree;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;

import java.util.Random;

public class WorldGenFen1 extends BOPWorldFeature {

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		final int leavesId = Blocks.LEAVES_OAK.id();
		final int logId = Blocks.LOG_OAK.id();
		final int worldHeight = par1World.getHeightBlocks();

		int var6 = par2Random.nextInt(5) + 7;
		int var7 = var6 - par2Random.nextInt(2) - 3;
		int var8 = var6 - var7;
		int var9 = 1 + par2Random.nextInt(var8 + 1);
		boolean var10 = true;

		if (par4 >= 1 && par4 + var6 + 1 <= worldHeight) {
			int var11;
			int var13;
			int var14;
			int var15;
			int var18;

			for (var11 = par4; var11 <= par4 + 1 + var6 && var10; ++var11) {
				if (var11 - par4 < var7) {
					var18 = 0;
				} else {
					var18 = var9;
				}

				for (var13 = par3 - var18; var13 <= par3 + var18 && var10; ++var13) {
					for (var14 = par5 - var18; var14 <= par5 + var18 && var10; ++var14) {
						if (var11 >= 0 && var11 < worldHeight) {
							var15 = getBlockId(par1World, var13, var11, var14);

							if (var15 != 0 && !isLeaves(var15)) {
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
					var18 = 0;

					for (var13 = par4 + var6; var13 >= par4 + var7; --var13) {
						for (var14 = par3 - var18; var14 <= par3 + var18; ++var14) {
							var15 = var14 - par3;

							for (int var16 = par5 - var18; var16 <= par5 + var18; ++var16) {
								int var17 = var16 - par5;

								if ((Math.abs(var15) != var18 || Math.abs(var17) != var18 || var18 <= 0)
										&& WorldFeatureTree.canLeavesReplace(par1World, var14, var13, var16)) {
									setBlock(par1World, var14, var13, var16, leavesId);
								}
							}
						}

						if (var18 >= 1 && var13 == par4 + var7 + 1) {
							--var18;
						} else if (var18 < var9) {
							++var18;
						}
					}

					for (var13 = 0; var13 < var6 - 1; ++var13) {

						if (isTrunkPath(par1World, par3, par4 + var13, par5)) {
							setBlock(par1World, par3, par4 + var13, par5, logId);
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
