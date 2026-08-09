package biomesoplenty.biomes;

import biomesoplenty.worldgen.tree.WorldGenPineTree;
import biomesoplenty.worldgen.tree.WorldGenTaiga5;
import biomesoplenty.worldgen.tree.WorldGenTaiga9;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.generate.chunk.PlacementMethod;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.tree.WorldFeatureTreeShrub;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenShield extends BiomeGenBase {

	public static final int GRASS_COLOR = 6586168;

	public static final int FOLIAGE_COLOR = 7902787;

	public static final int MAP_COLOR = 6586168;

	public BiomeGenShield(String key) {
		super(key);

		customBiomeDecorator.treesPerChunk = 7;
		customBiomeDecorator.grassPerChunk = 12;
		customBiomeDecorator.wheatGrassPerChunk = 6;
		customBiomeDecorator.mushroomsPerChunk = 4;
		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.gravelPerChunk = 6;
		customBiomeDecorator.gravelPerChunk2 = 6;
		customBiomeDecorator.shrubsPerChunk = 4;
		customBiomeDecorator.waterReedsPerChunk = 4;
		customBiomeDecorator.generateStoneInGrass2 = true;

		this.withPlacementDefaults(0.5f, 0.8f, 0.5f);
		this.setMinMaxHeight(0.1f, 0.3f);
		this.withDebugColor(MAP_COLOR);
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		if (random.nextInt(2) == 0) {
			return new WorldFeatureTreeShrub(Blocks.LEAVES_OAK.id(), Blocks.LOG_OAK.id());
		}
		if (random.nextInt(4) == 0) {
			return new WorldGenPineTree();
		}
		return random.nextInt(6) == 0
			? new WorldGenTaiga9(false)
			: new WorldGenTaiga5(false);
	}

	@Override
	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
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
}
