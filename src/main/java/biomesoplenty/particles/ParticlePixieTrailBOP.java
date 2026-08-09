package biomesoplenty.particles;

import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

public class ParticlePixieTrailBOP extends ParticlePuffBOP {

	@NotNull
	private static final IconCoordinate[] FRAMES = frames("pixietrail");

	public ParticlePixieTrailBOP(@NotNull World world, double x, double y, double z,
								 double xa, double ya, double za) {
		this(world, x, y, z, xa, ya, za, 1.0F);
	}

	public ParticlePixieTrailBOP(@NotNull World world, double x, double y, double z,
								 double xa, double ya, double za, float scale) {
		super(world, x, y, z, xa, ya, za, scale,
			FRAMES,
			8,
			10,
			0.0);

		int half = this.lifetime / 2;
		this.age = half + half * world.rand.nextInt(7);
	}
}
