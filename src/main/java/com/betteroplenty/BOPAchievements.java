package com.betteroplenty;

import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPCorals;
import com.betteroplenty.block.BOPGraves;
import com.betteroplenty.block.BOPNether;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.block.BOPPromisedLand;
import com.betteroplenty.block.BlockLogicFlowerBOP;
import com.betteroplenty.world.promised.DimensionPromisedLand;
import com.betteroplenty.item.BOPFlowerBands;
import com.betteroplenty.item.BOPFoods;
import com.betteroplenty.item.BOPItems;
import com.betteroplenty.item.ItemBOPScythe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFlowerStackable;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BOPAchievements {
	private BOPAchievements() {}

	public static Achievement FLOWER;

	public static Achievement FLOWER_BAND;

	public static Achievement DART_BLOWER;

	public static Achievement SCYTHE;

	public static Achievement BERRY;

	public static Achievement MOSS;

	public static Achievement THORN;

	public static Achievement CORAL;

	public static Achievement HONEY;

	public static Achievement WITHER_WART;

	public static Achievement GRAVE;

	public static Achievement PHANTOM;

	public static Achievement CELESTIAL;

	public static Achievement PROMISED;

	public record Placement(@NotNull Achievement achievement, int x, int y) {}

	private static final List<Placement> LAYOUT = new ArrayList<>();

	private static boolean registered = false;

	private static boolean ready = false;

	@NotNull
	public static List<Placement> layout() {
		return Collections.unmodifiableList(LAYOUT);
	}

	public static void register() {
		if (registered) {
			return;
		}
		registered = true;

		FLOWER = add("flower", Blocks.FLOWER_RED, null, 0, 0);

		FLOWER_BAND = add("flower_band", BOPFlowerBands.DULL, FLOWER, 2, -1);
		DART_BLOWER = add("dart_blower", BOPItems.DART_BLOWER, FLOWER_BAND, 4, -2);
		SCYTHE = add("scythe", BOPItems.IRON_SCYTHE, DART_BLOWER, 3, 1);

		BERRY = add("berry", BOPFoods.BERRIES, FLOWER, -2, 0);
		MOSS = add("moss", BOPPlants.MOSS, BERRY, -4, 1);
		THORN = add("thorn", BOPPlants.THORN, MOSS, -5, -1);

		CORAL = add("coral", BOPCorals.CORAL_PINK, THORN, -3, -2);

		HONEY = add("honey", BOPFoods.FILLED_HONEYCOMB, FLOWER, -1, 2);

		WITHER_WART = add("wither_wart", BOPNether.WITHER_WART, HONEY, 1, 3);

		GRAVE = add("grave", BOPGraves.GRAVE, WITHER_WART, -2, 4);

		PHANTOM = add("phantom", BOPItems.GHASTLY_SOUL, GRAVE, 0, 6);
		PHANTOM.setType(Achievement.TYPE_SPECIAL);

		PROMISED = add("promised", BOPPromisedLand.HOLY_GRASS, FLOWER, 1, -3);
		PROMISED.setType(Achievement.TYPE_SPECIAL);

		CELESTIAL = add("celestial", BOPItems.CRYSTAL_SHARD, PROMISED, -1, -4);

		ready = true;
		BetterOPlenty.LOGGER.info(
			"Registered {} BOP achievements of upstream's 17; 3 held back (achEnderporter is a "
				+ "settled DROP, achAmbrosia wants a water bottle BTA has no item for, achAllBOP "
				+ "gates on both). They form a connected subtree, so no parent was re-anchored and "
				+ "no coordinate was moved.",
			LAYOUT.size());
	}

	@NotNull
	private static Achievement add(
		@NotNull String name,
		@NotNull net.minecraft.core.item.IItemConvertible icon,
		@Nullable Achievement parent,
		int x,
		int y) {
		Achievement achievement = new Achievement(
			NamespaceID.fromPool(BetterOPlenty.MOD_ID, name),
			BetterOPlenty.MOD_ID + "." + name,
			icon,
			parent).registerAchievement();
		LAYOUT.add(new Placement(achievement, x, y));
		return achievement;
	}

	public static void onItemPickedUp(@Nullable Player player, @Nullable ItemStack stack) {
		if (player == null || stack == null || !ready) {
			return;
		}

		if (isFlower(stack)) {
			player.triggerAchievement(FLOWER);
		}

		if (stack.itemID == BOPFoods.BERRIES.id) {
			player.triggerAchievement(BERRY);
		}

		if (stack.itemID == BOPPlants.MOSS.id()) {
			player.triggerAchievement(MOSS);
		}

		if (stack.itemID == BOPPlants.THORN.id()) {
			player.triggerAchievement(THORN);
		}

		if (stack.itemID == BOPCorals.CORAL_PINK.id()
			|| stack.itemID == BOPCorals.CORAL_ORANGE.id()
			|| stack.itemID == BOPCorals.CORAL_BLUE.id()
			|| stack.itemID == BOPCorals.CORAL_GLOW.id()) {
			player.triggerAchievement(CORAL);
		}

		if (stack.itemID == BOPFoods.FILLED_HONEYCOMB.id) {
			player.triggerAchievement(HONEY);
		}

		if (stack.itemID == BOPNether.WITHER_WART.id()) {
			player.triggerAchievement(WITHER_WART);
		}

		if (stack.itemID == BOPGraves.GRAVE.id()) {
			player.triggerAchievement(GRAVE);
		}

		if (stack.itemID == BOPItems.GHASTLY_SOUL.id) {
			player.triggerAchievement(PHANTOM);
		}

		if (stack.itemID == BOPItems.CRYSTAL_SHARD.id) {
			player.triggerAchievement(CELESTIAL);
		}
	}

	public static void onDimensionEntered(@Nullable Player player, int dimensionId) {
		if (player == null || !ready) {
			return;
		}
		if (dimensionId == DimensionPromisedLand.DIMENSION_ID) {
			player.triggerAchievement(PROMISED);
		}
	}

	public static void onCrafted(@Nullable Player player, @Nullable ItemStack stack) {
		if (player == null || stack == null || !ready) {
			return;
		}

		if (stack.getItem() instanceof ItemBOPScythe) {
			player.triggerAchievement(SCYTHE);
		}

		if (stack.itemID == BOPItems.DART_BLOWER.id) {
			player.triggerAchievement(DART_BLOWER);
		}

		for (net.minecraft.core.item.Item band : BOPFlowerBands.all()) {
			if (band != null && stack.itemID == band.id) {
				player.triggerAchievement(FLOWER_BAND);
				break;
			}
		}
	}

	private static boolean isFlower(@NotNull ItemStack stack) {
		if (stack.itemID < 0 || stack.itemID >= Blocks.blocksList.length) {
			return false;
		}
		Block<?> block = Blocks.blocksList[stack.itemID];
		if (block == null) {
			return false;
		}
		return block.getLogic() instanceof BlockLogicFlowerBOP
			|| block.getLogic() instanceof BlockLogicFlowerStackable
			|| (BOPBlocks.LAVENDER != null && block == BOPBlocks.LAVENDER);
	}
}
