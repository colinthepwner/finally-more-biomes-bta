package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenBayou1;
import biomesoplenty.worldgen.tree.WorldGenBayou2;
import biomesoplenty.worldgen.tree.WorldGenBayou3;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.chunk.PlacementMethod;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenBayou extends BiomeGenBase {

	public static final int GRASS_COLOR = 9154411;

	public static final int FOLIAGE_COLOR = 11591816;

	public static final int MAP_COLOR = 9154411;

	public static final int SKY_COLOR = 11322556;

	public static final int WATER_COLOR = 16767282;

	public static final int FOG_COLOR = 9482133;

	public BiomeGenBayou(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 15;
		customBiomeDecorator.grassPerChunk = 15;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.reedsPerChunk = 25;
		customBiomeDecorator.mudPerChunk = 1;
		customBiomeDecorator.mudPerChunk2 = 1;
		customBiomeDecorator.toadstoolsPerChunk = 2;
		customBiomeDecorator.mushroomsPerChunk = 4;
		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.waterlilyPerChunk = 2;
		customBiomeDecorator.cattailsPerChunk = 1;
		customBiomeDecorator.highCattailsPerChunk = 1;
		customBiomeDecorator.waterLakesPerChunk = 5;
		customBiomeDecorator.algaePerChunk = 1;
		customBiomeDecorator.shrubsPerChunk = 2;
		customBiomeDecorator.wheatGrassPerChunk = 7;
		customBiomeDecorator.waterReedsPerChunk = 4;
		customBiomeDecorator.koruPerChunk = 1;
		customBiomeDecorator.generatePumpkins = false;

		waterColorMultiplier = WATER_COLOR;

		spawnableWaterCreatureList().clear();
		spawnableMonsterList().add(BOPMobs.SLIME, 10, 1, 3);

		this.withPlacementDefaults(0.5f, 0.9f, 0.5f);
		this.setMinMaxHeight(0.1f, 0.3f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(8) == 0
			? new WorldGenBayou3()
			: (random.nextInt(2) == 0 ? new WorldGenBayou1() : new WorldGenBayou2());
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("malachite",
			() -> new WorldGenBOPOreSingle(BOPBlocks.MALACHITE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));

		sink.add("moss",
			BOPDecorations.mossFeature(),
			BOPDecorations.mossSelector(),
			new PlacementMethod.TriesPerChunk(BOPDecorations.MOSS_TRIES));
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
		return 0.6F;
	}
}
