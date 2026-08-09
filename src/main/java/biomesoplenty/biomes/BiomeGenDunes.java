package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;
import org.jetbrains.annotations.NotNull;

public class BiomeGenDunes extends BiomeGenBase {

	public static final int MAP_COLOR = 15064744;

	public BiomeGenDunes(String key) {
		super(key);

		spawnableCreatureList().clear();

		customBiomeDecorator.treesPerChunk = -999;
		customBiomeDecorator.deadBushPerChunk = 5;
		customBiomeDecorator.duneGrassPerChunk = 75;
		customBiomeDecorator.desertSproutsPerChunk = 25;
		customBiomeDecorator.aloePerChunk = 5;
		customBiomeDecorator.reedsPerChunk = -999;
		customBiomeDecorator.waterReedsPerChunk = 4;
		customBiomeDecorator.generateLakes = false;

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(Blocks.SAND)
			.withFillerBlock(Blocks.SAND)
			.build());

		this.withPlacementDefaults(2.0f, 0.05f, 0.5f);
		this.setMinMaxHeight(0.5f, 1.3f);
		this.withDebugColor(MAP_COLOR);
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("ruby",
			() -> new WorldGenBOPOreSingle(BOPBlocks.RUBY_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
