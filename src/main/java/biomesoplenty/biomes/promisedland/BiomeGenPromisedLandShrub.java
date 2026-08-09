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
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenPromisedLandShrub extends BiomeGenBase {

	public static final int GRASS_COLOR = 7925125;

	public static final int SKY_COLOR = 5883101;

	public static final int FOG_COLOR = 16754234;

	public static final int MAP_COLOR = 13433204;

	public BiomeGenPromisedLandShrub(String key) {
		super(key);

		spawnableCreatureList().clear();
		spawnableWaterCreatureList().clear();
		spawnableMonsterList().clear();

		spawnableAmbientCreatureList().add(EntityBird.class, 10, 3, 5);
		spawnableMonsterList().add(EntityPixie.class, 4, 1, 3);

		customBiomeDecorator.treesPerChunk = 40;
		customBiomeDecorator.grassPerChunk = -999;
		customBiomeDecorator.holyTallGrassPerChunk = 50;
		customBiomeDecorator.promisedWillowPerChunk = 80;
		customBiomeDecorator.rainbowflowersPerChunk = 5;
		customBiomeDecorator.generateLakes = false;
		customBiomeDecorator.pondsPerChunk = -100;
		customBiomeDecorator.crystalsPerChunk = 25;
		customBiomeDecorator.crystals2PerChunk = 50;
		customBiomeDecorator.cloudsPerChunk = 1;
		customBiomeDecorator.generatePumpkins = false;
		customBiomeDecorator.generateMossySkystone = true;

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(BOPPromisedLand.HOLY_GRASS)
			.withFillerBlock(BOPPromisedLand.HOLY_DIRT)
			.build());

		this.withPlacementDefaults(2.0f, 2.0f, 0.75f);
		this.setMinMaxHeight(0.1f, 2.0f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id());
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
