package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;

import java.util.Random;

public class WorldGenCypress1 extends BOPWorldFeature {

	public WorldGenCypress1(boolean var1) {
	}

	@Override
	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		final int leavesId = Blocks.LEAVES_OAK.id();
		final int logId = Blocks.LOG_OAK.id();
		final int worldHeight = var1.getHeightBlocks();

		int var6 = var2.nextInt(5) + 8;
		int var7 = var2.nextInt(3) + 2;
		int var8 = var6 - var7;
		int var9 = 1;
		boolean var10 = true;

		if (var4 >= 1 && var4 + var6 + 1 <= worldHeight) {
			int var11;
			int var13;
			int var15;
			int var21;

			for (var11 = var4; var11 <= var4 + 1 + var6 && var10; ++var11) {
				if (var11 - var4 < var7) {
					var21 = 0;
				} else {
					var21 = var9;
				}

				for (var13 = var3 - var21; var13 <= var3 + var21 && var10; ++var13) {
					for (int var14 = var5 - var21; var14 <= var5 + var21 && var10; ++var14) {
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

				if (Blocks.hasTag(var11, BlockTags.GROWS_TREES) && var4 < worldHeight - var6 - 1) {

					WorldFeatureTree.onTreeGrown(var1, var3, var4, var5);
					var21 = var2.nextInt(2);
					var13 = 1;
					boolean var22 = false;
					int var17;
					int var16;

					for (var15 = 0; var15 <= var8; ++var15) {
						var16 = var4 + var6 - var15;

						for (var17 = var3 - var21; var17 <= var3 + var21; ++var17) {
							int var18 = var17 - var3;

							for (int var19 = var5 - var21; var19 <= var5 + var21; ++var19) {
								int var20 = var19 - var5;

								if ((Math.abs(var18) != var21 || Math.abs(var20) != var21 || var21 <= 0) && !isOpaqueCube(var1, var17, var16, var19)) {

									if (var2.nextInt(3) != 0) {
										setBlock(var1, var17, var16, var19, leavesId);
									}
								}
							}
						}

						if (var21 >= var13) {
							var21 = var22 ? 1 : 0;
							var22 = true;
							++var13;

							if (var13 > var9) {
								var13 = var9;
							}
						} else {
							++var21;
						}
					}

					var15 = var2.nextInt(3);

					for (var16 = 0; var16 < var6 - var15; ++var16) {

						if (isTrunkPath(var1, var3, var4 + var16, var5, leavesId)) {
							setBlock(var1, var3, var4 + var16, var5, logId);

							setBlock(var1, var3, (var4 + var6), var5, logId);
							setBlock(var1, var3, (var4 + var6) - 2, var5, logId);
							setBlock(var1, var3, (var4 + var6) - 1, var5, logId);
							setBlock(var1, var3, (var4 + var6), var5, logId);
							setBlock(var1, var3, (var4 + var6) + 1, var5, logId);
							setBlock(var1, var3 - 1, (var4 + var6) + 1, var5, leavesId);
							setBlock(var1, var3 + 1, (var4 + var6) + 1, var5, leavesId);
							setBlock(var1, var3, (var4 + var6) + 1, var5 - 1, leavesId);
							setBlock(var1, var3, (var4 + var6) + 1, var5 + 1, leavesId);
							setBlock(var1, var3, (var4 + var6) + 2, var5, leavesId);
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
