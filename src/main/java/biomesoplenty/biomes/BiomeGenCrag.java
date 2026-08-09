package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;
import org.jetbrains.annotations.NotNull;

public class BiomeGenCrag extends BiomeGenBase {

	public static final int WATER_COLOR = 944693;

	public static final int SKY_COLOR = 4944498;

	public static final int FOG_COLOR = 10514245;

	public static final int MAP_COLOR = 5209457;

	public BiomeGenCrag(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = -999;

		spawnableCreatureList().clear();
		spawnableWaterCreatureList().clear();

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(BOPBlocks.CRAG_ROCK)
			.withFillerBlock(BOPBlocks.CRAG_ROCK)
			.build());

		waterColorMultiplier = WATER_COLOR;

		this.withPlacementDefaults(0.4f, 0.2f, 0.5f);
		this.setMinMaxHeight(2.0f, 3.0f);
		this.withDebugColor(MAP_COLOR);
	}

	@Override
	public int getSkyColorByTemp(float temperature) {
		return SKY_COLOR;
	}

	@Override
	public int getBiomeFogColor() {
		return FOG_COLOR;
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("sapphire",
			() -> new WorldGenBOPOreSingle(BOPBlocks.SAPPHIRE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
