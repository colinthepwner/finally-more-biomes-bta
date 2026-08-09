package biomesoplenty.worldgen.tree;

import com.betteroplenty.compat.BOPWorldFeature;
import com.betteroplenty.block.BOPWoodSets;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;

import java.util.Random;

public class WorldGenSequoiaOrange extends BOPWorldFeature {

	public WorldGenSequoiaOrange(boolean var1) {
	}

	@Override
	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		final int leavesId = BOPWoodSets.ORANGE_AUTUMN.leaves.id();
		final int logId = Blocks.LOG_OAK.id();
		final int worldHeight = var1.getHeightBlocks();

		int var6 = var2.nextInt(10) + 25;
		int var7 = var2.nextInt(4) + 8;
		int var8 = var6 - var7;
		int var9 = 4;
		boolean var10 = true;

		if (var4 >= 1 && var4 + var6 + 1 <= worldHeight) {
			int var11;
			int var13;
			int var14;
			int var15;
			int var24;
			int var25;

			for (var11 = var4; var11 <= var4 + 1 + var6 && var10; ++var11) {
				if (var11 - var4 < var7) {
					var24 = 0;
				} else {
					var24 = var9;
				}

				for (var13 = var3 - var24; var13 <= var3 + var24 && var10; ++var13) {
					for (var14 = var5 - var24; var14 <= var5 + var24 && var10; ++var14) {
						if (var11 >= 0 && var11 < worldHeight) {
							var15 = getBlockId(var1, var13, var11, var14);

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
				var11 = getBlockId(var1, var3, var4 - 1, var5);
				var25 = getBlockId(var1, var3 + 1, var4 - 1, var5);
				var24 = getBlockId(var1, var3 - 1, var4 - 1, var5);
				var13 = getBlockId(var1, var3, var4 - 1, var5 + 1);
				var14 = getBlockId(var1, var3, var4 - 1, var5 - 1);

				if (Blocks.hasTag(var11, BlockTags.GROWS_TREES) && var4 < worldHeight - var6 - 1
					&& Blocks.hasTag(var24, BlockTags.GROWS_TREES)
					&& (Blocks.hasTag(var13, BlockTags.GROWS_TREES) || var24 == Blocks.DIRT.id())
					&& (Blocks.hasTag(var14, BlockTags.GROWS_TREES) || var24 == Blocks.DIRT.id())
					&& Blocks.hasTag(var25, BlockTags.GROWS_TREES)) {

					WorldFeatureTree.onTreeGrown(var1, var3, var4, var5);
					WorldFeatureTree.onTreeGrown(var1, var3 + 1, var4, var5);
					WorldFeatureTree.onTreeGrown(var1, var3 - 1, var4, var5);
					WorldFeatureTree.onTreeGrown(var1, var3, var4, var5 + 1);
					WorldFeatureTree.onTreeGrown(var1, var3, var4, var5 - 1);

					var15 = var2.nextInt(2);
					int var16 = 1;
					boolean var17 = false;
					int var19;
					int var18;
					int var20;

					for (var18 = 0; var18 <= var8; ++var18) {
						var19 = var4 + var6 - var18;

						for (var20 = var3 - var15; var20 <= var3 + var15; ++var20) {
							int var21 = var20 - var3;

							for (int var22 = var5 - var15; var22 <= var5 + var15; ++var22) {
								int var23 = var22 - var5;

								if ((Math.abs(var21) != var15 || Math.abs(var23) != var15 || var15 <= 0)
									&& !isOpaqueCube(var1, var20, var19, var22)) {
									setBlock(var1, var20, var19, var22, leavesId);
									setBlock(var1, var20 + 1, var19, var22, leavesId);
									setBlock(var1, var20 - 1, var19, var22, leavesId);
									setBlock(var1, var20, var19, var22 + 1, leavesId);
									setBlock(var1, var20, var19, var22 - 1, leavesId);

								}
							}
						}

						if (var15 >= var16) {
							var15 = var17 ? 1 : 0;
							var17 = true;
							++var16;

							if (var16 > var9) {
								var16 = var9;
							}
						} else {
							++var15;
						}
					}

					var18 = var2.nextInt(3);

					for (var19 = 0; var19 < var6 - var18; ++var19) {
						if (isTrunkPath(var1, var3, var4 + var19, var5, leavesId)) {
							setBlock(var1, var3, var4 + var19, var5, logId);
							setBlock(var1, var3 + 1, var4 + var19, var5, logId);
							setBlock(var1, var3 - 1, var4 + var19, var5, logId);
							setBlock(var1, var3, var4 + var19, var5 + 1, logId);
							setBlock(var1, var3, var4 + var19, var5 - 1, logId);
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
