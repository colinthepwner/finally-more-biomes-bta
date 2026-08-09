package biomesoplenty.biomes;

import biomesoplenty.entities.EntityGlob;
import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenDeadTree;
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

public class BiomeGenDeadSwamp extends BiomeGenBase {

	public static final int GRASS_COLOR = 6713420;
	public static final int FOLIAGE_COLOR = 6713420;

	public static final int SKY_COLOR = 6451816;

	public static final int FOG_COLOR = 9219993;
	public static final int MAP_COLOR = 6713420;

	public BiomeGenDeadSwamp(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 2;
		customBiomeDecorator.grassPerChunk = 25;
		customBiomeDecorator.highGrassPerChunk = 1;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.reedsPerChunk = -999;
		customBiomeDecorator.mudPerChunk = 3;
		customBiomeDecorator.mudPerChunk2 = 3;

		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;

		customBiomeDecorator.reedsBOPPerChunk = 2;
		customBiomeDecorator.wheatGrassPerChunk = 10;
		customBiomeDecorator.waterReedsPerChunk = 4;
		customBiomeDecorator.koruPerChunk = 1;

		waterColorMultiplier = 10661201;

		spawnableCreatureList().clear();
		spawnableWaterCreatureList().clear();

		spawnableCreatureList().add(EntityGlob.class, 1, 1, 1);

		this.withPlacementDefaults(0.8f, 0.9f, 0.5f);
		this.setMinMaxHeight(0.1f, 0.2f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return new WorldGenDeadTree(false);
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		if (random.nextInt(9) == 0) return new WorldFeatureTallGrass(Blocks.TALLGRASS.id());
		return new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id());
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("malachite",
			() -> new WorldGenBOPOreSingle(BOPBlocks.MALACHITE_ORE.id(), Blocks.STONE.id()),
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

	@Override
	public int getSkyColorByTemp(float temperature) {
		return SKY_COLOR;
	}

	@Override
	public int getBiomeFogColor() {
		return FOG_COLOR;
	}

	@Override
	public float getFogCloseness() {
		return 0.6F;
	}
}
