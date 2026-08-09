package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenJacaranda;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.world.BOPDecorations;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeFancy;
import net.minecraft.core.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenLavenderFields extends BiomeGenBase {

	public static final int GRASS_COLOR = 10601325;
	public static final int FOLIAGE_COLOR = 10601325;

	public static final int MAP_COLOR = 11035852;

	public BiomeGenLavenderFields(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 1;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.grassPerChunk = 20;
		customBiomeDecorator.wheatGrassPerChunk = 5;
		customBiomeDecorator.lavenderPerChunk = 999;
		customBiomeDecorator.generatePumpkins = true;

		this.withPlacementDefaults(0.6f, 0.7f, 0.5f);

		this.setMinMaxHeight(0.3f, 0.3f);

		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(3) == 0
			? new WorldFeatureTreeFancy(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id())
			: new WorldGenJacaranda(false);
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
