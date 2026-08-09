package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenReedBOP extends BOPWorldFeature {

	private final int caneID;
	private final int caneMetadata;

	public WorldGenReedBOP(int caneID, int caneMetadata) {
		this.caneID = caneID;
		this.caneMetadata = caneMetadata;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		for (int l = 0; l < 30; ++l) {
			int i1 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
			int j1 = par4;
			int k1 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);

			if (isAirBlock(par1World, i1, par4, k1)) {
				int l1 = 1 + par2Random.nextInt(par2Random.nextInt(3) + 1);

				if (canBlockStay(par1World, this.caneID, i1, j1, k1)) {
					for (int i2 = 0; i2 < l1; ++i2) {
						setBlock(par1World, i1, j1 + i2, k1, this.caneID, this.caneMetadata, 2);
					}
				}
			}
		}

		return true;
	}
}
