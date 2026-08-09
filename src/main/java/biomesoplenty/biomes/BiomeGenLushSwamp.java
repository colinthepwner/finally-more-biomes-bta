package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenSwampTall;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenLushSwamp extends BiomeGenBase {

	public static final int MAP_COLOR = 5746228;

	@SuppressWarnings("this-escape")
	public BiomeGenLushSwamp(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 10;
		customBiomeDecorator.grassPerChunk = 4;
		customBiomeDecorator.wheatGrassPerChunk = 4;
		customBiomeDecorator.mushroomsPerChunk = 8;
		customBiomeDecorator.reedsPerChunk = 16;
		customBiomeDecorator.cattailsPerChunk = 10;
		customBiomeDecorator.highCattailsPerChunk = 5;
		customBiomeDecorator.waterlilyPerChunk = 3;
		customBiomeDecorator.hydrangeasPerChunk = 1;
		customBiomeDecorator.reedsBOPPerChunk = 5;
		customBiomeDecorator.poisonWaterPerChunk = 2;
		customBiomeDecorator.carrotsPerChunk = 1;
		customBiomeDecorator.shrubsPerChunk = 5;
		customBiomeDecorator.koruPerChunk = 1;
		customBiomeDecorator.waterReedsPerChunk = 6;
		customBiomeDecorator.cloverPatchesPerChunk = 10;

		spawnableMonsterList().add(BOPMobs.SLIME, 1, 1, 1);

		this.withPlacementDefaults(0.7f, 1.0f, 0.5f);
		this.setMinMaxHeight(0.2f, 0.3f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return new WorldGenSwampTall();
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("malachite",
			() -> new WorldGenBOPOreSingle(BOPBlocks.MALACHITE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}
}
