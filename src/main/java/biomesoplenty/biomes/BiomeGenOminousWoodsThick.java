package biomesoplenty.biomes;

import biomesoplenty.worldgen.tree.WorldGenOminous1;
import biomesoplenty.worldgen.tree.WorldGenOminous3;
import biomesoplenty.worldgen.tree.WorldGenOminous4;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenOminousWoodsThick extends BiomeGenBase {

	public static final int GRASS_COLOR = 4145489;
	public static final int FOLIAGE_COLOR = 4145489;

	public static final int SKY_COLOR = 5522002;

	public static final int FOG_COLOR = 3420989;

	public static final int MAP_COLOR = 2698037;

	public BiomeGenOminousWoodsThick(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 14;
		customBiomeDecorator.grassPerChunk = 4;
		customBiomeDecorator.wheatGrassPerChunk = 2;
		customBiomeDecorator.highGrassPerChunk = 4;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.deathbloomsPerChunk = 2;
		customBiomeDecorator.mushroomsPerChunk = 8;
		customBiomeDecorator.reedsPerChunk = -999;

		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;

		customBiomeDecorator.thornsPerChunk = 14;
		customBiomeDecorator.poisonIvyPerChunk = 6;
		customBiomeDecorator.poisonWaterPerChunk = 5;

		waterColorMultiplier = 1973030;

		spawnableMonsterList().clear();
		spawnableCreatureList().clear();
		spawnableWaterCreatureList().clear();
		spawnableMonsterList().add(BOPMobs.CAVE_SPIDER, 5, 1, 2);
		spawnableMonsterList().add(BOPMobs.SPIDER, 7, 1, 2);
		spawnableMonsterList().add(BOPMobs.ENDERMAN, 10, 1, 4);
		spawnableCaveCreatureList().add(BOPMobs.BAT, 10, 8, 8);

		this.withPlacementDefaults(0.8f, 0.9f, 0.5f);
		this.setMinMaxHeight(0.4f, 0.8f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		if (random.nextInt(5) == 0) return new WorldGenOminous3(false);
		if (random.nextInt(3) == 0) return new WorldGenOminous4(false);
		return new WorldGenOminous1(false);
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		if (random.nextInt(6) == 0) return new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id());
		return new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id());
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
		return 0.1F;
	}
}
