package com.betteroplenty.item;

import biomesoplenty.entities.projectiles.EntityDart;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemDartBlower extends Item {

	private static final int USES = 63;

	public ItemDartBlower(@NotNull String name, @NotNull String namespaceId, int id) {
		super(name, namespaceId, id);
		this.maxStackSize = 1;
		this.setMaxDamage(USES);
	}

	@Nullable
	@Override
	public ItemStack onUse(@NotNull ItemStack selfStack, @NotNull World world, @NotNull Player player) {
		boolean consumes = player.getGamemode().hasBlockConsumption();

		if (consumes && !player.inventory.consumeInventoryItem(BOPItems.DART.id)) {
			return selfStack;
		}

		selfStack.damageItem(1, player);

		world.playSoundAtEntity(player, player, "random.bow", 1.0F, 1.75F);

		if (!world.isClientSide) {
			world.entityJoinedWorld(new EntityDart(world, player));
		}

		return selfStack;
	}
}
