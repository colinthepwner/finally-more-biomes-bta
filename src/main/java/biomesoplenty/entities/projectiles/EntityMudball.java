package biomesoplenty.entities.projectiles;

import com.betteroplenty.item.BOPItems;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.projectile.Projectile;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

public class EntityMudball extends Projectile {

	public EntityMudball(@NotNull World world) {
		super(world);
		this.modelItem = BOPItems.MUDBALL;
	}

	public EntityMudball(@NotNull World world, @NotNull Mob owner) {
		super(world, owner);
		this.modelItem = BOPItems.MUDBALL;
	}

	public EntityMudball(@NotNull World world, double x, double y, double z) {
		super(world, x, y, z);
		this.modelItem = BOPItems.MUDBALL;
	}
}
