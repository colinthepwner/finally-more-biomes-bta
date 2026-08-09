package com.betteroplenty.world;

import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

public final class BOPSuppression {
	private BOPSuppression() {}

	public static boolean suppressUngatedVanilla = false;

	public static boolean deferSnow = false;

	public static boolean suppressAt(@NotNull World world, int x, int y, int z) {
		return suppressUngatedVanilla && world.getBlockBiome(x, y, z) instanceof BiomeGenBase;
	}
}
