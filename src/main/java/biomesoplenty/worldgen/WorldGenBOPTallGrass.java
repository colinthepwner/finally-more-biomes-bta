package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenBOPTallGrass extends BOPWorldFeature {

	private final int tallGrassID;
	private final int tallGrassMetadata;

	public WorldGenBOPTallGrass(int par1, int par2) {
		this.tallGrassID = par1;
		this.tallGrassMetadata = par2;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {

		int id;

		do {
			id = getBlockId(par1World, par3, par4, par5);

			if (id != 0 && !isLeaves(id)) {
				break;
			}

			par4--;
		} while (par4 > 0);

		for (int i1 = 0; i1 < 128; ++i1) {
			int j1 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
			int k1 = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
			int l1 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);

			if (isAirBlock(par1World, j1, k1, l1) && canPlaceAt(par1World, tallGrassID, j1, k1, l1)) {
				setBlock(par1World, j1, k1, l1, this.tallGrassID, this.tallGrassMetadata, 2);
			}
		}

		return true;
	}
}
