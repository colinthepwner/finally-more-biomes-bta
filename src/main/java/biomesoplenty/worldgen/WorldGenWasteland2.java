package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPWastes;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenWasteland2 extends BOPWorldFeature {

	@Override
	public boolean generate(World var1, Random var2, int var3, int var4, int var5)
	{
		final int DRIED = BOPWastes.DRIED_DIRT.id();

		while (isAirBlock(var1, var3, var4, var5) && var4 > 2)
		{
			--var4;
		}

		int var6 = getBlockId(var1, var3, var4, var5);

		if (var6 != DRIED)
			return false;
		else
		{
			for (int var7 = -2; var7 <= 2; ++var7)
			{
				for (int var8 = -2; var8 <= 2; ++var8)
				{
					if (isAirBlock(var1, var3 + var7, var4 - 1, var5 + var8) && isAirBlock(var1, var3 + var7, var4 - 2, var5 + var8) && !isAirBlock(var1, var3 + var7, var4, var5 + var8))
						return false;
				}
			}

			setBlock(var1, var3, var4, var5, DRIED);

			setBlock(var1, var3 - 1, var4, var5, DRIED);
			setBlock(var1, var3 + 1, var4, var5, DRIED);
			setBlock(var1, var3, var4, var5 - 1, DRIED);
			setBlock(var1, var3, var4, var5 + 1, DRIED);

			setBlock(var1, var3 - 1, var4, var5 - 1, DRIED);
			setBlock(var1, var3 + 1, var4, var5 + 1, DRIED);
			setBlock(var1, var3 - 1, var4, var5 + 1, DRIED);
			setBlock(var1, var3 + 1, var4, var5 - 1, DRIED);

			setBlock(var1, var3 - 2, var4, var5 - 1, DRIED);
			setBlock(var1, var3 - 2, var4, var5, DRIED);
			setBlock(var1, var3 - 2, var4, var5 + 1, DRIED);
			setBlock(var1, var3 + 2, var4, var5 - 1, DRIED);
			setBlock(var1, var3 + 2, var4, var5, DRIED);
			setBlock(var1, var3 + 2, var4, var5 + 1, DRIED);
			setBlock(var1, var3 - 1, var4, var5 - 2, DRIED);
			setBlock(var1, var3, var4, var5 - 2, DRIED);
			setBlock(var1, var3 + 1, var4, var5 - 2, DRIED);
			setBlock(var1, var3 - 1, var4, var5 + 2, DRIED);
			setBlock(var1, var3, var4, var5 + 2, DRIED);
			setBlock(var1, var3 + 1, var4, var5 + 2, DRIED);

			setBlock(var1, var3, var4 + 1, var5, DRIED);

			setBlock(var1, var3 - 1, var4 + 1, var5, DRIED);
			setBlock(var1, var3 + 1, var4 + 1, var5, DRIED);
			setBlock(var1, var3, var4 + 1, var5 - 1, DRIED);
			setBlock(var1, var3, var4 + 1, var5 + 1, DRIED);

			setBlock(var1, var3 - 1, var4 + 1, var5 - 1, DRIED);
			setBlock(var1, var3 + 1, var4 + 1, var5 + 1, DRIED);
			setBlock(var1, var3 - 1, var4 + 1, var5 + 1, DRIED);
			setBlock(var1, var3 + 1, var4 + 1, var5 - 1, DRIED);

			setBlock(var1, var3 - 2, var4 + 1, var5 - 1, DRIED);
			setBlock(var1, var3 - 2, var4 + 1, var5, DRIED);
			setBlock(var1, var3 - 2, var4 + 1, var5 + 1, DRIED);
			setBlock(var1, var3 + 2, var4 + 1, var5 - 1, DRIED);
			setBlock(var1, var3 + 2, var4 + 1, var5, DRIED);
			setBlock(var1, var3 + 2, var4 + 1, var5 + 1, DRIED);
			setBlock(var1, var3 - 1, var4 + 1, var5 - 2, DRIED);
			setBlock(var1, var3, var4 + 1, var5 - 2, DRIED);
			setBlock(var1, var3 + 1, var4 + 1, var5 - 2, DRIED);
			setBlock(var1, var3 - 1, var4 + 1, var5 + 2, DRIED);
			setBlock(var1, var3, var4 + 1, var5 + 2, DRIED);
			setBlock(var1, var3 + 1, var4 + 1, var5 + 2, DRIED);

			setBlock(var1, var3 - 1, var4 + 2, var5, DRIED);
			setBlock(var1, var3 + 1, var4 + 2, var5, DRIED);
			setBlock(var1, var3, var4 + 2, var5 - 1, DRIED);
			setBlock(var1, var3, var4 + 2, var5 + 1, DRIED);
			setBlock(var1, var3 - 1, var4 + 2, var5 - 1, DRIED);
			setBlock(var1, var3 + 1, var4 + 2, var5 + 1, DRIED);
			setBlock(var1, var3 - 1, var4 + 2, var5 + 1, DRIED);
			setBlock(var1, var3 + 1, var4 + 2, var5 - 1, DRIED);

			setBlock(var1, var3 - 2, var4 + 2, var5 - 1, DRIED);
			setBlock(var1, var3 - 2, var4 + 2, var5, DRIED);
			setBlock(var1, var3 - 2, var4 + 2, var5 + 1, DRIED);
			setBlock(var1, var3 + 2, var4 + 2, var5 - 1, DRIED);
			setBlock(var1, var3 + 2, var4 + 2, var5, DRIED);
			setBlock(var1, var3 + 2, var4 + 2, var5 + 1, DRIED);
			setBlock(var1, var3 - 1, var4 + 2, var5 - 2, DRIED);
			setBlock(var1, var3, var4 + 2, var5 - 2, DRIED);
			setBlock(var1, var3 + 1, var4 + 2, var5 - 2, DRIED);
			setBlock(var1, var3 - 1, var4 + 2, var5 + 2, DRIED);
			setBlock(var1, var3, var4 + 2, var5 + 2, DRIED);
			setBlock(var1, var3 + 1, var4 + 2, var5 + 2, DRIED);

			setBlock(var1, var3, var4 + 2, var5, DRIED);

			setBlock(var1, var3 - 1, var4 + 3, var5, DRIED);
			setBlock(var1, var3 + 1, var4 + 3, var5, DRIED);
			setBlock(var1, var3, var4 + 3, var5 - 1, DRIED);
			setBlock(var1, var3, var4 + 3, var5 + 1, DRIED);
			setBlock(var1, var3 - 1, var4 + 3, var5 - 1, DRIED);
			setBlock(var1, var3 + 1, var4 + 3, var5 + 1, DRIED);
			setBlock(var1, var3 - 1, var4 + 3, var5 + 1, DRIED);
			setBlock(var1, var3 + 1, var4 + 3, var5 - 1, DRIED);

			setBlock(var1, var3, var4 + 3, var5, DRIED);

			setBlock(var1, var3 - 1, var4 + 4, var5, DRIED);
			setBlock(var1, var3 + 1, var4 + 4, var5, DRIED);
			setBlock(var1, var3, var4 + 4, var5 - 1, DRIED);
			setBlock(var1, var3, var4 + 4, var5 + 1, DRIED);
			setBlock(var1, var3 - 1, var4 + 4, var5 - 1, DRIED);
			setBlock(var1, var3 + 1, var4 + 4, var5 + 1, DRIED);
			setBlock(var1, var3 - 1, var4 + 4, var5 + 1, DRIED);
			setBlock(var1, var3 + 1, var4 + 4, var5 - 1, DRIED);

			setBlock(var1, var3, var4 + 4, var5, DRIED);

			setBlock(var1, var3 - 1, var4 + 5, var5, DRIED);
			setBlock(var1, var3 + 1, var4 + 5, var5, DRIED);
			setBlock(var1, var3, var4 + 5, var5 - 1, DRIED);
			setBlock(var1, var3, var4 + 5, var5 + 1, DRIED);
			setBlock(var1, var3 - 1, var4 + 5, var5 - 1, DRIED);
			setBlock(var1, var3 + 1, var4 + 5, var5 + 1, DRIED);
			setBlock(var1, var3 - 1, var4 + 5, var5 + 1, DRIED);
			setBlock(var1, var3 + 1, var4 + 5, var5 - 1, DRIED);

			setBlock(var1, var3, var4 + 5, var5, DRIED);

			setBlock(var1, var3 - 1, var4 + 6, var5, DRIED);
			setBlock(var1, var3 + 1, var4 + 6, var5, DRIED);
			setBlock(var1, var3, var4 + 6, var5 - 1, DRIED);
			setBlock(var1, var3, var4 + 6, var5 + 1, DRIED);

			setBlock(var1, var3, var4 + 6, var5, DRIED);

			setBlock(var1, var3 - 1, var4 + 7, var5, DRIED);
			setBlock(var1, var3 + 1, var4 + 7, var5, DRIED);
			setBlock(var1, var3, var4 + 7, var5 - 1, DRIED);
			setBlock(var1, var3, var4 + 7, var5 + 1, DRIED);

			setBlock(var1, var3, var4 + 7, var5, DRIED);

			setBlock(var1, var3 - 1, var4 + 8, var5, DRIED);
			setBlock(var1, var3 + 1, var4 + 8, var5, DRIED);
			setBlock(var1, var3, var4 + 8, var5 - 1, DRIED);
			setBlock(var1, var3, var4 + 8, var5 + 1, DRIED);

			setBlock(var1, var3, var4 + 8, var5, DRIED);
			setBlock(var1, var3, var4 + 9, var5, DRIED);
			setBlock(var1, var3, var4 + 10, var5, DRIED);
			setBlock(var1, var3, var4 + 11, var5, DRIED);
			setBlock(var1, var3, var4 + 12, var5, DRIED);

			return true;
		}
	}
}
