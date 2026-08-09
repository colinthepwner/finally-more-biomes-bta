package biomesoplenty.biomes.nether;

import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;

public class BiomeGenNetherBase extends BiomeGenBase {

	public static final int MAP_COLOR = 16711680;

	public BiomeGenNetherBase(String key) {
		super(key);

		customBiomeDecorator.gravesPerChunk = 1;
		customBiomeDecorator.burningBlossomsPerChunk = 1;
		customBiomeDecorator.waspHivesPerChunk = 1;

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(Blocks.NETHERRACK)
			.withFillerBlock(Blocks.NETHERRACK)
			.build());

		NetherSpawns.apply(this, 1);

		this.withPlacementDefaults(2.0f, 0.0f, 0.5f);
		this.withDebugColor(MAP_COLOR);
	}
}
