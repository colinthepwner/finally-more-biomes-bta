package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPGraves;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenGrave extends BOPWorldFeature {

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		while (isAirBlock(world, x, y, z) && y > 2) {
			--y;
		}

		int floor = getBlockId(world, x, y, z);

		if (floor != Blocks.NETHERRACK.id() && floor != Blocks.SOULSAND.id()) {
			return false;
		}

		for (int dx = -2; dx <= 2; ++dx) {
			for (int dz = -2; dz <= 2; ++dz) {
				if (isAirBlock(world, x + dx, y - 1, z + dz)
					&& isAirBlock(world, x + dx, y - 2, z + dz)
					&& !isAirBlock(world, x + dx, y, z + dz)) {
					return false;
				}
			}
		}

		int orientation = random.nextInt(4);
		int roll = random.nextInt(10);

		if (roll == 0) {

			int axis = orientation <= 1 ? AXIS_X : AXIS_Z;

			setBlockAndMetadata(world, x, y + 1, z, BOPGraves.GRAVE.id(), axis);
			setBlockAndMetadata(world, x, y + 2, z, BOPGraves.GRAVE_TOP.id(), axis);
		}

		return true;
	}

	private static final int AXIS_Z = 1;

	private static final int AXIS_X = 2;
}
