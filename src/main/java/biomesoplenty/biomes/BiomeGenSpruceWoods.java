package biomesoplenty.biomes;

import biomesoplenty.worldgen.tree.WorldGenTaiga5;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeTaigaTall;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenSpruceWoods extends BiomeGenBase {

	public static final int MAP_COLOR = 6396257;

	public BiomeGenSpruceWoods(String key) {
		super(key);

		spawnableCreatureList().add(BOPMobs.WOLF, 8, 4, 4);

		customBiomeDecorator.treesPerChunk = 10;
		customBiomeDecorator.grassPerChunk = 6;
		customBiomeDecorator.sproutsPerChunk = 3;
		customBiomeDecorator.mushroomsPerChunk = 4;
		customBiomeDecorator.poisonIvyPerChunk = 1;
		customBiomeDecorator.berryBushesPerChunk = 3;
		customBiomeDecorator.wheatGrassPerChunk = 10;
		customBiomeDecorator.carrotsPerChunk = 1;
		customBiomeDecorator.bluebellsPerChunk = 100;
		customBiomeDecorator.shrubsPerChunk = 5;
		customBiomeDecorator.waterReedsPerChunk = 2;

		this.withPlacementDefaults(0.6f, 0.7f, 0.5f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(3) == 0
			? new WorldGenTaiga5(false)
			: new WorldFeatureTreeTaigaTall(Blocks.LEAVES_PINE.id(), Blocks.LOG_PINE.id());
	}
}
