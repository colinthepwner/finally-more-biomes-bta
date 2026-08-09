package biomesoplenty.biomes;

import biomesoplenty.worldgen.tree.WorldGenAutumn;
import biomesoplenty.worldgen.tree.WorldGenRainforestTree1;
import biomesoplenty.worldgen.tree.WorldGenTaiga10;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenBorealForest extends BiomeGenBase {

	public static final int GRASS_COLOR = 10467185;

	public static final int FOLIAGE_COLOR = 13225573;

	public static final int MAP_COLOR = 10467185;

	public BiomeGenBorealForest(String key) {
		super(key);

		spawnableCreatureList().add(BOPMobs.WOLF, 5, 4, 4);

		customBiomeDecorator.treesPerChunk = 20;
		customBiomeDecorator.grassPerChunk = 50;
		customBiomeDecorator.wheatGrassPerChunk = 25;
		customBiomeDecorator.shrubsPerChunk = 10;
		customBiomeDecorator.waterReedsPerChunk = 4;

		this.withPlacementDefaults(0.6f, 0.7f, 0.5f);
		this.setMinMaxHeight(0.2f, 1.0f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(2) == 0
			? new WorldFeatureTallGrass(Blocks.TALLGRASS_FERN.id())
			: new WorldFeatureTallGrass(Blocks.TALLGRASS.id());
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(4) == 0
			? new WorldGenRainforestTree1(false)
			: (random.nextInt(5) == 0
				? new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id())
				: (random.nextInt(3) == 0
					? new WorldGenAutumn(false)
					: (random.nextInt(3) == 0
						? new WorldFeatureTree(Blocks.LEAVES_BIRCH.id(), Blocks.LOG_BIRCH.id(), 5)
						: new WorldGenTaiga10(false))));
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
