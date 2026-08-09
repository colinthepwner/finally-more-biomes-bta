package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPNether;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenNetherWart extends BOPWorldFeature {

	private final int tallGrassID;
	private final int tallGrassMetadata;

	public WorldGenNetherWart(int par1, int par2) {
		this.tallGrassID = par1;
		this.tallGrassMetadata = par2;
	}

	public WorldGenNetherWart() {
		this(0, 0);
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
			int var999 = par2Random.nextInt(16);

			if (isAirBlock(par1World, var8, var9, var10)
				&& getBlockId(par1World, var8, var9 - 1, var10) == Blocks.NETHERRACK.id()) {

				setBlock(par1World, var8, var9 - 1, var10, Blocks.SOULSAND.id(), 0, 2);

				if (var999 >= 6) {
					setBlock(par1World, var8, var9, var10, BOPNether.WITHER_WART.id(), 0, 2);
				}
			}
		}

		return true;
	}
}
