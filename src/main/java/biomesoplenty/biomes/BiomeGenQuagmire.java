package biomesoplenty.biomes;

import biomesoplenty.entities.EntityGlob;
import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.WorldGenQuagmire;
import biomesoplenty.worldgen.tree.WorldGenDeadTree;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.chunk.PlacementMethod;
import net.minecraft.core.world.biome.SurfaceProperties;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenQuagmire extends BiomeGenBase {

	public static final int GRASS_COLOR = 10390377;
	public static final int FOLIAGE_COLOR = 10390377;

	public static final int SKY_COLOR = 12436670;

	public static final int FOG_COLOR = 13291213;

	public static final int MAP_COLOR = 5257771;

	public BiomeGenQuagmire(String key) {
		super(key);

		spawnableCreatureList().clear();
		spawnableWaterCreatureList().clear();

		spawnableCreatureList().add(EntityGlob.class, 1, 1, 1);

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(Blocks.MUD)
			.withFillerBlock(Blocks.MUD)
			.build());

		customBiomeDecorator.treesPerChunk = 0;
		customBiomeDecorator.grassPerChunk = 10;
		customBiomeDecorator.mushroomsPerChunk = 3;
		customBiomeDecorator.flowersPerChunk = -999;

		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.wheatGrassPerChunk = 3;
		customBiomeDecorator.waterReedsPerChunk = 2;
		customBiomeDecorator.koruPerChunk = 1;

		customBiomeDecorator.generateQuagmire = true;

		waterColorMultiplier = 13390080;

		this.withPlacementDefaults(0.8f, 0.9f, 0.5f);
		this.setMinMaxHeight(0.2f, 0.3f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return new WorldGenDeadTree(false);
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("malachite",
			() -> new WorldGenBOPOreSingle(BOPBlocks.MALACHITE_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));

		sink.add("quagmire_grass",
			WorldGenQuagmire::new,
			new BOPDecorations.SeaOffsetUniform(0, 64),
			new PlacementMethod.TriesPerChunk(15));
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
