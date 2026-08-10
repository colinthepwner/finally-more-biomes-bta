package com.betteroplenty.client;

import com.betteroplenty.entity.PhantomAuraBridge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.render.particle.Particle;
import net.minecraft.client.render.particle.ParticleDispatcher;
import net.minecraft.client.render.particle.ParticleEntry;
import net.minecraft.core.world.World;

public final class PhantomAuraClient {

	public static void install() {
		PhantomAuraBridge.sink = PhantomAuraClient::spawn;
	}

	private static void spawn(World world, double x, double y, double z, double red, double green, double blue) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc == null || mc.activeCamera == null || mc.particleEngine == null) {
			return;
		}

		double dx = mc.activeCamera.getX() - x;
		double dy = mc.activeCamera.getY() - y;
		double dz = mc.activeCamera.getZ() - z;
		double max = 16.0 * GameSettings.PARTICLE_RENDER_DISTANCE.value.intValue();
		if (dx * dx + dy * dy + dz * dz > max * max) {
			return;
		}

		ParticleEntry entry = ParticleDispatcher.getInstance().getDispatch("puffrgb");
		if (entry == null || !entry.enabled()) {
			return;
		}

		Particle fx = entry.newParticle(world, x, y, z, red, green, blue, -1);
		if (fx != null) {
			mc.particleEngine.add(fx);
		}
	}

	private PhantomAuraClient() {}
}
