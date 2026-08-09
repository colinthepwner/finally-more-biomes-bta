package com.betteroplenty.block;

import com.betteroplenty.fluid.BOPDamageTypes;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.item.tool.ItemToolShears;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

public class BlockLogicDeathbloom extends BlockLogicFlowerBOP {

	public static final int DAMAGE_INTERVAL_TICKS = 40;

	public static final int HARVEST_DAMAGE = 300 / DAMAGE_INTERVAL_TICKS;

	private static final float[] AURA_RGB = {0x61 / 255.0F, 0x67 / 255.0F, 0x7C / 255.0F};

	private static final Map<Entity, Integer> LAST_APPLIED = Collections.synchronizedMap(new WeakHashMap<>());

	public BlockLogicDeathbloom(@NotNull Block<?> block) {

		super(block, BOPSoils.GROWS_FLOWERS);
	}

	@Override
	public void onEntityCollision(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Entity entity) {
		if (world.isClientSide || !(entity instanceof Mob mob) || !mob.isAlive()) {
			return;
		}
		if (isProtected(entity)) {
			return;
		}

		Integer last = LAST_APPLIED.get(entity);
		if (last != null && entity.tickCount - last < DAMAGE_INTERVAL_TICKS) {
			return;
		}
		LAST_APPLIED.put(entity, entity.tickCount);

		mob.hurt(null, 1, BOPDamageTypes.WITHER);
	}

	@Override
	public void onHarvest(@NotNull World world, @NotNull Player player, @NotNull TilePosc tilePos,
						  int data, @Nullable TileEntity tileEntity) {
		super.onHarvest(world, player, tilePos, data, tileEntity);

		if (world.isClientSide) {
			return;
		}
		ItemStack held = player.getHeldItem();
		if (held != null && held.getItem() instanceof ItemToolShears) {
			return;
		}
		player.hurt(null, HARVEST_DAMAGE, BOPDamageTypes.WITHER);
	}

	@Override
	public void animationTick(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Random rand) {
		super.animationTick(world, tilePos, rand);

		if (rand.nextInt(4) != 0) {
			world.spawnParticle("puffrgb",
				tilePos.x() + rand.nextFloat(), tilePos.y() + rand.nextFloat(), tilePos.z() + rand.nextFloat(),
				AURA_RGB[0], AURA_RGB[1], AURA_RGB[2], 0, false);
		}
		if (rand.nextInt(4) == 0) {
			world.spawnParticle("smoke",
				tilePos.x() + rand.nextFloat(), tilePos.y(), tilePos.z() + rand.nextFloat(),
				0.0, 0.0, 0.0, 0, false);
		}
	}

	static boolean isProtected(@NotNull Entity entity) {
		if (!(entity instanceof Player player)) {
			return false;
		}
		return wearing(player, HumanArmorShape.BOOTS, Items.ARMOR_BOOTS_LEATHER)
			&& wearing(player, HumanArmorShape.LEGS, Items.ARMOR_LEGGINGS_LEATHER);
	}

	private static boolean wearing(@NotNull Player player, @NotNull HumanArmorShape slot,
								   @NotNull net.minecraft.core.item.Item item) {
		ItemStack worn = player.getItemInArmorSlot(slot);
		return worn != null && worn.itemID == item.id;
	}
}
