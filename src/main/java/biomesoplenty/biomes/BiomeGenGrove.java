package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenChaparral2;
import biomesoplenty.worldgen.WorldGenPoplar;
import biomesoplenty.worldgen.WorldGenPoplar2;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenGrove extends BiomeGenBase {

	public static final int GRASS_COLOR = 5341009;

	public static final int FOLIAGE_COLOR = 6396257;

	public static final int MAP_COLOR = 5341009;

	public BiomeGenGrove(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 3;
		customBiomeDecorator.flowersPerChunk = 5;
		customBiomeDecorator.tinyFlowersPerChunk = 80;
		customBiomeDecorator.whiteFlowersPerChunk = 15;
		customBiomeDecorator.grassPerChunk = 8;
		customBiomeDecorator.wheatGrassPerChunk = 4;
		customBiomeDecorator.sproutsPerChunk = 1;
		customBiomeDecorator.lilyflowersPerChunk = 3;
		customBiomeDecorator.berryBushesPerChunk = 2;
		customBiomeDecorator.shrubsPerChunk = 3;
		customBiomeDecorator.cloverPatchesPerChunk = 20;
		customBiomeDecorator.generatePumpkins = false;

		this.withPlacementDefaults(0.4f, 0.8f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.4f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		if (random.nextInt(2) == 0) {
			return new WorldGenChaparral2();
		}
		return random.nextInt(3) == 0 ? new WorldGenPoplar2() : new WorldGenPoplar();
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(2) == 0
			? new WorldFeatureTallGrass(Blocks.TALLGRASS.id())
			: new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id());
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
