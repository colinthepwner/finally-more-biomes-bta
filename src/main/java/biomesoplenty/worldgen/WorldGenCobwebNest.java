package biomesoplenty.worldgen;

import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.monster.MobSpider;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenCobwebNest extends BOPWorldFeature {

	@SuppressWarnings("unused") private final int field_76527_a;
	@SuppressWarnings("unused") private final int field_76526_b;

	public WorldGenCobwebNest(int par1, int par2) {
		this.field_76526_b = par1;
		this.field_76527_a = par2;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		final int webId = Blocks.COBWEB.id();

		int var15;

		for (; ((var15 = getBlockId(par1World, par3, par4, par5)) == 0 || var15 == webId) && par4 > 0; --par4) {
			;
		}

		int var7 = getBlockId(par1World, par3, par4, par5);

		if (var7 == Blocks.GRASS.id()) {
			++par4;
			setBlockAndMetadata(par1World, par3, par4, par5, webId, 0);

			int var999 = par2Random.nextInt(3);

			if (var999 == 0) {
				MobSpider spider = new MobSpider(par1World);
				spider.moveTo(par3, par4 + 1, par5, 0.0F, 0.0F);
				par1World.entityJoinedWorld(spider);
			}

			for (int var8 = par4; var8 <= par4 + 1; ++var8) {
				int var9 = var8 - par4;
				int var10 = 2 - var9;

				for (int var11 = par3 - var10; var11 <= par3 + var10; ++var11) {
					int var12 = var11 - par3;

					for (int var13 = par5 - var10; var13 <= par5 + var10; ++var13) {
						int var14 = var13 - par5;

						if ((Math.abs(var12) != var10 || Math.abs(var14) != var10 || par2Random.nextInt(2) != 0)
								&& !isOpaqueCube(par1World, var11, var8, var13)) {
							setBlockAndMetadata(par1World, var11, var8, var13, webId, 0);
						}
					}
				}
			}
		}

		return true;
	}
}
