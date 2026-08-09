package biomesoplenty.particles;

import net.minecraft.client.render.particle.Particle;
import net.minecraft.client.render.tessellator.TessellatorParticle;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.helper.LightIndexHelper;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

abstract class ParticlePuffBOP extends Particle {

	private final IconCoordinate[] frames;

	private final float baseSize;

	private final int minBlockLight;

	private final double buoyancy;

	@NotNull
	static IconCoordinate[] frames(@NotNull String texturePrefix) {
		IconCoordinate[] frames = new IconCoordinate[8];
		for (int i = 0; i < frames.length; i++) {
			frames[i] = TextureRegistry.getTexture("betteroplenty:particle/" + texturePrefix + "_" + i);
		}
		return frames;
	}

	ParticlePuffBOP(@NotNull World world, double x, double y, double z,
					double xa, double ya, double za, float scale,
					@NotNull IconCoordinate[] frames, int lifetimeMultiplier,
					int minBlockLight, double buoyancy) {
		super(world, x, y, z, 0.0, 0.0, 0.0);

		this.xd *= 0.1;
		this.yd *= 0.1;
		this.zd *= 0.1;
		this.xd += xa;
		this.yd += ya;
		this.zd += za;

		this.size *= 0.75F;
		this.size *= scale;
		this.baseSize = this.size;

		this.lifetime = (int) (8.0 / (Math.random() * 0.8 + 0.2) * lifetimeMultiplier);
		this.lifetime = (int) (this.lifetime * scale);

		this.lifetime = Math.max(1, this.lifetime);

		this.noPhysics = false;
		this.buoyancy = buoyancy;
		this.minBlockLight = minBlockLight;

		this.frames = frames;
		this.tex = frames[frames.length - 1];
	}

	private IconCoordinate frameFor(int age) {
		return this.frames[MathHelper.clamp(7 - age * 8 / this.lifetime, 0, 7)];
	}

	@Override
	public void render(@NotNull TessellatorParticle tessellatorParticle, float partialTick) {

		this.size = this.baseSize * MathHelper.clamp((this.age + partialTick) / this.lifetime * 32.0F, 0.0F, 1.0F);
		super.render(tessellatorParticle, partialTick);
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

		this.tex = this.frameFor(this.age);
		this.yd += this.buoyancy;
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
		byte world = super.calcLightIndex(partialTick);
		return LightIndexHelper.blockLightFromIndex(world) >= this.minBlockLight
			? world
			: LightIndexHelper.setBlockLight(world, this.minBlockLight);
	}
}
