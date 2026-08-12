package com.betteroplenty;

import net.minecraft.core.data.registry.Registries;
import turniplabs.halplibe.helper.RecipeBuilder;

public final class BOPRecipeNamespaces {
	private BOPRecipeNamespaces() {}

	public static void initNamespaces() {
		if (Registries.RECIPES == null) {

			BetterOPlenty.LOGGER.error("Recipe namespace init ran with no recipe registry -- BOP "
				+ "recipes will not survive a multiplayer login.");
			return;
		}

		RecipeBuilder.initNameSpace(BetterOPlenty.MOD_ID);
	}
}
