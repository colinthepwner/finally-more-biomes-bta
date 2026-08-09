package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenDeadTree;
import biomesoplenty.worldgen.tree.WorldGenDeadTree2;
import biomesoplenty.worldgen.tree.WorldGenTaiga5;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenDeadForestSnow extends BiomeGenBase {

	public static final int GRASS_COLOR = 11176526;
	public static final int FOLIAGE_COLOR = 11903827;

	public static final int SKY_COLOR = 9873591;

	public static final int MAP_COLOR = 16777215;

	public BiomeGenDeadForestSnow(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 2;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.reedsPerChunk = -999;
		customBiomeDecorator.wheatGrassPerChunk = 1;
		customBiomeDecorator.shrubsPerChunk = 1;
		customBiomeDecorator.violetsPerChunk = 1;

		this.withPlacementDefaults(0.05f, 0.8f, 0.5f);
		this.setMinMaxHeight(0.2f, 0.7f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(6) == 0
			? new WorldGenDeadTree2(false)
			: (random.nextInt(3) == 0 ? new WorldGenTaiga5(false) : new WorldGenDeadTree(false));
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id());
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
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("tanzanite",
			() -> new WorldGenBOPOreSingle(BOPBlocks.TANZANITE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
