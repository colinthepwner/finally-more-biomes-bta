package biomesoplenty.biomes;

import biomesoplenty.worldgen.tree.WorldGenMaple;
import biomesoplenty.worldgen.tree.WorldGenTaiga5;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenMapleWoods extends BiomeGenBase {

	public static final int MAP_COLOR = 6988649;

	public BiomeGenMapleWoods(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 9;
		customBiomeDecorator.grassPerChunk = 1;
		customBiomeDecorator.wheatGrassPerChunk = 1;
		customBiomeDecorator.violetsPerChunk = 2;
		customBiomeDecorator.poisonIvyPerChunk = 1;
		customBiomeDecorator.shrubsPerChunk = 2;

		this.withPlacementDefaults(0.2f, 0.8f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.6f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(6) == 0 ? new WorldGenTaiga5(false) : new WorldGenMaple(false);
	}

}
