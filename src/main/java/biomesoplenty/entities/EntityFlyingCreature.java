package biomesoplenty.entities;

import net.minecraft.core.entity.MobFlying;
import net.minecraft.core.entity.animal.AmbientCreature;
import net.minecraft.core.world.World;

public abstract class EntityFlyingCreature extends MobFlying implements AmbientCreature {

	public EntityFlyingCreature(World world) {
		super(world);
	}
}
