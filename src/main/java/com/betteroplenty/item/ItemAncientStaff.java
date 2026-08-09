package com.betteroplenty.item;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.world.promised.DimensionPromisedLand;
import com.betteroplenty.world.promised.PromisedArrivalIsland;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemAncientStaff extends Item {

	private static final TextFormatting.Base NO_FORMAT = null;

	public ItemAncientStaff(@NotNull String name, @NotNull String namespaceId, int id) {
		super(name, namespaceId, id);
		this.maxStackSize = 1;
	}

	@Nullable
	@Override
	public ItemStack onUse(@NotNull ItemStack selfStack, @NotNull World world,
						   @NotNull Player player) {

		boolean promised = world.dimension == DimensionPromisedLand.PROMISED_LAND;
		if (world.dimension != Dimension.OVERWORLD && !promised) {
			player.sendMessageTranslated(NO_FORMAT, "phrase.betteroplenty.staffWrongDimension");
			return selfStack;
		}

		if (world.isClientSide) {
			return selfStack;
		}

		int originX = PromisedArrivalIsland.originX(player);
		int originZ = PromisedArrivalIsland.originZ(player);
		int originY = PromisedArrivalIsland.arrivalY(
			world, originX, originZ, PromisedArrivalIsland.STAFF_ARRIVAL_Y);

		if (!PromisedArrivalIsland.siteIsClear(world, originX, originY, originZ)) {
			player.sendMessageTranslated(NO_FORMAT, "phrase.betteroplenty.staffNoRoom");
			return selfStack;
		}

		if (!PromisedArrivalIsland.build(world, player, null, PromisedArrivalIsland.STAFF_ARRIVAL_Y)) {
			BetterOPlenty.LOGGER.error(
				"The Ancient Staff built its island at ({}, {}, {}) but the portal did not light; "
					+ "the staff has NOT been spent.", originX, originY, originZ);
			player.sendMessageTranslated(NO_FORMAT, "phrase.betteroplenty.staffNoRoom");
			return selfStack;
		}

		player.sendMessageTranslated(NO_FORMAT, promised
			? "phrase.betteroplenty.promisedPortalOther"
			: "phrase.betteroplenty.promisedPortalOverworld");

		return new ItemStack(BOPItems.ANCIENT_STAFF_DEPLETED, 1);
	}
}
