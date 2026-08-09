package biomesoplenty.biomes;

import biomesoplenty.worldgen.tree.WorldGenRedwoodTree;
import biomesoplenty.worldgen.tree.WorldGenRedwoodTree2;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenRedwoodForest extends BiomeGenBase {

	public static final int MAP_COLOR = 7187004;

	public BiomeGenRedwoodForest(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 75;
		customBiomeDecorator.grassPerChunk = 16;
		customBiomeDecorator.bushesPerChunk = 4;
		customBiomeDecorator.berryBushesPerChunk = 1;
		customBiomeDecorator.wheatGrassPerChunk = 7;
		customBiomeDecorator.shrubsPerChunk = 10;
		customBiomeDecorator.redwoodShrubsPerChunk = 100;
		customBiomeDecorator.waterReedsPerChunk = 2;
		customBiomeDecorator.generatePumpkins = false;

		this.withPlacementDefaults(0.8f, 0.4f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.4f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(4) == 0
			? new WorldGenRedwoodTree(false)
			: (random.nextInt(8) == 0
				? new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id())
				: new WorldGenRedwoodTree2(false));
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(4) == 0
			? new WorldFeatureTallGrass(Blocks.TALLGRASS_FERN.id())
			: new WorldFeatureTallGrass(Blocks.TALLGRASS.id());
	}
}
