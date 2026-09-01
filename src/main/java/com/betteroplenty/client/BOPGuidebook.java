package com.betteroplenty.client;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.entity.BOPEntities;
import com.betteroplenty.mixin.client.MobScoreAccessor;
import net.minecraft.client.gui.guidebook.mobs.MobInfoRegistry;
import net.minecraft.client.world.WorldEmpty;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class BOPGuidebook {
	private BOPGuidebook() {}

	private static final float ALWAYS = 1.0F;

	public static void registerMobPages() {
		int registered = 0;
		List<String> skipped = new ArrayList<>();

		try {
			for (Map.Entry<Class<? extends Mob>, String> entry : BOPEntities.REGISTERED.entrySet()) {
				try {
					register(entry.getKey(), entry.getValue());
					registered++;
				} catch (Throwable t) {

					skipped.add(entry.getValue());
					BetterOPlenty.LOGGER.warn("Guidebook: no page for {}", entry.getValue(), t);
				}
			}
		} catch (Throwable t) {

			BetterOPlenty.LOGGER.error("Guidebook registration failed; this port's mobs will be "
				+ "missing from the book. Deliberately swallowed rather than rethrown.", t);
			return;
		}

		if (skipped.isEmpty()) {
			BetterOPlenty.LOGGER.info("Guidebook: {} mob pages registered", registered);
		} else {
			BetterOPlenty.LOGGER.warn("Guidebook: {} mob pages registered, {} skipped: {}",
				registered, skipped.size(), String.join(", ", skipped));
		}
	}

	private static void register(Class<? extends Mob> mobClass, String id) {
		Mob sample = EntityDispatcher.getInstance().createEntityInWorld(mobClass, WorldEmpty.INSTANCE);
		if (sample == null) {
			throw new IllegalStateException("the dispatcher would not build one");
		}

		MobInfoRegistry.register(
			mobClass,
			BOPEntities.nameKeyFor(id),
			BOPEntities.descKeyFor(id),
			sample.getMaxHealth(),
			((MobScoreAccessor) sample).betteroplenty$getScoreValue(),
			dropsOf(sample)
		);
	}

	private static MobInfoRegistry.MobDrop[] dropsOf(Mob sample) {
		List<MobInfoRegistry.MobDrop> drops = new ArrayList<>();

		for (WeightedRandomLootObject loot : sample.mobDrops) {
			ItemStack stack = loot.getDefinedItemStack();
			if (stack == null) continue;

			int min = loot.isRandomYield() ? loot.getMinYield() : loot.getFixedYield();
			int max = loot.isRandomYield() ? loot.getMaxYield() : loot.getFixedYield();
			if (max <= 0) continue;

			drops.add(new MobInfoRegistry.MobDrop(stack, ALWAYS, Math.max(0, min), max));
		}

		return drops.toArray(new MobInfoRegistry.MobDrop[0]);
	}
}
