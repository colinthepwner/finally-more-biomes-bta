package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenBambooTree;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenBambooForest extends BiomeGenBase {

	public static final int GRASS_COLOR = 10739795;
	public static final int FOLIAGE_COLOR = 10739795;
	public static final int MAP_COLOR = 10739795;

	public static final int FOG_COLOR = 13428852;

	public BiomeGenBambooForest(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 30;
		customBiomeDecorator.grassPerChunk = 5;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.bushesPerChunk = 5;
		customBiomeDecorator.reedsBOPPerChunk = 6;
		customBiomeDecorator.wheatGrassPerChunk = 3;
		customBiomeDecorator.shrubsPerChunk = 6;
		customBiomeDecorator.cloverPatchesPerChunk = 10;
		customBiomeDecorator.generatePumpkins = false;

		this.withPlacementDefaults(1.2f, 0.9f, 0.5f);
		this.setMinMaxHeight(0.2f, 0.4f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(3) == 0
			? new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id())
			: new WorldGenBambooTree(false);
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(4) == 0
			? new WorldFeatureTallGrass(Blocks.TALLGRASS_FERN.id())
			: new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id());
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("topaz",
			() -> new WorldGenBOPOreSingle(BOPBlocks.TOPAZ_ORE.id(), Blocks.STONE.id()),
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

	@Override
	public int getBiomeFogColor() {
		return FOG_COLOR;
	}

	@Override
	public float getFogCloseness() {
		return 0.8F;
	}
}
