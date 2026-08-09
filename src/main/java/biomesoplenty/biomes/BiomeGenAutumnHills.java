package biomesoplenty.biomes;

import biomesoplenty.worldgen.tree.WorldGenDeadTree;
import biomesoplenty.worldgen.tree.WorldGenPersimmon;
import biomesoplenty.worldgen.tree.WorldGenTaiga4;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenAutumnHills extends BiomeGenBase {

	public static final int GRASS_COLOR = 12233056;

	public static final int FOLIAGE_COLOR = 12897365;

	public static final int MAP_COLOR = 12233056;

	public BiomeGenAutumnHills(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 6;
		customBiomeDecorator.grassPerChunk = 13;
		customBiomeDecorator.thornsPerChunk = 1;
		customBiomeDecorator.purpleFlowersPerChunk = 6;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.reedsPerChunk = -999;
		customBiomeDecorator.pumpkinsPerChunk = 2;
		customBiomeDecorator.bushesPerChunk = 45;
		customBiomeDecorator.berryBushesPerChunk = 5;
		customBiomeDecorator.sproutsPerChunk = 2;
		customBiomeDecorator.wheatGrassPerChunk = 16;
		customBiomeDecorator.shrubsPerChunk = 20;
		customBiomeDecorator.waterReedsPerChunk = 2;

		this.withPlacementDefaults(0.5f, 0.2f, 0.5f);
		this.setMinMaxHeight(0.5f, 0.8f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		if (random.nextInt(3) == 0) {
			return new WorldFeatureTallGrass(Blocks.TALLGRASS.id());
		}
		return random.nextInt(5) == 0
			? new WorldFeatureTallGrass(Blocks.TALLGRASS_FERN.id())
			: new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id());
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		if (random.nextInt(9) == 0) {
			return new WorldGenDeadTree(false);
		}
		if (random.nextInt(6) == 0) {
			return new WorldGenTaiga4(false);
		}
		return random.nextInt(5) == 0
			? new WorldGenPersimmon(false)
			: new WorldFeatureTree(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id(), 4);
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
