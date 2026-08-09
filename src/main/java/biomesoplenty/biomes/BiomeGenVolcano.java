package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenVolcano;
import com.betteroplenty.block.BOPWastes;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.world.biome.SurfaceProperties;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenVolcano extends BiomeGenBase {

	public static final int SKY_COLOR = 8026746;

	public static final int MAP_COLOR = 6645093;

	public BiomeGenVolcano(String key) {
		super(key);

		spawnableCreatureList().clear();

		customBiomeDecorator.treesPerChunk = 0;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.grassPerChunk = -999;
		customBiomeDecorator.lavaLakesPerChunk = 50;
		customBiomeDecorator.generateAsh = true;

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(BOPWastes.ASH_STONE)
			.withFillerBlock(BOPWastes.ASH_STONE)
			.build());

		this.withPlacementDefaults(2.0f, 0.05f, 0.5f);
		this.setMinMaxHeight(0.6f, 1.2f);
		this.withDebugColor(MAP_COLOR);
	}

	@Override
	public int getSkyColorByTemp(float temperature) {
		return SKY_COLOR;
	}

	@NotNull
	@Override
	public WorldFeature getTreeFeature(@NotNull Random random) {
		return new WorldGenVolcano();
	}
}
