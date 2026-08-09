package biomesoplenty.biomes.promisedland;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPPromisedLand;
import com.betteroplenty.world.BOPDecorations;
import biomesoplenty.entities.EntityBird;
import biomesoplenty.entities.EntityPixie;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.world.biome.SurfaceProperties;
import net.minecraft.core.world.generate.chunk.PlacementMethod;
import biomesoplenty.worldgen.tree.WorldGenPromisedTree;
import biomesoplenty.worldgen.tree.WorldGenPromisedTree2;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenPromisedLandForest extends BiomeGenBase {

	public static final int GRASS_COLOR = 7925125;

	public static final int SKY_COLOR = 5883101;

	public static final int FOG_COLOR = 16754234;

	public static final int MAP_COLOR = 7925125;

	public BiomeGenPromisedLandForest(String key) {
		super(key);

		spawnableCreatureList().clear();
		spawnableWaterCreatureList().clear();
		spawnableMonsterList().clear();

		spawnableAmbientCreatureList().add(EntityBird.class, 10, 3, 5);
		spawnableMonsterList().add(EntityPixie.class, 4, 1, 3);

		customBiomeDecorator.treesPerChunk = 10;
		customBiomeDecorator.grassPerChunk = -999;
		customBiomeDecorator.holyTallGrassPerChunk = 100;
		customBiomeDecorator.promisedWillowPerChunk = 40;
		customBiomeDecorator.pinkFlowersPerChunk = 12;
		customBiomeDecorator.rainbowflowersPerChunk = 10;
		customBiomeDecorator.blueMilksPerChunk = 5;
		customBiomeDecorator.generateLakes = false;
		customBiomeDecorator.pondsPerChunk = -100;
		customBiomeDecorator.hotSpringsPerChunk = 5;
		customBiomeDecorator.waterLakesPerChunk = 10;
		customBiomeDecorator.crystalsPerChunk = 25;
		customBiomeDecorator.crystals2PerChunk = 50;
		customBiomeDecorator.cloudsPerChunk = 1;
		customBiomeDecorator.generatePumpkins = false;
		customBiomeDecorator.generateMossySkystone = true;

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(BOPPromisedLand.HOLY_GRASS)
			.withFillerBlock(BOPPromisedLand.HOLY_DIRT)
			.build());

		this.withPlacementDefaults(2.0f, 2.0f, 0.25f);
		this.setMinMaxHeight(0.1f, 2.0f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(8) == 0 ? new WorldGenPromisedTree2(false) : new WorldGenPromisedTree(false);
	}

	@Override
	public int getBiomeGrassColor() {
		return GRASS_COLOR;
	}

	@Override
	public int getBiomeFoliageColor() {
		return GRASS_COLOR;
	}

	@Override
	public int getSkyColorByTemp(float temperature) {
		return SKY_COLOR;
	}

	@Override
	public int getBiomeFogColor() {
		return FOG_COLOR;
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("amethyst",
			() -> new WorldGenBOPOreSingle(BOPBlocks.AMETHYST_ORE.id(),
				BOPPromisedLand.HOLY_STONE.id()),
			new BOPDecorations.HeightOffsetUniform(30, 30),
			new PlacementMethod.TriesPerChunk(25));
	}

}
