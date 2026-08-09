package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPPromisedLand;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeature;

import java.util.Random;

public class WorldGenCloud extends WorldFeature {

	private final int numberOfBlocks;

	public WorldGenCloud() {
		this(48);
	}

	public WorldGenCloud(int numberOfBlocks) {
		this.numberOfBlocks = numberOfBlocks;
	}

	@Override
	public boolean place(World world, Random random, int x, int y, int z) {

		if (random.nextInt(5) != 0) {
			return false;
		}

		float f = random.nextFloat() * (float)Math.PI;
		double d = x + 8 + MathHelper.sin(f) * numberOfBlocks / 8.0F;
		double d1 = x + 8 - MathHelper.sin(f) * numberOfBlocks / 8.0F;
		double d2 = z + 8 + MathHelper.cos(f) * numberOfBlocks / 8.0F;
		double d3 = z + 8 - MathHelper.cos(f) * numberOfBlocks / 8.0F;
		double d4 = y + random.nextInt(9) - 2;
		double d5 = y + random.nextInt(9) - 2;

		int cloud = BOPPromisedLand.CLOUD.id();

		for (int i = 0; i <= numberOfBlocks; ++i) {
			double d6 = d + (d1 - d) * i / numberOfBlocks;
			double d7 = d4 + (d5 - d4) * i / numberOfBlocks;
			double d8 = d2 + (d3 - d2) * i / numberOfBlocks;
			double d9 = random.nextDouble() * numberOfBlocks / 32.0D;
			double d10 = (MathHelper.sin(i * (float)Math.PI / numberOfBlocks) + 1.0F) * d9 + 1.0D;
			double d11 = (MathHelper.sin(i * (float)Math.PI / numberOfBlocks) + 1.0F) * d9 + 1.0D;
			int j = (int) Math.floor(d6 - d10 / 1.0D);
			int k = (int) Math.floor(d7 - d11 / 1.0D);
			int l = (int) Math.floor(d8 - d10 / 1.0D);
			int i1 = (int) Math.floor(d6 + d10 / 1.0D);
			int j1 = (int) Math.floor(d7 + d11 / 1.0D);
			int k1 = (int) Math.floor(d8 + d10 / 1.0D);

			for (int l1 = j; l1 <= i1; ++l1) {
				double d12 = (l1 + 0.5D - d6) / (d10 / 1.0D);

				if (d12 * d12 < 1.0D) {
					for (int i2 = k; i2 <= j1; ++i2) {
						double d13 = (i2 + 0.5D - d7) / (d11 / 1.0D);

						if (d12 * d12 + d13 * d13 < 1.0D) {
							for (int j2 = l; j2 <= k1; ++j2) {
								double d14 = (j2 + 0.5D - d8) / (d10 / 1.0D);

								if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D && world.isAirBlock(l1, i2, j2)) {
									world.setBlockAndMetadataRaw(l1, i2, j2, cloud, 0);
								}
							}
						}
					}
				}
			}
		}

		return true;
	}
}
