package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.WorldGenCanyonShrub;
import biomesoplenty.worldgen.tree.WorldGenPineTree;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenCanyon extends BiomeGenBase {

	public static final int GRASS_COLOR = 11123300;
	public static final int FOLIAGE_COLOR = 11123300;

	public static final int MAP_COLOR = 11836528;

	public BiomeGenCanyon(String key) {
		super(key);

		spawnableCreatureList().clear();

		customBiomeDecorator.treesPerChunk = 7;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.canyonGrassPerChunk = 5;
		customBiomeDecorator.aloePerChunk = 2;
		customBiomeDecorator.waterReedsPerChunk = 2;
		customBiomeDecorator.generatePumpkins = false;
		customBiomeDecorator.generateCanyon = true;

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(BOPBlocks.HARD_DIRT)
			.withFillerBlock(BOPBlocks.HARD_DIRT)
			.build());

		this.withPlacementDefaults(0.8f, 0.4f, 0.5f);
		this.setMinMaxHeight(1.5f, 2.0f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(9) == 0
			? new WorldGenPineTree()
			: new WorldGenCanyonShrub(0, 0);
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("ruby",
			() -> new WorldGenBOPOreSingle(BOPBlocks.RUBY_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}

	@Override
	public int getBiomeGrassColor() {
		return GRASS_COLOR;
	}

	@Override
	public int getBiomeFoliageColor() {
		return FOLIAGE_COLOR;
	}
}
