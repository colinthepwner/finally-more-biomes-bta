package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenPineTree;
import biomesoplenty.worldgen.tree.WorldGenTaiga6;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenHotSprings extends BiomeGenBase {

	public static final int MAP_COLOR = 9371647;

	public BiomeGenHotSprings(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 3;
		customBiomeDecorator.grassPerChunk = -999;
		customBiomeDecorator.outbackPerChunk = 5;
		customBiomeDecorator.hotSpringsPerChunk = 8;
		customBiomeDecorator.lavaLakesPerChunk = 5;

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(Blocks.STONE)
			.withFillerBlock(Blocks.STONE)
			.build());

		this.withPlacementDefaults(0.5f, 0.7f, 0.5f);
		this.setMinMaxHeight(0.2f, 0.5f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(3) == 0
			? new WorldGenPineTree()
			: new WorldGenTaiga6(false);
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("sapphire",
			() -> new WorldGenBOPOreSingle(BOPBlocks.SAPPHIRE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
