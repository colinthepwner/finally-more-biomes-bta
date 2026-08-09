package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenSand extends BOPWorldFeature {

	private final int sandID;

	private final int radius;

	public WorldGenSand(int par1, int par2) {
		radius = par1;
		sandID = par2;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		if (getBlockMaterial(par1World, par3, par4, par5) != Materials.WATER)
			return false;
		else {
			int var6 = par2Random.nextInt(radius - 2) + 2;
			byte var7 = 2;

			for (int var8 = par3 - var6; var8 <= par3 + var6; ++var8) {
				for (int var9 = par5 - var6; var9 <= par5 + var6; ++var9) {
					int var10 = var8 - par3;
					int var11 = var9 - par5;

					if (var10 * var10 + var11 * var11 <= var6 * var6) {
						for (int var12 = par4 - var7; var12 <= par4 + var7; ++var12) {
							int var13 = getBlockId(par1World, var8, var12, var9);

							if (var13 == Blocks.DIRT.id() || var13 == Blocks.GRASS.id()) {
								setBlock(par1World, var8, var12, var9, sandID);
							}
						}
					}
				}
			}

			return true;
		}
	}
}
