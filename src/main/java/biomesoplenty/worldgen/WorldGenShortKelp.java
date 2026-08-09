package biomesoplenty.worldgen;

import java.util.Random;

public class WorldGenShortKelp extends WorldGenKelp {

	public WorldGenShortKelp(boolean notify) {
		super(notify);
	}

	@Override
	protected int strandLength(Random random) {
		return random.nextInt(4) + 3;
	}
}
