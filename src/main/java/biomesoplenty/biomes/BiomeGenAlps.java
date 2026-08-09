package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;
import org.jetbrains.annotations.NotNull;

public class BiomeGenAlps extends BiomeGenBase {

	public static final int MAP_COLOR = 13421772;

	public BiomeGenAlps(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = -999;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.grassPerChunk = -999;
		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(Blocks.STONE)
			.withFillerBlock(Blocks.STONE)
			.build());

		this.withPlacementDefaults(0.0f, 0.5f, 0.5f);
		this.setMinMaxHeight(2.0f, 3.0f);
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
