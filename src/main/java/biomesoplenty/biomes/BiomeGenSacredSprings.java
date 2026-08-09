package biomesoplenty.biomes;

import biomesoplenty.entities.EntityJungleSpider;
import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenMassiveTree;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenSacredSprings extends BiomeGenBase {

	public static final int GRASS_COLOR = 39259;
	public static final int FOLIAGE_COLOR = 39259;
	public static final int MAP_COLOR = 39259;

	public static final int SKY_COLOR = 1995007;

	public static final int FOG_COLOR = 8707327;

	public BiomeGenSacredSprings(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 30;
		customBiomeDecorator.grassPerChunk = 4;
		customBiomeDecorator.wheatGrassPerChunk = 1;
		customBiomeDecorator.waterlilyPerChunk = 5;
		customBiomeDecorator.generatePumpkins = false;

		spawnableMonsterList().add(EntityJungleSpider.class, 12, 6, 6);

		this.withPlacementDefaults(1.2f, 0.9f, 0.5f);
		this.setMinMaxHeight(0.4f, 1.2f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(150) == 0
			? new WorldGenMassiveTree(false)
			: new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id());
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("sapphire",
			() -> new WorldGenBOPOreSingle(BOPBlocks.SAPPHIRE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));

		sink.add("sacred_springs",
			() -> new WorldGenBOPOreSingle(Blocks.FLUID_WATER_FLOWING.id(),
				Blocks.STONE.id(), Blocks.DIRT.id()),
			new BOPDecorations.SeaOffsetUniform(11, 53),
			new BOPDecorations.TriesPerChunkPlusRandom(0, 75));
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
}
