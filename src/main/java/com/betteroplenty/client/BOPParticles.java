package com.betteroplenty.client;

import biomesoplenty.particles.ParticleDandelionBOP;
import biomesoplenty.particles.ParticleMagicTreeBOP;
import biomesoplenty.particles.ParticlePixieTrailBOP;
import biomesoplenty.particles.ParticleSteamBOP;
import com.betteroplenty.BetterOPlenty;
import net.minecraft.client.render.particle.ParticleDispatcher;
import net.minecraft.client.render.particle.ParticleEntry;
import turniplabs.halplibe.helper.ParticleHelper;

public final class BOPParticles {

	private BOPParticles() {
	}

	public static final String STEAM = "steam";
	public static final String DANDELION = "dandelion";
	public static final String MAGIC_TREE = "magictree";
	public static final String PIXIE_TRAIL = "pixietrail";

	public static void register() {
		ParticleDispatcher dispatcher = ParticleDispatcher.getInstance();
		int added = 0;

		added += add(dispatcher, STEAM,
			(world, x, y, z, mx, my, mz, data) -> new ParticleSteamBOP(world, x, y, z, mx, my, mz));
		added += add(dispatcher, MAGIC_TREE,
			(world, x, y, z, mx, my, mz, data) -> new ParticleMagicTreeBOP(world, x, y, z, mx, my, mz));
		added += add(dispatcher, PIXIE_TRAIL,
			(world, x, y, z, mx, my, mz, data) -> new ParticlePixieTrailBOP(world, x, y, z, mx, my, mz));

		added += add(dispatcher, DANDELION,
			(world, x, y, z, mx, my, mz, data) -> new ParticleDandelionBOP(world, x, y, z, 2.0F));

		BetterOPlenty.LOGGER.info("Registered {} of 4 BOP particle(s): {}, {}, {}, {}.",
			added, STEAM, MAGIC_TREE, PIXIE_TRAIL, DANDELION);
	}

	private static int add(ParticleDispatcher dispatcher, String id, ParticleEntry entry) {
		if (dispatcher.hasDispatch(id)) {
			BetterOPlenty.LOGGER.error(
				"Particle id '{}' is already registered; leaving it alone. BOP's own particle of "
					+ "that name will not appear, and whatever emits '{}' elsewhere would have "
					+ "started drawing BOP's if this had overwritten it.", id, id);
			return 0;
		}
		ParticleHelper.createParticle(id, entry);
		return 1;
	}
}
