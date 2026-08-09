package biomesoplenty.particles;

import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

public class ParticleSteamBOP extends ParticlePuffBOP {

	@NotNull
	private static final IconCoordinate[] FRAMES = frames("steam");

	public ParticleSteamBOP(@NotNull World world, double x, double y, double z,
							double xa, double ya, double za) {
		this(world, x, y, z, xa, ya, za, 1.0F);
	}

	public ParticleSteamBOP(@NotNull World world, double x, double y, double z,
							double xa, double ya, double za, float scale) {
		super(world, x, y, z, xa, ya, za, scale,
			FRAMES,
			1,
			0,
			0.004);

		this.rCol = this.gCol = this.bCol = (float) (1.0 - Math.random() * 0.2);
	}
}
