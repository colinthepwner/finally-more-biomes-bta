package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.WorldGenTundra1;
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

public class BiomeGenTundra extends BiomeGenBase {

	public static final int GRASS_COLOR = 11371606;
	public static final int FOLIAGE_COLOR = 12543566;
	public static final int MAP_COLOR = 11371606;

	public BiomeGenTundra(String key) {
		super(key);

		spawnableCreatureList().clear();

		customBiomeDecorator.treesPerChunk = 5;
		customBiomeDecorator.grassPerChunk = 8;
		customBiomeDecorator.wheatGrassPerChunk = 3;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.reedsPerChunk = -999;
		customBiomeDecorator.gravelPerChunk = 8;
		customBiomeDecorator.gravelPerChunk2 = 8;
		customBiomeDecorator.shrubsPerChunk = 2;
		customBiomeDecorator.waterReedsPerChunk = 2;
		customBiomeDecorator.violetsPerChunk = 1;

		this.withPlacementDefaults(0.2f, 0.8f, 0.5f);
		this.setMinMaxHeight(0.1f, 0.3f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(4) == 0
			? new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id())
			: new WorldGenTundra1();
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(4) == 0
			? new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id())
			: new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id());
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
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("tanzanite",
			() -> new WorldGenBOPOreSingle(BOPBlocks.TANZANITE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
