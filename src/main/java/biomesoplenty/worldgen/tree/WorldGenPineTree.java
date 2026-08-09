package biomesoplenty.worldgen.tree;

import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenPineTree extends BOPWorldFeature {

	@Override
	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		while (isAirBlock(var1, var3, var4, var5) && var4 > 2) {
			--var4;
		}

		int var6 = getBlockId(var1, var3, var4, var5);

		if (!Blocks.hasTag(var6, BlockTags.GROWS_TREES)
			&& var6 != BOPBlocks.HARD_DIRT.id() && var6 != Blocks.STONE.id()) {
			return false;
		} else {
			for (int var7 = -2; var7 <= 2; ++var7) {
				for (int var8 = -2; var8 <= 2; ++var8) {
					if (isAirBlock(var1, var3 + var7, var4 - 1, var5 + var8)
						&& isAirBlock(var1, var3 + var7, var4 - 2, var5 + var8)
						&& !isAirBlock(var1, var3 + var7, var4, var5 + var8)) {
						return false;
					}
				}
			}

			int baselength = 4 + var2.nextInt(6);
			int branches = 2 + var2.nextInt(4);

			int h = 1;

			buildBlock(var1, var3, var4, var5, Blocks.DIRT.id(), 0);
			for (int b = 0; b < baselength; b++) {
				buildBlock(var1, var3, var4 + h, var5, Blocks.LOG_PINE.id(), 0);
				h++;
			}

			int c = 1;
			for (int r = 0; r < branches; r++) {
				generateBranch(var1, var2, var3, var4 + h, var5, c);
				c++;
				h += 2;
			}

			generateTop(var1, var3, var4 + h, var5);
			return true;
		}
	}

	public void generateTop(World world, int x, int y, int z) {
		final int leavesId = Blocks.LEAVES_PINE.id();

		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				buildBlock(world, x + i, y, z + j, leavesId, 0);
			}
		}
		buildBlock(world, x, y, z, Blocks.LOG_PINE.id(), 0);
		buildBlock(world, x + 1, y + 1, z, leavesId, 0);
		buildBlock(world, x, y + 1, z - 1, leavesId, 0);
		buildBlock(world, x, y + 1, z + 1, leavesId, 0);
		buildBlock(world, x - 1, y + 1, z, leavesId, 0);
		buildBlock(world, x, y + 2, z, leavesId, 0);
	}

	public void generateBranch(World world, Random rand, int x, int y, int z, int n) {
		final int leavesId = Blocks.LEAVES_PINE.id();

		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				buildBlock(world, x + i, y, z + j, leavesId, 0);
			}
		}

		int var99999 = rand.nextInt(2);
		int var99998 = rand.nextInt(2);
		int var99997 = rand.nextInt(2);
		int var99996 = rand.nextInt(2);

		if (n % 2 == 0) {

			if (var99998 == 0) {
				buildBlock(world, x + 1, y - 1, z - 2, leavesId, 0);
				buildBlock(world, x + 2, y - 1, z - 1, leavesId, 0);
				if (var99999 == 0) {
					buildBlock(world, x + 2, y - 2, z - 2, leavesId, 0);
				} else {
					buildBlock(world, x + 2, y - 1, z - 2, leavesId, 0);
				}
			} else {
				buildBlock(world, x + 1, y, z - 2, leavesId, 0);
				buildBlock(world, x + 2, y, z - 1, leavesId, 0);
				buildBlock(world, x + 2, y, z - 2, leavesId, 0);
			}

			if (var99997 == 0) {
				buildBlock(world, x - 2, y - 1, z + 1, leavesId, 0);
				buildBlock(world, x - 1, y - 1, z + 2, leavesId, 0);
				if (var99996 == 0) {
					buildBlock(world, x - 2, y - 2, z + 2, leavesId, 0);
				} else {
					buildBlock(world, x - 2, y - 1, z + 2, leavesId, 0);
				}
			} else {
				buildBlock(world, x - 2, y, z + 1, leavesId, 0);
				buildBlock(world, x - 1, y, z + 2, leavesId, 0);
				buildBlock(world, x - 2, y, z + 2, leavesId, 0);
			}
		} else {

			if (var99998 == 0) {
				buildBlock(world, x + 2, y - 1, z + 1, leavesId, 0);
				buildBlock(world, x + 1, y - 1, z + 2, leavesId, 0);
				if (var99999 == 0) {
					buildBlock(world, x + 2, y - 2, z + 2, leavesId, 0);
				} else {
					buildBlock(world, x + 2, y - 1, z + 2, leavesId, 0);
				}
			} else {
				buildBlock(world, x + 2, y, z + 1, leavesId, 0);
				buildBlock(world, x + 1, y, z + 2, leavesId, 0);
				if (var99999 == 0) {
					buildBlock(world, x + 2, y - 1, z + 2, leavesId, 0);
				} else {
					buildBlock(world, x + 2, y, z + 2, leavesId, 0);
				}
			}

			if (var99997 == 0) {
				buildBlock(world, x - 1, y - 1, z - 2, leavesId, 0);
				buildBlock(world, x - 2, y - 1, z - 1, leavesId, 0);
				if (var99996 == 0) {
					buildBlock(world, x - 2, y - 2, z - 2, leavesId, 0);
				} else {
					buildBlock(world, x - 2, y - 1, z - 2, leavesId, 0);
				}
			} else {
				buildBlock(world, x - 1, y, z - 2, leavesId, 0);
				buildBlock(world, x - 2, y, z - 1, leavesId, 0);
				if (var99996 == 0) {
					buildBlock(world, x - 2, y - 1, z - 2, leavesId, 0);
				} else {
					buildBlock(world, x - 2, y, z - 2, leavesId, 0);
				}
			}
		}

		buildBlock(world, x, y, z, Blocks.LOG_PINE.id(), 0);
		buildBlock(world, x, y + 1, z, Blocks.LOG_PINE.id(), 0);
	}

	public void buildBlock(World world, int x, int y, int z, int id, int meta) {
		Material m = getBlockMaterial(world, x, y, z);
		if (m == Materials.AIR || m == Materials.LEAVES) {
			setBlock(world, x, y, z, id, meta, 2);
		}
	}
}
