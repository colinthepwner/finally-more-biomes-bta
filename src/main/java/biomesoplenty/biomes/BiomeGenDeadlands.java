package biomesoplenty.biomes;

import biomesoplenty.worldgen.WorldGenDeadlands;
import com.betteroplenty.block.BOPWastes;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;
import net.minecraft.core.world.biome.SurfaceProperties;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BiomeGenDeadlands extends BiomeGenBase {

	public static final int SKY_COLOR = 4464929;

	public static final int FOG_COLOR = 9849675;

	public static final int MAP_COLOR = 9849675;

	public static final int WATER_COLOR = 16711680;

	public BiomeGenDeadlands(String key) {
		super(key);

		spawnableCreatureList().clear();
		spawnableWaterCreatureList().clear();

		customBiomeDecorator.treesPerChunk = -999;
		customBiomeDecorator.grassPerChunk = 15;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.mushroomsPerChunk = -999;
		customBiomeDecorator.sandPerChunk = -999;
		customBiomeDecorator.sandPerChunk2 = -999;
		customBiomeDecorator.lavaLakesPerChunk = 25;
		customBiomeDecorator.smolderingGrassPerChunk = 5;
		customBiomeDecorator.generatePits = true;

		waterColorMultiplier = WATER_COLOR;

		spawnableCreatureList().add(BOPMobs.CREEPER, 30, 1, 7);
		spawnableCaveCreatureList().add(BOPMobs.BAT, 10, 8, 8);

		this.withSurfaceProperties(new SurfaceProperties.Builder()
			.withTopBlock(BOPWastes.ASH)
			.withFillerBlock(BOPWastes.ASH)
			.build());

		this.withPlacementDefaults(2.0f, 0.05f, 0.5f);
		this.setMinMaxHeight(0.1f, 0.5f);
		this.withDebugColor(MAP_COLOR);
	}

	@Override
	public int getSkyColorByTemp(float temperature) {
		return SKY_COLOR;
	}

	@Override
	public int getBiomeFogColor() {
		return FOG_COLOR;
	}

	@NotNull
	@Override
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return new WorldGenDeadlands();
	}
}
