package biomesoplenty.particles;

import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

public class ParticleMagicTreeBOP extends ParticlePuffBOP {

	@NotNull
	private static final IconCoordinate[] FRAMES = frames("magictree");

	public ParticleMagicTreeBOP(@NotNull World world, double x, double y, double z,
								double xa, double ya, double za) {
		this(world, x, y, z, xa, ya, za, 1.0F);
	}

	public ParticleMagicTreeBOP(@NotNull World world, double x, double y, double z,
								double xa, double ya, double za, float scale) {
		super(world, x, y, z, xa, ya, za, scale,
			FRAMES,
			64,
			9,
			-0.0004);

		int sixteenth = this.lifetime / 16;
		this.age = sixteenth + sixteenth * world.rand.nextInt(7);
	}
}
