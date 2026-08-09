package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenGiantFlowerYellow extends BOPWorldFeature {

	@Override
	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		while (isAirBlock(var1, var3, var4, var5) && var4 > 2) {
			--var4;
		}

		if (getBlockId(var1, var3, var4, var5) != BOPBlocks.LONG_GRASS.id()) {
			return false;
		}

		for (int var7 = -2; var7 <= 2; ++var7) {
			for (int var8 = -2; var8 <= 2; ++var8) {
				if (isAirBlock(var1, var3 + var7, var4 - 1, var5 + var8)
					&& isAirBlock(var1, var3 + var7, var4 - 2, var5 + var8)) {
					return false;
				}
			}
		}

		final int stem = BOPBlocks.BIG_FLOWER_STEM.id();
		final int petal = BOPBlocks.BIG_FLOWER_YELLOW.id();

		setBlock(var1, var3, var4, var5, Blocks.DIRT.id());
		setBlock(var1, var3, var4 + 1, var5, stem);
		setBlock(var1, var3, var4 + 2, var5, stem);
		setBlock(var1, var3, var4 + 3, var5, stem);
		setBlock(var1, var3, var4 + 4, var5, stem);
		setBlock(var1, var3, var4 + 5, var5, stem);

		setBlock(var1, var3 - 1, var4 + 5, var5, petal);
		setBlock(var1, var3 + 1, var4 + 5, var5, petal);
		setBlock(var1, var3, var4 + 5, var5 - 1, petal);
		setBlock(var1, var3, var4 + 5, var5 + 1, petal);

		setBlock(var1, var3 + 2, var4 + 5, var5 + 2, petal);
		setBlock(var1, var3 + 2, var4 + 5, var5 - 2, petal);
		setBlock(var1, var3 - 2, var4 + 5, var5 + 2, petal);
		setBlock(var1, var3 - 2, var4 + 5, var5 - 2, petal);

		setBlock(var1, var3, var4 + 6, var5, petal);
		setBlock(var1, var3 - 1, var4 + 6, var5, petal);
		setBlock(var1, var3 + 1, var4 + 6, var5, petal);
		setBlock(var1, var3, var4 + 6, var5 - 1, petal);
		setBlock(var1, var3, var4 + 6, var5 + 1, petal);
		setBlock(var1, var3 + 1, var4 + 6, var5 + 1, petal);
		setBlock(var1, var3 + 1, var4 + 6, var5 - 1, petal);
		setBlock(var1, var3 - 1, var4 + 6, var5 + 1, petal);
		setBlock(var1, var3 - 1, var4 + 6, var5 - 1, petal);
		setBlock(var1, var3 + 2, var4 + 6, var5, petal);
		setBlock(var1, var3 - 2, var4 + 6, var5, petal);
		setBlock(var1, var3, var4 + 6, var5 + 2, petal);
		setBlock(var1, var3, var4 + 6, var5 - 2, petal);

		setBlock(var1, var3, var4 + 7, var5, petal);
		setBlock(var1, var3 + 3, var4 + 7, var5, petal);
		setBlock(var1, var3 - 3, var4 + 7, var5, petal);
		setBlock(var1, var3, var4 + 7, var5 + 3, petal);
		setBlock(var1, var3, var4 + 7, var5 - 3, petal);

		return true;
	}
}
