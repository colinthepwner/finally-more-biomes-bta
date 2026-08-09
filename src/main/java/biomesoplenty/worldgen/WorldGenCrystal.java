package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPPromisedLand;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeature;

import java.util.Random;

public class WorldGenCrystal extends WorldFeature {

	private static final int ATTEMPTS = 1500;

	@Override
	public boolean place(World world, Random random, int x, int y, int z) {
		if (!world.isAirBlock(x, y, z)) {
			return false;
		}

		if (world.getBlockId(x, y + 1, z) != BOPPromisedLand.HOLY_STONE.id()) {
			return false;
		}

		int crystal = BOPPromisedLand.CRYSTAL.id();
		world.setBlockAndMetadataRaw(x, y, z, crystal, 0);

		for (int i = 0; i < ATTEMPTS; ++i) {
			int cx = x + random.nextInt(8) - random.nextInt(8);
			int cy = y - random.nextInt(12);
			int cz = z + random.nextInt(8) - random.nextInt(8);

			if (world.getBlockId(cx, cy, cz) != 0) {
				continue;
			}

			int touching = 0;
			if (world.getBlockId(cx - 1, cy, cz) == crystal) { ++touching; }
			if (world.getBlockId(cx + 1, cy, cz) == crystal) { ++touching; }
			if (world.getBlockId(cx, cy - 1, cz) == crystal) { ++touching; }
			if (world.getBlockId(cx, cy + 1, cz) == crystal) { ++touching; }
			if (world.getBlockId(cx, cy, cz - 1) == crystal) { ++touching; }
			if (world.getBlockId(cx, cy, cz + 1) == crystal) { ++touching; }

			if (touching == 1) {
				world.setBlockAndMetadataRaw(cx, cy, cz, crystal, 0);
			}
		}

		return true;
	}
}
