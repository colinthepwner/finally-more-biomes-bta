package biomesoplenty.biomes.nether;

import biomesoplenty.worldgen.WorldGenLavaSpring;
import com.betteroplenty.block.BOPWastes;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;
import net.minecraft.core.world.generate.chunk.PlacementMethod;
import net.minecraft.core.world.generate.feature.WorldFeatureFire;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.pos.TilePos;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenNetherLava extends BiomeGenBase {

	public static final int MAP_COLOR = 14247446;

	public BiomeGenNetherLava(String key) {
		super(key);

		customBiomeDecorator.grassPerChunk = 8;
		customBiomeDecorator.netherLavaPerChunk = 20;
		customBiomeDecorator.smolderingGrassPerChunk = 5;
		customBiomeDecorator.gravesPerChunk = 1;
		customBiomeDecorator.burningBlossomsPerChunk = 4;
		customBiomeDecorator.waspHivesPerChunk = 1;
		customBiomeDecorator.generateAsh = true;

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(BOPWastes.ASH)
			.withFillerBlock(BOPWastes.ASH)
			.build());

		NetherSpawns.apply(this, 4);

		this.withTags(net.minecraft.core.world.biome.BiomeTags.HAS_SURFACE_ASH);

		this.withPlacementDefaults(2.0f, 0.0f, 0.5f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return new WorldFeatureFire();
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("nether_lava_spring",

			() -> new WorldGenLavaSpring(Blocks.FLUID_LAVA_FLOWING.id(), 4),
			(world, chunk, random, minY, maxY, rangeY) -> new TilePos(
				chunk.pos.x() * 16 + random.nextInt(16),
				minY + rangeY / 2 + random.nextInt(Math.max(1, rangeY / 2)),
				chunk.pos.z() * 16 + random.nextInt(16)),
			new PlacementMethod.TriesPerChunk(3));
	}
}
