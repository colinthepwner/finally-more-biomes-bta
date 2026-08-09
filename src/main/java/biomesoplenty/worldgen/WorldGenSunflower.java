package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenSunflower extends BOPWorldFeature {

	private final int bottomBlockId;
	private final int topBlockId;

	public WorldGenSunflower(int bottomBlockId, int topBlockId) {
		this.bottomBlockId = bottomBlockId;
		this.topBlockId = topBlockId;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		for (int l = 0; l < 64; ++l) {
			int i1 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
			int j1 = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
			int k1 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);

			if (isAirBlock(par1World, i1, j1, k1) && isAirBlock(par1World, i1, j1 + 1, k1)
					&& (getFullBlockLightValue(par1World, i1, j1, k1) >= 8 || canBlockSeeTheSky(par1World, i1, j1, k1))
					&& canPlaceAt(par1World, bottomBlockId, i1, j1, k1)) {
				setBlock(par1World, i1, j1, k1, bottomBlockId, 0, 2);
				setBlock(par1World, i1, j1 + 1, k1, topBlockId, 0, 2);
			}
		}

		return true;
	}
}
