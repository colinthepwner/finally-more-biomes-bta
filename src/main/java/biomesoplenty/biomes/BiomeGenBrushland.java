package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBrush1;
import biomesoplenty.worldgen.WorldGenBrush2;
import biomesoplenty.worldgen.WorldGenChaparral2;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenBrushland extends BiomeGenBase {

	public static final int GRASS_COLOR = 13222271;

	public static final int FOLIAGE_COLOR = 11716223;

	public static final int MAP_COLOR = 13222271;

	public BiomeGenBrushland(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 10;
		customBiomeDecorator.grassPerChunk = 6;
		customBiomeDecorator.thornsPerChunk = 4;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.wheatGrassPerChunk = 3;
		customBiomeDecorator.shrubsPerChunk = 30;
		customBiomeDecorator.waterReedsPerChunk = 2;
		customBiomeDecorator.generateQuicksand = true;

		this.withPlacementDefaults(2.0f, 0.05f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.3f);
		this.withDebugColor(MAP_COLOR);
	}

	@Override
	public int getBiomeGrassColor() {
		return GRASS_COLOR;
	}

	@Override
	public int getBiomeFoliageColor() {
		return FOLIAGE_COLOR;
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(2) == 0
			? new WorldGenBrush2()
			: (random.nextInt(5) == 0 ? new WorldGenBrush1() : new WorldGenChaparral2());
	}
}
