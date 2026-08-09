package biomesoplenty.biomes.nether;

import biomesoplenty.entities.EntityPhantom;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;

public class BiomeGenNetherBone extends BiomeGenBase {

	public static final int MAP_COLOR = 15657658;

	public static final int PHANTOM_WEIGHT = 8;

	public BiomeGenNetherBone(String key) {
		super(key);

		customBiomeDecorator.boneSpinesPerChunk = 9;
		customBiomeDecorator.boneSpines2PerChunk = 12;
		customBiomeDecorator.gravesPerChunk = 1;
		customBiomeDecorator.waspHivesPerChunk = 1;

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(Blocks.NETHERRACK)
			.withFillerBlock(Blocks.NETHERRACK)
			.build());

		NetherSpawns.apply(this, 1);

		spawnableMonsterList().add(EntityPhantom.class, PHANTOM_WEIGHT, 1, 1);

		this.withPlacementDefaults(2.0f, 0.0f, 0.5f);
		this.withDebugColor(MAP_COLOR);
	}
}
