package biomesoplenty.biomes;

import biomesoplenty.worldgen.tree.WorldGenDeadTree3;
import biomesoplenty.worldgen.tree.WorldGenOminous1;
import biomesoplenty.worldgen.tree.WorldGenOminous2;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenOminousWoods extends BiomeGenBase {

	public static final int GRASS_COLOR = 4145489;
	public static final int FOLIAGE_COLOR = 4145489;

	public static final int SKY_COLOR = 5522002;

	public static final int FOG_COLOR = 3420989;
	public static final int MAP_COLOR = 4145489;

	public BiomeGenOminousWoods(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 8;
		customBiomeDecorator.grassPerChunk = 1;
		customBiomeDecorator.wheatGrassPerChunk = 1;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.deathbloomsPerChunk = 1;
		customBiomeDecorator.mushroomsPerChunk = 8;
		customBiomeDecorator.reedsPerChunk = -999;

		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;

		customBiomeDecorator.thornsPerChunk = 9;
		customBiomeDecorator.poisonIvyPerChunk = 3;

		customBiomeDecorator.poisonWaterPerChunk = 15;

		waterColorMultiplier = 1973030;

		spawnableMonsterList().clear();
		spawnableCreatureList().clear();
		spawnableWaterCreatureList().clear();
		spawnableMonsterList().add(BOPMobs.CAVE_SPIDER, 5, 1, 2);
		spawnableMonsterList().add(BOPMobs.ENDERMAN, 10, 1, 4);
		spawnableCaveCreatureList().add(BOPMobs.BAT, 10, 8, 8);

		this.withPlacementDefaults(0.8f, 0.9f, 0.5f);
		this.setMinMaxHeight(0.1f, 0.3f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		if (random.nextInt(2) == 0) return new WorldGenOminous1(false);
		if (random.nextInt(6) == 0) return new WorldGenDeadTree3(false);
		return new WorldGenOminous2();
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		if (random.nextInt(6) == 0) return new WorldFeatureTallGrass(Blocks.DEADBUSH.id());
		return new WorldFeatureTallGrass(Blocks.TALLGRASS.id());
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
