package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.block.BlockLogicBOPHangingMoss;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;

import java.util.Random;

public class WorldGenIvy extends BOPWorldFeature {

	private static final Side[] SIDES = {Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST};

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		Block<?> ivy = BOPPlants.IVY;
		int l = par3;

		int ceiling = 2 * par1World.getWorldType().getOceanY();

		for (int i1 = par5; par4 < ceiling; ++par4) {
			if (isAirBlock(par1World, par3, par4, par5)) {
				for (Side side : SIDES) {
					if (ivy.getLogic().canPlaceOnSide(par1World, new TilePos(par3, par4, par5), side)) {
						setBlock(par1World, par3, par4, par5, ivy.id(),
							BlockLogicBOPHangingMoss.attachmentData(side), 2);
						break;
					}
				}
			} else {
				par3 = l + par2Random.nextInt(4) - par2Random.nextInt(4);
				par5 = i1 + par2Random.nextInt(4) - par2Random.nextInt(4);
			}
		}

		return true;
	}
}
