package biomesoplenty.worldgen.tree;

import com.betteroplenty.block.BOPOrchard;
import com.betteroplenty.block.BlockLogicAppleLeaves;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;

import java.util.Random;

public class WorldGenApple extends BOPWorldFeature {

	@SuppressWarnings("unused")
	private final boolean notify;

	public WorldGenApple(boolean par1) {
		this.notify = par1;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		final int leavesId = BOPOrchard.APPLE_LEAVES.id();
		final int ceiling = par1World.getHeightBlocks();

		int var6 = par2Random.nextInt(3) + 5;
		boolean flag = true;

		if (par4 >= 1 && par4 + var6 + 1 <= ceiling) {
			int var8;
			int var10;
			int var11;
			int var12;

			for (var8 = par4; var8 <= par4 + 1 + var6; ++var8) {
				byte var9 = 1;

				if (var8 == par4) {
					var9 = 0;
				}

				if (var8 >= par4 + 1 + var6 - 2) {
					var9 = 2;
				}

				for (var10 = par3 - var9; var10 <= par3 + var9 && flag; ++var10) {
					for (var11 = par5 - var9; var11 <= par5 + var9 && flag; ++var11) {
						if (var8 >= 0 && var8 < ceiling) {
							var12 = getBlockId(par1World, var10, var8, var11);

							if (var12 != 0 && !isLeaves(var12)) {
								flag = false;
							}
						} else {
							flag = false;
						}
					}
				}
			}

			if (!flag) {
				return false;
			} else {
				var8 = getBlockId(par1World, par3, par4 - 1, par5);

				if (Blocks.hasTag(var8, BlockTags.GROWS_TREES) && par4 < ceiling - var6 - 1) {
					WorldFeatureTree.onTreeGrown(par1World, par3, par4 - 1, par5);
					int var16;

					for (var16 = par4 - 3 + var6; var16 <= par4 + var6; ++var16) {
						var10 = var16 - (par4 + var6);
						var11 = 1 - var10 / 2;

						for (var12 = par3 - var11; var12 <= par3 + var11; ++var12) {
							int var13 = var12 - par3;

							for (int var14 = par5 - var11; var14 <= par5 + var11; ++var14) {
								int var15 = var14 - par5;

								if ((Math.abs(var13) != var11 || Math.abs(var15) != var11
									|| par2Random.nextInt(2) != 0 && var10 != 0)
									&& !isOpaqueCube(par1World, var12, var16, var14)) {

									int var99 = par2Random.nextInt(50);
									int stage = switch (var99) {
										case 44 -> 3;
										case 30, 15 -> 2;
										case 10, 5, 0 -> 1;
										default -> 0;
									};
									setBlockAndMetadata(par1World, var12, var16, var14, leavesId,
										BlockLogicAppleLeaves.setGrowthRate(0, stage));
								}
							}
						}
					}

					for (var16 = 0; var16 < var6; ++var16) {
						if (isTrunkPath(par1World, par3, par4 + var16, par5)) {
							setBlockAndMetadata(par1World, par3, par4 + var16, par5,
								Blocks.LOG_OAK.id(), 0);
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
