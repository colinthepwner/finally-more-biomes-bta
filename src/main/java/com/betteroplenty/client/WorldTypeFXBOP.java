package com.betteroplenty.client;

import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.client.render.colorizer.Colorizers;
import net.minecraft.client.render.worldtype.WorldTypeFXOverworld;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.type.WorldType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class WorldTypeFXBOP extends WorldTypeFXOverworld {

	public WorldTypeFXBOP(@NotNull WorldType worldType) {
		super(worldType);
	}

	private Vector3f lastFogColor = null;

	@NotNull
	private final Vector3f biomeTarget = new Vector3f();
	@NotNull
	private final Vector3f fogColorOut = new Vector3f();

	@NotNull
	@Override
	public Vector3fc getFogColor(@NotNull World world, double x, double y, double z,
			float celestialAngle, float partialTick) {
		Vector3fc target = super.getFogColor(world, x, y, z, celestialAngle, partialTick);
		if (!Colorizers.fog.isEnabled()) {
			Biome biome = world.getBiomeProvider()
				.getBiome(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
			if (biome instanceof BiomeGenBase bop) {
				int color = bop.getBiomeFogColor();
				if (color != -1) {
					float dayProgress = MathHelper.cos(celestialAngle * (float) Math.PI * 2.0F) * 2.0F + 0.5F;
					dayProgress = MathHelper.clamp(dayProgress, 0.0F, 1.0F);
					float r = (color >> 16 & 0xFF) / 255.0F * (dayProgress * 0.94F + 0.06F);
					float g = (color >> 8 & 0xFF) / 255.0F * (dayProgress * 0.94F + 0.06F);
					float b = (color & 0xFF) / 255.0F * (dayProgress * 0.91F + 0.09F);
					target = this.biomeTarget.set(r, g, b);
				}
			}
		}

		if (lastFogColor == null) {
			lastFogColor = new Vector3f(target);
		} else {
			float rate = 0.005F;
			float dx = target.x() - lastFogColor.x;
			float dy = target.y() - lastFogColor.y;
			float dz = target.z() - lastFogColor.z;
			lastFogColor.x += MathHelper.clamp(dx, -rate, rate);
			lastFogColor.y += MathHelper.clamp(dy, -rate, rate);
			lastFogColor.z += MathHelper.clamp(dz, -rate, rate);
		}

		return this.fogColorOut.set(lastFogColor);
	}
}
