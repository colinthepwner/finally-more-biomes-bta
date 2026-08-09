package biomesoplenty.worldgen.tree;

import com.betteroplenty.block.BOPWoodSets;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenAcacia extends BOPWorldFeature {

	public WorldGenAcacia(boolean par1) {
		this(par1, 4, 0, 0, false);
	}

	public WorldGenAcacia(boolean par1, int par2, int par3, int par4, boolean par5) {
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int x, int y, int z) {
		final int logId = BOPWoodSets.ACACIA.log.id();

		int var5 = getBlockId(par1World, x, y - 1, z);

		if (var5 != Blocks.GRASS.id())
			return false;
		int rand = 4 + par2Random.nextInt(3);
		for (int i = 0; i < rand; i++) { setBlockAndMetadata(par1World, x, y + i, z, logId, 0); }

		if (par2Random.nextInt(4) == 0) {
			setBlockAndMetadata(par1World, x + 0, y + rand + 1, z + 1, logId, 0);
			setBlockAndMetadata(par1World, x + 1, y + rand + 2, z + 2, logId, 0);
			createAcaciaLeaves(par1World, par2Random, x + 1, y + rand + 2, z + 2, 3);
			createAcaciaLeaves(par1World, par2Random, x + 1, y + rand + 3, z + 2, 2);
		}

		if (par2Random.nextInt(4) == 0) {
			setBlockAndMetadata(par1World, x + 1, y + rand + 0, z + 0, logId, 0);
			setBlockAndMetadata(par1World, x + 2, y + rand + 1, z + 0, logId, 0);
			setBlockAndMetadata(par1World, x + 3, y + rand + 2, z - 1, logId, 0);
			createAcaciaLeaves(par1World, par2Random, x + 3, y + rand + 3, z - 1, 3);
			createAcaciaLeaves(par1World, par2Random, x + 3, y + rand + 4, z - 1, 2);
		}

		if (par2Random.nextInt(4) == 0) {
			setBlockAndMetadata(par1World, x - 1, y + rand + 0, z + 0, logId, 0);
			setBlockAndMetadata(par1World, x - 2, y + rand + 1, z + 0, logId, 0);
			setBlockAndMetadata(par1World, x - 3, y + rand + 2, z - 1, logId, 0);
			setBlockAndMetadata(par1World, x - 4, y + rand + 3, z - 2, logId, 0);
			createAcaciaLeaves(par1World, par2Random, x - 4, y + rand + 4, z - 2, 3);
			createAcaciaLeaves(par1World, par2Random, x - 4, y + rand + 5, z - 2, 2);
		}

		if (par2Random.nextInt(4) == 0) {
			setBlockAndMetadata(par1World, x + 0, y + rand + 0, z - 1, logId, 0);
			setBlockAndMetadata(par1World, x + 1, y + rand + 1, z - 2, logId, 0);
			setBlockAndMetadata(par1World, x + 2, y + rand + 2, z - 2, logId, 0);
			setBlockAndMetadata(par1World, x + 3, y + rand + 3, z - 2, logId, 0);
			createAcaciaLeaves(par1World, par2Random, x + 3, y + rand + 3, z - 2, 3);
			createAcaciaLeaves(par1World, par2Random, x + 3, y + rand + 4, z - 2, 2);
		}

		if (par2Random.nextInt(4) == 0) {
			setBlockAndMetadata(par1World, x + 0, y + rand + 0, z - 1, logId, 0);
			setBlockAndMetadata(par1World, x + 0, y + rand + 0, z - 2, logId, 0);
			setBlockAndMetadata(par1World, x + 1, y + rand + 1, z - 3, logId, 0);
			createAcaciaLeaves(par1World, par2Random, x + 1, y + rand + 1, z - 3, 3);
			createAcaciaLeaves(par1World, par2Random, x + 1, y + rand + 2, z - 3, 2);
		}

		setBlockAndMetadata(par1World, x - 0, y + rand + 0, z + 0, logId, 0);
		setBlockAndMetadata(par1World, x - 0, y + rand + 1, z + 0, logId, 0);
		setBlockAndMetadata(par1World, x - 0, y + rand + 2, z - 0, logId, 0);
		createAcaciaLeaves(par1World, par2Random, x + 0, y + rand + 3, z - 0, 3);
		createAcaciaLeaves(par1World, par2Random, x + 0, y + rand + 4, z - 0, 2);

		return true;
	}

	private void createAcaciaLeaves(World par1World, Random par2Random, int x, int y, int z, int size) {
		final int leavesId = BOPWoodSets.ACACIA.leaves.id();
		final int logId = BOPWoodSets.ACACIA.log.id();

		for (int x1 = -size + x; x1 < size + 1 + x; x1++) {
			for (int z1 = -size + z; z1 < size + 1 + z; z1++) {
				int var5 = getBlockId(par1World, x1, y, z1);
				if (var5 == 0) {
					if (x1 == -size + x && z1 == -size + z) {} else if (x1 == -size + x && z1 == size + z) {} else if (x1 == size + x && z1 == -size + z) {} else if (x1 == size + x && z1 == size + z) {}
					else {

						setBlockAndMetadata(par1World, x1, y, z1, leavesId, 0);
					}
				}
			}
		}
		if (size == 3) { setBlockAndMetadata(par1World, x, y, z, logId, 0); }
	}
}
