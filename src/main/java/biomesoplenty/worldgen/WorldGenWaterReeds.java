package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenWaterReeds extends BOPWorldFeature {

	private final int reedID;
	private final int reedMetadata;

	public WorldGenWaterReeds(int reedID, int reedMetadata) {
		this.reedID = reedID;
		this.reedMetadata = reedMetadata;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		for (int var6 = 0; var6 < 64; ++var6) {
			int var7 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
			int var8 = par4 + par2Random.nextInt(2) - par2Random.nextInt(2);
			int var9 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);

			if (isAirBlock(par1World, var7, var8, var9)
					&& getBlockId(par1World, var7, var8 - 1, var9) == Blocks.FLUID_WATER_STILL.id()
					&& canBlockStay(par1World, this.reedID, var7, var8, var9)) {
				for (int var900 = 2; var900 > -2; --var900) {
					int neighbour = getBlockId(par1World, var7 - var900, var8 - 1, var9 - var900);

					if (neighbour != Blocks.FLUID_WATER_STILL.id()
							&& neighbour != Blocks.FLUID_WATER_FLOWING.id()) {
						setBlock(par1World, var7, var8, var9, this.reedID, this.reedMetadata, 2);
					}
				}
			}
		}

		return true;
	}
}
