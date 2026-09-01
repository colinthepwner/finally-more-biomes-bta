package com.betteroplenty.entity;

import biomesoplenty.entities.EntityBird;
import biomesoplenty.entities.EntityGlob;
import biomesoplenty.entities.EntityJungleSpider;
import biomesoplenty.entities.EntityPhantom;
import biomesoplenty.entities.EntityPixie;
import biomesoplenty.entities.EntityRosester;
import biomesoplenty.entities.EntityWasp;
import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.factories.EntityFactory;
import net.minecraft.core.util.collection.NamespaceID;

import java.util.LinkedHashMap;
import java.util.Map;

public final class BOPEntities {
	private BOPEntities() {}

	private static final EntityDispatcher DISPATCHER = EntityDispatcher.getInstance();

	public static void register() {
		register(EntityJungleSpider.class, "jungle_spider", EntityJungleSpider::new);
		register(EntityRosester.class, "rosester", EntityRosester::new);
		register(EntityBird.class, "bird", EntityBird::new);
		register(EntityWasp.class, "wasp", EntityWasp::new);
		register(EntityPixie.class, "pixie", EntityPixie::new);
		register(EntityGlob.class, "glob", EntityGlob::new);
		register(EntityPhantom.class, "phantom", EntityPhantom::new);

		BetterOPlenty.LOGGER.info("Registered {} BOP entities.", REGISTERED.size());
		verify();
	}

	private static <T extends Mob> void register(Class<T> entityClass, String id,
	                                             EntityFactory<T> factory) {
		DISPATCHER.addMapping(entityClass,
			NamespaceID.getPermanent(BetterOPlenty.MOD_ID, id),
			factory,
			nameKeyFor(id));
		REGISTERED.put(entityClass, id);
	}

	public static final Map<Class<? extends Mob>, String> REGISTERED = new LinkedHashMap<>();

	public static String nameKeyFor(String id) {
		return "entity." + BetterOPlenty.MOD_ID + "." + id + ".name";
	}

	public static String descKeyFor(String id) {
		return "entity." + BetterOPlenty.MOD_ID + "." + id + ".desc";
	}

	private static void verify() {
		StringBuilder found = new StringBuilder();
		int ok = 0;
		for (Class<?> entity : REGISTERED.keySet()) {
			EntityDispatcher.EntityDispatcherEntry<?> entry =
				EntityDispatcher.getInstance().entryForClass((Class) entity);
			if (entry == null) {
				BetterOPlenty.LOGGER.error("Entity {} registered but the dispatcher cannot find it "
					+ "by class; it will never spawn and will not survive a chunk save.",
					entity.getSimpleName());
				continue;
			}
			if (entry.nameKey == null) {
				BetterOPlenty.LOGGER.error("Entity {} has no nameKey; it will show a raw id and be "
					+ "absent from the world-settings mob-spawning screen.", entry.stringID);
			}
			ok++;
			found.append(found.length() == 0 ? "" : ", ")
				.append(entry.stringID).append(" #").append(entry.numericID);
		}
		if (ok == REGISTERED.size()) {
			BetterOPlenty.LOGGER.info("Entity dispatcher resolves all {} BOP entities: {}.", ok, found);
		}
	}
}
