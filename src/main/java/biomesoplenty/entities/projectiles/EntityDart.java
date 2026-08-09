package biomesoplenty.entities.projectiles;

import com.betteroplenty.item.BOPItems;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.projectile.ProjectileArrow;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

public class EntityDart extends ProjectileArrow {

	private static final int DART_DAMAGE = 2;

	public EntityDart(@NotNull World world) {
		super(world);
		this.beDart();
	}

	public EntityDart(@NotNull World world, double x, double y, double z) {
		super(world, x, y, z, TYPE_NORMAL);
		this.beDart();
	}

	public EntityDart(@NotNull World world, @NotNull Mob owner) {
		super(world, owner, false, TYPE_NORMAL);
		this.beDart();
	}

	private void beDart() {
		this.stack = new ItemStack(BOPItems.DART);
	}

	@Override
	protected void initProjectile() {
		super.initProjectile();
		this.damage = DART_DAMAGE;
	}

	@Override
	protected void inGroundAction() {
		this.world.playSoundAtEntity(null, this, "random.drr", 1.0F,
			1.2F / (this.random.nextFloat() * 0.2F + 0.9F));

		for (int i = 0; i < 4; i++) {
			this.world.spawnParticle("item", this.x, this.y, this.z, 0.0, 0.0, 0.0,
				BOPItems.DART.id, false);
		}

		this.remove();
	}
}
