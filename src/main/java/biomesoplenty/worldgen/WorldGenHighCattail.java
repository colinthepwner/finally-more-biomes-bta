package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenHighCattail extends BOPWorldFeature {

	private final int highCattailID;
	private final int highCattailMetadata;

	private final int bottomID;
	private final int bottomMetadata;

	public WorldGenHighCattail(int par1, int par2, int bottomID, int bottomMetadata) {
		highCattailID = par1;
		highCattailMetadata = par2;
		this.bottomID = bottomID;
		this.bottomMetadata = bottomMetadata;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		int var11;

		while (((var11 = getBlockId(par1World, par3, par4, par5)) == 0 || isLeaves(var11)) && par4 > 0) {
			--par4;
		}

		for (int var7 = 0; var7 < 128; ++var7) {
			int var8 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
			int var9 = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
			int var10 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);

			if (isAirBlock(par1World, var8, var9, var10)
					&& canBlockStay(par1World, this.bottomID, var8, var9, var10)) {
				if (getBlockMaterial(par1World, var8 - 1, var9 - 1, var10) == Materials.WATER
						|| getBlockMaterial(par1World, var8 + 1, var9 - 1, var10) == Materials.WATER
						|| getBlockMaterial(par1World, var8, var9 - 1, var10 - 1) == Materials.WATER
						|| getBlockMaterial(par1World, var8, var9 - 1, var10 + 1) == Materials.WATER) {
					setBlock(par1World, var8, var9, var10, this.bottomID, this.bottomMetadata, 2);
					setBlock(par1World, var8, var9 + 1, var10, this.highCattailID, this.highCattailMetadata, 2);
				}
			}
		}

		return true;
	}
}
