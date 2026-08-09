package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenBOPBush extends BOPWorldFeature {

	private final int plantBlockId;
	private final int plantBlockMeta;

	public WorldGenBOPBush(int par1, int meta) {
		plantBlockId = par1;
		plantBlockMeta = meta;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		for (int var6 = 0; var6 < 64; ++var6) {
			int x = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
			int y = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
			int z = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);

			if (isAirBlock(par1World, x, y, z) && canPlaceAt(par1World, plantBlockId, x, y, z)) {
				setBlock(par1World, x, y, z, plantBlockId, plantBlockMeta, 2);
			}
		}

		return true;
	}
}
