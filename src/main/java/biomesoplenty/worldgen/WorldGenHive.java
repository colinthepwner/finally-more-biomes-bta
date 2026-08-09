package biomesoplenty.worldgen;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.block.BOPHive;
import com.betteroplenty.compat.BOPWorldFeature;
import com.betteroplenty.fluid.BOPFluids;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityMobSpawner;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;

import java.util.Random;

public class WorldGenHive extends BOPWorldFeature {

	private static final String WASP_ID =
		NamespaceID.getPermanent(BetterOPlenty.MOD_ID, "wasp").toString();

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {

		int baseWidth = 4 + rand.nextInt(2);
		int baseHeight = 8 + rand.nextInt(2);

		for (int air = 0; air < 26; air++) {
			if (getBlockId(world, x, y + 3, z) != Blocks.NETHERRACK.id() || !isAirBlock(world, x, (y + 2) - air, z)) {
				return false;
			}
		}

		for (int cubeno = 0; cubeno < 4; cubeno++) {
			float chance = 0.0F;
			boolean shell = false;

			switch (cubeno) {
				case 0:
					chance = 0.25F;
					shell = false;
					break;

				case 1:
					chance = 1.0F;
					shell = false;
					break;

				case 2:
					chance = 1.0F;
					shell = true;
					break;

				case 3:
					chance = 0.5F;
					shell = true;
					break;
			}

			int honeychance = rand.nextInt(2);

			generateHiveCubeSmall(world, x, y + cubeno, z, (baseHeight - 11) + (cubeno * 2), (baseWidth - 1) + cubeno, chance);

			generateHiveCube(world, x, (y - 2) + cubeno, z, baseHeight + (cubeno * 2), baseWidth + cubeno, cubeno, chance, honeychance, shell);

			generateHiveCubeSmall(world, x, (y - (baseHeight + 6)) + cubeno, z, (baseHeight - 10) + (cubeno * 2), (baseWidth - 1) + cubeno, chance);

			generateHiveCubeSmall(world, x, (y - (baseHeight + 7)) + cubeno, z, (baseHeight - 9) + (cubeno * 2), (baseWidth - 2) + cubeno, chance);

			generateHiveCubeSmall(world, x, (y - (baseHeight + 9)) + cubeno, z, (baseHeight - 9) + (cubeno * 2), (baseWidth - 4) + cubeno, chance);

			spawnWasps(world, rand, x, y, z);

			spawnEmptyHoneycombs(world, rand, x, y, z);

			spawnFilledHoneycombs(world, rand, x, y, z);
		}

		return true;
	}

	public void generateHiveCube(World world, int origx, int origy, int origz, int height, int width,
								 int cubeno, float chance, int honeychance, boolean shell) {
		for (int hLayer = 0; hLayer < height; hLayer++) {
			for (int i = -width; i < width; i++) {
				for (int j = -width; j < width; j++) {
					int px = origx + i;
					int py = origy - hLayer;
					int pz = origz + j;

					if ((hLayer == 0 || hLayer == (height - 1)) && (world.rand.nextFloat() <= chance)) {
						setBlockAndMetadata(world, px, py, pz, cell(shell).id(), 0);
					} else if ((i == -width || i == (width - 1) || j == -width || j == (width - 1))
							&& (world.rand.nextFloat() <= chance)) {
						setBlockAndMetadata(world, px, py, pz, cell(shell).id(), 0);
					}

					if (cubeno >= 2) {

						continue;
					}

					if (hLayer > (height / 2)) {
						if (honeychance == 0) {

							if (isNotComb(world, px, py, pz)) {
								setBlockAndMetadata(world, px, py, pz, BOPFluids.HONEY_STILL.id(), 0);
							}
						} else {
							if (isNotComb(world, px, py, pz)) {
								setBlockAndMetadata(world, px, py, pz, 0, 0);
							}
						}
					} else if (honeychance == 0 && hLayer == (height / 2)) {
						if (isNotComb(world, px, py, pz)) {
							setBlockAndMetadata(world, px, py, pz, BOPHive.HONEY_BLOCK.id(), 0);
						}
					} else {
						if (isNotComb(world, px, py, pz)) {
							setBlockAndMetadata(world, px, py, pz, 0, 0);
						}
					}
				}
			}
		}
	}

