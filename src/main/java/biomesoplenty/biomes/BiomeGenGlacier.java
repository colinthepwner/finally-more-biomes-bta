package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;
import org.jetbrains.annotations.NotNull;

public class BiomeGenGlacier extends BiomeGenBase {

	public static final int MAP_COLOR = 11582425;

	public BiomeGenGlacier(String key) {
		super(key);

		spawnableCreatureList().clear();

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(BOPBlocks.HARD_ICE)
			.withFillerBlock(BOPBlocks.HARD_ICE)
			.build());

		customBiomeDecorator.treesPerChunk = -999;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.grassPerChunk = -999;
		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;

		customBiomeDecorator.generateBaseWaterLakes = false;

		this.withPlacementDefaults(0.0f, 0.5f, 0.5f);
		this.setMinMaxHeight(0.4f, 0.8f);
		this.withDebugColor(MAP_COLOR);
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("tanzanite",
			() -> new WorldGenBOPOreSingle(BOPBlocks.TANZANITE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
