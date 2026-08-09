package biomesoplenty.particles;

import net.minecraft.client.render.particle.Particle;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.helper.LightIndexHelper;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

public class ParticleDandelionBOP extends Particle {

	@NotNull
	private static final IconCoordinate ICON = TextureRegistry.getTexture("betteroplenty:particle/dandelion");

	public ParticleDandelionBOP(@NotNull World world, double x, double y, double z, float scale) {
		super(world, x, y, z, 0.0, 0.0, 0.0);

		this.xd *= 0.2;
		this.yd *= 0.1;
		this.zd *= 0.2;

		this.size *= 0.25F;
		this.size *= scale;

		this.lifetime = (int) (8.0 / (Math.random() * 0.8 + 0.2));
		this.lifetime = (int) (this.lifetime * scale);

		this.noPhysics = false;
		this.tex = ICON;
	}

	@Override
	public void tick() {
		this.cachedLightmapCoord = this.calcLightIndex(1.0F);
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		if (this.age++ >= this.lifetime) {
			this.remove();
		}

		this.yd += 0.004;
		this.move(this.xd, this.yd, this.zd);

		if (this.y == this.yo) {
			this.xd *= 1.1;
			this.zd *= 1.1;
		}

		this.xd *= 0.96;
		this.yd *= 0.96;
		this.zd *= 0.96;

		if (this.onGround) {
			this.xd *= 0.7;
			this.zd *= 0.7;
		}
	}

	@Override
	public byte calcLightIndex(float partialTick) {
		return LightIndexHelper.setBlockLight(super.calcLightIndex(partialTick), 15);
	}
}
