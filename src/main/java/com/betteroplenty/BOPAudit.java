package com.betteroplenty;

import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.registry.Registries;
import com.betteroplenty.block.BOPWoodSet;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryFurnace;
import net.minecraft.core.item.Items;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.menu.MenuInventoryCreative;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BOPAudit {
	private BOPAudit() {}

	private static final String MISSING_FROM_CREATIVE =
		": not in the creative menu -- either no CreativeInventoryPlacement, or its After(X) anchor "
		+ "is not something BTA's own CreativeMenuContents lists (a modded anchor, or a block whose "
		+ "menu entry is really its ItemPlaceable -- Blocks.SUGARCANE vs Items.SUGARCANE)";

	public static void run() {
		List<String> problems = new ArrayList<>();
		int blocks = 0;
		int items = 0;

		try {
			Set<Integer> inCreative = creativeMenuItemIds();

			for (Block<?> block : Blocks.blocksList) {
				if (block == null || !BetterOPlenty.MOD_ID.equals(block.namespaceId().namespace())) {
					continue;
				}
				blocks++;
				String name = block.namespaceId().toString();

				checkLang(problems, name, block.getKey());

				if (!BlockModelDispatcher.getInstance().hasDispatch(block)) {
					problems.add(name + ": no block model -- it will be INVISIBLE in the world");
				}

				if (!block.hasTag(BlockTags.NOT_IN_CREATIVE_MENU) && !inCreative.contains(block.asItem().id)) {
					problems.add(name + MISSING_FROM_CREATIVE);
				}
			}

			for (Item item : Item.itemsList) {

				if (item == null || item instanceof ItemBlock
						|| !BetterOPlenty.MOD_ID.equals(item.namespaceID.namespace())) {
					continue;
				}
				items++;
				String name = item.namespaceID.toString();

				checkLang(problems, name, item.getKey());

				if (!ItemModelDispatcher.getInstance().hasDispatch(item)) {
					problems.add(name + ": no item model -- it will be INVISIBLE in the inventory");
				}
				if (!inCreative.contains(item.id)) {
					problems.add(name + MISSING_FROM_CREATIVE);
				}
			}
			auditDuplicateLangKeys(problems);
			logIdFingerprint();
		} catch (Throwable t) {
			BetterOPlenty.LOGGER.warn("Content audit could not complete; this is the audit's own "
				+ "problem, not the mod's.", t);
			return;
		}

		if (problems.isEmpty()) {
			BetterOPlenty.LOGGER.info(
				"Content audit: {} blocks and {} items, all with name, desc, model and a creative slot.",
				blocks, items);
		} else {
			BetterOPlenty.LOGGER.warn("Content audit found {} problem(s) across {} blocks and {} items:",
				problems.size(), blocks, items);
			for (String problem : problems) {
				BetterOPlenty.LOGGER.warn("  - {}", problem);
			}
		}
	}

	public static void auditRecipes() {
		try {
			if (Registries.RECIPES == null) {
				return;
			}

			Set<Integer> craftable = new HashSet<>();
			for (RecipeEntryCrafting<?, ?> recipe : Registries.RECIPES.getAllCraftingRecipes()) {
				if (recipe.getOutput() instanceof ItemStack stack) {
					craftable.add(stack.itemID);
				}
			}

			List<String> uncraftable = new ArrayList<>();
			for (ItemStack output : BOPRecipes.registeredOutputs()) {
				if (!craftable.contains(output.itemID)) {
					uncraftable.add(output.getItem().namespaceID.toString());
				}
			}

			if (uncraftable.isEmpty()) {
				BetterOPlenty.LOGGER.info("Recipe audit: all {} BOP recipes are live at a workbench.",
					BOPRecipes.registeredOutputs().size());
			} else {
				BetterOPlenty.LOGGER.warn(
					"Recipe audit: {} of {} BOP recipes are registered but NOT craftable -- the recipe "
						+ "cache was baked before they were added: {}",
					uncraftable.size(), BOPRecipes.registeredOutputs().size(), uncraftable);
			}
		} catch (Throwable t) {
			BetterOPlenty.LOGGER.warn("Recipe audit could not complete.", t);
		}
	}

	private static void auditDuplicateLangKeys(List<String> problems) {
		Map<String, String> seen = new HashMap<>();
		Set<String> reported = new HashSet<>();

		for (String file : LANG_FILES) {
			String path = "/assets/" + BetterOPlenty.MOD_ID + "/lang/en_US/" + file;
			try (InputStream in = BOPAudit.class.getResourceAsStream(path)) {
				if (in == null) {
					continue;
				}
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
				String line;
				while ((line = reader.readLine()) != null) {
					String trimmed = line.trim();
					int eq = trimmed.indexOf('=');
					if (trimmed.isEmpty() || trimmed.startsWith("#") || eq <= 0) {
						continue;
					}
					String key = trimmed.substring(0, eq).trim();
					String previous = seen.put(key, file);
					if (previous != null && reported.add(key)) {
						problems.add(key + ": defined twice (" + previous + " and " + file
							+ ") -- Properties.load silently keeps the last one");
					}
				}
			} catch (Exception e) {

			}
		}
	}

	private static final String[] LANG_FILES = {"en_US.lang", "betteroplenty.lang"};

	private static void logIdFingerprint() {
		List<String> entries = new ArrayList<>();
		for (Block<?> block : Blocks.blocksList) {
			if (block != null && BetterOPlenty.MOD_ID.equals(block.namespaceId().namespace())) {
				entries.add(block.namespaceId() + "=" + block.id());
			}
		}
		for (Item item : Item.itemsList) {
			if (item != null && !(item instanceof ItemBlock)
					&& BetterOPlenty.MOD_ID.equals(item.namespaceID.namespace())) {
				entries.add(item.namespaceID + "=" + item.id);
			}
		}
		Collections.sort(entries);

		BetterOPlenty.LOGGER.info("ID fingerprint: {} entries, hash {}. Same count + different hash "
			+ "= an id MOVED, and existing worlds are now wrong.",
			entries.size(), Integer.toHexString(String.join("\n", entries).hashCode()));
	}

	public static void auditLogSmelting() {
		try {
			if (Registries.RECIPES == null) {
				return;
			}

			ItemStack charcoal = new ItemStack(Items.COAL, 1, 1);
			List<String> notSmelting = new ArrayList<>();
			int checked = 0;

			for (BOPWoodSet set : BOPWoodSet.registered()) {
				if (set.log == null) {
					continue;
				}
				checked++;

				ItemStack output = null;
				for (RecipeEntryFurnace recipe : Registries.RECIPES.getAllFurnaceRecipes()) {
					if (recipe != null && recipe.matches(new ItemStack(set.log))) {
						output = recipe.getOutput();
					}
				}

				if (output == null || !output.isItemEqual(charcoal)) {
					notSmelting.add(set.key + (output == null ? " (no furnace recipe)" : " (-> " + output + ")"));
				}
			}

			if (notSmelting.isEmpty()) {
				BetterOPlenty.LOGGER.info("Smelting audit: all {} BOP log(s) smelt to charcoal.", checked);
			} else {
				BetterOPlenty.LOGGER.warn("Smelting audit: {} of {} BOP log(s) do NOT smelt to charcoal "
						+ "-- the minecraft:logs group join is not working: {}",
					notSmelting.size(), checked, notSmelting);
			}
		} catch (Throwable t) {
			BetterOPlenty.LOGGER.warn("Smelting audit could not complete.", t);
		}
	}

	private static void checkLang(List<String> problems, String name, String key) {
		I18n i18n = I18n.getInstance();
		if (i18n == null) {
			return;
		}
		if (key.concat(".name").equals(i18n.translateKey(key + ".name"))) {
			problems.add(name + ": missing " + key + ".name");
		}
		if (key.concat(".desc").equals(i18n.translateKey(key + ".desc"))) {
			problems.add(name + ": missing " + key + ".desc (rule 2 -- the Ctrl tooltip)");
		}
	}

	private static Set<Integer> creativeMenuItemIds() {
		Set<Integer> ids = new HashSet<>();
		for (ItemStack stack : MenuInventoryCreative.creativeContents) {
			if (stack != null) {
				ids.add(stack.itemID);
			}
		}
		return ids;
	}
}
