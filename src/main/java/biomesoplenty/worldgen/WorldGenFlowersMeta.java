package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenFlowersMeta extends BOPWorldFeature {

	public final int plantID;
	public final int metadata;
	public int chances = 64;

	public WorldGenFlowersMeta(int id, int meta) {
		this.plantID = id;
		this.metadata = meta;
	}

	public WorldGenFlowersMeta(int id, int meta, int count) {
		this.plantID = id;
		this.metadata = meta;
		this.chances = count;
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		for (int iter = 0; iter < chances; ++iter) {
			int posX = x + random.nextInt(8) - random.nextInt(8);
			int posY = y + random.nextInt(4) - random.nextInt(4);
			int posZ = z + random.nextInt(8) - random.nextInt(8);

			if (world.isAirBlock(posX, posY, posZ) && canBlockStay(world, this.plantID, posX, posY, posZ)) {
				setBlock(world, posX, posY, posZ, this.plantID, this.metadata, 2);
			}
		}

		return true;
	}
}
