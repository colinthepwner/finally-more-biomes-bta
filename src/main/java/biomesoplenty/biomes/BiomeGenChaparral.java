package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.WorldGenChaparral2;
import biomesoplenty.worldgen.WorldGenChaparral3;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenChaparral extends BiomeGenBase {

	public static final int GRASS_COLOR = 12638301;
	public static final int MAP_COLOR = 12638301;

	public BiomeGenChaparral(String key) {
		super(key);

		spawnableCreatureList().clear();

		customBiomeDecorator.treesPerChunk = 8;
		customBiomeDecorator.grassPerChunk = 20;
		customBiomeDecorator.bushesPerChunk = 10;
		customBiomeDecorator.berryBushesPerChunk = 2;
		customBiomeDecorator.generateStoneInGrass = true;
		customBiomeDecorator.generatePumpkins = false;
		customBiomeDecorator.carrotsPerChunk = 1;
		customBiomeDecorator.wheatGrassPerChunk = 10;
		customBiomeDecorator.shrubsPerChunk = 10;
		customBiomeDecorator.waterReedsPerChunk = 2;

		spawnableCreatureList().add(BOPMobs.HORSE, 5, 2, 6);

		this.withPlacementDefaults(0.8f, 0.4f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.6f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		if (random.nextInt(2) == 0) {
			return new WorldGenChaparral2();
		}
		return random.nextInt(5) == 0
			? new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id())
			: new WorldGenChaparral3();
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(2) == 0
			? new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id())
			: new WorldFeatureTallGrass(Blocks.TALLGRASS.id());
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("peridot",
			() -> new WorldGenBOPOreSingle(BOPBlocks.PERIDOT_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}

	@Override
	public int getBiomeGrassColor() {
		return GRASS_COLOR;
	}
}
