package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenMarsh extends BOPWorldFeature {

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		final int grassId = Blocks.GRASS.id();
		final int dirtId = Blocks.DIRT.id();
		final int waterStill = Blocks.FLUID_WATER_STILL.id();

		final int topWater = par1World.getWorldType().getOceanY() - 1;

		int var6 = par3;
		int var89;

		for (int var7 = par5; par4 < topWater; ++par4) {
			var89 = getBlockId(par1World, par3, par4 - 1, par5);

			if ((var89 == waterStill) && par4 < par1World.getHeightBlocks() - 2 - 1) {
				for (int var8 = 2; var8 <= 5; ++var8) {
					setBlock(par1World, par3, par4, par5, grassId);
					setBlock(par1World, par3 - 1, par4, par5, grassId);
					setBlock(par1World, par3 + 1, par4, par5, grassId);
					setBlock(par1World, par3, par4, par5 - 1, grassId);
					setBlock(par1World, par3, par4, par5 + 1, grassId);

					for (int var9 = 1; var9 <= 17; ++var9) {
						setBlock(par1World, par3, par4 - var9, par5, dirtId);
					}

					for (int var9 = 1; var9 <= 16; ++var9) {
						setBlock(par1World, par3 - 1, par4 - var9, par5, dirtId);
						setBlock(par1World, par3 + 1, par4 - var9, par5, dirtId);
						setBlock(par1World, par3, par4 - var9, par5 - 1, dirtId);
						setBlock(par1World, par3, par4 - var9, par5 + 1, dirtId);
					}

					if (par2Random.nextInt(3) == 0) {

						setBlock(par1World, par3, par4 + 1, par5, BOPPlants.HIGH_GRASS.id());
						setBlock(par1World, par3, par4 + 2, par5, BOPPlants.HIGH_GRASS_TOP.id());
						setBlock(par1World, par3 - 1, par4 + 1, par5, BOPPlants.HIGH_GRASS.id());
						setBlock(par1World, par3 - 1, par4 + 2, par5, BOPPlants.HIGH_GRASS_TOP.id());
						setBlock(par1World, par3 + 1, par4 + 1, par5, BOPPlants.HIGH_GRASS.id());
						setBlock(par1World, par3 + 1, par4 + 2, par5, BOPPlants.HIGH_GRASS_TOP.id());
						setBlock(par1World, par3, par4 + 1, par5 - 1, BOPPlants.HIGH_GRASS.id());
						setBlock(par1World, par3, par4 + 2, par5 - 1, BOPPlants.HIGH_GRASS_TOP.id());
						setBlock(par1World, par3, par4 + 1, par5 + 1, BOPPlants.HIGH_GRASS.id());
						setBlock(par1World, par3, par4 + 2, par5 + 1, BOPPlants.HIGH_GRASS_TOP.id());
					} else {

						setBlock(par1World, par3, par4 + 1, par5, Blocks.TALLGRASS.id());
						setBlock(par1World, par3 - 1, par4 + 1, par5, Blocks.TALLGRASS.id());
						setBlock(par1World, par3 + 1, par4 + 1, par5, Blocks.TALLGRASS.id());
						setBlock(par1World, par3, par4 + 1, par5 - 1, Blocks.TALLGRASS.id());
						setBlock(par1World, par3, par4 + 1, par5 + 1, Blocks.TALLGRASS.id());
					}

					break;
				}
			} else {

				par3 = var6 + par2Random.nextInt(4) - par2Random.nextInt(4);
				par5 = var7 + par2Random.nextInt(4) - par2Random.nextInt(4);
			}
		}

		return true;
	}
}
