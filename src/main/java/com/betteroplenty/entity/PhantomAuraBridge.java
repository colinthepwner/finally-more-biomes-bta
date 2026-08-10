package com.betteroplenty.entity;

import net.minecraft.core.world.World;

public final class PhantomAuraBridge {

	public interface Sink {
		void spawn(World world, double x, double y, double z, double red, double green, double blue);
	}

	public static volatile Sink sink;

	private PhantomAuraBridge() {}
}
