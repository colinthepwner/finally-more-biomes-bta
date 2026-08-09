package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenPumpkinAlt extends BOPWorldFeature {

	private final int plantBlockId;
	private final int plantBlockMeta;

	public WorldGenPumpkinAlt(int par1, int meta) {
		plantBlockId = par1;
		plantBlockMeta = meta;
	}

	private static boolean growsOn(int soilId) {
		return soilId == Blocks.GRASS.id()
			|| soilId == Blocks.DIRT.id()
			|| soilId == Blocks.FARMLAND_DIRT.id();
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		for (int l = 0; l < 64; ++l) {
			int i1 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
			int j1 = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
			int k1 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);

			if (isAirBlock(par1World, i1, j1, k1)
					&& (getFullBlockLightValue(par1World, i1, j1, k1) >= 8 || canBlockSeeTheSky(par1World, i1, j1, k1))
					&& growsOn(getBlockId(par1World, i1, j1 - 1, k1))
					&& canPlaceAt(par1World, plantBlockId, i1, j1, k1)) {

				setBlockAndMetadata(par1World, i1, j1, k1, Blocks.PUMPKIN_CARVED_IDLE.id(), par2Random.nextInt(4));
			}
		}

		return true;
	}
}
