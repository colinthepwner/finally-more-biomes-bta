package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenApple;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenOrchard extends BiomeGenBase {

	public static final int GRASS_COLOR = 14024557;
	public static final int FOLIAGE_COLOR = 14024557;
	public static final int MAP_COLOR = 14024557;

	public BiomeGenOrchard(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 2;
		customBiomeDecorator.flowersPerChunk = 20;
		customBiomeDecorator.wheatGrassPerChunk = 8;
		customBiomeDecorator.whiteFlowersPerChunk = 20;
		customBiomeDecorator.tinyFlowersPerChunk = 20;
		customBiomeDecorator.grassPerChunk = 15;
		customBiomeDecorator.portobellosPerChunk = 2;
		customBiomeDecorator.sunflowersPerChunk = 1;
		customBiomeDecorator.lilyflowersPerChunk = 2;
		customBiomeDecorator.berryBushesPerChunk = 3;
		customBiomeDecorator.carrotsPerChunk = 1;
		customBiomeDecorator.shrubsPerChunk = 10;
		customBiomeDecorator.waterReedsPerChunk = 4;
		customBiomeDecorator.cloverPatchesPerChunk = 15;

		spawnableCreatureList().add(BOPMobs.HORSE, 5, 2, 6);

		this.withPlacementDefaults(0.8f, 0.4f, 0.5f);
		this.setMinMaxHeight(0.1f, 0.2f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(3) == 0
			? new WorldGenApple(false)
			: new WorldFeatureTree(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id(), 4);
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("peridot",
			() -> new WorldGenBOPOreSingle(BOPBlocks.PERIDOT_ORE.id(), Blocks.STONE.id()),
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
