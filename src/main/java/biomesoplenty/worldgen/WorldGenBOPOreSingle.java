package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenBOPOreSingle extends BOPWorldFeature {

	private final int oreId;
	private final int[] targetIds;

	public WorldGenBOPOreSingle(int oreId, int targetId) {
		this.oreId = oreId;
		int[] hosts = com.betteroplenty.block.BOPOreVariants.hostsFor(oreId);
		this.targetIds = hosts != null && targetId == net.minecraft.core.block.Blocks.STONE.id()
			? hosts
			: new int[] {targetId};
	}

	public WorldGenBOPOreSingle(int oreId, int... targetIds) {
		this.oreId = oreId;
		this.targetIds = targetIds.clone();
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		if (com.betteroplenty.block.BOPOreVariants.needsShallowBand(this.oreId)
			&& random.nextInt(com.betteroplenty.block.BOPOreVariants.shallowShare()) == 0) {
			y = com.betteroplenty.block.BOPOreVariants.SHALLOW_BAND_MIN
				+ random.nextInt(com.betteroplenty.block.BOPOreVariants.SHALLOW_BAND_RANGE);
		}
		int found = getBlockId(world, x, y, z);
		for (int target : this.targetIds) {
			if (found == target) {

				return setBlock(world, x, y, z,
					com.betteroplenty.block.BOPOreVariants.variantFor(this.oreId, found));
			}
		}
		return false;
	}
}
