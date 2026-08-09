package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenIceTree;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenIcyHills extends BiomeGenBase {

	public static final int SKY_COLOR = 16777215;

	public static final int MAP_COLOR = 9355263;

	public BiomeGenIcyHills(String key) {
		super(key);

		spawnableCreatureList().clear();

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(Blocks.BLOCK_SNOW)
			.withFillerBlock(Blocks.BLOCK_SNOW)
			.build());

		customBiomeDecorator.treesPerChunk = 2;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.grassPerChunk = -999;

		spawnableCreatureList().add(BOPMobs.SNOWMAN, 30, 2, 4);

		this.withPlacementDefaults(0.0f, 0.5f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.8f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return new WorldGenIceTree(false);
	}

	@Override
	public int getSkyColorByTemp(float temperature) {
		return SKY_COLOR;
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("tanzanite",
			() -> new WorldGenBOPOreSingle(BOPBlocks.TANZANITE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
