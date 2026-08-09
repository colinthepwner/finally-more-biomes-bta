package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.WorldGenPrairie;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenPrairie extends BiomeGenBase {

	public static final int GRASS_COLOR = 13165952;

	public static final int FOLIAGE_COLOR = 11395195;

	public static final int MAP_COLOR = 13165952;

	public BiomeGenPrairie(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 1;
		customBiomeDecorator.grassPerChunk = 999;
		customBiomeDecorator.whiteFlowersPerChunk = 20;
		customBiomeDecorator.goldenrodsPerChunk = 40;
		customBiomeDecorator.portobellosPerChunk = 2;
		customBiomeDecorator.berryBushesPerChunk = 2;
		customBiomeDecorator.wheatGrassPerChunk = 25;
		customBiomeDecorator.carrotsPerChunk = 1;
		customBiomeDecorator.shrubsPerChunk = 3;
		customBiomeDecorator.waterReedsPerChunk = 4;

		spawnableCreatureList().add(BOPMobs.HORSE, 5, 2, 6);

		this.withPlacementDefaults(0.9f, 0.6f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.4f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return new WorldGenPrairie(false);
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(5) == 0
			? new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id())
			: (random.nextInt(3) == 0
				? new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id())
				: new WorldFeatureTallGrass(Blocks.TALLGRASS.id()));
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
