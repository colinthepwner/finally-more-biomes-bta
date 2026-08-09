package biomesoplenty.entities;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.MobFlying;
import net.minecraft.core.entity.monster.Enemy;
import net.minecraft.core.enums.LightLayer;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import org.jetbrains.annotations.NotNull;

public abstract class EntityFlyingMob extends MobFlying implements Enemy {

	protected int attackStrength = 2;

	public EntityFlyingMob(World world) {
		super(world);
	}

	@Override
	public int getMaxHealth() {
		return 20;
	}

	protected void attackEntity(@NotNull Entity entity, float distance) {
		if (this.attackTime <= 0 && distance < 2.0F
			&& entity.bb.maxY > this.bb.minY && entity.bb.minY < this.bb.maxY) {
			this.attackTime = 20;
			entity.hurt(this, this.attackStrength, DamageType.COMBAT);
		}
	}

	@Override
	public boolean hurt(Entity attacker, int damage, DamageType type) {
		return super.hurt(attacker, damage, type);
	}

	@Override
	public void onLivingUpdate() {
		if (this.calcBrightness(1.0F) > 0.5F) {
			this.entityAge += 2;
		}
		super.onLivingUpdate();
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.world.isClientSide && !this.world.getDifficulty().canHostileMobsSpawn()) {
			this.remove();
		}
	}

	@Override
	public boolean canSpawnHere() {
		TilePos blockPos = new TilePos(this.x, this.bb.minY, this.z);
		if (this.world.getSavedLightValue(LightLayer.Block, blockPos) > 0) {
			return false;
		}
		if (this.world.getSavedLightValue(LightLayer.Sky, blockPos) > this.random.nextInt(32)) {
			return false;
		}
		int blockLight = this.world.getBlockLightValue(blockPos);
		if (this.world.getCurrentWeather() != null
			&& this.world.getCurrentWeather().isMobDaylightSpawnAllowed()) {
			blockLight /= 2;
		}
		return blockLight <= 4 && super.canSpawnHere();
	}
}
