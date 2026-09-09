package com.betteroplenty.world.promised;

import com.betteroplenty.BOPDimensionId;
import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicPortal;
import net.minecraft.core.world.Dimension;

import java.lang.reflect.Field;

public final class DimensionPromisedLand {
	private DimensionPromisedLand() {}

	private static int dimensionId = -1;

	public static int dimensionId() {
		return dimensionId;
	}

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
				+ "to and from it will fail in PortalHandler.", e);
		}
	}

	public static boolean register() {
		if (dimensionId != -1 && Dimension.getDimensionList().get(dimensionId) == PROMISED_LAND) {
			return false;
		}

		int id = BOPDimensionId.choosePromised();
		if (id == -1) {

			return false;
		}

		try {
			Dimension.registerDimension(id, PROMISED_LAND);
		} catch (IllegalArgumentException raced) {

			BetterOPlenty.LOGGER.error("Dimension id {} was taken between choosing it and claiming "
				+ "it; the Promised Land will not be reachable. Registered ids: {}.",
				id, BOPDimensionId.describeIdSpace(), raced);
			return false;
		}

		dimensionId = id;
		if (id == BOPDimensionId.PREFERRED) {
			BetterOPlenty.LOGGER.info("Registered the Promised Land as dimension {}.", id);
		} else {

			BetterOPlenty.LOGGER.info("Registered the Promised Land as dimension {} rather than "
				+ "this mod's usual {}; the reason is in the line(s) above. Registered ids: {}.",
				id, BOPDimensionId.PREFERRED, BOPDimensionId.describeIdSpace());
		}
		return true;
	}
}
