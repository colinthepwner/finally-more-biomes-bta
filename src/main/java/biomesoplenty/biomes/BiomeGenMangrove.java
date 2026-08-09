package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenMangrove;
import biomesoplenty.worldgen.tree.WorldGenMangrove2;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenMangrove extends BiomeGenBase {

	public static final int MAP_COLOR = 7251289;

	public BiomeGenMangrove(String key) {
		super(key);

		spawnableCreatureList().clear();

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(Blocks.SAND)
			.withFillerBlock(Blocks.SAND)
			.build());

		customBiomeDecorator.treesPerChunk = 6;
		customBiomeDecorator.deadBushPerChunk = 1;
		customBiomeDecorator.deadGrassPerChunk = 9;
		customBiomeDecorator.reedsPerChunk = -999;
		customBiomeDecorator.cactiPerChunk = -999;
		customBiomeDecorator.waterReedsPerChunk = 2;
		customBiomeDecorator.desertSproutsPerChunk = 1;
		customBiomeDecorator.waterLakesPerChunk = 10;

		this.withPlacementDefaults(0.8f, 0.9f, 0.5f);
		this.setMinMaxHeight(0.1f, 0.3f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		if (random.nextInt(3) == 0) return new WorldGenMangrove2(0, 0);
		return new WorldGenMangrove(false);
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("sapphire",
			() -> new WorldGenBOPOreSingle(BOPBlocks.SAPPHIRE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
