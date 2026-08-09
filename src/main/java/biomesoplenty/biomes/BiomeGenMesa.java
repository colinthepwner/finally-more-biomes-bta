package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;
import org.jetbrains.annotations.NotNull;

public class BiomeGenMesa extends BiomeGenBase {

	public static final int SKY_COLOR = 15898486;

	public static final int FOG_COLOR = 14070383;

	public static final int MAP_COLOR = 13067319;

	public BiomeGenMesa(String key) {
		super(key);

		spawnableCreatureList().clear();

		customBiomeDecorator.treesPerChunk = -999;
		customBiomeDecorator.deadBushPerChunk = 2;
		customBiomeDecorator.desertGrassPerChunk = 10;
		customBiomeDecorator.tinyCactiPerChunk = 2;
		customBiomeDecorator.waterReedsPerChunk = 2;

		spawnableMonsterList().add(BOPMobs.SPIDER, 15, 2, 6);

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(BOPBlocks.RED_ROCK)
			.withFillerBlock(BOPBlocks.RED_ROCK)
			.build());

		this.withPlacementDefaults(2.0f, 0.05f, 0.5f);
		this.setMinMaxHeight(0.4f, 2.0f);
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
		sink.add("ruby",
			() -> new WorldGenBOPOreSingle(BOPBlocks.RUBY_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
