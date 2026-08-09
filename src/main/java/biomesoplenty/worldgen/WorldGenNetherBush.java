package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPWoodSets;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenNetherBush extends BOPWorldFeature {

	@Override
	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		final int leavesId = BOPWoodSets.HELLBARK.leaves.id();
		final int logId = BOPWoodSets.HELLBARK.log.id();

		while (isAirBlock(var1, var3, var4, var5) && var4 > 2) {
			--var4;
		}

		int var6 = getBlockId(var1, var3, var4, var5);
		int var99 = getBlockId(var1, var3, var4 + 1, var5);

		if (var6 != Blocks.NETHERRACK.id() && var99 != 0)
			return false;
		else {

			for (int var7 = -2; var7 <= 2; ++var7) {
				for (int var8 = -2; var8 <= 2; ++var8) {
					if (isAirBlock(var1, var3 + var7, var4 - 1, var5 + var8) && isAirBlock(var1, var3 + var7, var4 - 2, var5 + var8))
						return false;
				}
			}

			setBlockAndMetadata(var1, var3, var4, var5, Blocks.NETHERRACK.id(), 0);

			setBlockAndMetadata(var1, var3, var4 + 1, var5, logId, 0);
			setBlockAndMetadata(var1, var3, var4 + 2, var5, logId, 0);
			setBlockAndMetadata(var1, var3 + 1, var4 + 2, var5, leavesId, 0);
			setBlockAndMetadata(var1, var3 - 1, var4 + 2, var5, leavesId, 0);
			setBlockAndMetadata(var1, var3, var4 + 2, var5 + 1, leavesId, 0);
			setBlockAndMetadata(var1, var3, var4 + 2, var5 - 1, leavesId, 0);
			setBlockAndMetadata(var1, var3, var4 + 3, var5, leavesId, 0);
			return true;
		}
	}
}
