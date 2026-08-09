package biomesoplenty.biomes;

import biomesoplenty.worldgen.tree.WorldGenDeciduous;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenDeciduousForest extends BiomeGenBase {

	public static final int GRASS_COLOR = 12695369;
	public static final int FOLIAGE_COLOR = 12896570;

	public static final int MAP_COLOR = 12695369;

	public BiomeGenDeciduousForest(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 15;
		customBiomeDecorator.grassPerChunk = 10;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.toadstoolsPerChunk = 1;
		customBiomeDecorator.bushesPerChunk = 8;
		customBiomeDecorator.berryBushesPerChunk = 2;
		customBiomeDecorator.blueMilksPerChunk = 2;
		customBiomeDecorator.poisonIvyPerChunk = 1;
		customBiomeDecorator.wheatGrassPerChunk = 10;
		customBiomeDecorator.shrubsPerChunk = 10;
		customBiomeDecorator.waterReedsPerChunk = 2;

		this.withPlacementDefaults(0.7f, 0.8f, 0.5f);
		this.setMinMaxHeight(0.1f, 0.2f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(4) == 0
			? new WorldFeatureTreeShrub(Blocks.LEAVES_BIRCH.id(), Blocks.LOG_BIRCH.id())
			: new WorldGenDeciduous(false);
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(5) == 0
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
}
