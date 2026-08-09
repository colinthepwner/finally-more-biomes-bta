package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenAlgae extends BOPWorldFeature {

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		for (int var6 = 0; var6 < 80; ++var6) {
			int var7 = par3 + par2Random.nextInt(4) - par2Random.nextInt(4);
			int var8 = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
			int var9 = par5 + par2Random.nextInt(4) - par2Random.nextInt(4);

			if (isAirBlock(par1World, var7, var8, var9)
					&& getBlockId(par1World, var7, var8 - 1, var9) == Blocks.FLUID_WATER_STILL.id()) {
				setBlock(par1World, var7, var8, var9, Blocks.ALGAE.id());
			}
		}

		return true;
	}
}
