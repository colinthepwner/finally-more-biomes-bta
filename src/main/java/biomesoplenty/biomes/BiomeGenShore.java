package biomesoplenty.biomes;

import com.betteroplenty.compat.BiomeGenBase;

public class BiomeGenShore extends BiomeGenBase {

	public static final int MAP_COLOR = 9286496;

	public BiomeGenShore(String key) {
		super(key);

		spawnableCreatureList().clear();

		this.withPlacementDefaults(0.8f, 0.4f, 0.5f);
		this.setMinMaxHeight(-1.0f, 0.4f);
		this.withDebugColor(MAP_COLOR);
	}
}
