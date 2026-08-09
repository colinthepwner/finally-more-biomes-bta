package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenBOPOreSingle;
import biomesoplenty.worldgen.tree.WorldGenAcacia;
import biomesoplenty.worldgen.tree.WorldGenDeadTree3;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.SurfaceProperties;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTree;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import net.minecraft.core.world.pos.TilePosc;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenLushDesert extends BiomeGenBase {

	public static final int MAP_COLOR = 9087277;

	public BiomeGenLushDesert(String key) {
		super(key);

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(BOPBlocks.RED_ROCK)
			.withFillerBlock(BOPBlocks.RED_ROCK)
			.build());

		customBiomeDecorator.treesPerChunk = 12;
		customBiomeDecorator.grassPerChunk = 8;
		customBiomeDecorator.shrubsPerChunk = 10;
		customBiomeDecorator.wheatGrassPerChunk = 4;
		customBiomeDecorator.oasesPerChunk = 999;
		customBiomeDecorator.oasesPerChunk2 = 999;
		customBiomeDecorator.deadBushPerChunk = 2;
		customBiomeDecorator.purpleFlowersPerChunk = 5;
		customBiomeDecorator.desertGrassPerChunk = 10;
		customBiomeDecorator.cactiPerChunk = 20;
		customBiomeDecorator.tinyCactiPerChunk = 5;
		customBiomeDecorator.waterLakesPerChunk = 5;
		customBiomeDecorator.waterReedsPerChunk = 4;
		customBiomeDecorator.aloePerChunk = 3;
		customBiomeDecorator.generateGrass = true;
		customBiomeDecorator.generateSand = true;
		customBiomeDecorator.generatePumpkins = false;

		spawnableCreatureList().add(BOPMobs.HORSE, 5, 2, 6);

		this.withPlacementDefaults(0.8f, 0.2f, 0.5f);
		this.setMinMaxHeight(0.2f, 0.9f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		if (random.nextInt(4) == 0) {
			return new WorldGenAcacia(false);
		}
		if (random.nextInt(24) == 0) {
			return new WorldGenDeadTree3(false);
		}
		return random.nextInt(2) == 0
			? new WorldFeatureTree(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id(), 4)
			: new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id());
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
		sink.add("ruby",
			() -> new WorldGenBOPOreSingle(BOPBlocks.RUBY_ORE.id(), Blocks.STONE.id()),
			new BOPDecorations.HeightOffsetUniform(4, 28),
			new BOPDecorations.TriesPerChunkPlusRandom(12, 6));

		sink.add("desert_water",
			DesertWaterPocket::new,
			new BOPDecorations.SeaOffsetUniform(22, 106),
			new BOPDecorations.TriesPerChunkPlusRandom(0, 50));
	}

	private static final class DesertWaterPocket implements WorldFeatureInterface {
		@Override
		public boolean place(@NotNull World world, @NotNull Random random, @NotNull TilePosc tilePos) {
			int id = world.getBlockId(tilePos.x(), tilePos.y(), tilePos.z());
			if (id != Blocks.STONE.id() && id != BOPBlocks.RED_ROCK.id()) {
				return false;
			}
			return world.setBlockAndMetadataRaw(tilePos.x(), tilePos.y(), tilePos.z(),
				Blocks.FLUID_WATER_STILL.id(), 0);
		}
	}
}
