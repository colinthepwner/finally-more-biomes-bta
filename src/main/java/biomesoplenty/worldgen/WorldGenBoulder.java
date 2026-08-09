package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenBoulder extends BOPWorldFeature {

	@Override
	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		for (int var6 = 0; var6 < 64; ++var6) {
			int var7 = var3 + var2.nextInt(8) - var2.nextInt(8);
			int var8 = var4 + var2.nextInt(4) - var2.nextInt(4);
			int var9 = var5 + var2.nextInt(8) - var2.nextInt(8);

			if (isAirBlock(var1, var7, var8, var9)
					&& getBlockId(var1, var7, var8 - 1, var9) == Blocks.GRASS.id()
					&& canPlaceAt(var1, Blocks.PUMPKIN.id(), var7, var8, var9)) {
				setBlock(var1, var7, var8, var9, Blocks.STONE.id());
			}
		}

		return true;
	}
}
