package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenTundra1 extends BOPWorldFeature {

	@Override
	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		final int grassId = Blocks.GRASS.id();
		final int dirtId = Blocks.DIRT.id();
		final int stoneId = Blocks.STONE.id();

		while (isAirBlock(var1, var3, var4, var5) && var4 > 2) {
			--var4;
		}

		int var6 = getBlockId(var1, var3, var4, var5);
		int var95 = getBlockId(var1, var3 - 1, var4, var5);
		int var96 = getBlockId(var1, var3 + 1, var4, var5);
		int var97 = getBlockId(var1, var3, var4, var5 - 1);
		int var98 = getBlockId(var1, var3, var4, var5 + 1);

		if (var6 != grassId || var95 != grassId || var96 != grassId || var97 != grassId || var98 != grassId)
			return false;
		else {
			for (int var7 = -2; var7 <= 2; ++var7) {
				for (int var8 = -2; var8 <= 2; ++var8) {
					if (isAirBlock(var1, var3 + var7, var4 - 1, var5 + var8) && isAirBlock(var1, var3 + var7, var4 - 2, var5 + var8))
						return false;
				}
			}

			int var999 = var2.nextInt(2);

			if (var999 == 0) {
				setBlock(var1, var3, var4, var5, dirtId);
				setBlock(var1, var3 - 1, var4, var5, dirtId);
				setBlock(var1, var3 + 1, var4, var5, dirtId);
				setBlock(var1, var3, var4, var5 - 1, dirtId);
				setBlock(var1, var3, var4, var5 + 1, dirtId);
				setBlock(var1, var3, var4 + 1, var5, stoneId);
				setBlock(var1, var3 + 1, var4 + 1, var5, stoneId);
				setBlock(var1, var3 - 1, var4 + 1, var5, stoneId);
				setBlock(var1, var3, var4 + 1, var5 + 1, stoneId);
				setBlock(var1, var3, var4 + 1, var5 - 1, stoneId);
				setBlock(var1, var3, var4 + 2, var5, stoneId);
				return true;
			}
			if (var999 == 1) {
				setBlock(var1, var3, var4, var5, dirtId);
				setBlock(var1, var3, var4 + 1, var5, stoneId);
				return true;
			}

			return true;
		}
	}
}
