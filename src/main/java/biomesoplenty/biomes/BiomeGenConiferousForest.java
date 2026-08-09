package biomesoplenty.biomes;

import biomesoplenty.worldgen.tree.WorldGenTaiga3;
import biomesoplenty.worldgen.tree.WorldGenTaiga4;
import biomesoplenty.worldgen.tree.WorldGenTaiga9;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenConiferousForest extends BiomeGenBase {

	public static final int MAP_COLOR = 5410656;

	public BiomeGenConiferousForest(String key) {
		super(key);

		spawnableCreatureList().add(BOPMobs.WOLF, 8, 4, 4);

		customBiomeDecorator.treesPerChunk = 8;
		customBiomeDecorator.grassPerChunk = 10;
		customBiomeDecorator.mushroomsPerChunk = 8;
		customBiomeDecorator.toadstoolsPerChunk = 3;
		customBiomeDecorator.blueMilksPerChunk = 1;
		customBiomeDecorator.poisonIvyPerChunk = 1;
		customBiomeDecorator.berryBushesPerChunk = 1;
		customBiomeDecorator.wheatGrassPerChunk = 5;
		customBiomeDecorator.shrubsPerChunk = 8;
		customBiomeDecorator.waterReedsPerChunk = 2;
		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.gravelPerChunk = 1;
		customBiomeDecorator.gravelPerChunk2 = 1;
		customBiomeDecorator.cloverPatchesPerChunk = 10;

		this.withPlacementDefaults(0.5f, 0.4f, 0.5f);
		this.setMinMaxHeight(0.4f, 0.9f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		if (random.nextInt(3) == 0) {
			return new WorldGenTaiga3(false);
		}
		return random.nextInt(5) == 0
			? new WorldGenTaiga4(false)
			: new WorldGenTaiga9(false);
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(2) == 0
			? new WorldFeatureTallGrass(Blocks.TALLGRASS.id())
			: new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id());
	}

}
