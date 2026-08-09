package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenLog extends BOPWorldFeature {

	private static final int LOG_AXIS_X = 2;

	@Override
	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		while (isAirBlock(var1, var3, var4, var5) && var4 > 2) {
			--var4;
		}

		int var6 = getBlockId(var1, var3, var4, var5);
		int var61 = getBlockId(var1, var3 - 1, var4, var5);
		int var62 = getBlockId(var1, var3 + 1, var4, var5);
		int var63 = getBlockId(var1, var3 - 2, var4, var5);
		int var64 = getBlockId(var1, var3 + 2, var4, var5);

		if (var6 == Blocks.GRASS.id()) {
			if (var61 == Blocks.GRASS.id()) {
				if (var62 == Blocks.GRASS.id()) {
					if (var63 == Blocks.GRASS.id()) {
						if (var64 == Blocks.GRASS.id()) {
							for (int var7 = -2; var7 <= 2; ++var7) {
								for (int var8 = -2; var8 <= 2; ++var8) {
									if (!isAirBlock(var1, var3, var4 + 1, var5 + var8)
											&& !isAirBlock(var1, var3 - 1, var4 + 1, var5 + var8)
											&& !isAirBlock(var1, var3 + 1, var4 + 1, var5 + var8))
										return false;
								}
							}

							setBlock(var1, var3, var4 + 1, var5, Blocks.LOG_OAK.id(), LOG_AXIS_X, 2);
							setBlock(var1, var3 - 1, var4 + 1, var5, Blocks.LOG_OAK.id(), LOG_AXIS_X, 2);
							setBlock(var1, var3 + 1, var4 + 1, var5, Blocks.LOG_OAK.id(), LOG_AXIS_X, 2);
							return true;
						} else
							return false;
					} else
						return false;
				} else
					return false;
			} else
				return false;
		} else
			return false;
	}
}
