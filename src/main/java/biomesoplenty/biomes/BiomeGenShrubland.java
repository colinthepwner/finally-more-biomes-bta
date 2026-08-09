package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenShrubland extends BiomeGenBase {

	public static final int MAP_COLOR = 8168286;

	public BiomeGenShrubland(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 0;
		customBiomeDecorator.flowersPerChunk = 0;
		customBiomeDecorator.grassPerChunk = 5;
		customBiomeDecorator.wheatGrassPerChunk = 2;
		customBiomeDecorator.bushesPerChunk = 7;
		customBiomeDecorator.shrubsPerChunk = 5;
		customBiomeDecorator.generatePumpkins = false;

		spawnableCreatureList().add(BOPMobs.HORSE, 5, 2, 6);

		this.withPlacementDefaults(0.6f, 0.05f, 0.5f);
		this.setMinMaxHeight(0.2f, 0.2f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id());
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id());
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("peridot",
			() -> new WorldGenBOPOreSingle(BOPBlocks.PERIDOT_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
