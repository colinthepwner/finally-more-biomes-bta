package biomesoplenty.biomes;

import biomesoplenty.worldgen.tree.WorldGenSequoia;
import biomesoplenty.worldgen.tree.WorldGenSequoiaOrange;
import biomesoplenty.worldgen.tree.WorldGenSequoiaYellow;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.chunk.PlacementMethod;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenFungiForest extends BiomeGenBase {

	public static final int GRASS_COLOR = 15792496;
	public static final int MAP_COLOR = 15792496;

	public static final int FOLIAGE_COLOR = 11139946;

	public static final int SKY_COLOR = 11513806;

	public static final int WATER_COLOR = 65326;

	public static final int FOG_COLOR = 16050295;

	public static final int BIG_MUSHROOMS_PER_CHUNK = 8;

	public BiomeGenFungiForest(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 8;
		customBiomeDecorator.grassPerChunk = 4;
		customBiomeDecorator.sproutsPerChunk = 2;
		customBiomeDecorator.bushesPerChunk = 1;
		customBiomeDecorator.highGrassPerChunk = 1;
		customBiomeDecorator.mushroomsPerChunk = 8;
		customBiomeDecorator.bigMushroomsPerChunk = BIG_MUSHROOMS_PER_CHUNK;
		customBiomeDecorator.toadstoolsPerChunk = 5;
		customBiomeDecorator.portobellosPerChunk = 7;
		customBiomeDecorator.blueMilksPerChunk = 2;
		customBiomeDecorator.glowshroomsPerChunk = 1;
		customBiomeDecorator.blueFlowersPerChunk = 3;
		customBiomeDecorator.reedsBOPPerChunk = 1;
		customBiomeDecorator.wheatGrassPerChunk = 3;
		customBiomeDecorator.shrubsPerChunk = 1;
		customBiomeDecorator.cloverPatchesPerChunk = 20;
		customBiomeDecorator.generateMycelium = true;
		customBiomeDecorator.generatePumpkins = true;

		waterColorMultiplier = WATER_COLOR;

		spawnableCreatureList().clear();
		spawnableWaterCreatureList().clear();
		spawnableCreatureList().add(BOPMobs.MOOSHROOM, 3, 4, 8);

		this.withPlacementDefaults(0.9f, 1.0f, 0.5f);
		this.setMinMaxHeight(0.2f, 0.5f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(3) == 0
			? new WorldGenSequoiaOrange(false)
			: (random.nextInt(5) == 0
				? new WorldGenSequoiaYellow(false)
				: (random.nextInt(2) == 0
					? new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id())
					: new WorldGenSequoia(false)));
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(3) == 0
			? new WorldFeatureTallGrass(Blocks.TALLGRASS.id())
			: (random.nextInt(4) == 0
				? new WorldFeatureTallGrass(Blocks.TALLGRASS_FERN.id())
				: (random.nextInt(2) == 0
					? new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id())
					: new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id())));
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("moss",
			BOPDecorations.mossFeature(),
			BOPDecorations.mossSelector(),
			new PlacementMethod.TriesPerChunk(BOPDecorations.MOSS_TRIES));
	}

	@Override
	public int getBiomeGrassColor() {
		return GRASS_COLOR;
	}

	@Override
	public int getBiomeFoliageColor() {
		return FOLIAGE_COLOR;
	}

	@Override
	public int getSkyColorByTemp(float temperature) {
		return SKY_COLOR;
	}

	@Override
	public int getBiomeFogColor() {
		return FOG_COLOR;
	}
}
