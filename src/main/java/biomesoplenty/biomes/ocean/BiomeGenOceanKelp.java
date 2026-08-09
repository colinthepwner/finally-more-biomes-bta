package biomesoplenty.biomes.ocean;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class BiomeGenOceanKelp extends BiomeGenBase {

	public static final int MAP_COLOR = 27468;

	public BiomeGenOceanKelp(String key) {
		super(key);

		customBiomeDecorator.kelpPerChunk = 999;
		customBiomeDecorator.kelpThickPerChunk = 999;
		customBiomeDecorator.shortKelpPerChunk = 200;

		spawnableCreatureList().clear();

		this.withPlacementDefaults(0.5f, 0.9f, 0.5f);
		this.setMinMaxHeight(-0.4f, -0.1f);
		this.withDebugColor(MAP_COLOR);
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("sapphire",
			() -> new WorldGenBOPOreSingle(BOPBlocks.SAPPHIRE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
