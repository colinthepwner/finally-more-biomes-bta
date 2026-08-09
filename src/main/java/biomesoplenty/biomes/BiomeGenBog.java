package biomesoplenty.biomes;

import biomesoplenty.entities.EntityGlob;
import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.WorldGenBogBush;
import biomesoplenty.worldgen.WorldGenCypress1;
import biomesoplenty.worldgen.WorldGenCypress2;
import biomesoplenty.worldgen.WorldGenMarsh;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.chunk.PlacementMethod;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenBog extends BiomeGenBase {

	public static final int GRASS_COLOR = 14193503;

	public static final int FOLIAGE_COLOR = 14345593;
	public static final int MAP_COLOR = 14193503;

	private static final int MARSH_SEA_OFFSET = 2;

	private static final int MARSH_TRIES = 10;

	public BiomeGenBog(String key) {
		super(key);

		spawnableCreatureList().clear();
		spawnableWaterCreatureList().clear();

		spawnableCreatureList().add(EntityGlob.class, 1, 1, 1);

		customBiomeDecorator.treesPerChunk = 12;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.mushroomsPerChunk = 8;
		customBiomeDecorator.grassPerChunk = 5;
		customBiomeDecorator.bushesPerChunk = 6;
		customBiomeDecorator.mudPerChunk = 2;
		customBiomeDecorator.mudPerChunk2 = 2;

		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.algaePerChunk = 2;
		customBiomeDecorator.waterlilyPerChunk = 4;

		customBiomeDecorator.reedsBOPPerChunk = 8;
		customBiomeDecorator.blueMilksPerChunk = 1;
		customBiomeDecorator.waterLakesPerChunk = 6;
		customBiomeDecorator.wheatGrassPerChunk = 3;
		customBiomeDecorator.poisonWaterPerChunk = 2;
		customBiomeDecorator.waterReedsPerChunk = 8;
		customBiomeDecorator.koruPerChunk = 1;
		customBiomeDecorator.shrubsPerChunk = 10;
		customBiomeDecorator.generatePumpkins = false;

		this.withPlacementDefaults(0.8f, 0.9f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.3f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		if (random.nextInt(3) == 0) return new WorldGenCypress1(false);
		if (random.nextInt(6) == 0) return new WorldGenCypress2(false);
		return new WorldGenBogBush();
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		if (random.nextInt(2) == 0) return new WorldFeatureTallGrass(Blocks.TALLGRASS.id());
		return new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id());
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("malachite",
			() -> new WorldGenBOPOreSingle(BOPBlocks.MALACHITE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));

		sink.add("marsh",
			WorldGenMarsh::new,
			new BOPDecorations.SeaOffsetUniform(-MARSH_SEA_OFFSET, 1),
			new PlacementMethod.TriesPerChunk(MARSH_TRIES));
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
