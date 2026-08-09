package com.betteroplenty.block;

import com.betteroplenty.BOPIdManifest;
import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.BlockSounds;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public final class BOPNether {
	private BOPNether() {}

	public static Block<BlockLogic> OVERGROWN_NETHERRACK;

	public static Block<BlockLogicBOPGroundCover> WITHER_WART;

	public static Block<BlockLogicFlesh> FLESH;

	public static Block<BlockLogicGiantMushroom> MUSHROOM_CAP_BROWN;

	public static Block<BlockLogicGiantMushroom> MUSHROOM_CAP_RED;

	public static Block<BlockLogicGiantMushroom> MUSHROOM_STEM;

	private static final List<Block<?>> CROSS_MODELS = new ArrayList<>();
	private static final List<String> CROSS_TEXTURES = new ArrayList<>();

	private static final List<Block<?>> CROP_MODELS = new ArrayList<>();
	private static final List<String> CROP_MODEL_KEYS = new ArrayList<>();

	@NotNull public static List<Block<?>> crossModels() { return CROSS_MODELS; }
	@NotNull public static List<String> crossTextures() { return CROSS_TEXTURES; }
	@NotNull public static List<Block<?>> cropModels() { return CROP_MODELS; }
	@NotNull public static List<String> cropModelKeys() { return CROP_MODEL_KEYS; }

	public static void register() {
		BlockBuilder builder = new BlockBuilder(BetterOPlenty.MOD_ID);

		OVERGROWN_NETHERRACK = builder.clone()
			.setHardness(0.4f)
			.setBlockSound(BlockSounds.GRASS)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE, BlockTags.GROWS_FLOWERS, BlockTags.GROWS_TREES)
			.setCreativeInventoryPlacement(after(() -> Blocks.NETHERRACK))
			.build("overgrown_netherrack", 2160, block -> new BlockLogic(block, net.minecraft.core.block.material.Materials.NETHERRACK) {

				@Override
				public ItemStack[] getBreakResult(@NotNull net.minecraft.core.world.World world,
												  @NotNull net.minecraft.core.enums.EnumDropCause dropCause,
												  int data,
												  net.minecraft.core.block.entity.TileEntity tileEntity) {
					return switch (dropCause) {
						case SILK_TOUCH, PICK_BLOCK -> new ItemStack[]{new ItemStack(this.block)};
						case IMPROPER_TOOL -> null;
						default -> new ItemStack[]{new ItemStack(Blocks.NETHERRACK)};
					};
				}
			});

		BlockBuilder plant = builder.clone()
			.setHardness(0.0f)
			.setBlockSound(BlockSounds.GRASS)
			.setTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLACE_OVERWRITES,
				BlockTags.SHEARS_DO_SILK_TOUCH, BlockTags.MINEABLE_BY_SHEARS);

		WITHER_WART = plant.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.MUSHROOM_RED))
			.build("wither_wart", 2161, block -> new BlockLogicBOPGroundCover(block)
				.withSoil(soil -> soil == Blocks.SOULSAND)
				.withoutLight());

		CROP_MODELS.add(WITHER_WART);
		CROP_MODEL_KEYS.add("betteroplenty:block/crops/wither_wart");

		FLESH = builder.clone()
			.setHardness(0.4f)
			.setBlockSound(BlockSounds.GRAVEL)
			.setTags(BlockTags.MINEABLE_BY_SHOVEL)
			.setCreativeInventoryPlacement(after(() -> Blocks.SPONGE_DRY))
			.build("flesh", 2162, BlockLogicFlesh::new);

		BlockBuilder cap = builder.clone()
			.setHardness(0.2f)
			.setBlockSound(BlockSounds.WOOD)
			.setTags(BlockTags.MINEABLE_BY_AXE);

		MUSHROOM_CAP_BROWN = cap.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.MUSHROOM_BROWN))
			.build("mushroom_cap_brown", 2163,
				block -> new BlockLogicGiantMushroom(block, () -> Blocks.MUSHROOM_BROWN));

		MUSHROOM_CAP_RED = cap.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.MUSHROOM_RED))
			.build("mushroom_cap_red", 2164,
				block -> new BlockLogicGiantMushroom(block, () -> Blocks.MUSHROOM_RED));

		MUSHROOM_STEM = cap.clone()
			.setCreativeInventoryPlacement(after(() -> Blocks.MUSHROOM_BROWN))
			.build("mushroom_stem", 2165,
				block -> new BlockLogicGiantMushroom(block, () -> Blocks.MUSHROOM_BROWN));

		BetterOPlenty.LOGGER.info("Registered {} BOP Nether blocks (ids {}): the Undergarden's "
			+ "overgrown netherrack, giant mushroom caps and stem, Bloody Heap's flesh, and the "
			+ "wither wart.", 6,
			BOPIdManifest.span(OVERGROWN_NETHERRACK.id(), WITHER_WART.id(), FLESH.id(),
				MUSHROOM_CAP_BROWN.id(), MUSHROOM_CAP_RED.id(), MUSHROOM_STEM.id()));
	}

	@NotNull
	private static CreativeInventoryPlacement after(
			@NotNull Supplier<net.minecraft.core.item.IItemConvertible> neighbour) {
		return new CreativeInventoryPlacement.After(neighbour);
	}

	@NotNull
	public static List<Block<?>> all() {
		return Arrays.asList(OVERGROWN_NETHERRACK, WITHER_WART, FLESH,
			MUSHROOM_CAP_BROWN, MUSHROOM_CAP_RED, MUSHROOM_STEM);
	}
}
