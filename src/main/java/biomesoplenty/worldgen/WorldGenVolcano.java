package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPWastes;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenVolcano extends BOPWorldFeature {

	public static final int VENT_FLOOR_ABOVE_SEA = 11;

	public static int ventFloor(World world) {
		return world.getWorldType().getOceanY() + VENT_FLOOR_ABOVE_SEA;
	}

	private static void scheduleFlow(World world, int x, int y, int z, int lavaId) {
		world.scheduleBlockUpdate(x, y, z, lavaId, Blocks.FLUID_LAVA_FLOWING.getLogic().tickDelay());
	}

	@Override
	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		int floor = ventFloor(var1);

		while (isAirBlock(var1, var3, var4, var5) && var4 > floor) {
			--var4;
		}

		int var6 = getBlockId(var1, var3, var4, var5);

		if (var6 != BOPWastes.ASH_STONE.id()) {
			return false;
		} else {
			for (int var7 = -2; var7 <= 2; ++var7) {
				for (int var8 = -2; var8 <= 2; ++var8) {
					if (isAirBlock(var1, var3 + var7, var4 - 1, var5 + var8)
						&& isAirBlock(var1, var3 + var7, var4 - 2, var5 + var8)) {
						return false;
					}
				}
			}

			int lava = Blocks.FLUID_LAVA_FLOWING.id();

			setBlock(var1, var3, var4 - 1, var5, lava);
			setBlock(var1, var3, var4, var5, lava);
			setBlock(var1, var3, var4 + 1, var5, lava);
			setBlock(var1, var3 - 1, var4 + 1, var5, lava);
			setBlock(var1, var3 + 1, var4 + 1, var5, lava);
			setBlock(var1, var3, var4 + 1, var5 - 1, lava);
			setBlock(var1, var3, var4 + 1, var5 + 1, lava);

			scheduleFlow(var1, var3, var4 - 1, var5, lava);
			scheduleFlow(var1, var3, var4, var5, lava);
			scheduleFlow(var1, var3, var4 + 1, var5, lava);
			scheduleFlow(var1, var3 - 1, var4 + 1, var5, lava);
			scheduleFlow(var1, var3 + 1, var4 + 1, var5, lava);
			scheduleFlow(var1, var3, var4 + 1, var5 - 1, lava);
			scheduleFlow(var1, var3, var4 + 1, var5 + 1, lava);
			return true;
		}
	}
}
