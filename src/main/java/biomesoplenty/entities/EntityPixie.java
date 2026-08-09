package biomesoplenty.entities;

import com.betteroplenty.item.BOPItems;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.entity.monster.Enemy;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.joml.primitives.AABBd;

public class EntityPixie extends EntityFlyingCreature implements Enemy {

	public static final int UPSTREAM_GROUP_SIZE = 3;

	private static final int TRAIL_ATTEMPTS_PER_TICK = 7;

	public int courseChangeCooldown;
	public double waypointX;
	public double waypointY;
	public double waypointZ;

	public EntityPixie(World world) {
		super(world);
		this.setTextureIdentifier("betteroplenty", "pixie");

		this.setSize(1.0F, 1.0F);

		this.mobDrops.add(new WeightedRandomLootObject(BOPItems.PIXIE_DUST.getDefaultStack(), 0, 2));
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return UPSTREAM_GROUP_SIZE;
	}

	@Override
	public void baseTick() {
		super.baseTick();

		for (int i = 0; i < TRAIL_ATTEMPTS_PER_TICK; i++) {
			if (this.random.nextInt(2) != 0) {
				continue;
			}
			this.world.spawnParticle("pixietrail",
				this.x + this.random.nextDouble() * this.bbWidth,
				this.y + this.random.nextDouble() * this.bbHeight - this.heightOffset,
				this.z + this.random.nextDouble() * this.bbWidth,
				0.0, 0.0, 0.0, 0, false);
		}
	}

	@Override
	protected void updateAI() {
		this.entityAge++;
		this.tryToDespawn();

		double dx = this.waypointX - this.x;
		double dy = this.waypointY - this.y;
		double dz = this.waypointZ - this.z;
		double distSq = dx * dx + dy * dy + dz * dz;

		if (distSq < 1.0 || distSq > 3600.0) {
			this.waypointX = this.x + (this.random.nextFloat() * 4.0F - 2.0F) * 2.0F;
			this.waypointY = this.y + (this.random.nextFloat() * 4.0F - 2.0F) * 2.0F;
			this.waypointZ = this.z + (this.random.nextFloat() * 4.0F - 2.0F) * 2.0F;
		}

		if (this.courseChangeCooldown-- <= 0) {
			this.courseChangeCooldown += this.random.nextInt(2) + 2;
			double dist = MathHelper.sqrt(distSq);

			if (this.isCourseTraversable(dist)) {
				this.xd += dx / dist * 0.1;
				this.yd += dy / dist * 0.1;
				this.zd += dz / dist * 0.1;
			} else {
				this.waypointX = this.x;
				this.waypointY = this.y;
				this.waypointZ = this.z;
			}
		}

		this.yBodyRot = this.yRot = -((float)Math.atan2(this.xd, this.zd)) * 180.0F / (float)Math.PI;
	}

	private boolean isCourseTraversable(double distance) {
		double stepX = (this.waypointX - this.x) / distance;
		double stepY = (this.waypointY - this.y) / distance;
		double stepZ = (this.waypointZ - this.z) / distance;
		AABBd box = new AABBd(this.bb);

		for (int i = 1; i < distance; i++) {
			box.translate(stepX, stepY, stepZ);
			if (!this.world.getCubes(this, box).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean canSpawnHere() {
		int i = MathHelper.floor(this.x);
		int j = MathHelper.floor(this.bb.minY);
		int k = MathHelper.floor(this.z);
		return this.world.getBlockLightValue(i, j, k) > 8 && super.canSpawnHere();
	}

	@Override
	public String getLivingSound() {
		return "betteroplenty:mob.pixie.say";
	}

	@Override
	protected String getHurtSound() {
		return "betteroplenty:mob.pixie.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "betteroplenty:mob.pixie.hurt";
	}
}
