package biomesoplenty.biomes.beach;

import biomesoplenty.worldgen.WorldGenChaparral2;
import biomesoplenty.worldgen.WorldGenChaparral3;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties.Builder;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenBeachOvergrown extends BiomeGenBase {

	public static final int MAP_COLOR = 10200432;

	public BiomeGenBeachOvergrown(String key) {
		super(key);

		spawnableCreatureList().clear();

		customBiomeDecorator.treesPerChunk = 16;
		customBiomeDecorator.deadBushPerChunk = 3;
		customBiomeDecorator.duneGrassPerChunk = 25;
		customBiomeDecorator.cactiPerChunk = 5;

		customBiomeDecorator.outbackPerChunk = 7;
		customBiomeDecorator.waterReedsPerChunk = 4;

		this.withSurfaceProperties(new Builder()
			.withTopBlock(Blocks.SAND)
			.withFillerBlock(Blocks.SAND)
			.build());

		this.withPlacementDefaults(0.8f, 0.5f, 0.5f);
		this.setMinMaxHeight(0.0f, 0.1f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(3) == 0 ? new WorldGenChaparral2() : new WorldGenChaparral3();
	}
}
