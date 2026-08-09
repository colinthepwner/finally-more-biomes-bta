package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenChaparral2 extends BOPWorldFeature {

	@Override
	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		while (var1.isAirBlock(var3, var4, var5) && var4 > 2) {
			--var4;
		}

		int var6 = var1.getBlockId(var3, var4, var5);

		if (var6 != Blocks.GRASS.id() && var6 != Blocks.SAND.id()) {
			return false;
		} else {
			for (int var7 = -2; var7 <= 2; ++var7) {
				for (int var8 = -2; var8 <= 2; ++var8) {
					if (var1.isAirBlock(var3 + var7, var4 - 1, var5 + var8)
						&& var1.isAirBlock(var3 + var7, var4 - 2, var5 + var8)) {
						return false;
					}
				}
			}

			setBlock(var1, var3, var4, var5, Blocks.DIRT.id());
			setBlock(var1, var3, var4 + 1, var5, Blocks.LOG_OAK.id());
			setBlock(var1, var3, var4 + 2, var5, Blocks.LOG_OAK.id());
			setBlock(var1, var3 + 1, var4 + 2, var5, Blocks.LEAVES_OAK.id());
			setBlock(var1, var3 - 1, var4 + 2, var5, Blocks.LEAVES_OAK.id());
			setBlock(var1, var3, var4 + 2, var5 + 1, Blocks.LEAVES_OAK.id());
			setBlock(var1, var3, var4 + 2, var5 - 1, Blocks.LEAVES_OAK.id());
			setBlock(var1, var3, var4 + 3, var5, Blocks.LEAVES_OAK.id());
			return true;
		}
	}
}
