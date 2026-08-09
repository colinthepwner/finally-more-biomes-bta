package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenGrassland extends BiomeGenBase {

	public static final int GRASS_COLOR = 8379261;
	public static final int FOLIAGE_COLOR = 8379261;
	public static final int MAP_COLOR = 8379261;

	public BiomeGenGrassland(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = -999;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.grassPerChunk = 2;
		customBiomeDecorator.wheatGrassPerChunk = 1;
		customBiomeDecorator.reedsPerChunk = 35;
		customBiomeDecorator.mushroomsPerChunk = 20;
		customBiomeDecorator.waterLakesPerChunk = 15;
		customBiomeDecorator.portobellosPerChunk = 3;
		customBiomeDecorator.reedsBOPPerChunk = 5;
		customBiomeDecorator.waterReedsPerChunk = 2;
		customBiomeDecorator.generatePumpkins = false;

		spawnableCreatureList().add(BOPMobs.SHEEP, 14, 4, 4);
		spawnableCreatureList().add(BOPMobs.PIG, 12, 4, 4);
		spawnableCreatureList().add(BOPMobs.CHICKEN, 12, 4, 4);
		spawnableCreatureList().add(BOPMobs.COW, 10, 4, 4);
		spawnableCreatureList().add(BOPMobs.HORSE, 5, 2, 6);

		this.withPlacementDefaults(0.7f, 0.7f, 0.5f);
		this.setMinMaxHeight(0.2f, 0.5f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(3) == 0
			? new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id())
			: new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id());
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
