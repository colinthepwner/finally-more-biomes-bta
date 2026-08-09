package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenAcacia;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenSavannaPlateau extends BiomeGenBase {

	public static final int MAP_COLOR = 12106801;

	public BiomeGenSavannaPlateau(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 0;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.purpleFlowersPerChunk = 5;
		customBiomeDecorator.tinyFlowersPerChunk = 1;
		customBiomeDecorator.grassPerChunk = 10;
		customBiomeDecorator.wheatGrassPerChunk = 5;
		customBiomeDecorator.bushesPerChunk = 5;
		customBiomeDecorator.shrubsPerChunk = 1;
		customBiomeDecorator.generatePumpkins = false;

		this.withPlacementDefaults(1.5f, 0.1f, 0.5f);
		this.setMinMaxHeight(0.9f, 0.9f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(3) == 0
			? new WorldGenAcacia(false)
			: new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id());
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(4) == 0
			? new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id())
			: new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id());
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("ruby",
			() -> new WorldGenBOPOreSingle(BOPBlocks.RUBY_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
