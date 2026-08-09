package biomesoplenty.biomes;

import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureCherryTreeFancy;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeCherry;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenCherryBlossomGrove extends BiomeGenBase {

	public static final int GRASS_COLOR = 10747818;
	public static final int FOLIAGE_COLOR = 10747818;

	public static final int MAP_COLOR = 16289679;

	public BiomeGenCherryBlossomGrove(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 3;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.pinkFlowersPerChunk = 15;
		customBiomeDecorator.whiteFlowersPerChunk = 30;
		customBiomeDecorator.tinyFlowersPerChunk = 25;
		customBiomeDecorator.grassPerChunk = 15;
		customBiomeDecorator.lilyflowersPerChunk = 9;
		customBiomeDecorator.wheatGrassPerChunk = 1;
		customBiomeDecorator.shrubsPerChunk = 2;
		customBiomeDecorator.cloverPatchesPerChunk = 15;
		customBiomeDecorator.generatePumpkins = false;

		this.withPlacementDefaults(0.7f, 0.8f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.4f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(3) == 0
			? new WorldFeatureCherryTreeFancy(Blocks.LEAVES_CHERRY.id(), Blocks.LOG_CHERRY.id())
			: new WorldFeatureTreeCherry(Blocks.LEAVES_CHERRY.id(), Blocks.LOG_CHERRY.id(), 4);
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
