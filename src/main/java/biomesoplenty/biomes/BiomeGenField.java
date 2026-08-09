package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class BiomeGenField extends BiomeGenBase {

	public static final int MAP_COLOR = 4044093;

	public BiomeGenField(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = -999;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.rosesPerChunk = 75;
		customBiomeDecorator.grassPerChunk = 8;
		customBiomeDecorator.bushesPerChunk = 8;
		customBiomeDecorator.berryBushesPerChunk = 5;
		customBiomeDecorator.wheatGrassPerChunk = 4;
		customBiomeDecorator.waterReedsPerChunk = 4;
		customBiomeDecorator.generatePumpkins = true;

		spawnableCreatureList().add(BOPMobs.HORSE, 5, 2, 6);

		this.withPlacementDefaults(0.6f, 0.7f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.3f);
		this.withDebugColor(MAP_COLOR);
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("peridot",
			() -> new WorldGenBOPOreSingle(BOPBlocks.PERIDOT_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
