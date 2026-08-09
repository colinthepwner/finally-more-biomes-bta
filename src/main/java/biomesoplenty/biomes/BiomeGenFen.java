package biomesoplenty.biomes;

import biomesoplenty.entities.EntityGlob;
import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenDeadTree;
import biomesoplenty.worldgen.tree.WorldGenFen1;
import biomesoplenty.worldgen.tree.WorldGenFen2;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.chunk.PlacementMethod;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.WorldFeatureTallGrass;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenFen extends BiomeGenBase {

	public static final int GRASS_COLOR = 12240001;

	public static final int FOLIAGE_COLOR = 13547897;

	public static final int MAP_COLOR = 12240001;

	public static final int FOG_COLOR = 12638463;

	public BiomeGenFen(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 10;
		customBiomeDecorator.grassPerChunk = 15;
		customBiomeDecorator.highGrassPerChunk = 1;
		customBiomeDecorator.waterlilyPerChunk = 1;
		customBiomeDecorator.cattailsPerChunk = 1;
		customBiomeDecorator.highCattailsPerChunk = 1;
		customBiomeDecorator.pondsPerChunk = 99;
		customBiomeDecorator.toadstoolsPerChunk = 2;
		customBiomeDecorator.mushroomsPerChunk = 8;
		customBiomeDecorator.mudPerChunk = 1;
		customBiomeDecorator.mudPerChunk2 = 1;
		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.reedsBOPPerChunk = 5;
		customBiomeDecorator.algaePerChunk = 1;
		customBiomeDecorator.portobellosPerChunk = 1;
		customBiomeDecorator.wheatGrassPerChunk = 8;
		customBiomeDecorator.waterReedsPerChunk = 10;
		customBiomeDecorator.koruPerChunk = 1;
		customBiomeDecorator.shrubsPerChunk = 7;

		spawnableMonsterList().add(BOPMobs.SLIME, 10, 1, 3);

		spawnableCreatureList().add(EntityGlob.class, 1, 1, 1);

		this.withPlacementDefaults(0.4f, 0.4f, 0.5f);
		this.setMinMaxHeight(0.2f, 0.4f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return random.nextInt(3) == 0
			? new WorldGenFen2(false)
			: (random.nextInt(20) == 0 ? new WorldGenDeadTree(false) : new WorldGenFen1());
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return random.nextInt(4) == 0
			? new WorldFeatureTallGrass(Blocks.TALLGRASS.id())
			: (random.nextInt(3) == 0
				? new WorldFeatureTallGrass(BOPPlants.MEDIUM_GRASS.id())
				: new WorldFeatureTallGrass(BOPPlants.SHORT_GRASS.id()));
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
	public int getBiomeFogColor() {
		return FOG_COLOR;
	}

	@Override
	public float getFogCloseness() {
		return 0.8F;
	}
}
