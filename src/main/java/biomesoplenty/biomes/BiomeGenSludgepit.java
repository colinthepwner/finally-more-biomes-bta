package biomesoplenty.biomes;

import biomesoplenty.entities.EntityGlob;
import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.WorldGenBog1;
import biomesoplenty.worldgen.WorldGenBog2;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenSludgepit extends BiomeGenBase {

	public static final int GRASS_COLOR = 7627817;

	public static final int FOLIAGE_COLOR = 9539892;

	public static final int SKY_COLOR = 7039816;

	public static final int FOG_COLOR = 10463856;
	public static final int MAP_COLOR = 7627817;

	public BiomeGenSludgepit(String key) {
		super(key);

		spawnableCreatureList().clear();
		spawnableWaterCreatureList().clear();

		customBiomeDecorator.treesPerChunk = 30;
		customBiomeDecorator.grassPerChunk = 30;
		customBiomeDecorator.wheatGrassPerChunk = 10;
		customBiomeDecorator.mushroomsPerChunk = 8;
		customBiomeDecorator.flowersPerChunk = -999;

		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.mudPerChunk = 5;
		customBiomeDecorator.mudPerChunk2 = 5;
		customBiomeDecorator.deadBushPerChunk = 5;
		customBiomeDecorator.algaePerChunk = 2;
		customBiomeDecorator.poisonWaterPerChunk = 5;
		customBiomeDecorator.waterReedsPerChunk = 6;
		customBiomeDecorator.koruPerChunk = 1;

		spawnableCreatureList().add(BOPMobs.SLIME, 1, 1, 1);

		spawnableCreatureList().add(EntityGlob.class, 1, 1, 1);

		waterColorMultiplier = 11506176;

		this.withPlacementDefaults(0.8f, 0.9f, 0.5f);
		this.setMinMaxHeight(0.1f, 0.3f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		if (random.nextInt(3) == 0) return new WorldGenBog2();
		return new WorldGenBog1();
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		if (random.nextInt(9) == 0) return new WorldFeatureTallGrass(Blocks.DEADBUSH.id());
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
