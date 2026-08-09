package com.betteroplenty.mixin;

import net.minecraft.core.world.generate.chunk.perlin.overworld.ChunkDecoratorOverworld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = ChunkDecoratorOverworld.class, remap = false)
public interface ChunkDecoratorSnowInvoker {

	@Invoker("applySnowAndIceForColumn")
	void betteroplenty$applySnowAndIceForColumn(int dx, int dz, int oceanY);
}
