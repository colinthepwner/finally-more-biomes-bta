package com.betteroplenty.block;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.world.promised.DimensionPromisedLand;
import net.minecraft.core.block.BlockLogicPortal;
import net.minecraft.core.block.BlockLogicTransparent;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSounds;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

public final class BOPPromisedLand {
	private BOPPromisedLand() {}

	public static Block<BlockLogic> HOLY_STONE;

	public static Block<BlockLogic> HOLY_COBBLE;

	public static Block<BlockLogic> HOLY_BRICKS;

	public static Block<BlockLogic> HOLY_STONE_MOSSY;

	public static Block<BlockLogic> HOLY_GRASS;

	public static Block<BlockLogic> HOLY_DIRT;

	public static Block<BlockLogicBOPGroundCover> HOLY_TALL_GRASS;

	public static Block<BlockLogic> CRYSTAL;

	public static Block<BlockLogicCloud> CLOUD;

	public static Block<BlockLogicPortal> PORTAL_PROMISED;

	public static void register() {
		BlockBuilder builder = new BlockBuilder(BetterOPlenty.MOD_ID);

		BlockBuilder stone = builder.clone()
			.setHardness(1.5f)
			.setResistance(10.0f)
			.setBlockSound(BlockSounds.STONE)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE, BlockTags.CAVES_CUT_THROUGH);

		HOLY_STONE = stone.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.STONE))
			.build("holy_stone", 2200, block -> new BlockLogic(block, Materials.STONE));

		HOLY_COBBLE = stone.clone()
			.setHardness(1.6f)
			.setCreativeInventoryPlacement(after(() -> Blocks.COBBLE_STONE))
			.build("holy_cobble", 2201, block -> new BlockLogic(block, Materials.STONE));

		HOLY_BRICKS = stone.clone()
			.setHardness(1.1f)
			.setCreativeInventoryPlacement(after(() -> Blocks.BRICK_STONE))
			.build("holy_bricks", 2202, block -> new BlockLogic(block, Materials.STONE));

		HOLY_STONE_MOSSY = stone.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.COBBLE_STONE_MOSSY))
			.build("holy_stone_mossy", 2203, block -> new BlockLogic(block, Materials.STONE));

		HOLY_DIRT = builder.clone()
			.setHardness(0.6f)
			.setBlockSound(BlockSounds.GRAVEL)
			.setTags(BlockTags.MINEABLE_BY_SHOVEL, BlockTags.CAVES_CUT_THROUGH)
			.setCreativeInventoryPlacement(after(() -> Blocks.DIRT))
			.build("holy_dirt", 2205, block -> new BlockLogic(block, Materials.SAND));

		HOLY_GRASS = builder.clone()
			.setHardness(0.6f)
			.setBlockSound(BlockSounds.GRASS)
			.setTags(BlockTags.MINEABLE_BY_SHOVEL, BlockTags.MINEABLE_BY_PICKAXE,
				BlockTags.PASSIVE_MOBS_SPAWN, BlockTags.CAVES_CUT_THROUGH,
				BlockTags.CAVE_GEN_REPLACES_SURFACE, BlockTags.GROWS_TREES, BlockTags.GROWS_FLOWERS)
			.setCreativeInventoryPlacement(after(() -> Blocks.GRASS))
			.build("holy_grass", 2204,
				block -> new BlockLogicHolyGrass(block, HOLY_DIRT, () -> HOLY_TALL_GRASS));

		HOLY_TALL_GRASS = builder.clone()
			.setBlockSound(BlockSounds.GRASS)
			.setTags(BlockTags.NOT_IN_CREATIVE_MENU, BlockTags.BROKEN_BY_FLUIDS)
			.build("holy_tall_grass", 2207,
				block -> new BlockLogicBOPGroundCover(block)
					.withSoil(soil -> soil == HOLY_GRASS || soil == HOLY_DIRT));

		CRYSTAL = builder.clone()
			.setHardness(0.15f)
			.setResistance(5.0f)
			.setLuminance(15)
			.setBlockSound(BlockSounds.GLASS)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.setCreativeInventoryPlacement(after(() -> Blocks.GLOWSTONE))
			.build("crystal", 2208, block -> new BlockLogic(block, Materials.GLASS) {

				@Override
				public net.minecraft.core.item.ItemStack[] getBreakResult(
					@NotNull net.minecraft.core.world.World world,
					@NotNull net.minecraft.core.enums.EnumDropCause dropCause,
					int data,
					net.minecraft.core.block.entity.TileEntity tileEntity) {
					return switch (dropCause) {
						case SILK_TOUCH, PICK_BLOCK ->
							new net.minecraft.core.item.ItemStack[]{
								new net.minecraft.core.item.ItemStack(this.block)};
						default ->
							new net.minecraft.core.item.ItemStack[]{
								new net.minecraft.core.item.ItemStack(
									com.betteroplenty.item.BOPItems.CRYSTAL_SHARD, 4)};
					};
				}
			});

		CLOUD = builder.clone()
			.setHardness(0.1f)
			.setLightOpacity(3)
			.setBlockSound(BlockSounds.CLOTH)
			.setTags(BlockTags.MINEABLE_BY_SHOVEL)
			.setCreativeInventoryPlacement(after(() -> Blocks.SPONGE_DRY))

			.build("cloud", 2209, BlockLogicCloud::new);

		BetterOPlenty.LOGGER.info(
			"Registered {} Promised Land blocks (holy stone x4, grass, dirt, tall grass, "
			+ "crystal, cloud).", 9);
	}

	public static void registerPortal() {
		PORTAL_PROMISED = new BlockBuilder(BetterOPlenty.MOD_ID)
			.setHardness(-1.0f)
			.setResistance(6000000.0f)
			.setLuminance(15)
			.setBlockSound(BlockSounds.GLASS)
			.setTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.NOT_IN_CREATIVE_MENU)
			.build("portal.promised", 2206,
				block -> new BlockLogicPortal(block, DimensionPromisedLand.PROMISED_LAND,
					Blocks.BLOCK_QUARTZ, Blocks.AIR));

		DimensionPromisedLand.attachPortalBlock(PORTAL_PROMISED);
		BetterOPlenty.LOGGER.info("Registered the Promised Land portal (frame: quartz).");
	}

	private static CreativeInventoryPlacement after(
			@NotNull java.util.function.Supplier<net.minecraft.core.item.IItemConvertible> neighbour) {
		return new CreativeInventoryPlacement.After(neighbour);
	}
}
