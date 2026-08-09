package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenTaiga3;
import biomesoplenty.worldgen.tree.WorldGenTaiga4;
import biomesoplenty.worldgen.tree.WorldGenTaiga9;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenConiferousForestSnow extends BiomeGenBase {

	public static final int MAP_COLOR = 16777215;

	public BiomeGenConiferousForestSnow(String key) {
		super(key);

		spawnableCreatureList().clear();

		customBiomeDecorator.treesPerChunk = 2;
		customBiomeDecorator.mushroomsPerChunk = 4;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.shrubsPerChunk = 4;
		customBiomeDecorator.wheatGrassPerChunk = 1;
		customBiomeDecorator.violetsPerChunk = 3;
		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.gravelPerChunk = 1;
		customBiomeDecorator.gravelPerChunk2 = 1;

		this.withPlacementDefaults(0.0f, 0.5f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.6f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(5) == 0
			? new WorldGenTaiga3(false)
			: (random.nextInt(3) == 0 ? new WorldGenTaiga4(false) : new WorldGenTaiga9(false));
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(2) == 0
			? new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id())
			: new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id());
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("tanzanite",
			() -> new WorldGenBOPOreSingle(BOPBlocks.TANZANITE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
