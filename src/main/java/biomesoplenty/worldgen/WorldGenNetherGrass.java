package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPNether;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.block.BOPWoodSets;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenNetherGrass extends BOPWorldFeature {

	private final int tallGrassID;
	private final int tallGrassMetadata;

	public WorldGenNetherGrass(int par1, int par2) {
		this.tallGrassID = par1;
		this.tallGrassMetadata = par2;
	}

	public WorldGenNetherGrass() {
		this(0, 0);
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		int var11;

		while (((var11 = getBlockId(par1World, par3, par4, par5)) == 0 || isLeaves(var11)) && par4 > 0) {
			--par4;
		}

		for (int var7 = 0; var7 < 128; ++var7) {
			int var8 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
			int var9 = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
			int var10 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);
			int var99 = par2Random.nextInt(9);

			if (isAirBlock(par1World, var8, var9, var10)
				&& getBlockId(par1World, var8, var9 - 1, var10) == Blocks.NETHERRACK.id()) {

				setBlock(par1World, var8, var9 - 1, var10, BOPNether.OVERGROWN_NETHERRACK.id(), 0, 2);

				if (var99 == 0) {
					setBlock(par1World, var8, var9, var10, Blocks.MUSHROOM_RED.id(), 0, 2);
				}

				if (var99 == 1) {
					setBlock(par1World, var8, var9, var10, Blocks.MUSHROOM_BROWN.id(), 0, 2);
				}

				if (var99 == 2) {
					setBlock(par1World, var8, var9, var10, BOPPlants.SHORT_GRASS.id(), 0, 2);
				}

				if (var99 == 5) {
					setBlock(par1World, var8, var9, var10, BOPPlants.MEDIUM_GRASS.id(), 0, 2);
				}

				if (var99 == 6) {
					setBlock(par1World, var8, var9, var10, BOPBlocks.WHEAT_GRASS.id(), 0, 2);
				}

				if (var99 == 7) {
					setBlock(par1World, var8, var9, var10, BOPPlants.DAMP_GRASS.id(), 0, 2);
				}

				if (var99 == 8) {
					setBlock(par1World, var8, var9, var10, Blocks.TALLGRASS.id(), 0, 2);
				}

				if (var99 == 3) {
					hellbarkStub(par1World, var8, var9, var10);
				}

				if (var99 == 4) {
					hellbarkStub(par1World, var8, var9, var10);
				}
			}
		}

		return true;
	}

	private static void hellbarkStub(World world, int x, int y, int z) {
		final int log = BOPWoodSets.HELLBARK.log.id();
		final int leaves = BOPWoodSets.HELLBARK.leaves.id();

		setBlock(world, x, y, z, log, 0, 2);

		if (isAirBlock(world, x, y + 1, z)) {
			setBlock(world, x, y + 1, z, log, 0, 2);
		}
		if (isAirBlock(world, x + 1, y + 1, z)) {
			setBlock(world, x + 1, y + 1, z, leaves, 0, 2);
		}
		if (isAirBlock(world, x - 1, y + 1, z)) {
			setBlock(world, x - 1, y + 1, z, leaves, 0, 2);
		}
		if (isAirBlock(world, x, y + 1, z + 1)) {
			setBlock(world, x, y + 1, z + 1, leaves, 0, 2);
		}
		if (isAirBlock(world, x, y + 1, z - 1)) {
			setBlock(world, x, y + 1, z - 1, leaves, 0, 2);
		}
		if (isAirBlock(world, x, y + 2, z)) {
			setBlock(world, x, y + 2, z, leaves, 0, 2);
		}
	}
}
