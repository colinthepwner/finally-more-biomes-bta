package biomesoplenty.biomes;

import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenBirchForest extends BiomeGenBase {

	public static final int MAP_COLOR = 8431445;

	public BiomeGenBirchForest(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 5;
		customBiomeDecorator.grassPerChunk = 3;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.tinyFlowersPerChunk = 6;
		customBiomeDecorator.poisonIvyPerChunk = 3;
		customBiomeDecorator.lilyOfTheValleysPerChunk = 15;
		customBiomeDecorator.wheatGrassPerChunk = 1;
		customBiomeDecorator.shrubsPerChunk = 1;
		customBiomeDecorator.cloverPatchesPerChunk = 20;

		this.withPlacementDefaults(0.4f, 0.3f, 0.5f);
		this.setMinMaxHeight(0.1f, 0.2f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return new WorldFeatureTree(Blocks.LEAVES_BIRCH.id(), Blocks.LOG_BIRCH.id(), 5);
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(3) == 0
			? new WorldFeatureTallGrass(Blocks.TALLGRASS.id())
			: new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id());
	}

}
