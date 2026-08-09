package biomesoplenty.biomes.nether;

import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.entity.BOPMobs;

final class NetherSpawns {

	private NetherSpawns() {
	}

	static void apply(BiomeGenBase biome, int magmaCubeWeight) {
		biome.spawnableMonsterList().clear();
		biome.spawnableCreatureList().clear();
		biome.spawnableWaterCreatureList().clear();
		biome.spawnableCaveCreatureList().clear();

		biome.spawnableMonsterList().add(BOPMobs.GHAST, 50, 4, 4);
		biome.spawnableMonsterList().add(BOPMobs.PIG_ZOMBIE, 100, 4, 4);
		biome.spawnableMonsterList().add(BOPMobs.SLIME, magmaCubeWeight, 4, 4);
	}
}
