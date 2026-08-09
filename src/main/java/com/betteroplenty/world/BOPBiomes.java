package com.betteroplenty.world;

import biomesoplenty.biomes.BiomeGenAlps;
import biomesoplenty.biomes.BiomeGenAutumnHills;
import biomesoplenty.biomes.BiomeGenBadlands;
import biomesoplenty.biomes.BiomeGenBrushland;
import biomesoplenty.biomes.BiomeGenDunes;
import biomesoplenty.biomes.BiomeGenWasteland;
import biomesoplenty.biomes.BiomeGenVolcano;
import biomesoplenty.biomes.BiomeGenDeadlands;
import biomesoplenty.biomes.BiomeGenBirchForest;
import biomesoplenty.biomes.BiomeGenMesa;
import biomesoplenty.biomes.BiomeGenConiferousForest;
import biomesoplenty.biomes.BiomeGenGrove;
import biomesoplenty.biomes.BiomeGenHotSprings;
import biomesoplenty.biomes.BiomeGenJadeCliffs;
import biomesoplenty.biomes.BiomeGenMountain;
import biomesoplenty.biomes.BiomeGenMysticGrove;
import biomesoplenty.biomes.BiomeGenMysticGroveThin;
import biomesoplenty.biomes.BiomeGenOasis;
import biomesoplenty.biomes.BiomeGenSacredSprings;
import biomesoplenty.biomes.BiomeGenShield;
import biomesoplenty.biomes.BiomeGenWetland;
import biomesoplenty.biomes.BiomeGenWoodland;
import biomesoplenty.biomes.BiomeGenAlpsBase;
import biomesoplenty.biomes.BiomeGenBambooForest;
import biomesoplenty.biomes.BiomeGenFungiForest;
import biomesoplenty.biomes.BiomeGenRainforest;
import biomesoplenty.biomes.BiomeGenTropicalRainforest;
import biomesoplenty.biomes.BiomeGenTropics;
import biomesoplenty.biomes.BiomeGenBayou;
import biomesoplenty.biomes.BiomeGenFen;
import biomesoplenty.biomes.BiomeGenLushSwamp;
import biomesoplenty.biomes.BiomeGenMarsh;
import biomesoplenty.biomes.BiomeGenMoor;
import biomesoplenty.biomes.BiomeGenSilkglades;
import biomesoplenty.biomes.BiomeGenAlpsForest;
import biomesoplenty.biomes.BiomeGenArctic;
import biomesoplenty.biomes.BiomeGenBorealForest;
import biomesoplenty.biomes.BiomeGenCherryBlossomGrove;
import biomesoplenty.biomes.BiomeGenDeciduousForest;
import biomesoplenty.biomes.BiomeGenGarden;
import biomesoplenty.biomes.BiomeGenSeasonalForest;
import biomesoplenty.biomes.BiomeGenSeasonalSpruceForest;
import biomesoplenty.biomes.BiomeGenTemperateRainforest;
import biomesoplenty.biomes.BiomeGenTimber;
import biomesoplenty.biomes.BiomeGenTimberThin;
import biomesoplenty.biomes.BiomeGenConiferousForestSnow;
import biomesoplenty.biomes.BiomeGenCrag;
import biomesoplenty.biomes.BiomeGenDeadForest;
import biomesoplenty.biomes.BiomeGenDeadForestSnow;
import biomesoplenty.biomes.BiomeGenField;
import biomesoplenty.biomes.BiomeGenFrostForest;
import biomesoplenty.biomes.BiomeGenGlacier;
import biomesoplenty.biomes.BiomeGenIcyHills;
import biomesoplenty.biomes.BiomeGenMapleWoods;
import biomesoplenty.biomes.BiomeGenTundra;
import biomesoplenty.biomes.BiomeGenFieldForest;
import biomesoplenty.biomes.BiomeGenCanyon;
import biomesoplenty.biomes.BiomeGenCanyonRavine;
import biomesoplenty.biomes.BiomeGenChaparral;
import biomesoplenty.biomes.BiomeGenHeathland;
import biomesoplenty.biomes.BiomeGenLushDesert;
import biomesoplenty.biomes.BiomeGenOrchard;
import biomesoplenty.biomes.BiomeGenOutback;
import biomesoplenty.biomes.BiomeGenPasture;
import biomesoplenty.biomes.BiomeGenPastureMeadow;
import biomesoplenty.biomes.BiomeGenPastureThin;
import biomesoplenty.biomes.BiomeGenGrassland;
import biomesoplenty.biomes.BiomeGenHighland;
import biomesoplenty.biomes.BiomeGenLavenderFields;
import biomesoplenty.biomes.BiomeGenMeadow;
import biomesoplenty.biomes.BiomeGenMeadowForest;
import biomesoplenty.biomes.BiomeGenOvergrownGreens;
import biomesoplenty.biomes.BiomeGenPolar;
import biomesoplenty.biomes.BiomeGenPrairie;
import biomesoplenty.biomes.BiomeGenRedwoodForest;
import biomesoplenty.biomes.BiomeGenSavanna;
import biomesoplenty.biomes.BiomeGenSavannaPlateau;
import biomesoplenty.biomes.BiomeGenScrubland;
import biomesoplenty.biomes.BiomeGenShrubland;
import biomesoplenty.biomes.BiomeGenShrublandForest;
import biomesoplenty.biomes.BiomeGenSpruceWoods;
import biomesoplenty.biomes.BiomeGenSteppe;
import biomesoplenty.biomes.BiomeGenThicket;

