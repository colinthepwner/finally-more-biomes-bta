package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPBones;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenBoneSpine2 extends BOPWorldFeature {

	private static final int[] SHORT_RINGS = {1, 3, 5, 7};

	private static final int[] TALL_RINGS = {1, 3, 5, 7, 9, 11, 13};

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {

		while (isAirBlock(world, x, y, z) && y > 2) {
			--y;
		}

		if (getBlockId(world, x, y, z) != Blocks.NETHERRACK.id()) {
			return false;
		}

		for (int dx = -2; dx <= 2; ++dx) {
			for (int dz = -2; dz <= 2; ++dz) {
				if (!isAirBlock(world, x + dx, y - 1, z + dz)) {
					return false;
				}
			}
		}

		int[] rings = random.nextInt(2) == 0 ? SHORT_RINGS : TALL_RINGS;

		for (int ring : rings) {
			placeRibRing(world, x, y - ring, z);
			setBlockAndMetadata(world, x, y - ring - 1, z, BOPBones.MEDIUM.id(), 0);
		}

		int cap = rings[rings.length - 1] + 2;
		setBlockAndMetadata(world, x, y - cap, z, BOPBones.SMALL.id(), 0);

		return true;
	}

	private static void placeRibRing(World world, int x, int y, int z) {
		setBlockAndMetadata(world, x, y, z, BOPBones.LARGE.id(), 0);

		setBlockAndMetadata(world, x - 1, y, z, BOPBones.MEDIUM.id(), WorldGenBoneSpine.AXIS_X);
		setBlockAndMetadata(world, x + 1, y, z, BOPBones.MEDIUM.id(), WorldGenBoneSpine.AXIS_X);
		setBlockAndMetadata(world, x, y, z - 1, BOPBones.MEDIUM.id(), WorldGenBoneSpine.AXIS_Z);
		setBlockAndMetadata(world, x, y, z + 1, BOPBones.MEDIUM.id(), WorldGenBoneSpine.AXIS_Z);

		setBlockAndMetadata(world, x - 2, y, z, BOPBones.SMALL.id(), WorldGenBoneSpine.AXIS_X);
		setBlockAndMetadata(world, x + 2, y, z, BOPBones.SMALL.id(), WorldGenBoneSpine.AXIS_X);
		setBlockAndMetadata(world, x, y, z - 2, BOPBones.SMALL.id(), WorldGenBoneSpine.AXIS_Z);
		setBlockAndMetadata(world, x, y, z + 2, BOPBones.SMALL.id(), WorldGenBoneSpine.AXIS_Z);
	}
}
