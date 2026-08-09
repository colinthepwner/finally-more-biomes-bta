package biomesoplenty.biomes.nether;

import biomesoplenty.entities.EntityPhantom;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;

public class BiomeGenNetherDesert extends BiomeGenBase {

	public static final int MAP_COLOR = 7691854;

	public BiomeGenNetherDesert(String key) {
		super(key);

		customBiomeDecorator.thornsPerChunk = 10;
		customBiomeDecorator.gravesPerChunk = 1;
		customBiomeDecorator.waspHivesPerChunk = 1;

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(Blocks.SOULSAND)
			.withFillerBlock(Blocks.SOULSAND)
			.build());

		NetherSpawns.apply(this, 1);

		spawnableMonsterList().add(EntityPhantom.class, BiomeGenNetherBone.PHANTOM_WEIGHT, 1, 1);

		this.withPlacementDefaults(2.0f, 0.0f, 0.5f);
		this.withDebugColor(MAP_COLOR);
	}
}
