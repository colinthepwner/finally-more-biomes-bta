package biomesoplenty.biomes;

import biomesoplenty.entities.EntityJungleSpider;
import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeFancyRainforest;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenTropicalRainforest extends BiomeGenBase {

	public static final int GRASS_COLOR = 11002176;

	public static final int FOLIAGE_COLOR = 8970560;
	public static final int MAP_COLOR = 8970560;

	public static final int SKY_COLOR = 11128415;

	public static final int WATER_COLOR = 6160128;

	public static final int FOG_COLOR = 16228194;

	public BiomeGenTropicalRainforest(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 12;
		customBiomeDecorator.grassPerChunk = 9;
		customBiomeDecorator.highGrassPerChunk = 4;
		customBiomeDecorator.reedsPerChunk = 10;
		customBiomeDecorator.waterlilyPerChunk = 2;
		customBiomeDecorator.orangeFlowersPerChunk = 10;
		customBiomeDecorator.generatePumpkins = false;
		customBiomeDecorator.generateMelons = true;
		customBiomeDecorator.sproutsPerChunk = 2;
		customBiomeDecorator.generateQuicksand = true;
		customBiomeDecorator.poisonIvyPerChunk = 4;
		customBiomeDecorator.lilyflowersPerChunk = 2;
		customBiomeDecorator.shrubsPerChunk = 15;
		customBiomeDecorator.wheatGrassPerChunk = 5;

		waterColorMultiplier = WATER_COLOR;

		spawnableMonsterList().add(EntityJungleSpider.class, 12, 6, 6);

		this.withPlacementDefaults(1.2f, 0.9f, 0.5f);
		this.setMinMaxHeight(0.3f, 0.7f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(5) == 0
			? new WorldFeatureTree(Blocks.LEAVES_CACAO.id(), Blocks.LOG_OAK_MOSSY.id(),
				4 + random.nextInt(7))
			: new WorldFeatureTreeFancyRainforest(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK_MOSSY.id());
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("topaz",
			() -> new WorldGenBOPOreSingle(BOPBlocks.TOPAZ_ORE.id(), Blocks.STONE.id()),
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
		return 0.8F;
	}
}
