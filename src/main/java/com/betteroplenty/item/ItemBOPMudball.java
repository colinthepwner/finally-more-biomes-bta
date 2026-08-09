package com.betteroplenty.item;

import biomesoplenty.entities.projectiles.EntityMudball;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.IDispensable;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ItemBOPMudball extends Item implements IDispensable {

	public ItemBOPMudball(@NotNull String name, @NotNull String namespaceId, int id) {
		super(name, namespaceId, id);
		this.maxStackSize = 64;
	}

	@Nullable
	@Override
	public ItemStack onUse(@NotNull ItemStack selfStack, @NotNull World world, @NotNull Player player) {
		selfStack.consumeItem(player);
		world.playSoundAtEntity(player, player, "random.bow", 0.5F,
			0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!world.isClientSide) {
			world.entityJoinedWorld(new EntityMudball(world, player));
		}

		return selfStack;
	}

	@Override
	public void onUseByActivator(@NotNull ItemStack selfStack, @NotNull World world,
								 @NotNull TileEntityActivator activator, @NotNull Random random,
								 @NotNull TilePosc blockPos, @NotNull Direction direction,
								 double offX, double offY, double offZ) {
		EntityMudball mudball = new EntityMudball(world,
			blockPos.x() + offX, blockPos.y() + offY, blockPos.z() + offZ);
		mudball.setHeading(direction.offsetX() * 0.6,
			direction.offsetY() == 0 ? 0.1 : direction.offsetY() * 0.6,
			direction.offsetZ() * 0.6F, 1.1F, 6.0F);
		world.entityJoinedWorld(mudball);
		selfStack.stackSize--;
	}

	@Override
	public void onDispensed(@NotNull ItemStack selfStack, @NotNull World world,
							@NotNull Random random, @NotNull Direction direction,
							double x, double y, double z) {
		if (selfStack.consumeItem(null)) {
			EntityMudball mudball = new EntityMudball(world, x, y, z);
			mudball.setHeading(direction.offsetX(), direction.offsetY() + 0.1, direction.offsetZ(),
				1.1F, 6.0F);
			world.entityJoinedWorld(mudball);
		}
	}
}
