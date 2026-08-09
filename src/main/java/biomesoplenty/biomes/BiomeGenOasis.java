package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties.Builder;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreePalm;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenOasis extends BiomeGenBase {

	public static final int MAP_COLOR = 7712283;

	public BiomeGenOasis(String key) {
		super(key);

		this.withSurfaceProperties(new Builder()
			.withTopBlock(Blocks.SAND)
			.withFillerBlock(Blocks.SAND)
			.build());

		customBiomeDecorator.treesPerChunk = 3;
		customBiomeDecorator.grassPerChunk = 8;
		customBiomeDecorator.wheatGrassPerChunk = 4;
		customBiomeDecorator.reedsPerChunk = 100;
		customBiomeDecorator.oasesPerChunk = 15;
		customBiomeDecorator.oasesPerChunk2 = 15;
		customBiomeDecorator.cactiPerChunk = 7;
		customBiomeDecorator.desertSproutsPerChunk = 3;
		customBiomeDecorator.tinyCactiPerChunk = 2;
		customBiomeDecorator.generatePumpkins = false;
		customBiomeDecorator.generateMelons = true;
		customBiomeDecorator.generateQuicksand = true;
		customBiomeDecorator.waterLakesPerChunk = 10;
		customBiomeDecorator.aloePerChunk = 4;

		spawnableCreatureList().clear();

		this.withPlacementDefaults(0.9f, 0.7f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.4f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return new WorldFeatureTreePalm(Blocks.LOG_PALM, Blocks.LEAVES_PALM, false, false, true);
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("topaz",
			() -> new WorldGenBOPOreSingle(BOPBlocks.TOPAZ_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}

}
