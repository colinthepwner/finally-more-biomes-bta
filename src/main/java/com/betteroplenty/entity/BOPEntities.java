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
import net.minecraft.core.util.collection.NamespaceID;

public final class BOPEntities {
	private BOPEntities() {}

	private static final EntityDispatcher DISPATCHER = EntityDispatcher.getInstance();

	public static void register() {
		DISPATCHER.addMapping(EntityJungleSpider.class,
			NamespaceID.getPermanent(BetterOPlenty.MOD_ID, "jungle_spider"),
			EntityJungleSpider::new,
			"entity.betteroplenty.jungle_spider.name");

		DISPATCHER.addMapping(EntityRosester.class,
			NamespaceID.getPermanent(BetterOPlenty.MOD_ID, "rosester"),
			EntityRosester::new,
			"entity.betteroplenty.rosester.name");

		DISPATCHER.addMapping(EntityBird.class,
			NamespaceID.getPermanent(BetterOPlenty.MOD_ID, "bird"),
			EntityBird::new,
			"entity.betteroplenty.bird.name");

		DISPATCHER.addMapping(EntityWasp.class,
			NamespaceID.getPermanent(BetterOPlenty.MOD_ID, "wasp"),
			EntityWasp::new,
			"entity.betteroplenty.wasp.name");

		DISPATCHER.addMapping(EntityPixie.class,
			NamespaceID.getPermanent(BetterOPlenty.MOD_ID, "pixie"),
			EntityPixie::new,
			"entity.betteroplenty.pixie.name");

		DISPATCHER.addMapping(EntityGlob.class,
			NamespaceID.getPermanent(BetterOPlenty.MOD_ID, "glob"),
			EntityGlob::new,
			"entity.betteroplenty.glob.name");

		DISPATCHER.addMapping(EntityPhantom.class,
			NamespaceID.getPermanent(BetterOPlenty.MOD_ID, "phantom"),
			EntityPhantom::new,
			"entity.betteroplenty.phantom.name");

		BetterOPlenty.LOGGER.info("Registered {} BOP entities.", REGISTERED.length);
		verify();
	}

	private static final Class<?>[] REGISTERED = {
		EntityJungleSpider.class, EntityRosester.class, EntityBird.class, EntityWasp.class,
		EntityPixie.class, EntityGlob.class, EntityPhantom.class
	};

	private static void verify() {
		StringBuilder found = new StringBuilder();
		int ok = 0;
		for (Class<?> entity : REGISTERED) {
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
		if (ok == REGISTERED.length) {
			BetterOPlenty.LOGGER.info("Entity dispatcher resolves all {} BOP entities: {}.", ok, found);
		}
	}
}
