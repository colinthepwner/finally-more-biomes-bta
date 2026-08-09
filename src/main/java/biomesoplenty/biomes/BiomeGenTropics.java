package biomesoplenty.biomes;

import biomesoplenty.entities.EntityJungleSpider;
import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.WorldGenTropicsShrub;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreePalm;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenTropics extends BiomeGenBase {

	public static final int SKY_COLOR = 507391;

	public static final int FOG_COLOR = 7724287;

	public static final int MAP_COLOR = 2211330;

	public static final int MOUNTAIN_MAP_COLOR = 2544388;

	public BiomeGenTropics(String key) {
		this(key, 0.0f, 0.4f, MAP_COLOR);
	}

	public BiomeGenTropics(String key, float rootHeight, float heightVariation, int mapColor) {
		super(key);

		customBiomeDecorator.treesPerChunk = 12;
		customBiomeDecorator.grassPerChunk = 7;
		customBiomeDecorator.wheatGrassPerChunk = 4;
		customBiomeDecorator.flowersPerChunk = 10;
		customBiomeDecorator.sandPerChunk = 50;
		customBiomeDecorator.sandPerChunk2 = 50;
		customBiomeDecorator.orangeFlowersPerChunk = 10;
		customBiomeDecorator.whiteFlowersPerChunk = 4;
		customBiomeDecorator.sunflowersPerChunk = 2;
		customBiomeDecorator.hibiscusPerChunk = 45;
		customBiomeDecorator.shrubsPerChunk = 4;
		customBiomeDecorator.generatePumpkins = false;

		spawnableMonsterList().add(EntityJungleSpider.class, 12, 6, 6);
		spawnableCreatureList().clear();

		this.withPlacementDefaults(2.0f, 2.0f, 0.5f);
		this.setMinMaxHeight(rootHeight, heightVariation);
		this.withDebugColor(mapColor);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(2) == 0
			? new WorldFeatureTreePalm(Blocks.LOG_PALM, Blocks.LEAVES_PALM, false, false, false)
			: (random.nextInt(2) == 0
				? new WorldGenTropicsShrub()
				: new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id()));
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("topaz",
			() -> new WorldGenBOPOreSingle(BOPBlocks.TOPAZ_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));
	}

	@Override
	public int getSkyColorByTemp(float temperature) {
		return SKY_COLOR;
	}

	@Override
	public int getBiomeFogColor() {
		return FOG_COLOR;
	}
}
