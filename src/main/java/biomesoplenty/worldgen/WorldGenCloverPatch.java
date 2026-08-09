package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenCloverPatch extends BOPWorldFeature {

	private final int plantBlockId;
	private final int plantBlockMeta;

	public WorldGenCloverPatch(int par1, int meta) {
		plantBlockId = par1;
		plantBlockMeta = meta;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		for (int l = 0; l < 128; ++l) {
			int i1 = par3 + par2Random.nextInt(6) - par2Random.nextInt(6);
			int j1 = par4;
			int k1 = par5 + par2Random.nextInt(6) - par2Random.nextInt(6);

			if (isAirBlock(par1World, i1, j1, k1)
					&& (getFullBlockLightValue(par1World, i1, j1, k1) >= 8 || canBlockSeeTheSky(par1World, i1, j1, k1))
					&& canPlaceAt(par1World, plantBlockId, i1, j1, k1)) {
				setBlock(par1World, i1, j1, k1, plantBlockId, plantBlockMeta, 2);
			}
		}

		return true;
	}
}
