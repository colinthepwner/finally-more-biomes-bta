package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.WorldGenOutbackShrub;
import biomesoplenty.worldgen.tree.WorldGenOutbackTree;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenOutback extends BiomeGenBase {

	public static final int MAP_COLOR = 10843716;

	public BiomeGenOutback(String key) {
		super(key);

		spawnableCreatureList().clear();

		customBiomeDecorator.treesPerChunk = 3;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.outbackPerChunk = 10;
		customBiomeDecorator.deadBushPerChunk = 7;
		customBiomeDecorator.tinyCactiPerChunk = 2;
		customBiomeDecorator.cactiPerChunk = 4;
		customBiomeDecorator.bushesPerChunk = 5;
		customBiomeDecorator.generatePumpkins = false;

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(BOPBlocks.HARD_SAND)
			.withFillerBlock(BOPBlocks.HARD_SAND)
			.build());

		this.withPlacementDefaults(0.8f, 0.05f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.4f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(3) == 0
			? new WorldGenOutbackShrub(0, 0)
			: new WorldGenOutbackTree();
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("ruby",
			() -> new WorldGenBOPOreSingle(BOPBlocks.RUBY_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
