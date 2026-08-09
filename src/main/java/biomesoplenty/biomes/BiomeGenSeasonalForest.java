package biomesoplenty.biomes;

import biomesoplenty.worldgen.tree.WorldGenAutumn;
import biomesoplenty.worldgen.tree.WorldGenAutumn2;
import biomesoplenty.worldgen.tree.WorldGenDeadTree2;
import biomesoplenty.worldgen.tree.WorldGenMaple;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.block.BOPWoodSets;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeFancy;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenSeasonalForest extends BiomeGenBase {

	public static final int GRASS_COLOR = 12502092;
	public static final int FOLIAGE_COLOR = 11781186;

	public static final int FOG_COLOR = 16764548;
	public static final int MAP_COLOR = 12502092;

	public BiomeGenSeasonalForest(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 20;
		customBiomeDecorator.grassPerChunk = 8;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.toadstoolsPerChunk = 4;
		customBiomeDecorator.wheatGrassPerChunk = 4;
		customBiomeDecorator.shrubsPerChunk = 15;
		customBiomeDecorator.waterReedsPerChunk = 4;

		spawnableCreatureList().add(BOPMobs.WOLF, 5, 4, 4);

		this.withPlacementDefaults(0.7f, 0.8f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.7f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(2) == 0
			? new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id())
			: new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id());
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		if (random.nextInt(2) == 0) return new WorldGenAutumn2(false);
		if (random.nextInt(3) == 0) return new WorldGenAutumn(false);
		if (random.nextInt(6) == 0) {
			return new WorldFeatureTreeFancy(BOPWoodSets.ORANGE_AUTUMN.leaves.id(), Blocks.LOG_OAK.id());
		}
		if (random.nextInt(6) == 0) {
			return new WorldFeatureTreeFancy(BOPWoodSets.MAPLE.leaves.id(), Blocks.LOG_OAK.id());
		}
		if (random.nextInt(3) == 0) return new WorldGenMaple(false);
		if (random.nextInt(5) == 0) return new WorldGenDeadTree2(false);
		if (random.nextInt(6) == 0) {
			return new WorldFeatureTreeFancy(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id());
		}
		return new WorldFeatureTree(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id(), 4);
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
}
