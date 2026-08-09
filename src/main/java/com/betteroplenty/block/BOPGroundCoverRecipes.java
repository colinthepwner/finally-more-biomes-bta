package com.betteroplenty.block;

import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.DyeColor;
import turniplabs.halplibe.helper.RecipeBuilder;

import java.util.ArrayList;
import java.util.List;

public final class BOPGroundCoverRecipes {
	private BOPGroundCoverRecipes() {}

	private static final List<ItemStack> OUTPUTS = new ArrayList<>();

	private static ItemStack track(ItemStack output) {
		OUTPUTS.add(output);
		return output;
	}

	public static void register() {
		RecipeBuilder.initNameSpace(BetterOPlenty.MOD_ID);

		RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
			.addInput(BOPPlants.CATTAIL)
			.create("brown_dye_from_cattail",
				track(new ItemStack(Items.DYE, 2, DyeColor.BROWN.itemMeta)));

		RecipeBuilder.Shapeless(BetterOPlenty.MOD_ID)
			.addInput(BOPPlants.MOSS)
			.create("green_dye_from_moss",
				track(new ItemStack(Items.DYE, 2, DyeColor.GREEN.itemMeta)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("CCC", "CCC", "CCC")
			.addInput('C', BOPPlants.CATTAIL)
			.create("wool_from_cattails",
				track(new ItemStack(Blocks.WOOL, 1, DyeColor.WHITE.blockMeta)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("MMM", "MCM", "MMM")
			.addInput('M', BOPPlants.MOSS)
			.addInput('C', Blocks.COBBLE_STONE)
			.create("mossy_cobble_from_moss", track(new ItemStack(Blocks.COBBLE_STONE_MOSSY, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("MMM", "MSM", "MMM")
			.addInput('M', BOPPlants.MOSS)
			.addInput('S', Blocks.BRICK_STONE_POLISHED)
			.create("mossy_stone_brick_from_moss",
				track(new ItemStack(Blocks.BRICK_STONE_POLISHED_MOSSY, 1)));

		RecipeBuilder.Shaped(BetterOPlenty.MOD_ID)
			.setShape("###")
			.addInput('#', BOPPlants.BARLEY)
			.create("wheat_from_barley", track(new ItemStack(Items.WHEAT, 1)));

		RecipeBuilder.Furnace(BetterOPlenty.MOD_ID)
			.setInput(BOPPlants.TINY_CACTUS)
			.create("green_dye_from_tiny_cactus",
				new ItemStack(Items.DYE, 1, DyeColor.GREEN.itemMeta));

		invalidateRecipeCache();
		audit();
	}

	private static void invalidateRecipeCache() {
		if (Registries.RECIPES == null) {
			BetterOPlenty.LOGGER.error("Recipe registry does not exist yet -- the ground-cover recipes "
				+ "will not be craftable. Is register() being called before AFTER_GAME_START?");
			return;
		}
		Registries.RECIPES.invalidateCaches();
	}

	private static void audit() {
		try {
			if (Registries.RECIPES == null) {
				return;
			}

			List<ItemStack> missing = new ArrayList<>(OUTPUTS);
			for (RecipeEntryCrafting<?, ?> recipe : Registries.RECIPES.getAllCraftingRecipes()) {
				if (recipe.getOutput() instanceof ItemStack stack) {
					missing.removeIf(want -> want.itemID == stack.itemID
						&& want.getMetadata() == stack.getMetadata());
				}
			}

			if (missing.isEmpty()) {
				BetterOPlenty.LOGGER.info(
					"Registered {} BOP ground-cover recipes, all live at a workbench, plus 1 smelting row.",
					OUTPUTS.size());
			} else {
				BetterOPlenty.LOGGER.warn("Recipe audit: {} of {} ground-cover recipes are registered but "
					+ "NOT craftable -- the recipe cache was baked before they were added.",
					missing.size(), OUTPUTS.size());
			}
		} catch (Throwable t) {
			BetterOPlenty.LOGGER.warn("Ground-cover recipe audit could not complete.", t);
		}
	}
}
