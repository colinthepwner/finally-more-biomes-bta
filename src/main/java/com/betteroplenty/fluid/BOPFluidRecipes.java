package com.betteroplenty.fluid;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.item.BOPItems;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.helper.RecipeBuilder;

public final class BOPFluidRecipes {
	private BOPFluidRecipes() {}

	public static void register() {
		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("A A", " A ")
			.addInput('A', BOPItems.AMETHYST)
			.create("amethyst_bucket", new ItemStack(BOPFluids.BUCKET_AMETHYST, 1));

		invalidateRecipeCache();
		audit();
	}

	private static void invalidateRecipeCache() {
		if (Registries.RECIPES == null) {
			BetterOPlenty.LOGGER.error("Recipe registry does not exist yet -- the amethyst bucket will "
				+ "not be craftable. Is BOPFluidRecipes.register() being called before AFTER_GAME_START?");
			return;
		}
		Registries.RECIPES.invalidateCaches();
	}

	private static void audit() {
		try {
			if (Registries.RECIPES == null) {
				return;
			}
			for (RecipeEntryCrafting<?, ?> recipe : Registries.RECIPES.getAllCraftingRecipes()) {
				if (recipe.getOutput() instanceof ItemStack stack
						&& stack.itemID == BOPFluids.BUCKET_AMETHYST.id) {
					BetterOPlenty.LOGGER.info("Registered 1 BOP fluid recipe (the amethyst bucket), "
						+ "live at a workbench.");
					return;
				}
			}
			BetterOPlenty.LOGGER.warn("Recipe audit: the amethyst bucket is registered but NOT craftable "
				+ "-- the recipe cache was baked before it was added.");
		} catch (Throwable t) {
			BetterOPlenty.LOGGER.warn("Fluid recipe audit could not complete.", t);
		}
	}
}
