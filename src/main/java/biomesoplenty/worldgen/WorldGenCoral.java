package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPCorals;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenCoral extends BOPWorldFeature {

	public WorldGenCoral() {
		super();
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		for (int l = 0; l < 64; ++l) {
			int i1 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
			int j1 = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
			int k1 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);
			int var999 = par2Random.nextInt(4);

			if (BOPCorals.growsOnSeaBed(Blocks.getBlock(par1World.getBlockId(i1, j1 - 1, k1)))
				&& WorldGenKelp.isWater(par1World, i1, j1, k1)
				&& WorldGenKelp.isWater(par1World, i1, j1 + 1, k1)) {
				Block<?> coral = colour(var999);
				setBlockAndMetadata(par1World, i1, j1, k1, coral.id(), 0);
			}
		}

		return true;
	}

	private static Block<?> colour(int roll) {
		switch (roll) {
			case 0:
				return BOPCorals.CORAL_PINK;
			case 1:
				return BOPCorals.CORAL_ORANGE;
			case 2:
				return BOPCorals.CORAL_BLUE;
			default:
				return BOPCorals.CORAL_GLOW;
		}
	}
}
