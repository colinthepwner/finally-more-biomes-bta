package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.WorldGenCobwebNest;
import biomesoplenty.worldgen.tree.WorldGenBirchWillow;
import biomesoplenty.worldgen.tree.WorldGenDeadTree;
import biomesoplenty.worldgen.tree.WorldGenWillow;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenSilkglades extends BiomeGenBase {

	public static final int GRASS_COLOR = 13420973;

	public static final int FOLIAGE_COLOR = 14146486;

	public static final int MAP_COLOR = 13420973;

	public static final int SKY_COLOR = 13553096;

	public static final int WATER_COLOR = 16777079;

	public static final int FOG_COLOR = 10062450;

	public BiomeGenSilkglades(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 6;
		customBiomeDecorator.grassPerChunk = 2;
		customBiomeDecorator.wheatGrassPerChunk = 1;
		customBiomeDecorator.mushroomsPerChunk = 4;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.reedsPerChunk = -999;
		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.gravelPerChunk = 3;
		customBiomeDecorator.gravelPerChunk2 = 3;
		customBiomeDecorator.sproutsPerChunk = 2;
		customBiomeDecorator.poisonIvyPerChunk = 2;
		customBiomeDecorator.cobwebsPerChunk = 9;
		customBiomeDecorator.waterReedsPerChunk = 4;
		customBiomeDecorator.koruPerChunk = 1;
		customBiomeDecorator.generatePumpkins = true;

		waterColorMultiplier = WATER_COLOR;

		spawnableWaterCreatureList().clear();
		spawnableCreatureList().clear();
		spawnableCreatureList().add(BOPMobs.SPIDER, 7, 1, 2);

		this.withPlacementDefaults(0.5f, 0.9f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.3f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(5) == 0
			? new WorldGenBirchWillow()
			: (random.nextInt(7) == 0
				? new WorldGenDeadTree(false)
				: (random.nextInt(12) == 0 ? new WorldGenCobwebNest(0, 0) : new WorldGenWillow()));
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return new WorldFeatureTallGrass(Blocks.DEADBUSH.id());
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
		return 0.8F;
	}
}
