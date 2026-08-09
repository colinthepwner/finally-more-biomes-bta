package com.betteroplenty.fluid;

import biomesoplenty.fluids.FluidLiquidPoison;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

@FunctionalInterface
public interface BOPFluidContact {

	void apply(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Entity entity);

	int DAMAGE_INTERVAL_TICKS = 25;

	int HEAL_INTERVAL_TICKS = 50;

	double HONEY_SLOWDOWN = 0.55;

	Map<Entity, Integer> POISON_LAST_APPLIED = Collections.synchronizedMap(new WeakHashMap<>());

	Map<Entity, Integer> SPRING_WATER_LAST_APPLIED = Collections.synchronizedMap(new WeakHashMap<>());

	BOPFluidContact LIQUID_POISON = (world, tilePos, entity) -> {
		if (!(entity instanceof Mob mob) || !mob.isAlive()) {
			return;
		}
		if (!acquire(POISON_LAST_APPLIED, entity, DAMAGE_INTERVAL_TICKS)) {
			return;
		}

		if (mob.getHealth() > 1) {
			mob.hurt(null, 1, BOPDamageTypes.POISON);
		}

		world.playSoundAtEntity(null, entity, "random.fizz", 0.25F,
			0.4F + world.rand.nextFloat() * 0.2F);
		for (int i = 0; i < 4; i++) {
			world.spawnParticle("puffrgb",
				entity.x + (world.rand.nextDouble() - 0.5) * entity.bbWidth,
				entity.bb.minY + world.rand.nextDouble() * entity.bbHeight,
				entity.z + (world.rand.nextDouble() - 0.5) * entity.bbWidth,
				FluidLiquidPoison.PARTICLE_RGB[0], FluidLiquidPoison.PARTICLE_RGB[1],
				FluidLiquidPoison.PARTICLE_RGB[2], 0, false);
		}
	};

	BOPFluidContact SPRING_WATER = (world, tilePos, entity) -> {
		if (!(entity instanceof Mob mob) || !mob.isAlive()) {
			return;
		}
		if (mob.getHealth() >= mob.getMaxHealth()) {
			return;
		}
		if (!acquire(SPRING_WATER_LAST_APPLIED, entity, HEAL_INTERVAL_TICKS)) {
			return;
		}
		mob.heal(1);
	};

	BOPFluidContact HONEY = (world, tilePos, entity) -> {
		if (!(entity instanceof Mob)) {
			return;
		}
		entity.xd *= HONEY_SLOWDOWN;
		entity.zd *= HONEY_SLOWDOWN;
	};

	static boolean acquire(@NotNull Map<Entity, Integer> lastApplied, @NotNull Entity entity, int interval) {
		Integer last = lastApplied.get(entity);
		if (last != null && entity.tickCount - last < interval) {
			return false;
		}
		lastApplied.put(entity, entity.tickCount);
		return true;
	}
}