import biomesoplenty.biomes.BiomeGenBog;
import biomesoplenty.biomes.BiomeGenDeadSwamp;
import biomesoplenty.biomes.BiomeGenMangrove;
import biomesoplenty.biomes.BiomeGenOminousWoods;
import biomesoplenty.biomes.BiomeGenOminousWoodsThick;
import biomesoplenty.biomes.BiomeGenQuagmire;
import biomesoplenty.biomes.BiomeGenSludgepit;
import biomesoplenty.biomes.BiomeGenShore;
import biomesoplenty.biomes.beach.BiomeGenBeachGravel;
import biomesoplenty.biomes.beach.BiomeGenBeachOvergrown;
import biomesoplenty.biomes.ocean.BiomeGenOcean;
import biomesoplenty.biomes.ocean.BiomeGenOceanAbyss;
import biomesoplenty.biomes.ocean.BiomeGenOceanCoral;
import biomesoplenty.biomes.ocean.BiomeGenOceanKelp;
import biomesoplenty.biomes.nether.BiomeGenNetherBase;
import biomesoplenty.biomes.nether.BiomeGenNetherBlood;
import biomesoplenty.biomes.nether.BiomeGenNetherBone;
import biomesoplenty.biomes.nether.BiomeGenNetherDesert;
import biomesoplenty.biomes.nether.BiomeGenNetherGarden;
import biomesoplenty.biomes.nether.BiomeGenNetherLava;
import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.world.nether.BOPNetherClimate;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class BOPBiomes {
	private BOPBiomes() {}

	public static final int BIOME_ID_CEILING = 254;

	private static final List<BiomeGenBase> REGISTERED = new ArrayList<>();
	private static final Map<String, BiomeGenBase> BY_PLAN_ID = new LinkedHashMap<>();
	private static final Set<BiomeGenBase> TERRAIN_PLACED = new LinkedHashSet<>();

	private static final List<BiomeGenBase> NETHER = new ArrayList<>();

	private static final List<BiomeGenBase> PROMISED_LAND = new ArrayList<>();

	public static BiomeGenLavenderFields LAVENDER_FIELDS;
	public static BiomeGenAlps ALPS;
	public static BiomeGenAlpsForest ALPS_FOREST;
	public static BiomeGenAlpsBase ALPS_BASE;

	public static BiomeGenThicket THICKET;
	public static BiomeGenHighland HIGHLAND;
	public static BiomeGenPolar POLAR;
	public static BiomeGenGrassland GRASSLAND;

	public static BiomeGenOutback OUTBACK;
	public static BiomeGenHeathland HEATHLAND;
	public static BiomeGenLushDesert LUSH_DESERT;
	public static BiomeGenCanyon CANYON;
	public static BiomeGenCanyonRavine CANYON_RAVINE;
	public static BiomeGenChaparral CHAPARRAL;
	public static BiomeGenOrchard ORCHARD;
	public static BiomeGenPasture PASTURE;
	public static BiomeGenPastureMeadow PASTURE_MEADOW;
	public static BiomeGenPastureThin PASTURE_THIN;

	public static BiomeGenShrubland SHRUBLAND;
	public static BiomeGenShrublandForest SHRUBLAND_FOREST;
	public static BiomeGenField FIELD;
	public static BiomeGenFieldForest FIELD_FOREST;
	public static BiomeGenMeadow MEADOW;
	public static BiomeGenMeadowForest MEADOW_FOREST;
	public static BiomeGenSpruceWoods SPRUCE_WOODS;
	public static BiomeGenBorealForest BOREAL_FOREST;
	public static BiomeGenRedwoodForest REDWOOD_FOREST;
	public static BiomeGenPrairie PRAIRIE;

	public static BiomeGenScrubland SCRUBLAND;
	public static BiomeGenDeadForest DEAD_FOREST;
	public static BiomeGenSavanna SAVANNA;
	public static BiomeGenSavannaPlateau SAVANNA_PLATEAU;
	public static BiomeGenSteppe STEPPE;
	public static BiomeGenOvergrownGreens OVERGROWN_GREENS;

	public static BiomeGenArctic ARCTIC;
	public static BiomeGenGlacier GLACIER;
	public static BiomeGenIcyHills ICY_HILLS;
	public static BiomeGenFrostForest FROST_FOREST;
	public static BiomeGenConiferousForestSnow CONIFEROUS_FOREST_SNOW;
	public static BiomeGenDeadForestSnow DEAD_FOREST_SNOW;
	public static BiomeGenTundra TUNDRA;
	public static BiomeGenMapleWoods MAPLE_WOODS;
	public static BiomeGenCrag CRAG;

	public static BiomeGenCherryBlossomGrove CHERRY_BLOSSOM_GROVE;
	public static BiomeGenDeciduousForest DECIDUOUS_FOREST;
	public static BiomeGenGarden GARDEN;
	public static BiomeGenSeasonalForest SEASONAL_FOREST;
	public static BiomeGenSeasonalSpruceForest SEASONAL_SPRUCE_FOREST;
	public static BiomeGenTemperateRainforest TEMPERATE_RAINFOREST;
	public static BiomeGenTimber TIMBER;
	public static BiomeGenTimberThin TIMBER_THIN;

	public static BiomeGenMountain MOUNTAIN;
	public static BiomeGenJadeCliffs JADE_CLIFFS;
	public static BiomeGenAutumnHills AUTUMN_HILLS;
	public static BiomeGenConiferousForest CONIFEROUS_FOREST;
	public static BiomeGenGrove GROVE;

	public static BiomeGenBirchForest BIRCH_FOREST;
	public static BiomeGenHotSprings HOT_SPRINGS;
	public static BiomeGenShield SHIELD;

	public static BiomeGenMysticGrove MYSTIC_GROVE;

	public static BiomeGenMysticGroveThin MYSTIC_GROVE_THIN;
	public static BiomeGenOasis OASIS;
	public static BiomeGenSacredSprings SACRED_SPRINGS;
	public static BiomeGenWoodland WOODLAND;
	public static BiomeGenWetland WETLAND;

	public static BiomeGenBayou BAYOU;
	public static BiomeGenMarsh MARSH;
	public static BiomeGenSilkglades SILKGLADES;
	public static BiomeGenMoor MOOR;
	public static BiomeGenFen FEN;
	public static BiomeGenLushSwamp LUSH_SWAMP;

	public static BiomeGenBambooForest BAMBOO_FOREST;
	public static BiomeGenTropicalRainforest TROPICAL_RAINFOREST;
	public static BiomeGenRainforest RAINFOREST;
	public static BiomeGenTropics TROPICS;
	public static BiomeGenTropics TROPICS_MOUNTAIN;
	public static BiomeGenFungiForest FUNGI_FOREST;

	public static BiomeGenOminousWoods OMINOUS_WOODS;
	public static BiomeGenOminousWoodsThick OMINOUS_WOODS_THICK;
	public static BiomeGenDeadSwamp DEAD_SWAMP;
	public static BiomeGenQuagmire QUAGMIRE;
	public static BiomeGenSludgepit SLUDGEPIT;
	public static BiomeGenMangrove MANGROVE;
	public static BiomeGenBog BOG;

	public static BiomeGenBadlands BADLANDS;
	public static BiomeGenMesa MESA;
	public static BiomeGenDeadlands DEADLANDS;
	public static BiomeGenVolcano VOLCANO;
	public static BiomeGenWasteland WASTELAND;
	public static BiomeGenBrushland BRUSHLAND;
	public static BiomeGenDunes DUNES;

	public static BiomeGenOcean OCEAN;

	public static BiomeGenShore SHORE;
	public static BiomeGenBeachGravel BEACH_GRAVEL;
	public static BiomeGenBeachOvergrown BEACH_OVERGROWN;
	public static BiomeGenOceanAbyss OCEAN_ABYSS;
	public static BiomeGenOceanCoral OCEAN_CORAL;
	public static BiomeGenOceanKelp OCEAN_KELP;

	public static BiomeGenNetherBase NETHER_BASE;
	public static BiomeGenNetherGarden NETHER_GARDEN;

	public static biomesoplenty.biomes.promisedland.BiomeGenPromisedLandPlains PROMISED_LAND_PLAINS;

	public static biomesoplenty.biomes.promisedland.BiomeGenPromisedLandForest PROMISED_LAND_FOREST;

	public static biomesoplenty.biomes.promisedland.BiomeGenPromisedLandShrub PROMISED_LAND_SHRUB;

	public static biomesoplenty.biomes.promisedland.BiomeGenPromisedLandSwamp PROMISED_LAND_SWAMP;
	public static BiomeGenNetherDesert NETHER_DESERT;
	public static BiomeGenNetherLava NETHER_LAVA;
	public static BiomeGenNetherBone NETHER_BONE;
	public static BiomeGenNetherBlood NETHER_BLOOD;

	public static void register() {
		LAVENDER_FIELDS = add("lavender_fields", BiomeGenLavenderFields::new);

		ALPS = add("alps", BiomeGenAlps::new);
		ALPS_FOREST = add("alps_forest", BiomeGenAlpsForest::new);
		ALPS_BASE = add("alps_base", BiomeGenAlpsBase::new);

		THICKET = add("thicket", BiomeGenThicket::new);
		HIGHLAND = add("highland", BiomeGenHighland::new);
		POLAR = add("polar", BiomeGenPolar::new);
		GRASSLAND = add("grassland", BiomeGenGrassland::new);

		OUTBACK = add("outback", BiomeGenOutback::new);
		HEATHLAND = add("heathland", BiomeGenHeathland::new);
		LUSH_DESERT = add("lush_desert", BiomeGenLushDesert::new);
		CANYON = add("canyon", BiomeGenCanyon::new);
		CANYON_RAVINE = add("canyon_ravine", BiomeGenCanyonRavine::new);
		CHAPARRAL = add("chaparral", BiomeGenChaparral::new);
		ORCHARD = add("orchard", BiomeGenOrchard::new);
		PASTURE = add("pasture", BiomeGenPasture::new);
		PASTURE_MEADOW = add("pasture_meadow", BiomeGenPastureMeadow::new);
		PASTURE_THIN = add("pasture_thin", BiomeGenPastureThin::new);

		SHRUBLAND = add("shrubland", BiomeGenShrubland::new);
		SHRUBLAND_FOREST = add("shrubland_forest", BiomeGenShrublandForest::new);
		FIELD = add("field", BiomeGenField::new);
		FIELD_FOREST = add("field_forest", BiomeGenFieldForest::new);
		MEADOW = add("meadow", BiomeGenMeadow::new);
		MEADOW_FOREST = add("meadow_forest", BiomeGenMeadowForest::new);
		SPRUCE_WOODS = add("spruce_woods", BiomeGenSpruceWoods::new);
		BOREAL_FOREST = add("boreal_forest", BiomeGenBorealForest::new);
		REDWOOD_FOREST = add("redwood_forest", BiomeGenRedwoodForest::new);
		PRAIRIE = add("prairie", BiomeGenPrairie::new);

		SCRUBLAND = add("scrubland", BiomeGenScrubland::new);
		DEAD_FOREST = add("dead_forest", BiomeGenDeadForest::new);
		SAVANNA = add("savanna", BiomeGenSavanna::new);
		SAVANNA_PLATEAU = add("savanna_plateau", BiomeGenSavannaPlateau::new);
		STEPPE = add("steppe", BiomeGenSteppe::new);
		OVERGROWN_GREENS = add("overgrown_greens", BiomeGenOvergrownGreens::new);

		ARCTIC = add("arctic", BiomeGenArctic::new);
		GLACIER = add("glacier", BiomeGenGlacier::new);
		ICY_HILLS = add("icy_hills", BiomeGenIcyHills::new);
		FROST_FOREST = add("frost_forest", BiomeGenFrostForest::new);
		CONIFEROUS_FOREST_SNOW = add("coniferous_forest_snow", BiomeGenConiferousForestSnow::new);
		DEAD_FOREST_SNOW = add("dead_forest_snow", BiomeGenDeadForestSnow::new);
		TUNDRA = add("tundra", BiomeGenTundra::new);
		MAPLE_WOODS = add("maple_woods", BiomeGenMapleWoods::new);
		CRAG = add("crag", BiomeGenCrag::new);

		CHERRY_BLOSSOM_GROVE = add("cherry_blossom_grove", BiomeGenCherryBlossomGrove::new);
		DECIDUOUS_FOREST = add("deciduous_forest", BiomeGenDeciduousForest::new);
		GARDEN = add("garden", BiomeGenGarden::new);
		SEASONAL_FOREST = add("seasonal_forest", BiomeGenSeasonalForest::new);
		SEASONAL_SPRUCE_FOREST = add("seasonal_spruce_forest", BiomeGenSeasonalSpruceForest::new);
		TEMPERATE_RAINFOREST = add("temperate_rainforest", BiomeGenTemperateRainforest::new);
		TIMBER = add("timber", BiomeGenTimber::new);
		TIMBER_THIN = add("timber_thin", BiomeGenTimberThin::new);

		MOUNTAIN = add("mountain", BiomeGenMountain::new);
		JADE_CLIFFS = add("jade_cliffs", BiomeGenJadeCliffs::new);
		AUTUMN_HILLS = add("autumn_hills", BiomeGenAutumnHills::new);
		CONIFEROUS_FOREST = add("coniferous_forest", BiomeGenConiferousForest::new);
		GROVE = add("grove", BiomeGenGrove::new);
		BIRCH_FOREST = add("birch_forest", BiomeGenBirchForest::new);
		HOT_SPRINGS = add("hot_springs", BiomeGenHotSprings::new);
		SHIELD = add("shield", BiomeGenShield::new);

		MYSTIC_GROVE = add("mystic_grove", BiomeGenMysticGrove::new);
		MYSTIC_GROVE_THIN = add("mystic_grove_thin", BiomeGenMysticGroveThin::new);
		OASIS = add("oasis", BiomeGenOasis::new);
		SACRED_SPRINGS = add("sacred_springs", BiomeGenSacredSprings::new);
		WOODLAND = add("woodland", BiomeGenWoodland::new);
		WETLAND = add("wetland", BiomeGenWetland::new);

		BAYOU = add("bayou", BiomeGenBayou::new);
		MARSH = add("marsh", BiomeGenMarsh::new);
		SILKGLADES = add("silkglades", BiomeGenSilkglades::new);
		MOOR = add("moor", BiomeGenMoor::new);
		FEN = add("fen", BiomeGenFen::new);
		LUSH_SWAMP = add("lush_swamp", BiomeGenLushSwamp::new);

		BAMBOO_FOREST = add("bamboo_forest", BiomeGenBambooForest::new);
		TROPICAL_RAINFOREST = add("tropical_rainforest", BiomeGenTropicalRainforest::new);
		RAINFOREST = add("rainforest", BiomeGenRainforest::new);
		FUNGI_FOREST = add("fungi_forest", BiomeGenFungiForest::new);

		TROPICS = add("tropics", BiomeGenTropics::new);
		TROPICS_MOUNTAIN = add("tropics_mountain", key -> new BiomeGenTropics(key, 0.7f, 1.2f,
			BiomeGenTropics.MOUNTAIN_MAP_COLOR));

		OMINOUS_WOODS = add("ominous_woods", BiomeGenOminousWoods::new);
		OMINOUS_WOODS_THICK = add("ominous_woods_thick", BiomeGenOminousWoodsThick::new);
		DEAD_SWAMP = add("dead_swamp", BiomeGenDeadSwamp::new);
		QUAGMIRE = add("quagmire", BiomeGenQuagmire::new);
		SLUDGEPIT = add("sludgepit", BiomeGenSludgepit::new);
		MANGROVE = add("mangrove", BiomeGenMangrove::new);
		BOG = add("bog", BiomeGenBog::new);

		BADLANDS = add("badlands", BiomeGenBadlands::new);
		MESA = add("mesa", BiomeGenMesa::new);

		DEADLANDS = add("deadlands", BiomeGenDeadlands::new);
		VOLCANO = add("volcano", BiomeGenVolcano::new);
		WASTELAND = add("wasteland", BiomeGenWasteland::new);

		BRUSHLAND = add("brushland", BiomeGenBrushland::new);
		DUNES = add("dunes", BiomeGenDunes::new);

		SHORE = addTerrainPlaced("shore", BiomeGenShore::new);
		BEACH_GRAVEL = addTerrainPlaced("beach_gravel", BiomeGenBeachGravel::new);
		BEACH_OVERGROWN = addTerrainPlaced("beach_overgrown", BiomeGenBeachOvergrown::new);
		OCEAN_ABYSS = addTerrainPlaced("ocean_abyss", BiomeGenOceanAbyss::new);
		OCEAN_CORAL = addTerrainPlaced("ocean_coral", BiomeGenOceanCoral::new);
		OCEAN_KELP = addTerrainPlaced("ocean_kelp", BiomeGenOceanKelp::new);

		registerNether();

		OCEAN = addMaskPlaced("ocean", BiomeGenOcean::new);

		BetterOPlenty.LOGGER.info("Registered {} BOP biomes ({} of {} planned overworld biomes, "
				+ "plus {} placed by terrain adjacency, plus {} in the Nether).",
			REGISTERED.size(), BY_PLAN_ID.size(), BOPClimate.roster().size(),
			REGISTERED.size() - BY_PLAN_ID.size(), NETHER.size());
		checkIdCeiling();
	}

	private static void registerNether() {
		NETHER_BASE = addNether("nether_base", BiomeGenNetherBase::new);
		NETHER_GARDEN = addNether("nether_garden", BiomeGenNetherGarden::new);

		PROMISED_LAND_PLAINS = addPromised("promised_plains",
			biomesoplenty.biomes.promisedland.BiomeGenPromisedLandPlains::new);
		PROMISED_LAND_FOREST = addPromised("promised_forest",
			biomesoplenty.biomes.promisedland.BiomeGenPromisedLandForest::new);
		PROMISED_LAND_SHRUB = addPromised("promised_shrub",
			biomesoplenty.biomes.promisedland.BiomeGenPromisedLandShrub::new);
		PROMISED_LAND_SWAMP = addPromised("promised_swamp",
			biomesoplenty.biomes.promisedland.BiomeGenPromisedLandSwamp::new);
		NETHER_DESERT = addNether("nether_desert", BiomeGenNetherDesert::new);
		NETHER_LAVA = addNether("nether_lava", BiomeGenNetherLava::new);
		NETHER_BONE = addNether("nether_bone", BiomeGenNetherBone::new);
		NETHER_BLOOD = addNether("nether_blood", BiomeGenNetherBlood::new);

		BOPNetherClimate.logPartition();
	}

	@NotNull

	private static <T extends BiomeGenBase> T addPromised(@NotNull String planId,
	                                                      @NotNull Function<String, T> factory) {
		if (BOPClimate.planned(planId) != null) {
			throw new IllegalStateException("Biome '" + planId + "' has a BOPClimate roster row, so"
				+ " it is an overworld biome placed by climate -- registering it here would put it"
				+ " in the Promised Land as well.");
		}
		T biome = factory.apply(translationKey(planId));
		Biomes.register(registryKey(planId), biome);
		PROMISED_LAND.add(biome);
		return biome;
	}

	private static <T extends BiomeGenBase> T addNether(@NotNull String planId,
	                                                    @NotNull Function<String, T> factory) {
		if (BOPClimate.planned(planId) != null) {
			throw new IllegalStateException("Biome '" + planId + "' has a BOPClimate roster row, so"
				+ " it is an overworld biome placed by climate -- registering it here would put it"
				+ " in the Nether as well.");
		}
		T biome = factory.apply(translationKey(planId));
		Biomes.register(registryKey(planId), biome);
		NETHER.add(biome);
		BOPNetherClimate.add(biome);
		return biome;
	}

	@NotNull
	public static List<BiomeGenBase> nether() {
		return Collections.unmodifiableList(NETHER);
	}

	@NotNull
	public static List<BiomeGenBase> promisedLand() {
		return Collections.unmodifiableList(PROMISED_LAND);
	}

	@NotNull
	public static String registryKey(@NotNull String planId) {
		return BetterOPlenty.MOD_ID + ":bop." + planId;
	}

	@NotNull
	public static String translationKey(@NotNull String planId) {
		return BetterOPlenty.MOD_ID + ".bop." + planId;
	}

	@NotNull
	private static <T extends BiomeGenBase> T add(@NotNull String planId,
	                                              @NotNull Function<String, T> factory) {
		BOPClimate.Planned planned = BOPClimate.planned(planId);
		if (planned == null) {
			throw new IllegalStateException("Biome '" + planId + "' has no row in BOPClimate's "
				+ "planned roster, so it has no climate box and no tags. Add it there first.");
		}
		T biome = factory.apply(translationKey(planId));
		Biomes.register(registryKey(planId), biome);

		biome.seasonResist = BOPClimate.seasonResist(planId);

		for (Tag<Biome> tag : planned.tags) {
			biome.withTags(tag);
		}

		REGISTERED.add(biome);
		BY_PLAN_ID.put(planId, biome);
		return biome;
	}

	@NotNull
	private static <T extends BiomeGenBase> T addTerrainPlaced(@NotNull String planId,
	                                                           @NotNull Function<String, T> factory) {
		if (BOPClimate.planned(planId) != null) {
			throw new IllegalStateException("Biome '" + planId + "' has a BOPClimate roster row, so "
				+ "it is climate-placed and belongs in add(...) -- registering it here would give it "
				+ "a climate box and a terrain placement at the same time.");
		}
		T biome = factory.apply(translationKey(planId));
		Biomes.register(registryKey(planId), biome);
		REGISTERED.add(biome);
		TERRAIN_PLACED.add(biome);
		return biome;
	}

	@NotNull
	private static <T extends BiomeGenBase> T addMaskPlaced(@NotNull String planId,
	                                                        @NotNull Function<String, T> factory) {
		if (BOPClimate.planned(planId) != null) {
			throw new IllegalStateException("Biome '" + planId + "' has a BOPClimate roster row. The"
				+ " mask-placed sea owns the altitude axis below SEA_SHARE and must not also hold a"
				+ " share of the land.");
		}
		T biome = factory.apply(translationKey(planId));
		Biomes.register(registryKey(planId), biome);
		REGISTERED.add(biome);
		return biome;
	}

	@NotNull
	public static Set<BiomeGenBase> terrainPlaced() {
		return Collections.unmodifiableSet(TERRAIN_PLACED);
	}

	private static final Map<String, Biome> BTA_FILLED = Map.of(
		"plains", Biomes.OVERWORLD_PLAINS,
		"desert", Biomes.OVERWORLD_DESERT,
		"forest", Biomes.OVERWORLD_FOREST,
		"forest_hills", Biomes.OVERWORLD_FOREST,
		"taiga", Biomes.OVERWORLD_TAIGA,
		"taiga_hills", Biomes.OVERWORLD_TAIGA,
		"swampland", Biomes.OVERWORLD_SWAMPLAND,
		"jungle", Biomes.OVERWORLD_RAINFOREST,
		"jungle_hills", Biomes.OVERWORLD_RAINFOREST,
		"originValley", Biomes.OVERWORLD_RETRO);

	private static final Map<String, String> BOP_FILLED = Map.of(
		"extreme_hills", "mountain");

	@Nullable
	public static Biome btaFilled(@NotNull String planId) {
		return BTA_FILLED.get(planId);
	}

	@Nullable
	public static Biome filledBy(@NotNull String planId) {
		Biome bta = BTA_FILLED.get(planId);
		if (bta != null) {
			return bta;
		}
		String substitute = BOP_FILLED.get(planId);
		return substitute == null ? null : BY_PLAN_ID.get(substitute);
	}

	static void addRanges(@NotNull BOPBiomeRangeMap map) {
		if (REGISTERED.isEmpty()) {
			throw new IllegalStateException("addRanges called before any biome was registered.");
		}

		if (OCEAN != null) {
			map.addRange(OCEAN, BOPClimate.oceanRange());
		}

		int owned = 0;
		int bta = 0;
		int borrowed = 0;
		for (BOPClimate.Cell cell : BOPClimate.partition()) {
			Biome biome = BY_PLAN_ID.get(cell.owner.id);
			if (biome != null) {
				owned++;
			} else if (filledBy(cell.owner.id) != null) {
				biome = filledBy(cell.owner.id);
				bta++;
			} else {
				biome = nearestPorted(cell.owner);
				borrowed++;
			}
			map.addRange(biome, cell.toRange());
		}

		BetterOPlenty.LOGGER.info(
			"Biome range map: {} boxes over {} planned biomes -- {} served by their own biome, "
				+ "{} by one of BTA's under E5, {} standing in for a biome that is not ported yet."
				+ " All {} share the land above altitude {}; the sea below it is {}.",
			owned + bta + borrowed, BOPClimate.roster().size(), owned, bta, borrowed,
			BOPClimate.roster().size(), String.format("%.2f", BOPClimate.SEA_SHARE),
			OCEAN == null ? "MISSING -- the land/water mask has nothing to place"
				: Registries.BIOMES.getKey(OCEAN));
	}

	@NotNull
	private static BiomeGenBase nearestPorted(@NotNull BOPClimate.Planned want) {
		BiomeGenBase best = null;
		double bestDistance = Double.MAX_VALUE;
		for (Map.Entry<String, BiomeGenBase> entry : BY_PLAN_ID.entrySet()) {
			BOPClimate.Planned have = BOPClimate.planned(entry.getKey());
			if (have == null) {
				continue;
			}
			double dt = (want.temperature - have.temperature) / 2.0;
			double dr = want.rainfall - have.rainfall;
			double distance = dt * dt + dr * dr;
			if (distance < bestDistance) {
				bestDistance = distance;
				best = entry.getValue();
			}
		}
		if (best == null) {
			throw new IllegalStateException("No ported biome to stand in for " + want.id);
		}
		return best;
	}

	private static void checkIdCeiling() {
		int id = 0;
		for (Biome biome : Registries.BIOMES) {

			if (id > BIOME_ID_CEILING) {
				throw new IllegalStateException("Biome " + Registries.BIOMES.getKey(biome)
					+ " sits at registry index " + id + ", past the " + BIOME_ID_CEILING
					+ " that a chunk section can store in a byte. The biome roster has outgrown "
					+ "BTA's chunk format; widening ChunkSection.biome to short[] is the next step. "
					+ "Note that ids are alphabetical rank, so this may well be one of BTA's own "
					+ "biomes rather than a BOP one.");
			}
			id++;
		}

		int highestPlanned = Registries.BIOMES.size() + (BOPClimate.roster().size() - BY_PLAN_ID.size()) - 1;
		if (highestPlanned > BIOME_ID_CEILING) {
			BetterOPlenty.LOGGER.warn(
				"Biome registry holds {} biomes and {} more are planned, which would reach id {}; "
					+ "the chunk format tops out at {}. The roster will need trimming or "
					+ "ChunkSection.biome widening.",
				Registries.BIOMES.size(), BOPClimate.roster().size() - BY_PLAN_ID.size(),
				highestPlanned, BIOME_ID_CEILING);
		}
	}

	@NotNull
	public static List<BiomeGenBase> registered() {
		return Collections.unmodifiableList(REGISTERED);
	}

	@Nullable
	public static BiomeGenBase byPlanId(@NotNull String planId) {
		return BY_PLAN_ID.get(planId);
	}

	@NotNull
	public static Biome servingBiome(@NotNull BOPClimate.Planned planned) {
		BiomeGenBase own = BY_PLAN_ID.get(planned.id);
		if (own != null) {
			return own;
		}
		Biome filled = filledBy(planned.id);
		return filled != null ? filled : nearestPorted(planned);
	}

	@NotNull
	public static Biome[] allBiomes() {
		return REGISTERED.toArray(new Biome[0]);
	}
}
