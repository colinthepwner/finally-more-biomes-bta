package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenLog;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeFancy;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenWoodland extends BiomeGenBase {

	public static final int MAP_COLOR = 8694061;

	public BiomeGenWoodland(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 9;
		customBiomeDecorator.grassPerChunk = 7;
		customBiomeDecorator.mushroomsPerChunk = 4;
		customBiomeDecorator.wheatGrassPerChunk = 3;
		customBiomeDecorator.toadstoolsPerChunk = 3;
		customBiomeDecorator.shrubsPerChunk = 20;
		customBiomeDecorator.waterReedsPerChunk = 2;
		customBiomeDecorator.cloverPatchesPerChunk = 10;

		this.withPlacementDefaults(1.7f, 0.2f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.7f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(5) == 0
			? new WorldGenLog()
			: (random.nextInt(10) == 0
				? new WorldFeatureTreeFancy(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id())
				: new WorldFeatureTree(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id(), 4));
	}

}
