package biomesoplenty.biomes.ocean;

import com.betteroplenty.compat.BiomeGenBase;

public class BiomeGenOcean extends BiomeGenBase {

	public static final int MAP_COLOR = 112;

	public BiomeGenOcean(String key) {
		super(key);

		spawnableCreatureList().clear();

		this.withPlacementDefaults(0.5f, 0.5f, 0.5f);
		this.setMinMaxHeight(-1.0f, 0.4f);
		this.withDebugColor(MAP_COLOR);
	}
}
