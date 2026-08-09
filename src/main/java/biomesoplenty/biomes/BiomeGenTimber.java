package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenChaparral2;
import biomesoplenty.worldgen.WorldGenChaparral3;
import biomesoplenty.worldgen.tree.WorldGenDeciduous2;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenTimber extends BiomeGenBase {

	public static final int GRASS_COLOR = 10923366;
	public static final int FOLIAGE_COLOR = 11049817;
	public static final int MAP_COLOR = 10923366;

	public BiomeGenTimber(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 20;
		customBiomeDecorator.grassPerChunk = 8;
		customBiomeDecorator.wheatGrassPerChunk = 4;
		customBiomeDecorator.thornsPerChunk = 2;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.toadstoolsPerChunk = 2;
		customBiomeDecorator.waterReedsPerChunk = 4;
		customBiomeDecorator.shrubsPerChunk = 10;

		this.withPlacementDefaults(0.7f, 0.8f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.4f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		if (random.nextInt(4) == 0) return new WorldGenChaparral3();
		if (random.nextInt(8) == 0) return new WorldGenChaparral2();
		return new WorldGenDeciduous2(false);
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
