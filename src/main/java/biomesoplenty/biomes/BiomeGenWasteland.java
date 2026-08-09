package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenWasteland;
import biomesoplenty.worldgen.WorldGenWasteland2;
import biomesoplenty.worldgen.WorldGenWasteland3;
import biomesoplenty.worldgen.WorldGenWasteland4;
import biomesoplenty.worldgen.tree.WorldGenDeadTree3;
import com.betteroplenty.block.BOPWastes;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.world.biome.SurfaceProperties;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenWasteland extends BiomeGenBase {

	public static final int SKY_COLOR = 9477744;

	public static final int FOG_COLOR = 12106885;

	public static final int MAP_COLOR = 5919808;

	public static final int GRASS_COLOR = 10330232;

	public static final int FOLIAGE_COLOR = 10067541;

	public static final int WATER_COLOR = 15073024;

	public BiomeGenWasteland(String key) {
		super(key);

		spawnableCreatureList().clear();
		spawnableWaterCreatureList().clear();

		customBiomeDecorator.treesPerChunk = 0;
		customBiomeDecorator.grassPerChunk = 20;
		customBiomeDecorator.deadGrassPerChunk = 14;
		customBiomeDecorator.poisonWaterPerChunk = 10;
		customBiomeDecorator.waterLakesPerChunk = 2;

		waterColorMultiplier = WATER_COLOR;

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(BOPWastes.DRIED_DIRT)
			.withFillerBlock(BOPWastes.DRIED_DIRT)
			.build());

		this.withPlacementDefaults(2.0f, 0.05f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.4f);
		this.withDebugColor(MAP_COLOR);
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
		return 0.3F;
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(6) == 0
			? new WorldGenDeadTree3(false)
			: (random.nextInt(2) == 0 ? new WorldGenWasteland2() : new WorldGenWasteland());
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(2) == 0 ? new WorldGenWasteland4() : new WorldGenWasteland3();
	}
}
