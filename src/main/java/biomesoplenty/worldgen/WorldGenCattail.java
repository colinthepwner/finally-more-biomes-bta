package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenCattail extends BOPWorldFeature {

	private final int cattailID;
	private final int cattailMetadata;

	public WorldGenCattail(int cattailID, int cattailMetadata) {
		this.cattailID = cattailID;
		this.cattailMetadata = cattailMetadata;
	}

	@Override
	public boolean generate(World world, Random par2Random, int par3, int par4, int par5) {
		int var11;

		while (((var11 = getBlockId(world, par3, par4, par5)) == 0 || isLeaves(var11)) && par4 > 0) {
			--par4;
		}

		for (int var7 = 0; var7 < 128; ++var7) {
			int x = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
			int y = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
			int z = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);

			if (isAirBlock(world, x, y, z) && canBlockStay(world, this.cattailID, x, y, z)) {
				if (getBlockMaterial(world, x - 1, y - 1, z) == Materials.WATER
						|| getBlockMaterial(world, x + 1, y - 1, z) == Materials.WATER
						|| getBlockMaterial(world, x, y - 1, z - 1) == Materials.WATER
						|| getBlockMaterial(world, x, y - 1, z + 1) == Materials.WATER) {
					setBlock(world, x, y, z, this.cattailID, this.cattailMetadata, 2);
				}
			}
		}

		return true;
	}
}
