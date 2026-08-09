package biomesoplenty.biomes.nether;

import com.betteroplenty.block.BOPNether;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.world.biome.SurfaceProperties;

public class BiomeGenNetherBlood extends BiomeGenBase {

	public static final int MAP_COLOR = 11091006;

	public BiomeGenNetherBlood(String key) {
		super(key);

		customBiomeDecorator.gravesPerChunk = 1;
		customBiomeDecorator.waspHivesPerChunk = 1;

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(BOPNether.FLESH)
			.withFillerBlock(BOPNether.FLESH)
			.build());

		NetherSpawns.apply(this, 1);

		this.withPlacementDefaults(2.0f, 0.0f, 0.5f);
		this.withDebugColor(MAP_COLOR);
	}
}
