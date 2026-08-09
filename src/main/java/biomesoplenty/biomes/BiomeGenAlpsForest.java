package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenTaiga6;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;
import net.minecraft.core.world.biome.SurfaceProperties.Builder;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenAlpsForest extends BiomeGenBase {

	public static final int MAP_COLOR = 8034682;

	public BiomeGenAlpsForest(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 8;
		customBiomeDecorator.flowersPerChunk = 2;
		customBiomeDecorator.grassPerChunk = 3;
		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;

		customBiomeDecorator.violetsPerChunk = 2;

		this.withSurfaceProperties(new Builder()
			.withTopBlock(Blocks.STONE)
			.withFillerBlock(Blocks.STONE)
			.build());

		this.withPlacementDefaults(0.0f, 0.5f, 0.5f);
		this.setMinMaxHeight(1.0f, 2.0f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return new WorldGenTaiga6(false);
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("tanzanite",
			() -> new WorldGenBOPOreSingle(BOPBlocks.TANZANITE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
