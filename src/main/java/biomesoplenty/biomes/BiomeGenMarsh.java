package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class BiomeGenMarsh extends BiomeGenBase {

	public static final int MAP_COLOR = 6725742;

	public static final int FOG_COLOR = 12638463;

	public BiomeGenMarsh(String key) {
		super(key);

		spawnableCreatureList().clear();
		spawnableWaterCreatureList().clear();

		customBiomeDecorator.treesPerChunk = -999;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.grassPerChunk = 50;
		customBiomeDecorator.wheatGrassPerChunk = 50;
		customBiomeDecorator.highGrassPerChunk = 50;
		customBiomeDecorator.reedsPerChunk = -999;
		customBiomeDecorator.waterLakesPerChunk = 100;
		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.mudPerChunk = 1;
		customBiomeDecorator.mudPerChunk2 = 1;
		customBiomeDecorator.koruPerChunk = 1;
		customBiomeDecorator.waterReedsPerChunk = 10;
		customBiomeDecorator.generatePumpkins = false;

		spawnableMonsterList().add(BOPMobs.SLIME, 10, 1, 3);

		this.withPlacementDefaults(0.5f, 0.9f, 0.5f);
		this.setMinMaxHeight(0.2f, 0.2f);
		this.withDebugColor(MAP_COLOR);
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("sapphire",
			() -> new WorldGenBOPOreSingle(BOPBlocks.SAPPHIRE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}

	@Override
	public int getBiomeFogColor() {
		return FOG_COLOR;
	}

	@Override
	public float getFogCloseness() {
		return 0.6F;
	}
}
