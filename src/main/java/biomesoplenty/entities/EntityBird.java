package biomesoplenty.entities;

import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.joml.primitives.AABBd;

public class EntityBird extends EntityFlyingCreature {

	public static final int UPSTREAM_GROUP_SIZE = 5;

	public int courseChangeCooldown;
	public double waypointX;
	public double waypointY;
	public double waypointZ;

	public EntityBird(World world) {
		super(world);
		this.setTextureIdentifier("betteroplenty", "bird");

		this.setSize(1.0F, 1.0F);

		this.mobDrops.add(new WeightedRandomLootObject(Items.FEATHER_CHICKEN.getDefaultStack(), 0, 2));
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return UPSTREAM_GROUP_SIZE;
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
			this.waypointX = this.x + (this.random.nextFloat() * 8.0F - 4.0F) * 6.0F;
			this.waypointY = this.y + (this.random.nextFloat() * 2.0F - 1.0F) * 6.0F;
			this.waypointZ = this.z + (this.random.nextFloat() * 8.0F - 4.0F) * 6.0F;
		}

		if (this.courseChangeCooldown-- <= 0) {
			this.courseChangeCooldown = this.courseChangeCooldown + this.random.nextInt(2) + 2;
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
	public String getLivingSound() {
		return "betteroplenty:mob.bird.say";
	}

	@Override
	protected String getHurtSound() {
		return "betteroplenty:mob.bird.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "betteroplenty:mob.bird.hurt";
	}
}
