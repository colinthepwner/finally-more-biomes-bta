package biomesoplenty.biomes.nether;

import biomesoplenty.worldgen.WorldGenNetherMushroom;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BiomeGenNetherGarden extends BiomeGenBase {

	public static final int MAP_COLOR = 10331695;

	public BiomeGenNetherGarden(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 80;
		customBiomeDecorator.netherVinesPerChunk = 60;
		customBiomeDecorator.mushroomsPerChunk = 30;
		customBiomeDecorator.bigMushroomsPerChunk = 30;
		customBiomeDecorator.netherWartPerChunk = 8;
		customBiomeDecorator.netherGrassPerChunk = 10;
		customBiomeDecorator.glowshroomsPerChunk = 3;
		customBiomeDecorator.toadstoolsPerChunk = 5;
		customBiomeDecorator.gravesPerChunk = 1;
		customBiomeDecorator.burningBlossomsPerChunk = 8;
		customBiomeDecorator.waspHivesPerChunk = 1;

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(Blocks.NETHERRACK)
			.withFillerBlock(Blocks.NETHERRACK)
			.build());

		NetherSpawns.apply(this, 1);

		this.withPlacementDefaults(2.0f, 0.0f, 0.5f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@Nullable Random random) {
		return WorldGenNetherMushroom.nether();
	}
}
