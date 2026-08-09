package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenTaiga5;
import biomesoplenty.worldgen.tree.WorldGenWillow;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.chunk.PlacementMethod;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenWetland extends BiomeGenBase {

	public static final int GRASS_COLOR = 5935967;
	public static final int FOLIAGE_COLOR = 5215831;

	public static final int MAP_COLOR = 5215831;

	public static final int FOG_COLOR = 6189472;

	public BiomeGenWetland(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 10;
		customBiomeDecorator.grassPerChunk = 10;
		customBiomeDecorator.wheatGrassPerChunk = 5;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.mushroomsPerChunk = 8;
		customBiomeDecorator.toadstoolsPerChunk = 1;
		customBiomeDecorator.reedsPerChunk = 15;
		customBiomeDecorator.reedsBOPPerChunk = 15;
		customBiomeDecorator.clayPerChunk = 2;
		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.mudPerChunk = 5;
		customBiomeDecorator.mudPerChunk2 = 5;
		customBiomeDecorator.waterlilyPerChunk = 4;
		customBiomeDecorator.lilyflowersPerChunk = 4;
		customBiomeDecorator.cattailsPerChunk = 20;
		customBiomeDecorator.highCattailsPerChunk = 10;
		customBiomeDecorator.blueFlowersPerChunk = 6;
		customBiomeDecorator.blueMilksPerChunk = 1;
		customBiomeDecorator.portobellosPerChunk = 1;
		customBiomeDecorator.berryBushesPerChunk = 1;
		customBiomeDecorator.shrubsPerChunk = 10;
		customBiomeDecorator.waterReedsPerChunk = 8;
		customBiomeDecorator.koruPerChunk = 1;
		customBiomeDecorator.cloverPatchesPerChunk = 15;

		spawnableCreatureList().clear();
		spawnableWaterCreatureList().clear();
		spawnableMonsterList().add(BOPMobs.SLIME, 10, 1, 3);

		waterColorMultiplier = 6512772;

		this.withPlacementDefaults(0.8f, 0.9f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.5f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(2) == 0
			? new WorldGenTaiga5(false)
			: new WorldGenWillow();
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(6) == 0
			? new WorldFeatureTallGrass(Blocks.TALLGRASS_FERN.id())
			: new WorldFeatureTallGrass(Blocks.TALLGRASS.id());
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("malachite",
			() -> new WorldGenBOPOreSingle(BOPBlocks.MALACHITE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));

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
	public int getBiomeFogColor() {
		return FOG_COLOR;
	}

	@Override
	public float getFogCloseness() {
		return 0.8F;
	}
}
