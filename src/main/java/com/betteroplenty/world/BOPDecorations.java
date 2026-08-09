package com.betteroplenty.world;

import biomesoplenty.worldgen.WorldGenAlgae;
import biomesoplenty.worldgen.WorldGenAsh;
import biomesoplenty.worldgen.WorldGenBOPBush;
import biomesoplenty.worldgen.WorldGenBOPDarkFlowers;
import biomesoplenty.worldgen.WorldGenBOPFlowers;
import biomesoplenty.worldgen.WorldGenBOPPumpkin;
import biomesoplenty.worldgen.WorldGenBOPTallGrass;
import biomesoplenty.worldgen.WorldGenBadlands;
import biomesoplenty.worldgen.WorldGenBoneSpine;
import biomesoplenty.worldgen.WorldGenBoneSpine2;
import biomesoplenty.worldgen.WorldGenBoulder;
import biomesoplenty.worldgen.WorldGenCattail;
import biomesoplenty.worldgen.WorldGenGrave;
import biomesoplenty.worldgen.WorldGenCloverPatch;
import biomesoplenty.worldgen.WorldGenCobwebs;
import biomesoplenty.worldgen.WorldGenCoral;
import biomesoplenty.worldgen.WorldGenGravel;
import biomesoplenty.worldgen.WorldGenHive;
import biomesoplenty.worldgen.WorldGenKelp;
import biomesoplenty.worldgen.WorldGenShortKelp;
import biomesoplenty.worldgen.WorldGenSponge;
import biomesoplenty.worldgen.WorldGenLilyflower;
import biomesoplenty.worldgen.WorldGenHighCattail;
import biomesoplenty.worldgen.WorldGenHighGrass;
import biomesoplenty.worldgen.WorldGenCanyon;
import biomesoplenty.worldgen.WorldGenCanyonGrass;
import biomesoplenty.worldgen.WorldGenMesa;
import biomesoplenty.worldgen.WorldGenIvy;
import biomesoplenty.worldgen.WorldGenMoss;
import biomesoplenty.worldgen.WorldGenPumpkinAlt;
import biomesoplenty.worldgen.WorldGenQuicksand;
import biomesoplenty.worldgen.WorldGenMossySkystone;
import biomesoplenty.worldgen.WorldGenMud;
import biomesoplenty.worldgen.WorldGenMycelium;
import biomesoplenty.worldgen.WorldGenNetherGrass;
import biomesoplenty.worldgen.WorldGenNetherMushroom;
import biomesoplenty.worldgen.WorldGenNetherVines;
import biomesoplenty.worldgen.WorldGenNetherWart;
import biomesoplenty.worldgen.WorldGenOasis;
import biomesoplenty.worldgen.WorldGenSand;
import biomesoplenty.worldgen.WorldGenOutback;
import biomesoplenty.worldgen.WorldGenPit;
import biomesoplenty.worldgen.WorldGenSmolderingGrass;
import biomesoplenty.worldgen.WorldGenRedwoodShrub;
import biomesoplenty.worldgen.WorldGenReedBOP;
import biomesoplenty.worldgen.WorldGenShield;
import biomesoplenty.worldgen.WorldGenSprout;
import biomesoplenty.worldgen.WorldGenSteppe;
import biomesoplenty.worldgen.WorldGenSunflower;
import biomesoplenty.worldgen.WorldGenWaterReeds;
import biomesoplenty.worldgen.WorldGenWaterlily;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPFormations;
import com.betteroplenty.block.BOPFlowers;
import biomesoplenty.worldgen.WorldGenCloud;
import biomesoplenty.worldgen.WorldGenCrystal;
import biomesoplenty.worldgen.tree.WorldGenPromisedWillow;
import com.betteroplenty.block.BOPPromisedLand;
import com.betteroplenty.block.BOPJungle;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.block.BOPWastes;
import com.betteroplenty.block.BOPTerracotta;
import com.betteroplenty.compat.BiomeDecoratorBOP;
import com.betteroplenty.fluid.BOPFluidDecorations;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.BlockLogicLeavesBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.PlaceableFeature;
import net.minecraft.core.world.generate.chunk.PlacementMethod;
import net.minecraft.core.world.generate.chunk.PositionSelector;
import net.minecraft.core.world.generate.chunk.PositionSelectors;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.biome.BiomeTags;
import net.minecraft.core.world.generate.feature.WorldFeatureCactus;
import net.minecraft.core.world.generate.feature.WorldFeatureClay;
import net.minecraft.core.world.generate.feature.WorldFeatureDeadBush;
import net.minecraft.core.world.generate.feature.WorldFeatureLake;
import net.minecraft.core.world.generate.feature.WorldFeatureLiquid;
import net.minecraft.core.world.generate.feature.WorldFeatureSugarCane;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public final class BOPDecorations {
	private BOPDecorations() {}

	public interface Counter extends ToIntFunction<BiomeDecoratorBOP> {}

	public record Entry(
		@NotNull String id,
		@NotNull Counter counter,
		@NotNull Supplier<WorldFeatureInterface> feature,
		@NotNull PositionSelector selector,
		@NotNull PlacementFactory method,
		boolean liveAtZero
	) {}

	public interface PlacementFactory {
		@NotNull PlacementMethod create(int count);
	}

	private static final PlacementFactory TRIES = PlacementMethod.TriesPerChunk::new;

	private static final List<Entry> ENTRIES = new ArrayList<>();

	private static void counter(
		@NotNull String id,
		@NotNull Counter counter,
		@NotNull Supplier<WorldFeatureInterface> feature,
		@NotNull PositionSelector selector,
		@NotNull PlacementFactory method
	) {
		counter(id, counter, feature, selector, method, false);
	}

	private static void counter(
		@NotNull String id,
		@NotNull Counter counter,
		@NotNull Supplier<WorldFeatureInterface> feature,
		@NotNull PositionSelector selector,
		@NotNull PlacementFactory method,
		boolean liveAtZero
	) {

		for (Entry existing : ENTRIES) {
			if (existing.id().equals(id)) {
				throw new IllegalStateException("Duplicate BOP decoration row '" + id + "'. Two "
					+ "rows with one id place the same feature twice at the same positions, so the "
					+ "counter runs at double density. If both rows are wanted, give them different "
					+ "ids (see mushrooms_brown / flat_mushrooms / mushrooms_red).");
			}
		}
		ENTRIES.add(new Entry(id, counter, feature, selector, method, liveAtZero));
	}

	private static void counter(
		@NotNull String id,
		@NotNull Counter counter,
		@NotNull Supplier<WorldFeatureInterface> feature,
		@NotNull PositionSelector selector
	) {
		counter(id, counter, feature, selector, TRIES);
	}

	static {

		counter("trees", d -> d.treesPerChunk,
			BiomeTreeFeature::new, SurfaceOrFloor.INSTANCE,
			count -> new TriesPerChunkPlusChance(count, 10),
			true);

		counter("grass", d -> d.grassPerChunk,
			BiomeGrassFeature::new, PositionSelectors.HeightRangeUniform);

		counter("lavender", d -> d.lavenderPerChunk,
			() -> new WorldGenBOPFlowers(BOPBlocks.LAVENDER.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("pumpkins", d -> d.generatePumpkins ? 1 : 0,
			WorldGenBOPPumpkin::new, PositionSelectors.HeightRangeUniform,
			count -> new PlacementMethod.ChanceToPlace(32));

		counter("pumpkins_carved", d -> d.pumpkinsPerChunk,
			() -> new WorldGenPumpkinAlt(Blocks.PUMPKIN_CARVED_IDLE.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("wheat_grass", d -> d.wheatGrassPerChunk,
			WheatOrDampGrass::new, PositionSelectors.HeightRangeUniform);

		counter("steppe", d -> d.steppePerChunk,
			() -> new WorldGenSteppe(Blocks.SAND.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("outback", d -> d.outbackPerChunk,
			() -> new WorldGenOutback(BOPPlants.MEDIUM_GRASS.id()),
			PositionSelectors.HeightRangeUniform);

		counter("canyon_grass", d -> d.canyonGrassPerChunk,
			() -> new WorldGenCanyonGrass(BOPPlants.MEDIUM_GRASS.id()),
			PositionSelectors.HeightRangeUniform);

		counter("stone_in_grass_2", d -> d.generateStoneInGrass2 ? 1 : 0,
			() -> new WorldGenShield(Blocks.STONE.id(), 48),
			SurfaceBandUniform.INSTANCE,
			count -> new PlacementMethod.TriesPerChunk(20));

		counter("stone_in_grass", d -> d.generateStoneInGrass ? 1 : 0,
			() -> new WorldGenMycelium(Blocks.STONE.id(), 32),
			SurfaceBandUniform.INSTANCE,
			count -> new PlacementMethod.TriesPerChunk(15));

		counter("canyon", d -> d.generateCanyon ? 1 : 0,
			() -> new WorldGenCanyon(BOPBlocks.RED_ROCK.id(), 48),
			SurfaceBandUniform.INSTANCE,
			count -> new PlacementMethod.TriesPerChunk(15));

		counter("grass_mesa", d -> d.generateGrass ? 1 : 0,
			() -> new WorldGenMesa(Blocks.GRASS.id(), 48),
			FullColumnUniform.INSTANCE,
			count -> new PlacementMethod.TriesPerChunk(20));

		counter("sand_mesa", d -> d.generateSand ? 1 : 0,
			() -> new WorldGenMesa(Blocks.SAND.id(), 32),
			FullColumnUniform.INSTANCE,
			count -> new PlacementMethod.TriesPerChunk(15));

		counter("clay_in_clay", d -> d.generateClayInClay ? 1 : 0,
			() -> new WorldGenBadlands(BOPTerracotta.STAINED_CLAY_ORANGE.id(), 32),
			SurfaceBandUniform.INSTANCE,
			count -> new PlacementMethod.TriesPerChunk(20));

		counter("clay_in_clay_2", d -> d.generateClayInClay2 ? 1 : 0,
			() -> new WorldGenBadlands(Blocks.BLOCK_CLAY.id(), 32),
			SurfaceBandUniform.INSTANCE,
			count -> new PlacementMethod.TriesPerChunk(20));

		counter("clay_in_stone", d -> d.generateClayInStone ? 1 : 0,
			() -> new WorldGenBadlands(BOPTerracotta.STAINED_CLAY_RED.id(), 32),
			SurfaceBandUniform.INSTANCE,
			count -> new PlacementMethod.TriesPerChunk(20));

		counter("clay_in_stone_2", d -> d.generateClayInStone2 ? 1 : 0,
			() -> new WorldGenBadlands(BOPTerracotta.STAINED_CLAY_RED.id(), 32),
			SurfaceBandUniform.INSTANCE,
			count -> new PlacementMethod.TriesPerChunk(20));

		counter("boulders", d -> d.generateBoulders ? 1 : 0,
			WorldGenBoulder::new, PositionSelectors.HeightRangeUniform,
			count -> new PlacementMethod.ChanceToPlace(32));

		counter("gravel", d -> d.gravelPerChunk,
			() -> new WorldGenGravel(7, Blocks.GRAVEL.id()),
			TopSolidOrLiquid.INSTANCE);

		counter("gravel_2", d -> d.gravelPerChunk2,
			() -> new WorldGenGravel(7, Blocks.GRAVEL.id()),
			TopSolidOrLiquid.INSTANCE);

		counter("oases", d -> d.oasesPerChunk,
			() -> new WorldGenOasis(7, Blocks.GRASS.id()),
			TopSolidOrLiquid.INSTANCE);

		counter("oases_2", d -> d.oasesPerChunk2,
			() -> new WorldGenOasis(7, Blocks.GRASS.id()),
			TopSolidOrLiquid.INSTANCE);

		counter("sand", d -> d.sandPerChunk,
			() -> new WorldGenSand(7, Blocks.SAND.id()),
			TopSolidOrLiquid.INSTANCE);

		counter("sand_2", d -> d.sandPerChunk2,
			() -> new WorldGenSand(7, Blocks.SAND.id()),
			TopSolidOrLiquid.INSTANCE);

		counter("sand_2_again", d -> d.sandPerChunk2,
			() -> new WorldGenSand(7, Blocks.SAND.id()),
			TopSolidOrLiquid.INSTANCE);

		counter("mycelium", d -> d.generateMycelium ? 1 : 0,
			() -> new WorldGenMycelium(BOPJungle.MYCELIUM.id(), 32),
			WholeColumnUniform.INSTANCE,
			count -> new PlacementMethod.TriesPerChunk(10));

		counter("ash", d -> d.generateAsh ? 1 : 0,
			() -> new WorldGenAsh(BOPWastes.ASH.id(), 32),
			WholeColumnUniform.INSTANCE,
			count -> new PlacementMethod.TriesPerChunk(10));

		counter("pits", d -> d.generatePits ? 1 : 0,
			() -> new WorldGenPit(BOPWastes.ASH.id()),
			TopSolidOrLiquid.INSTANCE);

		counter("mud", d -> d.mudPerChunk,
			() -> new WorldGenMud(7, Blocks.MUD.id()),
			TopSolidOrLiquid.INSTANCE);

		counter("mud_2", d -> d.mudPerChunk2,
			() -> new WorldGenMud(7, Blocks.MUD.id()),
			TopSolidOrLiquid.INSTANCE);

		counter("hot_springs", d -> d.hotSpringsPerChunk,
			BOPFluidDecorations.springWaterLake(),
			BOPFluidDecorations.BIOME_POOL_DEPTH);

		counter("poison_water", d -> d.poisonWaterPerChunk,
			BOPFluidDecorations.liquidPoisonLake(),
			BOPFluidDecorations.BIOME_POOL_DEPTH);

		counter("spring_water_underground", d -> BOPFluidDecorations.UNDERGROUND_ALWAYS,
			BOPFluidDecorations.springWaterLake(),
			BOPFluidDecorations.UNDERGROUND_POCKET_DEPTH,
			count -> new TriesPerChunkWithChance(
				BOPFluidDecorations.UNDERGROUND_TRIES,
				BOPFluidDecorations.UNDERGROUND_SPRING_WATER_CHANCE));

		counter("liquid_poison_underground", d -> BOPFluidDecorations.UNDERGROUND_ALWAYS,
			BOPFluidDecorations.liquidPoisonLake(),
			BOPFluidDecorations.UNDERGROUND_POCKET_DEPTH,
			count -> new TriesPerChunkWithChance(
				BOPFluidDecorations.UNDERGROUND_TRIES,
				BOPFluidDecorations.UNDERGROUND_LIQUID_POISON_CHANCE));

		counter("bushes", d -> d.bushesPerChunk,
			() -> new WorldGenBOPBush(BOPPlants.BUSH.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("shrubs", d -> d.shrubsPerChunk,
			() -> new WorldGenBOPBush(BOPPlants.SHRUB.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("poison_ivy", d -> d.poisonIvyPerChunk,
			() -> new WorldGenBOPBush(BOPPlants.POISON_IVY.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("berry_bushes", d -> d.berryBushesPerChunk,
			() -> new WorldGenBOPFlowers(BOPPlants.BERRY_BUSH.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("redwood_shrubs", d -> d.redwoodShrubsPerChunk,
			() -> new WorldGenRedwoodShrub(0, 0),
			new SeaOffsetUniform(6, 50));

		counter("sprouts", d -> d.sproutsPerChunk,
			() -> new WorldGenSprout(BOPPlants.SPROUT.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("high_grass", d -> d.highGrassPerChunk,
			() -> new WorldGenHighGrass(BOPPlants.HIGH_GRASS.id(), 0, BOPPlants.HIGH_GRASS_TOP.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("koru", d -> d.koruPerChunk,
			() -> new WorldGenBOPTallGrass(BOPPlants.KORU.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("clover_patches", d -> d.cloverPatchesPerChunk,
			() -> new WorldGenCloverPatch(BOPPlants.CLOVER_PATCH.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("roots", d -> d.rootsPerChunk,
			() -> new WorldGenBOPTallGrass(BOPPlants.ROOT.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("dead_grass", d -> d.deadGrassPerChunk,
			() -> new WorldGenBOPFlowers(BOPPlants.DEAD_GRASS.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("smoldering_grass", d -> d.smolderingGrassPerChunk,
			WorldGenSmolderingGrass::new,
			PositionSelectors.HeightRangeUniform);

		counter("desert_grass", d -> d.desertGrassPerChunk,
			() -> new WorldGenBOPFlowers(BOPPlants.DESERT_GRASS.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("desert_sprouts", d -> d.desertSproutsPerChunk,
			() -> new WorldGenBOPFlowers(BOPPlants.DESERT_SPROUTS.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("dune_grass", d -> d.duneGrassPerChunk,
			() -> new WorldGenBOPFlowers(BOPPlants.DUNE_GRASS.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("thorns", d -> d.thornsPerChunk,
			() -> new WorldGenBOPFlowers(BOPPlants.THORN.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("cobwebs", d -> d.cobwebsPerChunk,
			() -> new WorldGenCobwebs(Blocks.COBWEB.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("carrots", d -> d.carrotsPerChunk,
			() -> new WorldGenBOPFlowers(BOPPlants.WILD_CARROT.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("tiny_cacti", d -> d.tinyCactiPerChunk,
			() -> new WorldGenBOPFlowers(BOPPlants.TINY_CACTUS.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("cattails", d -> d.cattailsPerChunk,
			() -> new WorldGenCattail(BOPPlants.CATTAIL.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("high_cattails", d -> d.highCattailsPerChunk,
			() -> new WorldGenHighCattail(BOPPlants.CATTAIL_TOP.id(), 0, BOPPlants.CATTAIL_BOTTOM.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("river_cane", d -> d.reedsBOPPerChunk,
			() -> new WorldGenReedBOP(BOPPlants.RIVER_CANE.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("algae", d -> d.algaePerChunk,
			WorldGenAlgae::new, SurfaceDrop.INSTANCE);

		counter("water_reeds", d -> d.waterReedsPerChunk,
			() -> new WorldGenWaterReeds(BOPPlants.REED.id(), 0),
			SurfaceDrop.INSTANCE);

		counter("waterlily", d -> d.waterlilyPerChunk,
			WorldGenWaterlily::new, SurfaceDrop.INSTANCE);

		counter("kelp", d -> d.kelpPerChunk,
			() -> new WorldGenKelp(false), SeaColumnUniform.FULL_CHUNK);

		counter("kelp_thick", d -> d.kelpThickPerChunk,
			() -> new WorldGenKelp(false), SeaColumnUniform.QUARTER_CHUNK);

		counter("short_kelp", d -> d.shortKelpPerChunk,
			() -> new WorldGenShortKelp(false), SeaColumnUniform.FULL_CHUNK);

		counter("coral", d -> d.coralPerChunk,
			WorldGenCoral::new, PositionSelectors.HeightRangeUniform);

		counter("sponge", d -> d.generateSponge ? 1 : 0,
			WorldGenSponge::new, SeaColumnUniform.FULL_CHUNK,
			count -> new PlacementMethod.TriesPerChunk(5));

		counter("flowers_yellow", d -> d.flowersPerChunk,
			() -> new WorldGenBOPFlowers(Blocks.FLOWER_YELLOW.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("flowers_dandelion", d -> d.flowersPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.DANDELION.id(), 0),
			PositionSelectors.HeightRangeUniform,
			count -> new TriesPerChunkWithChance(count, 6));

		counter("tiny_flowers", d -> d.tinyFlowersPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.CLOVER.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("blue_flowers", d -> d.blueFlowersPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.SWAMP_FLOWER.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("orange_flowers", d -> d.orangeFlowersPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.COSMOS.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("pink_flowers", d -> d.pinkFlowersPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.DAFFODIL.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("purple_flowers", d -> d.purpleFlowersPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.WILDFLOWER.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("white_flowers", d -> d.whiteFlowersPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.ANEMONE.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("glow_flowers", d -> d.glowFlowersPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.GLOW_FLOWER.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("hydrangeas", d -> d.hydrangeasPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.HYDRANGEA.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("violets", d -> d.violetsPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.VIOLET.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("deathblooms", d -> d.deathbloomsPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.DEATHBLOOM.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("aloe", d -> d.aloePerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.BROMELIAD.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("hibiscus", d -> d.hibiscusPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.HIBISCUS.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("lily_of_the_valleys", d -> d.lilyOfTheValleysPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.LILY_OF_THE_VALLEY.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("goldenrods", d -> d.goldenrodsPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.GOLDENROD.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("bluebells", d -> d.bluebellsPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.BLUEBELLS.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("icy_iris", d -> d.icyIrisPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.ICY_IRIS.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("burning_blossoms", d -> d.burningBlossomsPerChunk,
			() -> new WorldGenBOPDarkFlowers(BOPFlowers.BURNING_BLOSSOM.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("miners_delight", d -> d.minersDelightPerChunk,
			() -> new WorldGenBOPDarkFlowers(BOPFlowers.MINERS_DELIGHT.id(), 0),
			new HeightOffsetUniform(0, 45));

		counter("rainbow_flowers", d -> d.rainbowflowersPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.RAINBOW_FLOWER.id(), 0),
			PositionSelectors.HeightRangeUniform,
			count -> new TriesPerChunkWithChance(count, 10));

		counter("sunflowers", d -> d.sunflowersPerChunk,
			() -> new WorldGenSunflower(BOPFlowers.SUNFLOWER.id(), BOPFlowers.SUNFLOWER_TOP.id()),
			PositionSelectors.HeightRangeUniform);

		counter("lily_flowers", d -> d.lilyflowersPerChunk,
			() -> new WorldGenLilyflower(BOPFlowers.LILY_FLOWER.id()),
			PositionSelectors.HeightRangeUniform);

		counter("holy_tall_grass", d -> d.holyTallGrassPerChunk,
			() -> new WorldGenBOPFlowers(BOPPromisedLand.HOLY_TALL_GRASS.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("crystals", d -> d.crystalsPerChunk,
			WorldGenCrystal::new, PositionSelectors.HeightRangeUniform);
		counter("crystals2", d -> d.crystals2PerChunk,
			WorldGenCrystal::new, PositionSelectors.HeightRangeUniform);

		counter("clouds", d -> d.cloudsPerChunk,
			WorldGenCloud::new, (world, chunk, random, minY, maxY, rangeY) -> {
				int x = chunk.pos.x() * 16 + random.nextInt(16) + 8;
				int z = chunk.pos.z() * 16 + random.nextInt(16) + 8;
				return new TilePos(x, random.nextInt(64), z);
			});

		counter("promised_willow", d -> d.promisedWillowPerChunk,
			WorldGenPromisedWillow::new, PositionSelectors.HeightRangeUniform);

		counter("mossy_skystone", d -> d.generateMossySkystone ? 1 : 0,
			() -> new WorldGenMossySkystone(BOPPromisedLand.HOLY_STONE_MOSSY.id(), 24),
			PositionSelectors.HeightRangeUniform,
			count -> new PlacementMethod.TriesPerChunk(15));

		counter("toadstools", d -> d.toadstoolsPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.TOADSTOOL.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("portobellos", d -> d.portobellosPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.PORTOBELLO.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("blue_milks", d -> d.blueMilksPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.BLUE_MILK_CAP.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("glowshrooms", d -> d.glowshroomsPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.GLOWSHROOM.id(), 0),
			new HeightOffsetUniform(0, 80));

		counter("roses", d -> d.rosesPerChunk,
			() -> new WorldGenBOPFlowers(Blocks.FLOWER_RED.id(), 0),
			PositionSelectors.HeightRangeUniform);

		counter("flowers_red", d -> d.flowersPerChunk,
			() -> new WorldGenBOPFlowers(Blocks.FLOWER_RED.id(), 0),
			PositionSelectors.HeightRangeUniform,
			count -> new TriesPerChunkWithChance(count, 4));

		counter("mushrooms_brown", d -> d.mushroomsPerChunk,
			() -> new WorldGenBOPFlowers(Blocks.MUSHROOM_BROWN.id(), 0),
			SurfaceOrFloor.INSTANCE,
			count -> new TriesPerChunkWithChance(count, 4));

		counter("flat_mushrooms", d -> d.mushroomsPerChunk,
			() -> new WorldGenBOPFlowers(BOPFlowers.FLAT_MUSHROOM.id(), 0),
			PositionSelectors.HeightRangeUniform,
			count -> new TriesPerChunkWithChance(count, 6));

		counter("mushrooms_red", d -> d.mushroomsPerChunk,
			() -> new WorldGenBOPFlowers(Blocks.MUSHROOM_RED.id(), 0),
			PositionSelectors.HeightRangeUniform,
			count -> new TriesPerChunkWithChance(count, 8));

		counter("mushrooms_brown_base", d -> 1,
			() -> new WorldGenBOPFlowers(Blocks.MUSHROOM_BROWN.id(), 0),
			PositionSelectors.HeightRangeUniform,
			count -> new PlacementMethod.ChanceToPlace(4));

		counter("mushrooms_red_base", d -> 1,
			() -> new WorldGenBOPFlowers(Blocks.MUSHROOM_RED.id(), 0),
			PositionSelectors.HeightRangeUniform,
			count -> new PlacementMethod.ChanceToPlace(8));

		counter("reeds", d -> d.reedsPerChunk,
			WorldFeatureSugarCane::new, PositionSelectors.HeightRangeUniform);

		counter("reeds_base", d -> 10,
			WorldFeatureSugarCane::new, PositionSelectors.HeightRangeUniform);

		counter("cacti", d -> d.cactiPerChunk,
			WorldFeatureCactus::new, PositionSelectors.HeightRangeUniform);

		counter("dead_bushes", d -> d.deadBushPerChunk,
			() -> new WorldFeatureDeadBush(Blocks.DEADBUSH.id()),
			PositionSelectors.HeightRangeUniform);

		counter("stalagmites", d -> d.stalagmitesPerChunk,
			() -> new WorldGenBOPTallGrass(BOPFormations.STALAGMITE.id(), 0),
			SubSeaColumnUniform.INSTANCE);

		counter("stalactites", d -> d.stalactitesPerChunk,
			() -> new WorldGenBOPTallGrass(BOPFormations.STALACTITE.id(), 0),
			SubSeaColumnUniform.INSTANCE);

		counter("bone_spines", d -> d.boneSpinesPerChunk,
			WorldGenBoneSpine::new,
			PositionSelectors.HeightRangeUniform);

		counter("bone_spines_hanging", d -> d.boneSpines2PerChunk,
			WorldGenBoneSpine2::new,
			UpperHalfUniform.INSTANCE);

		counter("graves", d -> d.gravesPerChunk,
			WorldGenGrave::new,
			PositionSelectors.HeightRangeUniform);

		counter("quicksand", d -> d.generateQuicksand ? 1 : 0,
			() -> new WorldGenQuicksand(BOPBlocks.QUICKSAND.id(), 24),
			SurfaceBandUniform.INSTANCE,
			count -> new PlacementMethod.TriesPerChunk(5));

		counter("clay", d -> d.clayPerChunk,
			() -> new WorldFeatureClay(4),
			TopSolidOrLiquid.INSTANCE);

		counter("water_lakes", d -> d.waterLakesPerChunk,
			WaterOrIceLake::new, new NestedHeightUniform(240, 8));

		counter("lava_lakes", d -> d.lavaLakesPerChunk,
			() -> new WorldFeatureLake(Blocks.FLUID_LAVA_STILL.id()),
			BOPFluidDecorations.BIOME_POOL_DEPTH);

		counter("water_lakes_base", d -> d.generateBaseWaterLakes ? 1 : 0,
			WaterOrIceLake::new, PositionSelectors.HeightRangeUniform,
			count -> new PlacementMethod.ChanceToPlace(4));

		counter("lava_lakes_base", d -> 1,
			BelowSeaLavaLake::new, new NestedHeightUniform(248, 8),
			count -> new PlacementMethod.ChanceToPlace(8));

		counter("ponds", d -> d.generateLakes ? 50 + d.pondsPerChunk : 0,
			() -> new WorldFeatureLiquid(Blocks.FLUID_WATER_FLOWING.id()),
			new NestedHeightUniform(120, 8));

		counter("lava_springs", d -> d.generateLakes ? 20 : 0,
			() -> new WorldFeatureLiquid(Blocks.FLUID_LAVA_FLOWING.id()),
			BOPFluidDecorations.BIOME_POOL_DEPTH);

		counter("nether_lava", d -> d.netherLavaPerChunk,
			() -> new WorldFeatureLake(Blocks.FLUID_LAVA_STILL.id()),
			NetherPoolDepth.INSTANCE);

		counter("nether_vines", d -> d.netherVinesPerChunk,
			WorldGenNetherVines::new, PositionSelectors.HeightRangeUniform);

		counter("nether_grass", d -> d.netherGrassPerChunk,
			WorldGenNetherGrass::new, PositionSelectors.HeightRangeUniform);

		counter("nether_wart", d -> d.netherWartPerChunk,
			WorldGenNetherWart::new, PositionSelectors.HeightRangeUniform);

		counter("big_mushrooms", d -> d.bigMushroomsPerChunk,
			WorldGenNetherMushroom::overworld, SurfaceOrFloor.INSTANCE);

		counter("wasp_hives", d -> d.waspHivesPerChunk,
			WorldGenHive::new,
			(world, chunk, random, minY, maxY, rangeY) -> new TilePos(
				chunk.pos.x() * 16 + random.nextInt(16) + 8,
				minY + (rangeY * 25) / 64 + random.nextInt(Math.max(1, rangeY / 2)),
				chunk.pos.z() * 16 + random.nextInt(16) + 8),
			count -> new TriesPerChunkUnlessChance(count, 4));

	}

	@NotNull
	public static List<Entry> entries() {
		return ENTRIES;
	}

	public interface ExtraDecorationSink {
		void add(@NotNull String id, @NotNull Supplier<WorldFeatureInterface> feature,
				 @NotNull PositionSelector selector, @NotNull PlacementMethod method);
	}

	public static final class TriesPerChunkPlusRandom implements PlacementMethod {
		private final int base;
		private final int range;

		public TriesPerChunkPlusRandom(int base, int range) {
			this.base = base;
			this.range = range;
		}

		@Override
		public void placeFeature(@NotNull PlaceableFeature feature, @NotNull World world, @NotNull Chunk chunk, @NotNull Random random) {
			int count = this.base + random.nextInt(this.range);
			for (int i = 0; i < count; i++) {
				feature.placeFeature(world, chunk, random);
			}
		}
	}

	public static final class HeightOffsetUniform implements PositionSelector {
		private final int offset;
		private final int range;

		public HeightOffsetUniform(int offset, int range) {
			this.offset = offset;
			this.range = range;
		}

		@NotNull
		@Override
		public TilePos getValue(@NotNull World world, @NotNull Chunk chunk, @NotNull Random random, int minY, int maxY, int rangeY) {
			int x = chunk.pos.x() * 16 + random.nextInt(16) + 8;
			int z = chunk.pos.z() * 16 + random.nextInt(16) + 8;
			return new TilePos(x, random.nextInt(this.range) + this.offset, z);
		}
	}

	public static final class TopSolidOrLiquid implements PositionSelector {
		public static final TopSolidOrLiquid INSTANCE = new TopSolidOrLiquid();

		private TopSolidOrLiquid() {}

		@NotNull
		@Override
		public TilePos getValue(@NotNull World world, @NotNull Chunk chunk, @NotNull Random random, int minY, int maxY, int rangeY) {
			int x = chunk.pos.x() * 16 + random.nextInt(16) + 8;
			int z = chunk.pos.z() * 16 + random.nextInt(16) + 8;

			int y = world.getHeightValue(x, z);

			while (y > 0) {
				TilePos pos = new TilePos(x, y, z);
				if (!world.isAirBlock(pos)) {
					Material material = world.getBlockMaterial(pos);
					if (material.blocksMotion() && material != Materials.LEAVES) {
						return new TilePos(x, y + 1, z);
					}
				}
				--y;
			}

			return new TilePos(x, 0, z);
		}
	}

	public static final class TriesPerChunkWithChance implements PlacementMethod {
		private final int tries;
		private final int chance;

		public TriesPerChunkWithChance(int tries, int chance) {
			this.tries = tries;
			this.chance = chance;
		}

		@Override
		public void placeFeature(@NotNull PlaceableFeature feature, @NotNull World world, @NotNull Chunk chunk, @NotNull Random random) {
			for (int i = 0; i < this.tries; i++) {
				if (random.nextInt(this.chance) == 0) {
					feature.placeFeature(world, chunk, random);
				}
			}
		}
	}

	public static final class TriesPerChunkUnlessChance implements PlacementMethod {
		private final int tries;
		private final int chance;

		public TriesPerChunkUnlessChance(int tries, int chance) {
			this.tries = tries;
			this.chance = chance;
		}

		@Override
		public void placeFeature(@NotNull PlaceableFeature feature, @NotNull World world, @NotNull Chunk chunk, @NotNull Random random) {
			for (int i = 0; i < this.tries; i++) {
				if (random.nextInt(this.chance) != 0) {
					feature.placeFeature(world, chunk, random);
				}
			}
		}
	}

	public static final class TriesPerChunkPlusChance implements PlacementMethod {
		private final int tries;
		private final int chance;

		public TriesPerChunkPlusChance(int tries, int chance) {
			this.tries = tries;
			this.chance = chance;
		}

		@Override
		public void placeFeature(@NotNull PlaceableFeature feature, @NotNull World world, @NotNull Chunk chunk, @NotNull Random random) {
			int count = this.tries;
			if (random.nextInt(this.chance) == 0) {
				++count;
			}

			for (int i = 0; i < count; i++) {
				feature.placeFeature(world, chunk, random);
			}
		}
	}

	private abstract static class BiomeDispatchFeature implements WorldFeatureInterface {
		@Override
		public boolean place(@NotNull World world, @NotNull Random random, @NotNull TilePosc tilePos) {
			if (!(world.getBlockBiome(tilePos) instanceof BiomeGenBase biome)) {
				return false;
			}

			WorldFeatureInterface delegate = this.select(biome, random);
			if (delegate == null) {
				return false;
			}

			if (delegate instanceof WorldFeature scalable) {
				scalable.init(1.0, 1.0, 1.0);
			}

			return delegate.place(world, random, tilePos);
		}

		protected abstract WorldFeatureInterface select(@NotNull BiomeGenBase biome, @NotNull Random random);
	}

	private static final class BiomeTreeFeature extends BiomeDispatchFeature {
		@Override
		protected WorldFeatureInterface select(@NotNull BiomeGenBase biome, @NotNull Random random) {
			return biome.getTreeFeature(random);
		}

		@Override
		public boolean place(@NotNull World world, @NotNull Random random, @NotNull TilePosc tilePos) {
			boolean decay = BlockLogicLeavesBase.enableDecay;
			BlockLogicLeavesBase.enableDecay = false;
			try {
				return super.place(world, random, tilePos);
			} finally {
				BlockLogicLeavesBase.enableDecay = decay;
			}
		}
	}

	private static final class BiomeGrassFeature extends BiomeDispatchFeature {
		@Override
		protected WorldFeatureInterface select(@NotNull BiomeGenBase biome, @NotNull Random random) {
			return biome.getRandomWorldGenForGrass(random);
		}
	}

	@NotNull
	public static WorldFeatureInterface defaultGrassFeature() {
		return new WorldFeatureTallGrass(Blocks.TALLGRASS.id());
	}

	private static final class WheatOrDampGrass implements WorldFeatureInterface {
		private final WorldFeatureInterface wheat =
			new WorldFeatureTallGrass(BOPBlocks.WHEAT_GRASS.id());
		private final WorldFeatureInterface damp =
			new WorldFeatureTallGrass(BOPPlants.DAMP_GRASS.id());

		@Override
		public boolean place(@NotNull World world, @NotNull Random random, @NotNull TilePosc tilePos) {
			return random.nextInt(2) == 0
				? this.damp.place(world, random, tilePos)
				: this.wheat.place(world, random, tilePos);
		}
	}

	public static final class SurfaceDrop implements PositionSelector {
		public static final SurfaceDrop INSTANCE = new SurfaceDrop();

		private SurfaceDrop() {}

		@NotNull
		@Override
		public TilePos getValue(@NotNull World world, @NotNull Chunk chunk, @NotNull Random random, int minY, int maxY, int rangeY) {
			int x = chunk.pos.x() * 16 + random.nextInt(16) + 8;
			int z = chunk.pos.z() * 16 + random.nextInt(16) + 8;

			int y = random.nextInt(world.getHeightBlocks());
			while (y > 0 && world.isAirBlock(x, y - 1, z)) {
				--y;
			}

			return new TilePos(x, y, z);
		}
	}

	public static final class NestedHeightUniform implements PositionSelector {
		private final int outer;
		private final int plus;

		public NestedHeightUniform(int outer, int plus) {
			this.outer = outer;
			this.plus = plus;
		}

		@NotNull
		@Override
		public TilePos getValue(@NotNull World world, @NotNull Chunk chunk, @NotNull Random random, int minY, int maxY, int rangeY) {
			int x = chunk.pos.x() * 16 + random.nextInt(16) + 8;
			int z = chunk.pos.z() * 16 + random.nextInt(16) + 8;
			return new TilePos(x, random.nextInt(random.nextInt(this.outer) + this.plus), z);
		}
	}

	private static final class WaterOrIceLake implements WorldFeatureInterface {
		private final WorldFeatureInterface water = new WorldFeatureLake(Blocks.FLUID_WATER_STILL.id());
		private final WorldFeatureInterface ice = new WorldFeatureLake(Blocks.ICE.id());

		@Override
		public boolean place(@NotNull World world, @NotNull Random random, @NotNull TilePosc tilePos) {
			boolean frozen = world.getBlockBiome(tilePos).hasTag(BiomeTags.HAS_SURFACE_ICE);
			return (frozen ? this.ice : this.water).place(world, random, tilePos);
		}
	}

	private static final class BelowSeaLavaLake implements WorldFeatureInterface {
		private final WorldFeatureInterface lava = new WorldFeatureLake(Blocks.FLUID_LAVA_STILL.id());

		@Override
		public boolean place(@NotNull World world, @NotNull Random random, @NotNull TilePosc tilePos) {
			if (tilePos.y() < world.getWorldType().getOceanY() || random.nextInt(10) == 0) {
				return this.lava.place(world, random, tilePos);
			}
			return false;
		}
	}

	public static final class FullColumnUniform implements PositionSelector {

		public static final FullColumnUniform INSTANCE = new FullColumnUniform();

		private FullColumnUniform() {}

		@NotNull
		@Override
		public TilePos getValue(@NotNull World world, @NotNull Chunk chunk, @NotNull Random random,
		                        int minY, int maxY, int rangeY) {
			int seaLevel = upstreamWaterline(world);
			int x = chunk.pos.x() * 16 + random.nextInt(16);
			int z = chunk.pos.z() * 16 + random.nextInt(16);
			return new TilePos(x, boundedNextInt(random, 2 * seaLevel), z);
		}
	}

	private static int upstreamWaterline(@NotNull World world) {
		int oceanY = world.getWorldType().getOceanY();
		return oceanY > 0 ? oceanY : Math.max(1, world.getHeightBlocks() / 2);
	}

	private static int boundedNextInt(@NotNull Random random, int bound) {
		return bound <= 0 ? 0 : random.nextInt(bound);
	}

	public static final class SurfaceBandUniform implements PositionSelector {

		public static final SurfaceBandUniform INSTANCE = new SurfaceBandUniform();

		private SurfaceBandUniform() {}

		@NotNull
		@Override
		public TilePos getValue(@NotNull World world, @NotNull Chunk chunk, @NotNull Random random,
		                        int minY, int maxY, int rangeY) {
			int seaLevel = upstreamWaterline(world);
			int x = chunk.pos.x() * 16 + random.nextInt(16);
			int z = chunk.pos.z() * 16 + random.nextInt(16);
			return new TilePos(x, seaLevel + boundedNextInt(random, seaLevel), z);
		}
	}

	public static final class WholeColumnUniform implements PositionSelector {

		public static final WholeColumnUniform INSTANCE = new WholeColumnUniform();

		private WholeColumnUniform() {}

		@NotNull
		@Override
		public TilePos getValue(@NotNull World world, @NotNull Chunk chunk, @NotNull Random random,
		                        int minY, int maxY, int rangeY) {
			int seaLevel = upstreamWaterline(world);
			int x = chunk.pos.x() * 16 + random.nextInt(16);
			int z = chunk.pos.z() * 16 + random.nextInt(16);
			return new TilePos(x, boundedNextInt(random, 2 * seaLevel), z);
		}
	}

	public static final class SubSeaColumnUniform implements PositionSelector {

		private static final int BELOW_SEA = 4;

		public static final SubSeaColumnUniform INSTANCE = new SubSeaColumnUniform();

		private SubSeaColumnUniform() {}

		@NotNull
		@Override
		public TilePos getValue(@NotNull World world, @NotNull Chunk chunk, @NotNull Random random,
		                        int minY, int maxY, int rangeY) {
			int seaLevel = upstreamWaterline(world);
			int x = chunk.pos.x() * 16 + random.nextInt(16) + 8;
			int z = chunk.pos.z() * 16 + random.nextInt(16) + 8;

			return new TilePos(x, boundedNextInt(random, seaLevel - BELOW_SEA), z);
		}
	}

	public static final class UpperHalfUniform implements PositionSelector {

		public static final UpperHalfUniform INSTANCE = new UpperHalfUniform();

		private UpperHalfUniform() {}

		@NotNull
		@Override
		public TilePos getValue(@NotNull World world, @NotNull Chunk chunk, @NotNull Random random,
		                        int minY, int maxY, int rangeY) {
			int half = world.getHeightBlocks() / 2;
			int x = chunk.pos.x() * 16 + random.nextInt(16) + 8;
			int z = chunk.pos.z() * 16 + random.nextInt(16) + 8;
			return new TilePos(x, half + random.nextInt(half), z);
		}
	}

	public static final class SurfaceOrFloor implements PositionSelector {

		public static final SurfaceOrFloor INSTANCE = new SurfaceOrFloor();

		private SurfaceOrFloor() {}

		@NotNull
		@Override
		public TilePos getValue(@NotNull World world, @NotNull Chunk chunk, @NotNull Random random,
		                        int minY, int maxY, int rangeY) {
			int x = chunk.pos.x() * 16 + random.nextInt(16) + 8;
			int z = chunk.pos.z() * 16 + random.nextInt(16) + 8;
			int y = world.getHeightValue(x, z);

			if (world.getWorldType().hasCeiling()) {

				y = Math.min(y - 1, maxY);
				while (y > minY && !world.isAirBlock(x, y, z)) {
					--y;
				}
				while (y > minY && world.isAirBlock(x, y, z)) {
					--y;
				}
				++y;
			}

			return new TilePos(x, y, z);
		}
	}

	public static final class NetherPoolDepth implements PositionSelector {

		private static final int BOP_NETHER_HEIGHT = 128;

		private static final int RANGE = 112;

		public static final NetherPoolDepth INSTANCE = new NetherPoolDepth();

		private NetherPoolDepth() {}

		@NotNull
		@Override
		public TilePos getValue(@NotNull World world, @NotNull Chunk chunk, @NotNull Random random,
		                        int minY, int maxY, int rangeY) {
			int x = chunk.pos.x() * 16 + random.nextInt(16) + 8;
			int z = chunk.pos.z() * 16 + random.nextInt(16) + 8;
			int y = random.nextInt(random.nextInt(random.nextInt(RANGE) + 8) + 8);
			return new TilePos(x, minY + y * rangeY / BOP_NETHER_HEIGHT, z);
		}
	}

	public static final class SeaColumnUniform implements PositionSelector {

		public static final SeaColumnUniform FULL_CHUNK = new SeaColumnUniform(16);

		public static final SeaColumnUniform QUARTER_CHUNK = new SeaColumnUniform(8);

		private final int spread;

		private SeaColumnUniform(int spread) {
			this.spread = spread;
		}

		@NotNull
		@Override
		public TilePos getValue(@NotNull World world, @NotNull Chunk chunk, @NotNull Random random,
		                        int minY, int maxY, int rangeY) {
			int x = chunk.pos.x() * 16 + random.nextInt(this.spread);
			int z = chunk.pos.z() * 16 + random.nextInt(this.spread);
			return new TilePos(x, boundedNextInt(random, upstreamWaterline(world)), z);
		}
	}

	public static final class SeaOffsetUniform implements PositionSelector {
		private final int offset;
		private final int range;

		public SeaOffsetUniform(int offset, int range) {
			this.offset = offset;
			this.range = range;
		}

		@NotNull
		@Override
		public TilePos getValue(@NotNull World world, @NotNull Chunk chunk, @NotNull Random random,
		                        int minY, int maxY, int rangeY) {
			int x = chunk.pos.x() * 16 + random.nextInt(16) + 8;
			int z = chunk.pos.z() * 16 + random.nextInt(16) + 8;
			int base = upstreamWaterline(world) + this.offset;
			return new TilePos(x, random.nextInt(this.range) + base, z);
		}
	}

	public static final int MOSS_TRIES = 20;

	public static final int MOSS_SEA_OFFSET = 6;

	@NotNull
	public static PositionSelector mossSelector() {
		return new SeaOffsetUniform(-MOSS_SEA_OFFSET, 1);
	}

	@NotNull
	public static Supplier<WorldFeatureInterface> mossFeature() {
		return WorldGenMoss::new;
	}

	public static final int IVY_TRIES = 50;

	@NotNull
	public static PositionSelector ivySelector() {
		return new PositionSelector() {
			@NotNull
			@Override
			public TilePos getValue(@NotNull World world, @NotNull Chunk chunk, @NotNull Random random,
			                        int minY, int maxY, int rangeY) {
				int x = chunk.pos.x() * 16 + random.nextInt(16) + 8;
				int z = chunk.pos.z() * 16 + random.nextInt(16) + 8;
				return new TilePos(x, world.getWorldType().getOceanY() / 2, z);
			}
		};
	}

	@NotNull
	public static Supplier<WorldFeatureInterface> ivyFeature() {
		return WorldGenIvy::new;
	}
}
