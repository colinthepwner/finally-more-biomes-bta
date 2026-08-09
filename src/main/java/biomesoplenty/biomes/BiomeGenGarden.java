package biomesoplenty.biomes;

import biomesoplenty.entities.EntityRosester;
import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.WorldGenGiantFlowerRed;
import biomesoplenty.worldgen.WorldGenGiantFlowerYellow;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenGarden extends BiomeGenBase {

	public static final int GRASS_COLOR = 7656308;
	public static final int FOLIAGE_COLOR = 6742630;

	public static final int MAP_COLOR = 7656308;

	public BiomeGenGarden(String key) {
		super(key);

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(BOPBlocks.LONG_GRASS)
			.withFillerBlock(Blocks.DIRT)
			.build());

		customBiomeDecorator.treesPerChunk = 2;
		customBiomeDecorator.flowersPerChunk = 20;
		customBiomeDecorator.whiteFlowersPerChunk = 25;
		customBiomeDecorator.highGrassPerChunk = 6;
		customBiomeDecorator.hydrangeasPerChunk = 3;
		customBiomeDecorator.sproutsPerChunk = 2;
		customBiomeDecorator.sunflowersPerChunk = 4;
		customBiomeDecorator.rosesPerChunk = 20;
		customBiomeDecorator.grassPerChunk = 25;

		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.lilyflowersPerChunk = 4;
		customBiomeDecorator.wheatGrassPerChunk = 10;
		customBiomeDecorator.shrubsPerChunk = 10;
		customBiomeDecorator.waterReedsPerChunk = 4;
		customBiomeDecorator.generatePumpkins = true;

		spawnableCreatureList().clear();

		spawnableCreatureList().add(EntityRosester.class, 10, 4, 4);
		spawnableCreatureList().add(EntityRosester.class, 10, 2, 4);

		this.withPlacementDefaults(0.7f, 0.8f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.4f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		if (random.nextInt(6) == 0) return new WorldGenGiantFlowerRed();
		if (random.nextInt(6) == 0) return new WorldGenGiantFlowerYellow();
		return new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id());
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		if (random.nextInt(4) == 0) return new WorldFeatureTallGrass(Blocks.TALLGRASS.id());
		return random.nextInt(2) == 0
			? new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id())
			: new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id());
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("peridot",
			() -> new WorldGenBOPOreSingle(BOPBlocks.PERIDOT_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}

	@Override
	public int getBiomeGrassColor() {
		return GRASS_COLOR;
	}

	@Override
	public int getBiomeFoliageColor() {
		return FOLIAGE_COLOR;
	}
}
