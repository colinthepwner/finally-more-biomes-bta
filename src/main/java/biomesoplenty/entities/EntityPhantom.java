package biomesoplenty.entities;

import com.betteroplenty.entity.PhantomAuraBridge;
import com.betteroplenty.item.BOPItems;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.MobMonster;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;

public class EntityPhantom extends MobMonster {

	public static final int UPSTREAM_GROUP_SIZE = 1;

	private static final int PARTICLES_PER_TICK = 9;

	public EntityPhantom(World world) {
		super(world);

		this.moveSpeed = 0.45F;

	}

	@Override
	protected void dropDeathItems() {
		super.dropDeathItems();
		if (this.random.nextInt(3) == 0) {
			this.dropItem(BOPItems.GHASTLY_SOUL.getDefaultStack(), 1.0F);
		}
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return UPSTREAM_GROUP_SIZE;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (!this.onGround && this.yd < 0.0) {
			this.yd *= 0.6;
		}

		this.fallDistance = 0.0F;
	}

	@Override
	protected void causeFallDamage(float distance) {
	}

	@Override
	public void tick() {
		super.tick();
		this.spawnAura(0.0, 0.0, 0.0);
	}

	@Override
	public boolean hurt(Entity attacker, int damage, DamageType type) {
		this.spawnAura(66.0, 0.0, 0.0);
		return super.hurt(attacker, damage, type);
	}

	private void spawnAura(double red, double green, double blue) {
		PhantomAuraBridge.Sink sink = PhantomAuraBridge.sink;
		for (int i = 0; i < PARTICLES_PER_TICK; i++) {
			double px = this.x + this.random.nextDouble() * this.bbWidth;
			double py = this.y + this.random.nextDouble() * this.bbHeight - this.heightOffset;
			double pz = this.z + this.random.nextDouble() * this.bbWidth;
			if (sink != null) {
				sink.spawn(this.world, px, py, pz, red, green, blue);
			} else {
				this.world.spawnParticle("puffrgb", px, py, pz, red, green, blue, -1, false);
			}
		}
	}

	@Override
	public boolean canSpawnHere() {
		if (!this.world.getDifficulty().canHostileMobsSpawn()) {
			return false;
		}

		int blockX = MathHelper.floor(this.x);
		int blockY = MathHelper.floor(this.bb.minY);
		int blockZ = MathHelper.floor(this.z);
		if (Blocks.hasTag(this.world.getBlockId(blockX, blockY, blockZ), BlockTags.PREVENT_MOB_SPAWNS)) {
			return false;
		}
		if (!this.world.areBlocksLoaded(
			new TilePos(this.bb.minX - 1.0, this.bb.minY - 1.0, this.bb.minZ - 1.0),
			new TilePos(this.bb.maxX + 1.0, this.bb.maxY + 1.0, this.bb.maxZ + 1.0))) {
			return false;
		}

		return this.world.checkIfAABBIsClear(this.bb)
			&& this.world.getCubes(this, this.bb).isEmpty()
			&& !this.world.getIsAnyLiquid(this.bb);
	}

	@Override
	public String getLivingSound() {
		return "betteroplenty:mob.phantom.say";
	}

	@Override
	protected String getHurtSound() {
		return "betteroplenty:mob.phantom.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "betteroplenty:mob.phantom.death";
	}
}
