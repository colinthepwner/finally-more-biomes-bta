package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.WorldGenJacarandaShrub;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenHeathland extends BiomeGenBase {

	public static final int GRASS_COLOR = 13550967;
	public static final int FOLIAGE_COLOR = 11454081;
	public static final int MAP_COLOR = 13550967;

	public BiomeGenHeathland(String key) {
		super(key);

		spawnableCreatureList().clear();

		customBiomeDecorator.treesPerChunk = 3;
		customBiomeDecorator.grassPerChunk = 10;
		customBiomeDecorator.wheatGrassPerChunk = 5;
		customBiomeDecorator.purpleFlowersPerChunk = 30;
		customBiomeDecorator.deadBushPerChunk = 2;
		customBiomeDecorator.berryBushesPerChunk = 1;
		customBiomeDecorator.shrubsPerChunk = 5;
		customBiomeDecorator.generatePumpkins = false;

		spawnableCreatureList().add(BOPMobs.HORSE, 5, 2, 6);

		this.withPlacementDefaults(0.8f, 0.1f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.4f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		if (random.nextInt(3) == 0) {
			return new WorldGenJacarandaShrub(0, 0);
		}
		return random.nextInt(2) == 0
			? new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id())
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
