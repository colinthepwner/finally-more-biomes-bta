package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenMesa extends BOPWorldFeature {

	private final int minableBlockId;

	private final int numberOfBlocks;

	public WorldGenMesa(int par1, int par2) {
		this.minableBlockId = par1;
		this.numberOfBlocks = par2;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		float var6 = par2Random.nextFloat() * (float) Math.PI;
		double var7 = par3 + 8 + MathHelper.sin(var6) * numberOfBlocks / 8.0F;
		double var9 = par3 + 8 - MathHelper.sin(var6) * numberOfBlocks / 8.0F;
		double var11 = par5 + 8 + MathHelper.cos(var6) * numberOfBlocks / 8.0F;
		double var13 = par5 + 8 - MathHelper.cos(var6) * numberOfBlocks / 8.0F;
		double var15 = par4 + par2Random.nextInt(3) - 2;
		double var17 = par4 + par2Random.nextInt(3) - 2;

		for (int var19 = 0; var19 <= numberOfBlocks; ++var19) {
			double var20 = var7 + (var9 - var7) * var19 / numberOfBlocks;
			double var22 = var15 + (var17 - var15) * var19 / numberOfBlocks;
			double var24 = var11 + (var13 - var11) * var19 / numberOfBlocks;
			double var26 = par2Random.nextDouble() * numberOfBlocks / 16.0D;
			double var28 = (MathHelper.sin(var19 * (float) Math.PI / numberOfBlocks) + 1.0F) * var26 + 1.0D;
			double var30 = (MathHelper.sin(var19 * (float) Math.PI / numberOfBlocks) + 1.0F) * var26 + 1.0D;
			int var32 = MathHelper.floor(var20 - var28 / 2.0D);
			int var33 = MathHelper.floor(var22 - var30 / 2.0D);
			int var34 = MathHelper.floor(var24 - var28 / 2.0D);
			int var35 = MathHelper.floor(var20 + var28 / 2.0D);
			int var36 = MathHelper.floor(var22 + var30 / 2.0D);
			int var37 = MathHelper.floor(var24 + var28 / 2.0D);

			for (int var38 = var32; var38 <= var35; ++var38) {
				double var39 = (var38 + 0.5D - var20) / (var28 / 2.0D);

				if (var39 * var39 < 1.0D) {
					for (int var41 = var33; var41 <= var36; ++var41) {
						double var42 = (var41 + 0.5D - var22) / (var30 / 2.0D);

						if (var39 * var39 + var42 * var42 < 1.0D) {
							for (int var44 = var34; var44 <= var37; ++var44) {
								double var45 = (var44 + 0.5D - var24) / (var28 / 2.0D);

								if (var39 * var39 + var42 * var42 + var45 * var45 < 1.0D
									&& getBlockId(par1World, var38, var41, var44) == BOPBlocks.RED_ROCK.id()
									&& getBlockMetadata(par1World, var38, var41, var44) == 0) {
									setBlock(par1World, var38, var41, var44, this.minableBlockId);
								}
							}
						}
					}
				}
			}
		}

		return true;
	}
}
