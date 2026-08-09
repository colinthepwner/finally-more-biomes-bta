package biomesoplenty.entities;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.gamemode.Gamemodes;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.joml.primitives.AABBd;

public class EntityWasp extends EntityFlyingMob {

	private static final double SEARCH_RANGE = 100.0;

	private static final double ENGAGE_RANGE = 64.0;

	public int courseChangeCooldown;
	public double waypointX;
	public double waypointY;
	public double waypointZ;

	private Entity targetedEntity;

	private int aggroCooldown;

	public EntityWasp(World world) {
		super(world);
		this.setTextureIdentifier("betteroplenty", "wasp");
		this.setSize(1.0F, 1.0F);

		this.attackStrength = 3;
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
			this.waypointX = this.x + (this.random.nextFloat() * 2.0F - 1.0F) * 4.0F;
			this.waypointY = this.y + (this.random.nextFloat() * 2.0F - 1.0F) * 4.0F;
			this.waypointZ = this.z + (this.random.nextFloat() * 2.0F - 1.0F) * 4.0F;
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

		if (this.targetedEntity != null && this.targetedEntity.removed) {
			this.targetedEntity = null;
		}

		if (this.targetedEntity == null || this.aggroCooldown-- <= 0) {
			this.targetedEntity = this.findVulnerablePlayer();
			if (this.targetedEntity != null) {
				this.aggroCooldown = 20;
			}
		}

		if (this.targetedEntity != null
			&& this.targetedEntity.distanceToSqr(this) < ENGAGE_RANGE * ENGAGE_RANGE) {
			double tx = this.targetedEntity.x - this.x;
			double tz = this.targetedEntity.z - this.z;
			this.yBodyRot = this.yRot = -((float)Math.atan2(tx, tz)) * 180.0F / (float)Math.PI;

			if (this.canEntityBeSeen(this.targetedEntity)) {

				this.waypointX = this.targetedEntity.x;
				this.waypointY = this.targetedEntity.y;
				this.waypointZ = this.targetedEntity.z;
				this.attackEntity(this.targetedEntity, this.targetedEntity.distanceTo(this));
			}
		} else {
			this.yBodyRot = this.yRot =
				-((float)Math.atan2(this.xd, this.zd)) * 180.0F / (float)Math.PI;
		}
	}

	private Entity findVulnerablePlayer() {
		Player player = this.world.getClosestPlayerToEntity(this, SEARCH_RANGE);
		if (player == null
			|| player.gamemode == Gamemodes.CREATIVE
			|| player.gamemode == Gamemodes.SPECTATOR) {
			return null;
		}
		return player;
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
	public boolean hurt(Entity attacker, int damage, DamageType type) {
		return super.hurt(attacker, damage, type);
	}

	@Override
	public String getLivingSound() {
		return "betteroplenty:mob.wasp.say";
	}

	@Override
	protected String getHurtSound() {
		return "betteroplenty:mob.wasp.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "betteroplenty:mob.wasp.hurt";
	}
}
