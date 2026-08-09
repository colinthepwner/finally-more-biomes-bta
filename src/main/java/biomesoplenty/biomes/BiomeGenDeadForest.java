package biomesoplenty.biomes;

import biomesoplenty.worldgen.tree.WorldGenDeadTree;
import biomesoplenty.worldgen.tree.WorldGenDeadTree2;
import biomesoplenty.worldgen.tree.WorldGenTaiga5;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenDeadForest extends BiomeGenBase {

	public static final int GRASS_COLOR = 12362085;
	public static final int FOLIAGE_COLOR = 12362085;
	public static final int MAP_COLOR = 12362085;

	public static final int SKY_COLOR = 9873591;

	public BiomeGenDeadForest(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 3;
		customBiomeDecorator.grassPerChunk = 1;
		customBiomeDecorator.thornsPerChunk = 2;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.reedsPerChunk = -999;
		customBiomeDecorator.wheatGrassPerChunk = 1;
		customBiomeDecorator.shrubsPerChunk = 2;
		customBiomeDecorator.waterReedsPerChunk = 2;

		this.withPlacementDefaults(1.2f, 0.1f, 0.5f);
		this.setMinMaxHeight(0.2f, 0.7f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(3) == 0
			? new WorldGenDeadTree(false)
			: (random.nextInt(4) == 0 ? new WorldGenTaiga5(false) : new WorldGenDeadTree2(false));
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(9) == 0
			? new WorldFeatureTallGrass(Blocks.DEADBUSH.id())
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
	public int getSkyColorByTemp(float temperature) {
		return SKY_COLOR;
	}
}
