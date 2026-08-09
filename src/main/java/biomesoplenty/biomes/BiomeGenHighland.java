package biomesoplenty.biomes;

import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import org.jetbrains.annotations.NotNull;

public class BiomeGenHighland extends BiomeGenBase {

	public static final int MAP_COLOR = 8170854;

	public BiomeGenHighland(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = -999;
		customBiomeDecorator.highGrassPerChunk = 25;
		customBiomeDecorator.grassPerChunk = 25;
		customBiomeDecorator.wheatGrassPerChunk = 10;
		customBiomeDecorator.potatoesPerChunk = -999;

		customBiomeDecorator.generateBoulders = false;

		customBiomeDecorator.carrotsPerChunk = 1;

		this.withPlacementDefaults(0.5f, 0.5f, 0.5f);
		this.setMinMaxHeight(0.9f, 1.9f);
		this.withDebugColor(MAP_COLOR);
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {

	}
}
