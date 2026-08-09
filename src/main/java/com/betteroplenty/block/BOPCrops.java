package com.betteroplenty.block;

import com.betteroplenty.BOPIdManifest;
import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSounds;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.BlockBuilder;

public final class BOPCrops {

	private BOPCrops() {
	}

	public static Block<BlockLogicTurnip> TURNIP_CROP;

	public static void register() {

		TURNIP_CROP = new BlockBuilder(BetterOPlenty.MOD_ID)
			.setHardness(0.0f)
			.setBlockSound(BlockSounds.GRASS)
			.setTags(BlockTags.NOT_IN_CREATIVE_MENU, BlockTags.BROKEN_BY_FLUIDS)

			.build("turnip_crop", 2210, BlockLogicTurnip::new);

		BetterOPlenty.LOGGER.info("Registered the turnip crop (id {}).", TURNIP_CROP.id());
	}

	@NotNull
	public static String[] turnipStageModels() {
		String p = "betteroplenty:block/crops/turnip/stage";
		return new String[]{p + "0", p + "0", p + "1", p + "1", p + "2", p + "2", p + "2", p + "3"};
	}
}
