package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeTaigaTall;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenMeadow extends BiomeGenBase {

	public static final int GRASS_COLOR = 6533741;
	public static final int FOLIAGE_COLOR = 6533741;
	public static final int MAP_COLOR = 6533741;

	public BiomeGenMeadow(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 2;
		customBiomeDecorator.grassPerChunk = 10;
		customBiomeDecorator.wheatGrassPerChunk = 5;
		customBiomeDecorator.tinyFlowersPerChunk = 14;
		customBiomeDecorator.mushroomsPerChunk = 2;
		customBiomeDecorator.flowersPerChunk = 10;
		customBiomeDecorator.carrotsPerChunk = 1;
		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.hydrangeasPerChunk = 3;
		customBiomeDecorator.sunflowersPerChunk = 1;
		customBiomeDecorator.shrubsPerChunk = 5;
		customBiomeDecorator.cloverPatchesPerChunk = 15;
		customBiomeDecorator.generatePumpkins = false;

		spawnableCreatureList().add(BOPMobs.HORSE, 5, 2, 6);

		this.withPlacementDefaults(0.7f, 0.7f, 0.5f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(5) == 0
			? new WorldFeatureTreeTaigaTall(Blocks.LEAVES_PINE.id(), Blocks.LOG_PINE.id())
			: new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_PINE.id());
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

	@Override
	public int getBiomeFoliageColor() {
		return FOLIAGE_COLOR;
	}
}
