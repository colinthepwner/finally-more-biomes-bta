package biomesoplenty.biomes;

import biomesoplenty.entities.EntityGlob;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenMoor extends BiomeGenBase {

	public static final int GRASS_COLOR = 6394725;
	public static final int FOLIAGE_COLOR = 6394725;
	public static final int MAP_COLOR = 6394725;

	public static final int SKY_COLOR = 10536403;

	public static final int WATER_COLOR = 5800566;

	public BiomeGenMoor(String key) {
		super(key);

		spawnableCreatureList().clear();
		spawnableWaterCreatureList().clear();

		spawnableCreatureList().add(EntityGlob.class, 1, 1, 1);

		customBiomeDecorator.treesPerChunk = -999;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.grassPerChunk = 15;
		customBiomeDecorator.mushroomsPerChunk = 2;
		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.mudPerChunk = 1;
		customBiomeDecorator.mudPerChunk2 = 1;
		customBiomeDecorator.waterLakesPerChunk = 10;
		customBiomeDecorator.blueFlowersPerChunk = 6;
		customBiomeDecorator.wheatGrassPerChunk = 7;
		customBiomeDecorator.koruPerChunk = 1;
		customBiomeDecorator.generatePumpkins = false;

		waterColorMultiplier = WATER_COLOR;

		this.withPlacementDefaults(0.5f, 1.0f, 0.5f);
		this.setMinMaxHeight(0.7f, 0.8f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(4) == 0
			? new WorldFeatureTallGrass(Blocks.TALLGRASS.id())
			: (random.nextInt(3) == 0
				? new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id())
				: new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id()));
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
}
