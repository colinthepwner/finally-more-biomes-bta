package com.betteroplenty.item;

import com.betteroplenty.BOPIdManifest;
import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.fluid.BOPDamageTypes;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

public final class BOPFoods {
	private BOPFoods() {}

	private static final int FIRST_ID = 18872;

	public static Item BERRIES;
	public static Item SHROOM_POWDER;
	public static Item WILD_CARROTS;
	public static Item SUNFLOWER_SEEDS;
	public static Item SALAD_FRUIT;
	public static Item SALAD_VEGGIE;
	public static Item SALAD_SHROOM;
	public static Item EARTH;
	public static Item PERSIMMON;
	public static Item FILLED_HONEYCOMB;
	public static Item AMBROSIA;
	public static Item TURNIP;

	public static class Returning extends ItemFood {
		private final java.util.function.Supplier<Item> container;

		public Returning(String name, String nsid, int id, int heal, int ticks,
		                 java.util.function.Supplier<Item> container) {
			super(name, nsid, id, heal, ticks, false, 1);
			this.container = container;
		}

		@Override
		public ItemStack onUse(@NotNull ItemStack stack, @NotNull World world, @NotNull Player player) {
			ItemStack result = super.onUse(stack, world, player);
			Item vessel = this.container.get();
			if (vessel == null) {
				return result;
			}

			return (result == null || result.stackSize <= 0) ? new ItemStack(vessel) : result;
		}
	}

	public static class Harmful extends ItemFood {

		public static final float BITE_CHANCE = 0.6F;

		public static final int BITE_DAMAGE = 1;

		public Harmful(String name, String nsid, int id, int heal, int ticks) {
			super(name, nsid, id, heal, ticks, false, 64);
		}

		@Override
		public ItemStack onUse(@NotNull ItemStack stack, @NotNull World world, @NotNull Player player) {

			if (!stack.consumeItem(player)) {
				return stack;
			}
			player.eatFood(stack);
			world.playSoundAtEntity(player, player, "random.bite",
				0.5F, 1.1F);

			if (!world.isClientSide && world.rand.nextFloat() < BITE_CHANCE) {
				player.hurt(null, BITE_DAMAGE, BOPDamageTypes.SICKNESS);
			}
			return stack;
		}
	}

	public static void register() {
		ItemBuilder builder = new ItemBuilder(BetterOPlenty.MOD_ID)

			.setCreativeInventoryPlacement(new CreativeInventoryPlacement.After(() -> Items.FOOD_APPLE));

		int id = FIRST_ID;

		BERRIES = builder.clone().build(food("berries", id++, 1, 2));

		SHROOM_POWDER = builder.clone().build(harmful("shroom_powder", id++, 1, 2));
		WILD_CARROTS = builder.clone().build(harmful("wild_carrots", id++, 3, 6));
		SUNFLOWER_SEEDS = builder.clone().build(food("sunflower_seeds", id++, 2, 4));

		SALAD_FRUIT = builder.clone().build(
			new Returning("salad_fruit", "betteroplenty:item/saladfruit", id++, 6, 12, () -> Items.BOWL));
		SALAD_VEGGIE = builder.clone().build(
			new Returning("salad_veggie", "betteroplenty:item/saladveggie", id++, 6, 12, () -> Items.BOWL));
		SALAD_SHROOM = builder.clone().build(
			new Returning("salad_shroom", "betteroplenty:item/saladshroom", id++, 6, 12, () -> Items.BOWL));

		EARTH = builder.clone().build(food("earth", id++, 0, 0));

		PERSIMMON = builder.clone().build(food("persimmon", id++, 5, 10));
		FILLED_HONEYCOMB = builder.clone().build(food("filled_honeycomb", id++, 3, 6));

		AMBROSIA = builder.clone().build(
			new Returning("ambrosia", "betteroplenty:item/ambrosia", id++, 6, 0, () -> Items.JAR));

		TURNIP = builder.clone().build(food("turnip", id++, 3, 6));

		BetterOPlenty.LOGGER.info(
			"Registered {} BOP foods, ids {}, on BTA's heal model (hunger -> healAmount 1:1, "
				+ "saturation dropped, 4 return their container).",
			12, BOPIdManifest.span(BERRIES.id, SHROOM_POWDER.id, WILD_CARROTS.id,
				SUNFLOWER_SEEDS.id, SALAD_FRUIT.id, SALAD_VEGGIE.id, SALAD_SHROOM.id, EARTH.id,
				PERSIMMON.id, FILLED_HONEYCOMB.id, AMBROSIA.id, TURNIP.id));
	}

	private static ItemFood food(String name, int id, int heal, int ticks) {
		return new ItemFood(name, "betteroplenty:item/" + name.replace("_", ""), id, heal, ticks,
			false, 64);
	}

	private static ItemFood harmful(String name, int id, int heal, int ticks) {
		return new Harmful(name, "betteroplenty:item/" + name.replace("_", ""), id, heal, ticks);
	}
}
