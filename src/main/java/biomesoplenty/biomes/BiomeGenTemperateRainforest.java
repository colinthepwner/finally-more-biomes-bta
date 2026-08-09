package biomesoplenty.biomes;

import biomesoplenty.worldgen.tree.WorldGenSequoia;
import biomesoplenty.worldgen.tree.WorldGenTemperate;
import biomesoplenty.worldgen.tree.WorldGenWillow;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.chunk.PlacementMethod;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenTemperateRainforest extends BiomeGenBase {

	public static final int GRASS_COLOR = 11981671;
	public static final int FOLIAGE_COLOR = 12311907;

	public static final int SKY_COLOR = 11061213;

	public static final int FOG_COLOR = 13753294;
	public static final int MAP_COLOR = 12311907;

	public BiomeGenTemperateRainforest(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 22;
		customBiomeDecorator.grassPerChunk = 25;
		customBiomeDecorator.wheatGrassPerChunk = 10;
		customBiomeDecorator.generatePumpkins = false;
		customBiomeDecorator.mushroomsPerChunk = 4;
		customBiomeDecorator.blueMilksPerChunk = 3;
		customBiomeDecorator.poisonIvyPerChunk = 1;
		customBiomeDecorator.carrotsPerChunk = 1;
		customBiomeDecorator.gravelPerChunk = 4;
		customBiomeDecorator.gravelPerChunk2 = 4;

		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.shrubsPerChunk = 10;
		customBiomeDecorator.waterReedsPerChunk = 2;

		this.withPlacementDefaults(0.7f, 0.8f, 0.5f);
		this.setMinMaxHeight(0.2f, 0.6f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		if (random.nextInt(10) == 0) return new WorldGenWillow();
		if (random.nextInt(6) == 0) return new WorldGenSequoia(false);
		if (random.nextInt(2) == 0) return new WorldGenTemperate(false);
		return new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id());
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		if (random.nextInt(6) == 0) return new WorldFeatureTallGrass(Blocks.TALLGRASS.id());
		if (random.nextInt(2) == 0) return new WorldFeatureTallGrass(Blocks.TALLGRASS_FERN.id());
		if (random.nextInt(4) == 0) return new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id());
		return new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id());
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

	@Override
	public float getFogCloseness() {
		return 0.8F;
	}
}
