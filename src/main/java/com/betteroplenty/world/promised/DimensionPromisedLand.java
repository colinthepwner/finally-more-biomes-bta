package com.betteroplenty.world.promised;

import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicPortal;
import net.minecraft.core.world.Dimension;

import java.lang.reflect.Field;

public final class DimensionPromisedLand {
	private DimensionPromisedLand() {}

	public static final int DIMENSION_ID = 3;

	public static Dimension PROMISED_LAND;

	public static void create() {
		PROMISED_LAND = new Dimension(
			"promised",
			Dimension.OVERWORLD,
			1.0F,
			null,
			WorldTypePromisedLand.PROMISED_LAND);
	}

	public static void attachPortalBlock(Block<BlockLogicPortal> portal) {
		try {
			Field field = Dimension.class.getField("portalBlock");
			field.setAccessible(true);
			field.set(PROMISED_LAND, portal);
		} catch (ReflectiveOperationException | RuntimeException e) {
			BetterOPlenty.LOGGER.error("Could not attach the Promised Land's portal block; travel "
				+ "to and from dimension {} will fail in PortalHandler.", DIMENSION_ID, e);
		}
	}

	public static void register() {
		if (Dimension.getDimensionList().containsKey(DIMENSION_ID)) {
			BetterOPlenty.LOGGER.error(
				"Dimension id {} is already taken; the Promised Land will not be reachable.",
				DIMENSION_ID);
			return;
		}
		Dimension.registerDimension(DIMENSION_ID, PROMISED_LAND);
		BetterOPlenty.LOGGER.info("Registered the Promised Land as dimension {}.", DIMENSION_ID);
	}
}
