package biomesoplenty.biomes;

import net.minecraft.core.entity.monster.MobSkeleton;
import biomesoplenty.worldgen.tree.WorldGenJacaranda;
import biomesoplenty.worldgen.tree.WorldGenMystic2;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeFancy;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShapeSwamp;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenMysticGroveThin extends BiomeGenBase {

	public static final int GRASS_COLOR = 6934491;
	public static final int FOLIAGE_COLOR = 7397529;
	public static final int SKY_COLOR = 8972496;
	public static final int FOG_COLOR = 16755401;

	public static final int MAP_COLOR = 5481645;

	public BiomeGenMysticGroveThin(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 3;
		customBiomeDecorator.grassPerChunk = 7;
		customBiomeDecorator.wheatGrassPerChunk = 3;
		customBiomeDecorator.flowersPerChunk = 8;
		customBiomeDecorator.pinkFlowersPerChunk = 6;
		customBiomeDecorator.glowFlowersPerChunk = 15;
		customBiomeDecorator.rosesPerChunk = 8;
		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.sproutsPerChunk = 1;
		customBiomeDecorator.hydrangeasPerChunk = 3;
		customBiomeDecorator.blueMilksPerChunk = 1;
		customBiomeDecorator.lilyflowersPerChunk = 3;
		customBiomeDecorator.poisonWaterPerChunk = 1;
		customBiomeDecorator.cloverPatchesPerChunk = 20;
		customBiomeDecorator.shrubsPerChunk = 2;

		waterColorMultiplier = 16715898;

		spawnableMonsterList().clear();
		spawnableCreatureList().clear();
		spawnableWaterCreatureList().clear();

		spawnableMonsterList().add(MobSkeleton.class, 10, 4, 4);

		this.withPlacementDefaults(0.9f, 1.0f, 0.5f);
		this.setMinMaxHeight(0.1f, 0.3f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(4) == 0
			? new WorldGenMystic2(false)
			: (random.nextInt(2) == 0
				? new WorldGenJacaranda(false)
				: (random.nextInt(6) == 0
					? new WorldFeatureTreeFancy(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id())
					: (random.nextInt(5) == 0
						? new WorldFeatureTreeShapeSwamp(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id(), 9)
						: new WorldFeatureTree(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id(), 4))));
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(5) == 0
			? new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id())
			: (random.nextInt(3) == 0
				? new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id())
				: new WorldFeatureTallGrass(Blocks.TALLGRASS.id()));
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
}
