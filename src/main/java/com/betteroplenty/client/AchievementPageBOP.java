package com.betteroplenty.client;

import com.betteroplenty.BOPAchievements;
import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.item.BOPFoods;
import java.util.Objects;
import net.minecraft.client.gui.achievements.data.AchievementPage;
import net.minecraft.client.gui.achievements.data.AchievementPageRegistry;
import net.minecraft.client.gui.achievements.pages.AchievementPageOverworld;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import org.jetbrains.annotations.NotNull;

public class AchievementPageBOP extends AchievementPageOverworld {

	public static final String KEY = "gui.achievements.page." + BetterOPlenty.MOD_ID;

	private static AchievementPageBOP page;

	public AchievementPageBOP(String key, ItemStack icon) {
		super(key, icon);
	}

	public static void register() {
		if (page != null) {
			return;
		}
		BOPAchievements.register();
		if (BOPAchievements.layout().isEmpty()) {
			BetterOPlenty.LOGGER.error(
				"No BOP achievements are registered, so the achievement page is being skipped -- "
					+ "BOPAchievements.register() ran but produced nothing.");
			return;
		}

		page = new AchievementPageBOP(KEY, BOPFoods.EARTH.getDefaultStack());
		for (BOPAchievements.Placement placement : BOPAchievements.layout()) {
			page.addAchievement(placement.achievement(), placement.x(), placement.y());
		}
		AchievementPageRegistry.getInstance().register(page);

		BetterOPlenty.LOGGER.info(
			"Registered the BOP achievement page with {} entries; it is page {} of {}.",
			BOPAchievements.layout().size(),
			AchievementPageRegistry.getInstance().getPageIndex(page) + 1,
			AchievementPageRegistry.getInstance().getPages().size());
		verifyLangKeys();
	}

	private static void verifyLangKeys() {
		StringBuilder missing = new StringBuilder();
		int checked = 0;
		for (BOPAchievements.Placement placement : BOPAchievements.layout()) {
			String key = placement.achievement().getStatKey();
			checked += 2;
			if (placement.achievement().getStatName().equals(key)) {
				missing.append(missing.length() == 0 ? "" : ", ").append(key);
			}
			if (placement.achievement().getDescription().equals(key + ".desc")) {
				missing.append(missing.length() == 0 ? "" : ", ").append(key).append(".desc");
			}
		}

		String pageName = I18n.getInstance().translateKey(KEY + ".name");
		String pageDesc = I18n.getInstance().translateKey(KEY + ".desc");
		checked += 2;
		if (pageName.equals(KEY + ".name")) {
			missing.append(missing.length() == 0 ? "" : ", ").append(KEY).append(".name");
		}
		if (pageDesc.equals(KEY + ".desc")) {
			missing.append(missing.length() == 0 ? "" : ", ").append(KEY).append(".desc");
		}

		if (missing.length() == 0) {
			BetterOPlenty.LOGGER.info("Achievement lang: all {} keys resolve.", checked);
		} else {
			BetterOPlenty.LOGGER.error(
				"Achievement lang: {} of {} keys are MISSING and will draw raw in game: {}",
				missing.toString().split(", ").length, checked, missing);
		}
	}

	@NotNull
	@Override
	public AchievementPage.AchievementEntry onOpenAchievement() {
		return Objects.requireNonNull(this.getEntry(BOPAchievements.FLOWER));
	}
}
