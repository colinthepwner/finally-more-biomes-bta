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

public class BiomeGenSteppe extends BiomeGenBase {

	public static final int GRASS_COLOR = 13413215;
	public static final int FOLIAGE_COLOR = 13413215;
	public static final int MAP_COLOR = 13413215;

	public BiomeGenSteppe(String key) {
		super(key);

		spawnableCreatureList().clear();

		customBiomeDecorator.treesPerChunk = -999;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.grassPerChunk = 15;
		customBiomeDecorator.deadBushPerChunk = 7;
		customBiomeDecorator.tinyCactiPerChunk = 1;
		customBiomeDecorator.generateQuicksand = true;
		customBiomeDecorator.steppePerChunk = 6;
		customBiomeDecorator.aloePerChunk = 2;
		customBiomeDecorator.generatePumpkins = false;

		spawnableCreatureList().add(BOPMobs.HORSE, 5, 2, 6);

		this.withPlacementDefaults(2.0f, 0.05f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.4f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id());
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("ruby",
			() -> new WorldGenBOPOreSingle(BOPBlocks.RUBY_ORE.id(), Blocks.STONE.id()),
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
