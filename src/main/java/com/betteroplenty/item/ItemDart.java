package com.betteroplenty.item;

import biomesoplenty.entities.projectiles.EntityDart;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.IDispensable;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ItemDart extends Item implements IDispensable {

	public ItemDart(@NotNull String name, @NotNull String namespaceId, int id) {
		super(name, namespaceId, id);
		this.maxStackSize = 64;
	}

	@Nullable
	@Override
	public ItemStack onUse(@NotNull ItemStack selfStack, @NotNull World world, @NotNull Player player) {
		return selfStack;
	}

	@Override
	public void onDispensed(@NotNull ItemStack selfStack, @NotNull World world,
							@NotNull Random random, @NotNull Direction direction,
							double x, double y, double z) {
		if (selfStack.consumeItem(null)) {
			EntityDart dart = new EntityDart(world, x, y, z);
			dart.setHeading(direction.offsetX(), direction.offsetY() + 0.1, direction.offsetZ(),
				1.1F, 6.0F);
			world.entityJoinedWorld(dart);
		}
	}
}
