package biomesoplenty.biomes;

import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenThicket extends BiomeGenBase {

	public static final int MAP_COLOR = 7248193;

	public BiomeGenThicket(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 17;
		customBiomeDecorator.grassPerChunk = 1;
		customBiomeDecorator.wheatGrassPerChunk = 1;
		customBiomeDecorator.thornsPerChunk = 25;
		customBiomeDecorator.shrubsPerChunk = 15;

		this.withPlacementDefaults(0.6f, 0.2f, 0.5f);
		this.setMinMaxHeight(0.2f, 0.2f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(5) == 0
			? new WorldFeatureTree(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id(), 4)
			: new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id());
	}
}
