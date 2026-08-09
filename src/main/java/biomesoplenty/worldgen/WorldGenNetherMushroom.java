package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPJungle;
import com.betteroplenty.block.BOPNether;
import com.betteroplenty.block.BlockLogicGiantMushroom;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;
import java.util.function.IntPredicate;

public class WorldGenNetherMushroom extends BOPWorldFeature {

	private final int mushroomType;

	private final IntPredicate soil;

	public static WorldGenNetherMushroom nether() {
		return new WorldGenNetherMushroom(1, id ->
			id == Blocks.NETHERRACK.id() || id == BOPNether.OVERGROWN_NETHERRACK.id());
	}

	public static WorldGenNetherMushroom overworld() {
		return new WorldGenNetherMushroom(-1, id ->
			id == Blocks.GRASS.id() || id == Blocks.DIRT.id() || id == BOPJungle.MYCELIUM.id());
	}

	private WorldGenNetherMushroom(int mushroomType, IntPredicate soil) {
		this.mushroomType = mushroomType;
		this.soil = soil;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {

		int l = par2Random.nextInt(2);

		if (this.mushroomType >= 0) {
			l = this.mushroomType;
		}

		int i1 = par2Random.nextInt(3) + 4;
		boolean flag = true;
		int height = par1World.getHeightBlocks();

		if (par4 >= 1 && par4 + i1 + 1 < height) {
			int j1;
			int k1;
			int l1;
			int i2;

			for (j1 = par4; j1 <= par4 + 1 + i1; ++j1) {
				byte b0 = 3;

				if (j1 <= par4 + 3) {
					b0 = 0;
				}

				for (k1 = par3 - b0; k1 <= par3 + b0 && flag; ++k1) {
					for (l1 = par5 - b0; l1 <= par5 + b0 && flag; ++l1) {
						if (j1 >= 0 && j1 < height) {
							i2 = getBlockId(par1World, k1, j1, l1);

							if (i2 != 0 && !isLeaves(i2)) {
								flag = false;
							}
						} else {
							flag = false;
						}
					}
				}
			}

			if (!flag) {
				return false;
			} else {
				j1 = getBlockId(par1World, par3, par4 - 1, par5);

				if (!this.soil.test(j1)) {
					return false;
				} else {
					int cap = capBlockId(l);
					int j2 = par4 + i1;

					if (l == 1) {
						j2 = par4 + i1 - 3;
					}

					for (k1 = j2; k1 <= par4 + i1; ++k1) {
						l1 = 1;

						if (k1 < par4 + i1) {
							++l1;
						}

						if (l == 0) {
							l1 = 3;
						}

						for (i2 = par3 - l1; i2 <= par3 + l1; ++i2) {
							for (int k2 = par5 - l1; k2 <= par5 + l1; ++k2) {
								int l2 = 5;

								if (i2 == par3 - l1) {
									--l2;
								}

								if (i2 == par3 + l1) {
									++l2;
								}

								if (k2 == par5 - l1) {
									l2 -= 3;
								}

								if (k2 == par5 + l1) {
									l2 += 3;
								}

								if (l == 0 || k1 < par4 + i1) {
									if ((i2 == par3 - l1 || i2 == par3 + l1)
										&& (k2 == par5 - l1 || k2 == par5 + l1)) {
										continue;
									}

									if (i2 == par3 - (l1 - 1) && k2 == par5 - l1) {
										l2 = 1;
									}

									if (i2 == par3 - l1 && k2 == par5 - (l1 - 1)) {
										l2 = 1;
									}

									if (i2 == par3 + (l1 - 1) && k2 == par5 - l1) {
										l2 = 3;
									}

									if (i2 == par3 + l1 && k2 == par5 - (l1 - 1)) {
										l2 = 3;
									}

									if (i2 == par3 - (l1 - 1) && k2 == par5 + l1) {
										l2 = 7;
									}

									if (i2 == par3 - l1 && k2 == par5 + (l1 - 1)) {
										l2 = 7;
									}

									if (i2 == par3 + (l1 - 1) && k2 == par5 + l1) {
										l2 = 9;
									}

									if (i2 == par3 + l1 && k2 == par5 + (l1 - 1)) {
										l2 = 9;
									}
								}

								if (l2 == 5 && k1 < par4 + i1) {
									l2 = 0;
								}

								if ((l2 != 0 || par4 >= par4 + i1 - 1)
									&& canBeReplacedByCap(par1World, i2, k1, k2)) {
									setBlockAndMetadata(par1World, i2, k1, k2, cap, l2);
								}
							}
						}
					}

					for (k1 = 0; k1 < i1; ++k1) {
						if (canBeReplacedByCap(par1World, par3, par4 + k1, par5)) {
							setBlockAndMetadata(par1World, par3, par4 + k1, par5,
								BOPNether.MUSHROOM_STEM.id(), BlockLogicGiantMushroom.STEM);
						}
					}

					return true;
				}
			}
		} else {
			return false;
		}
	}

	private static int capBlockId(int l) {
		return l == 0 ? BOPNether.MUSHROOM_CAP_BROWN.id() : BOPNether.MUSHROOM_CAP_RED.id();
	}

	private static boolean canBeReplacedByCap(World world, int x, int y, int z) {
		return !isOpaqueCube(world, x, y, z);
	}
}
