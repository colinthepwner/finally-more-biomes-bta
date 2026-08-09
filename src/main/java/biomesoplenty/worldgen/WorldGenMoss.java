package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.block.BlockLogicBOPHangingMoss;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenMoss extends BOPWorldFeature {

	private static final Side[] SIDES = {Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST};

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		Block<?> moss = BOPPlants.MOSS;
		int var6 = par3;

		int ceiling = par1World.getWorldType().getOceanY() + 16;

		for (int var7 = par5; par4 < ceiling; ++par4) {
			if (isAirBlock(par1World, par3, par4, par5)) {
				for (Side side : SIDES) {
					if (moss.getLogic().canPlaceOnSide(par1World,
							new net.minecraft.core.world.pos.TilePos(par3, par4, par5), side)) {
						int var999 = par2Random.nextInt(4);

						if (var999 == 0) {
							setBlock(par1World, par3, par4, par5, moss.id(),
								BlockLogicBOPHangingMoss.attachmentData(side), 2);
						}
						break;
					}
				}
			} else {
				par3 = var6 + par2Random.nextInt(4) - par2Random.nextInt(4);
				par5 = var7 + par2Random.nextInt(4) - par2Random.nextInt(4);
			}
		}

		return true;
	}
}
