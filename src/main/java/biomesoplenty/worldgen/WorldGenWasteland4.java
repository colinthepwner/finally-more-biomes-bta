package biomesoplenty.worldgen;

import com.betteroplenty.block.BOPWastes;
import com.betteroplenty.compat.BOPWorldFeature;
import net.minecraft.core.world.World;

import java.util.Random;

public class WorldGenWasteland4 extends BOPWorldFeature {

	@Override
	public boolean generate(World var1, Random var2, int var3, int var4, int var5)
	{
		final int DRIED = BOPWastes.DRIED_DIRT.id();

		while (isAirBlock(var1, var3, var4, var5) && var4 > 2)
		{
			--var4;
		}

		int var6 = getBlockId(var1, var3, var4, var5);
		int var95 = getBlockId(var1, var3 - 1, var4, var5);
		int var96 = getBlockId(var1, var3 + 1, var4, var5);
		int var97 = getBlockId(var1, var3, var4, var5 - 1);
		int var98 = getBlockId(var1, var3, var4, var5 + 1);

		if (var6 != DRIED || var95 != DRIED || var96 != DRIED || var97 != DRIED || var98 != DRIED )
			return false;
		else
		{
			for (int var7 = -2; var7 <= 2; ++var7)
			{
				for (int var8 = -2; var8 <= 2; ++var8)
				{
					if (isAirBlock(var1, var3 + var7, var4 - 1, var5 + var8) && isAirBlock(var1, var3 + var7, var4 - 2, var5 + var8))
						return false;
				}
			}

			int var999 = var2.nextInt(2);

			if (var999 == 0)
			{
				setBlock(var1, var3, var4 - 1, var5, 0);
				setBlock(var1, var3 + 1, var4 - 1, var5, 0);
				setBlock(var1, var3 - 1, var4 - 1, var5, 0);
				setBlock(var1, var3, var4 - 1, var5 + 1, 0);
				setBlock(var1, var3, var4 - 1, var5 - 1, 0);
				setBlock(var1, var3, var4 - 2, var5, 0);
				return true;
			}
			if (var999 == 1)
			{
				setBlock(var1, var3, var4 - 1, var5, 0);
				return true;
			}

			return true;
		}
	}
}
