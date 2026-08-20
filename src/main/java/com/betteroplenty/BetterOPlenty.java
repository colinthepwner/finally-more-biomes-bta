package com.betteroplenty;

import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPGroundCoverRecipes;
import com.betteroplenty.block.BOPWoodSet;
import com.betteroplenty.entity.BOPEntities;
import com.betteroplenty.entity.BOPProjectiles;
import com.betteroplenty.entity.BOPSpawnAudit;
import com.betteroplenty.fluid.BOPFluidRecipes;
import com.betteroplenty.fluid.BOPFluids;
import com.betteroplenty.item.BOPItems;
import com.betteroplenty.world.BOPBiomes;
import com.betteroplenty.world.BOPWorldAudit;
import com.betteroplenty.world.BiomeProviderBOP;
import com.betteroplenty.world.WorldTypeBOP;
import com.betteroplenty.world.nether.WorldTypeNetherBOP;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.event.defs.CommonEvents;
import turniplabs.halplibe.util.dependency.Key;

public class BetterOPlenty implements ModInitializer {
	public static final String MOD_ID = HalpLibe.registerMod("betteroplenty", true);
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CommonEvents.BEFORE_GAME_START.listen(Key.of(MOD_ID), this::beforeGameStart);
		CommonEvents.AFTER_GAME_START.listen(Key.of(MOD_ID), this::afterGameStartGuarded);

		CommonEvents.RECIPES_NAMESPACE_INIT.listen(Key.of(MOD_ID), BOPRecipeNamespaces::initNamespaces);
		LOGGER.info("Finally More Biomes initialized.");
	}

	private void beforeGameStart() {

		com.betteroplenty.block.BOPCrops.register();
		BOPItems.register();
		com.betteroplenty.item.BOPFoods.register();
		BOPBlocks.register();

		com.betteroplenty.block.BOPPromisedLand.register();
		com.betteroplenty.block.BOPOreVariants.register();

		BOPFluids.register();

		BOPEntities.register();

		BOPProjectiles.register();

		BOPBiomes.register();
		BiomeProviderBOP.init();

		WorldTypeNetherBOP.register();

		com.betteroplenty.world.promised.WorldTypePromisedLand.register();
		com.betteroplenty.world.promised.DimensionPromisedLand.create();

		com.betteroplenty.block.BOPPromisedLand.registerPortal();
		WorldTypeBOP.register();
	}

	private void afterGameStartGuarded() {
		try {
			afterGameStart();
		} catch (Throwable t) {
			LOGGER.error("afterGameStart failed; parts of this mod may be missing. Deliberately "
				+ "swallowed: throwing here would cancel afterGameStart for every mod loaded after "
				+ "this one.", t);
		}
	}

	private void afterGameStart() {

		com.betteroplenty.world.promised.DimensionPromisedLand.register();
		WorldTypeBOP.registerWorldTypeGroup();

		BOPSounds.register();

		BOPWoodSet.joinItemGroups();
		BOPRecipes.register();

		BOPFluidRecipes.register();

		BOPGroundCoverRecipes.register();

		BOPAchievements.register();

		net.minecraft.core.world.biome.provider.BiomeProviderNether.init();
		LOGGER.info("Rebuilt BTA's Nether biome-range grid on final biome ids "
			+ "(it locks from a static initialiser this mod touches mid-registration).");

		BOPWorldAudit.run();
		BOPSpawnAudit.run();
		BOPIdManifest.run();
	}
}
