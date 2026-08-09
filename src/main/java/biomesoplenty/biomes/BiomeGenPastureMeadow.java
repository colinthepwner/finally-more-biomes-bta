package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenPastureMeadow extends BiomeGenBase {

	public static final int GRASS_COLOR = 13166666;
	public static final int FOLIAGE_COLOR = 13166666;

	public static final int MAP_COLOR = 13166666;

	public BiomeGenPastureMeadow(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 1;
		customBiomeDecorator.grassPerChunk = 15;
		customBiomeDecorator.wheatGrassPerChunk = 5;
		customBiomeDecorator.sunflowersPerChunk = 99;
		customBiomeDecorator.whiteFlowersPerChunk = 20;

		spawnableCreatureList().clear();

		this.withPlacementDefaults(0.8f, 0.4f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.4f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(8) == 0
			? new WorldFeatureTallGrass(BOPPlants.BARLEY.id())
			: new WorldFeatureTallGrass(Blocks.TALLGRASS.id());
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
