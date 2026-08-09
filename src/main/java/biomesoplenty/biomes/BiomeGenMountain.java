package biomesoplenty.biomes;

import biomesoplenty.worldgen.tree.WorldGenPineTree;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenMountain extends BiomeGenBase {

	public static final int MAP_COLOR = 8430421;

	public BiomeGenMountain(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 2;
		customBiomeDecorator.grassPerChunk = 3;
		customBiomeDecorator.wheatGrassPerChunk = 1;
		customBiomeDecorator.berryBushesPerChunk = 3;
		customBiomeDecorator.shrubsPerChunk = 10;
		customBiomeDecorator.waterReedsPerChunk = 4;

		this.withPlacementDefaults(0.5f, 0.1f, 0.5f);
		this.setMinMaxHeight(1.0f, 1.5f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(4) == 0
			? new WorldGenPineTree()
			: new WorldFeatureTree(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id(), 4);
	}

}
