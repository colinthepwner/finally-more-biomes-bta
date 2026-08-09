package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPCorals;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenKelp extends BOPWorldFeature {

	public WorldGenKelp(boolean notify) {
		super();
	}

	protected int strandLength(Random random) {
		return random.nextInt(10) + 3;
	}

	@Override
	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		int var6 = this.strandLength(var2);
		int var7 = var2.nextInt(3) + 2;

		if (var4 >= 1 && var4 + var6 + 1 <= var1.getHeightBlocks()) {
			int var11 = var1.getBlockId(var3, var4 - 1, var5);

			if ((var11 == Blocks.SAND.id() || var11 == Blocks.DIRT.id())
				&& var4 < var1.getHeightBlocks() - var6 - 1) {

				if (var1.getBlockMaterial(var3, var4, var5) != Materials.WATER) {
					return false;
				}

				var2.nextInt(2);

				int var15 = var2.nextInt(3);
				int var999 = 0;

				for (int var16 = 0; var16 < var6 - var15; ++var16) {
					if (isWater(var1, var3, var4 + var16 + 2, var5)) {

						setBlockAndMetadata(var1, var3, var4, var5, BOPCorals.KELP_BOTTOM.id(), 0);
						setBlockAndMetadata(var1, var3, var4 + var16, var5, BOPCorals.KELP_MIDDLE.id(), 0);
						++var999;
					}
				}

				if (isWater(var1, var3, var4 + var999 + 1, var5)) {
					if (var999 == 0) {
						setBlockAndMetadata(var1, var3, var4, var5, BOPCorals.KELP_SINGLE.id(), 0);
					} else {
						setBlockAndMetadata(var1, var3, var4, var5, BOPCorals.KELP_BOTTOM.id(), 0);
						setBlockAndMetadata(var1, var3, var4 + var999, var5, BOPCorals.KELP_TOP.id(), 0);
					}
				}

				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	protected static boolean isWater(World world, int x, int y, int z) {
		return Blocks.hasTag(world.getBlockId(x, y, z), BlockTags.IS_WATER);
	}
}
