package biomesoplenty.worldgen.tree;

import com.betteroplenty.block.BOPWoodSets;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenMangrove2 extends BOPWorldFeature {

	public WorldGenMangrove2(int par1, int par2) {
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		final int leavesId = BOPWoodSets.MANGROVE.leaves.id();
		final int logId = BOPWoodSets.MANGROVE.log.id();

		int var15;

		for (boolean var6 = false; ((var15 = getBlockId(par1World, par3, par4, par5)) == 0 || var15 == leavesId) && par4 > 0; --par4) {
			;
		}

		int var7 = getBlockId(par1World, par3, par4, par5);

		if (var7 == Blocks.SAND.id()) {
			++par4;
			setBlock(par1World, par3, par4, par5, logId);

			for (int var8 = par4; var8 <= par4 + 1; ++var8) {
				int var9 = var8 - par4;
				int var10 = 2 - var9;

				for (int var11 = par3 - var10; var11 <= par3 + var10; ++var11) {
					int var12 = var11 - par3;

					for (int var13 = par5 - var10; var13 <= par5 + var10; ++var13) {
						int var14 = var13 - par5;

						if ((Math.abs(var12) != var10 || Math.abs(var14) != var10 || par2Random.nextInt(2) != 0) && !isOpaqueCube(par1World, var11, var8, var13)) {
							setBlock(par1World, var11, var8, var13, leavesId);
						}
					}
				}
			}
		}

		return true;
	}
}
