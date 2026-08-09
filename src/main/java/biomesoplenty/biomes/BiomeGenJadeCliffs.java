package biomesoplenty.biomes;

import biomesoplenty.worldgen.tree.WorldGenPineTree;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenJadeCliffs extends BiomeGenBase {

	public static final int GRASS_COLOR = 8168808;

	public static final int FOLIAGE_COLOR = 9096298;

	public static final int SKY_COLOR = 12045485;

	public static final int MAP_COLOR = 9096298;

	public BiomeGenJadeCliffs(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 12;
		customBiomeDecorator.grassPerChunk = 3;
		customBiomeDecorator.wheatGrassPerChunk = 1;
		customBiomeDecorator.carrotsPerChunk = 1;

		this.withPlacementDefaults(0.5f, 0.1f, 0.5f);
		this.setMinMaxHeight(0.5f, 1.5f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(4) == 0
			? new WorldFeatureTreeShrub(Blocks.LEAVES_PINE.id(), Blocks.LOG_OAK.id())
			: new WorldGenPineTree();
	}

	@Override
	public int getBiomeGrassColor() {
		return GRASS_COLOR;
	}

	@Override
	public int getBiomeFoliageColor() {
		return FOLIAGE_COLOR;
	}

	@Override
	public int getSkyColorByTemp(float temperature) {
		return SKY_COLOR;
	}

}