	public void generateHiveCubeSmall(World world, int origx, int origy, int origz, int height, int width,
									  float chance) {
		for (int hLayer = 0; hLayer < height; hLayer++) {
			for (int i = -width; i < width; i++) {
				for (int j = -width; j < width; j++) {
					if ((hLayer == 0 || hLayer == (height - 1)) && (world.rand.nextFloat() <= chance)) {
						setBlockAndMetadata(world, origx + i, origy - hLayer, origz + j, BOPHive.HIVE.id(), 0);
					} else if ((i == -width || i == (width - 1) || j == -width || j == (width - 1))
							&& (world.rand.nextFloat() <= chance)) {
						setBlockAndMetadata(world, origx + i, origy - hLayer, origz + j, BOPHive.HIVE.id(), 0);
					}
				}
			}
		}
	}

	public void spawnWasps(World world, Random rand, int x, int y, int z) {
		for (int spawn = 0; spawn < 50; spawn++) {
			int spawnx = (x - 12) + rand.nextInt(24);
			int spawny = y - rand.nextInt(24);
			int spawnz = (z - 12) + rand.nextInt(24);

			if (!isShell(world, spawnx, spawny, spawnz)) {
				continue;
			}
			if (!isShell(world, spawnx - 1, spawny, spawnz) || !isShell(world, spawnx + 1, spawny, spawnz)
					|| !isShell(world, spawnx, spawny, spawnz - 1) || !isShell(world, spawnx, spawny, spawnz + 1)
					|| !isShell(world, spawnx, spawny - 1, spawnz) || !isShell(world, spawnx, spawny + 1, spawnz)) {
				continue;
			}

			world.setBlockWithNotify(spawnx, spawny, spawnz, Blocks.MOBSPAWNER.id());

			TileEntity tileEntity = world.getTileEntity(new TilePos(spawnx, spawny, spawnz));
			if (tileEntity instanceof TileEntityMobSpawner spawner) {
				spawner.setMobId(WASP_ID);
			}
		}
	}

	public void spawnEmptyHoneycombs(World world, Random rand, int x, int y, int z) {
		for (int spawn = 0; spawn < 50; spawn++) {
			int spawnx = (x - 8) + rand.nextInt(16);
			int spawny = y - rand.nextInt(12);
			int spawnz = (z - 8) + rand.nextInt(16);

			if (getBlockId(world, spawnx, spawny, spawnz) == BOPHive.HONEYCOMB.id()) {
				setBlockAndMetadata(world, spawnx, spawny, spawnz, BOPHive.HONEYCOMB_EMPTY.id(), 0);
			}
		}
	}

	public void spawnFilledHoneycombs(World world, Random rand, int x, int y, int z) {
		for (int spawn = 0; spawn < 20; spawn++) {
			int spawnx = (x - 8) + rand.nextInt(16);
			int spawny = y - rand.nextInt(12);
			int spawnz = (z - 8) + rand.nextInt(16);

			if (getBlockId(world, spawnx, spawny, spawnz) == BOPHive.HONEYCOMB.id()) {
				setBlockAndMetadata(world, spawnx, spawny, spawnz, BOPHive.HONEYCOMB_FILLED.id(), 0);
			}
		}
	}

	private static Block<?> cell(boolean shell) {
		return shell ? BOPHive.HIVE : BOPHive.HONEYCOMB;
	}

	private static boolean isNotComb(World world, int x, int y, int z) {
		return getBlockId(world, x, y, z) != BOPHive.HONEYCOMB.id();
	}

	private static boolean isShell(World world, int x, int y, int z) {
		return getBlockId(world, x, y, z) == BOPHive.HIVE.id();
	}
}
