package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPTerracotta;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;
import org.jetbrains.annotations.NotNull;

public class BiomeGenBadlands extends BiomeGenBase {

	public static final int SKY_COLOR = 9814727;

	public static final int MAP_COLOR = 9788226;

	public BiomeGenBadlands(String key) {
		super(key);

		spawnableCreatureList().clear();

		customBiomeDecorator.treesPerChunk = -999;
		customBiomeDecorator.deadBushPerChunk = 4;
		customBiomeDecorator.reedsPerChunk = -999;
		customBiomeDecorator.cactiPerChunk = 2;
		customBiomeDecorator.clayPerChunk = 3;
		customBiomeDecorator.generateClayInClay = true;
		customBiomeDecorator.generateClayInClay2 = true;
		customBiomeDecorator.generateClayInStone = true;
		customBiomeDecorator.generateClayInStone2 = true;

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(BOPTerracotta.HARDENED_CLAY)
			.withFillerBlock(BOPTerracotta.HARDENED_CLAY)
			.build());

		this.withPlacementDefaults(2.0f, 0.05f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.9f);
		this.withDebugColor(MAP_COLOR);
	}

	@Override
	public int getSkyColorByTemp(float temperature) {
		return SKY_COLOR;
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("ruby",
			() -> new WorldGenBOPOreSingle(BOPBlocks.RUBY_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
