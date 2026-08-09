package com.betteroplenty.block;

import com.betteroplenty.BOPIdManifest;
import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSounds;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.util.Arrays;
import java.util.List;

public final class BOPBones {
	private BOPBones() {}

	public static Block<BlockLogicBOPBones> SMALL;

	public static Block<BlockLogicBOPBones> MEDIUM;

	public static Block<BlockLogic> LARGE;

	public static void register() {

		BlockBuilder bone = new BlockBuilder(BetterOPlenty.MOD_ID)
			.setHardness(3.0f)
			.setResistance(5.0f)
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.setCreativeInventoryPlacement(afterBonePile());

		SMALL = bone.clone().build("bones_small", 2120,
			block -> new BlockLogicBOPBones(block, BlockLogicBOPBones.SMALL_INSET));

		MEDIUM = bone.clone().build("bones_medium", 2121,
			block -> new BlockLogicBOPBones(block, BlockLogicBOPBones.MEDIUM_INSET));

		LARGE = bone.clone().build("bones_large", 2122,
			block -> new BlockLogic(block, Materials.STONE));

		BetterOPlenty.LOGGER.info(
			"Registered {} BOP bone blocks (ids {}); WorldGenBoneSpine builds them into "
				+ "Boneyard's spines.", all().size(),
			BOPIdManifest.span(SMALL.id(), MEDIUM.id(), LARGE.id()));
	}

	@NotNull
	private static CreativeInventoryPlacement afterBonePile() {
		return new CreativeInventoryPlacement.After(() -> Blocks.BONE_PILE);
	}

	@NotNull
	public static List<Block<?>> all() {
		return Arrays.asList(SMALL, MEDIUM, LARGE);
	}
}
