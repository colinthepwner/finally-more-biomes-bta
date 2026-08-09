package biomesoplenty.biomes.beach;

import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties.Builder;

public class BiomeGenBeachGravel extends BiomeGenBase {

	public static final int MAP_COLOR = 9210248;

	public BiomeGenBeachGravel(String key) {
		super(key);

		spawnableCreatureList().clear();

		customBiomeDecorator.treesPerChunk = -999;
		customBiomeDecorator.deadBushPerChunk = -999;
		customBiomeDecorator.reedsPerChunk = -999;
		customBiomeDecorator.cactiPerChunk = -999;

		this.withSurfaceProperties(new Builder()
			.withTopBlock(Blocks.GRAVEL)
			.withFillerBlock(Blocks.GRAVEL)
			.build());

		this.withPlacementDefaults(0.2f, 0.8f, 0.5f);
		this.setMinMaxHeight(0.0f, 0.1f);
		this.withDebugColor(MAP_COLOR);
	}
}
