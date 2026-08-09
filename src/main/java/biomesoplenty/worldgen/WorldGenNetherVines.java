package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.block.BlockLogicBOPHangingMoss;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenNetherVines extends BOPWorldFeature {

	@Override
	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		while (isAirBlock(var1, var3, var4, var5) && var4 > 2) {
			--var4;
		}

		int var6 = getBlockId(var1, var3, var4, var5);

		if (var6 != Blocks.NETHERRACK.id()) {
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

		drape(var1, var3 - 1, var4, var5, 15, Side.WEST);
		drape(var1, var3 + 1, var4, var5, 20, Side.EAST);
		drape(var1, var3, var4, var5 - 1, 25, Side.NORTH);
		drape(var1, var3, var4, var5 + 1, 30, Side.SOUTH);

		return true;
	}

	private static void drape(World world, int x, int y, int z, int depth, Side hangsOn) {
		int data = BlockLogicBOPHangingMoss.attachmentData(hangsOn);

		for (int dy = 0; dy <= depth; ++dy) {
			if (isAirBlock(world, x, y - dy, z)) {
				setBlock(world, x, y - dy, z, BOPPlants.IVY.id(), data, 2);
			}
		}
	}
}
