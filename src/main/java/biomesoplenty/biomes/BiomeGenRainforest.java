package biomesoplenty.biomes;

import biomesoplenty.entities.EntityJungleSpider;
import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenRainforestTree1;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeFancy;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenRainforest extends BiomeGenBase {

	public static final int GRASS_COLOR = 1759340;

	public static final int FOLIAGE_COLOR = 1368687;
	public static final int MAP_COLOR = 1368687;

	public BiomeGenRainforest(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 14;
		customBiomeDecorator.grassPerChunk = 25;
		customBiomeDecorator.pinkFlowersPerChunk = 2;
		customBiomeDecorator.flowersPerChunk = 25;
		customBiomeDecorator.rosesPerChunk = 10;
		customBiomeDecorator.mushroomsPerChunk = 25;
		customBiomeDecorator.orangeFlowersPerChunk = 6;
		customBiomeDecorator.wheatGrassPerChunk = 10;
		customBiomeDecorator.shrubsPerChunk = 5;
		customBiomeDecorator.generatePumpkins = false;
		customBiomeDecorator.cloverPatchesPerChunk = 20;

		spawnableMonsterList().add(EntityJungleSpider.class, 12, 6, 6);

		this.withPlacementDefaults(2.0f, 2.0f, 0.5f);
		this.setMinMaxHeight(0.2f, 1.8f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(15) == 0
			? new WorldFeatureTree(Blocks.LEAVES_BIRCH.id(), Blocks.LOG_BIRCH.id(), 5)
			: (random.nextInt(5) == 0
				? new WorldFeatureTreeFancy(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id())
				: new WorldGenRainforestTree1(false));
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(4) == 0
			? new WorldFeatureTallGrass(Blocks.TALLGRASS_FERN.id())
			: new WorldFeatureTallGrass(Blocks.TALLGRASS.id());
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("topaz",
			() -> new WorldGenBOPOreSingle(BOPBlocks.TOPAZ_ORE.id(), Blocks.STONE.id()),
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
