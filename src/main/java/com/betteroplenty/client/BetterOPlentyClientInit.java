package com.betteroplenty.client;

import com.betteroplenty.BOPAudit;
import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.fluid.BOPFluids;
import com.betteroplenty.world.BOPBiomes;
import com.betteroplenty.world.WorldTypeBOP;
import com.betteroplenty.world.nether.WorldTypeNetherBOP;
import com.betteroplenty.world.promised.WorldTypePromisedLand;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.worldtype.WorldTypeFXDispatcher;
import net.minecraft.client.render.worldtype.WorldTypeFXNether;
import turniplabs.halplibe.event.defs.ClientEvents;
import turniplabs.halplibe.util.dependency.Key;

public class BetterOPlentyClientInit implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientEvents.AFTER_CLIENT_START.listen(Key.of(BetterOPlenty.MOD_ID), () -> {
			registerWorldTypeFX();

			BOPParticles.register();
			auditBiomeColors();

			AchievementPageBOP.register();

			BOPAudit.run();

			BOPFluids.audit();
		});
	}

	private void registerWorldTypeFX() {
		if (WorldTypeBOP.BOP == null) {
			BetterOPlenty.LOGGER.error("BOP world type is not registered; skipping its sky FX. "
				+ "Clouds would fall back to the Empty world's y=108 layer.");
			return;
		}

		WorldTypeFXDispatcher.getInstance().addDispatch(
			new WorldTypeFXBOP(WorldTypeBOP.BOP).setHasAurora(true));

		BetterOPlenty.LOGGER.info(
			"Registered BOP sky FX (clouds at the extended-overworld default, per-biome fog).");

		registerNetherFX();
		registerPromisedLandFX();
	}

	private void registerNetherFX() {
		if (WorldTypeNetherBOP.NETHER_BOP == null) {
			BetterOPlenty.LOGGER.error("BOP Nether world type is not registered; skipping its sky FX. "
				+ "Clouds would fall back to the Empty world's y=108 layer -- inside the Nether.");
			return;
		}

		WorldTypeFXDispatcher.getInstance().addDispatch(
			new WorldTypeFXNether(WorldTypeNetherBOP.NETHER_BOP)
				.setHasClouds(false)
				.setHasSky(false));

		BetterOPlenty.LOGGER.info(
			"Registered BOP Nether sky FX (no clouds, no sky, BTA's Nether fog).");
	}

	private void registerPromisedLandFX() {
		if (WorldTypePromisedLand.PROMISED_LAND == null) {
			BetterOPlenty.LOGGER.error("Promised Land world type is not registered; skipping its "
				+ "sky FX. Fog would fall back to the Empty world's pale haze, and clouds to its "
				+ "y=108 layer -- deep inside the islands.");
			return;
		}

		WorldTypeFXDispatcher.getInstance().addDispatch(
			new WorldTypeFXPromised(WorldTypePromisedLand.PROMISED_LAND)
				.setHasGround(false)
				.setCloudHeight(8.0F));

		BetterOPlenty.LOGGER.info(
			"Registered Promised Land sky FX (clouds at y=8, "
				+ "no ground plane, per-biome fog).");
	}

	private void auditBiomeColors() {
		int sky = 0;
		int water = 0;
		int fog = 0;

		for (BiomeGenBase biome : BOPBiomes.registered()) {
			try {
				if (biome.getClass().getMethod("getSkyColorByTemp", float.class)
						.getDeclaringClass() != BiomeGenBase.class) {
					sky++;
				}
			} catch (NoSuchMethodException impossible) {

				throw new AssertionError(impossible);
			}
			if (biome.getBiomeWaterColor() != -1) {
				water++;
			}
			if (biome.getBiomeFogColor() != -1) {
				fog++;
			}
		}

		BetterOPlenty.LOGGER.info(

			"Biome colours: of {} ported biome(s), {} set a sky colour (31 upstream do), "
				+ "{} a water tint (18 do), {} a fog colour (33 do, in BOP 1.2.1 only).",
			BOPBiomes.registered().size(), sky, water, fog);
	}
}
