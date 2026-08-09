package biomesoplenty.worldgen.tree;

import com.betteroplenty.block.BOPWoodSets;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenMangrove extends BOPWorldFeature {

	private final int minTreeHeight;

	public WorldGenMangrove(boolean par1) {
		this(par1, 4, 0, 0, false);
	}

	public WorldGenMangrove(boolean par1, int par2, int par3, int par4, boolean par5) {
		minTreeHeight = par2;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		final int leavesId = BOPWoodSets.MANGROVE.leaves.id();
		final int logId = BOPWoodSets.MANGROVE.log.id();
		final int worldHeight = par1World.getHeightBlocks();
		final int sand = Blocks.SAND.id();
		final int waterStill = Blocks.FLUID_WATER_STILL.id();
		final int waterMoving = Blocks.FLUID_WATER_FLOWING.id();

		int var6 = par2Random.nextInt(3) + minTreeHeight;
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

							if (var12 != 0 && var12 != leavesId && var12 != sand && var12 != Blocks.GRASS.id() && var12 != Blocks.DIRT.id() && var12 != logId) {
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

				if ((var8 == sand || var8 == waterStill || var8 == waterMoving) && par4 < worldHeight - var6 - 1) {
					var9 = 1;
					byte var18 = 0;
					int var13;
					int var14;
					int var15;

					for (var11 = par4 - var9 + var6; var11 <= par4 + var6; ++var11) {
						var12 = var11 - (par4 + var6);
						var13 = var18 + 1 - var12;

						for (var14 = par3 - var13; var14 <= par3 + var13; ++var14) {
							var15 = var14 - par3;

							for (int var16 = par5 - var13; var16 <= par5 + var13; ++var16) {
								int var17 = var16 - par5;

								if ((Math.abs(var15) != var13 || Math.abs(var17) != var13 || par2Random.nextInt(2) != 0 && var12 != 0) && !isOpaqueCube(par1World, var14, var11, var16)) {

									setBlockAndMetadata(par1World, var14, var11, var16, leavesId, 0);
									setBlockAndMetadata(par1World, var14, var11 - 1, var16, leavesId, 0);

									setBlockAndMetadata(par1World, par3 + 1, (par4 + var6) - 3, par5, leavesId, 0);
									setBlockAndMetadata(par1World, par3 - 1, (par4 + var6) - 3, par5, leavesId, 0);
									setBlockAndMetadata(par1World, par3, (par4 + var6) - 3, par5 + 1, leavesId, 0);
									setBlockAndMetadata(par1World, par3, (par4 + var6) - 3, par5 - 1, leavesId, 0);
								}
							}
						}
					}

					for (var11 = 0; var11 < var6; ++var11) {

						if (isTrunkPath(par1World, par3, par4 + var11, par5, leavesId)) {

							setBlockAndMetadata(par1World, par3, par4 + var11, par5, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 - 1, par5, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 - 2, par5, logId, 0);

							setBlockAndMetadata(par1World, par3 - 1, par4, par5, logId, 0);
							setBlockAndMetadata(par1World, par3 + 1, par4, par5, logId, 0);
							setBlockAndMetadata(par1World, par3, par4, par5 - 1, logId, 0);
							setBlockAndMetadata(par1World, par3, par4, par5 + 1, logId, 0);

							setBlockAndMetadata(par1World, par3 - 1, par4 - 1, par5, logId, 0);
							setBlockAndMetadata(par1World, par3 + 1, par4 - 1, par5, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 - 1, par5 - 1, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 - 1, par5 + 1, logId, 0);

							setBlockAndMetadata(par1World, par3 - 1, par4 - 2, par5, logId, 0);
							setBlockAndMetadata(par1World, par3 + 1, par4 - 2, par5, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 - 2, par5 - 1, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 - 2, par5 + 1, logId, 0);

							setBlockAndMetadata(par1World, par3 - 2, par4 - 3, par5, logId, 0);
							setBlockAndMetadata(par1World, par3 + 2, par4 - 3, par5, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 - 3, par5 - 2, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 - 3, par5 + 2, logId, 0);

							setBlockAndMetadata(par1World, par3 - 2, par4 - 4, par5, logId, 0);
							setBlockAndMetadata(par1World, par3 + 2, par4 - 4, par5, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 - 4, par5 - 2, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 - 4, par5 + 2, logId, 0);

							setBlockAndMetadata(par1World, par3 - 3, par4 - 5, par5, logId, 0);
							setBlockAndMetadata(par1World, par3 + 3, par4 - 5, par5, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 - 5, par5 - 3, logId, 0);
							setBlockAndMetadata(par1World, par3, par4 - 5, par5 + 3, logId, 0);

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
