package com.betteroplenty.agent;

import net.minecraft.client.gui.worldsettings.ScreenWorldSettings;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.world.settings.WorldConfiguration;

final class ScreenMobSpawningProbe extends ScreenWorldSettings {

	ScreenMobSpawningProbe(int scroll) {
		super(null, new WorldConfiguration());
		this.selectedPage = PAGE_MOB_SPAWNING;
		this.rightScrollAmount = scroll;
	}

	static int namedMobCount() {
		int count = 0;
		for (EntityDispatcher.EntityDispatcherEntry<?> entry
			: EntityDispatcher.getInstance().entries) {
			if (Mob.class.isAssignableFrom(entry.entityClass) && entry.nameKey != null) {
				count++;
			}
		}
		return count;
	}
}
