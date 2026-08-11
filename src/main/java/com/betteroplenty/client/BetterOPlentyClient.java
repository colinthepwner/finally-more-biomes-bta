package com.betteroplenty.client;

import biomesoplenty.entities.EntityBird;
import biomesoplenty.entities.projectiles.EntityDart;
import biomesoplenty.entities.projectiles.EntityMudball;
import biomesoplenty.entities.EntityGlob;
import biomesoplenty.entities.EntityPhantom;
import biomesoplenty.entities.EntityPixie;
import biomesoplenty.entities.EntityWasp;
import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.block.BOPBlocks;
import com.betteroplenty.block.BOPBones;
import com.betteroplenty.block.BOPCorals;
import com.betteroplenty.block.BOPFlowers;
import com.betteroplenty.block.BOPFormations;
import com.betteroplenty.block.BOPPromisedLand;
import com.betteroplenty.block.BOPGraves;
import com.betteroplenty.block.BOPHive;
import com.betteroplenty.block.BOPJungle;
import com.betteroplenty.block.BOPNether;
import com.betteroplenty.block.BOPOrchard;
import com.betteroplenty.block.BOPPlants;
import com.betteroplenty.block.BOPTerracotta;
import com.betteroplenty.block.BOPWastes;
import com.betteroplenty.block.BOPWoodSet;
import net.minecraft.client.render.EntityRendererDispatcher;
import net.minecraft.client.render.entity.EntityRendererSprite;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.client.render.colorizer.Colorizers;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelAxisAligned;
import net.minecraft.client.render.block.model.BlockModelCrossedSquares;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelFluid;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.client.render.block.model.generic.BlockModelGenericLeaves;
import net.minecraft.client.render.block.model.generic.BlockModelGenericShifted;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.item.Item;
import com.betteroplenty.fluid.BOPFluids;
import com.betteroplenty.fluid.ItemBucketAmethyst;
import com.betteroplenty.fluid.client.ItemModelBucketAmethyst;
import com.betteroplenty.item.BOPFlowerBands;
import com.betteroplenty.item.BOPItems;
import turniplabs.halplibe.util.ModelEntrypoint;

import java.util.List;

public class BetterOPlentyClient implements ModelEntrypoint {
	@Override
	public void initBlockModels(BlockModelDispatcher dispatcher) {

		BOPParticles.resolveTextures();

		BOPItemIcons.resolve();

		dispatcher.addDispatch(new BlockModelStandard<>(BOPBlocks.SPIKE_CUBE)
			.withTextures("betteroplenty:block/spike_cube"));

		dispatcher.addDispatch(new BlockModelCrossedSquares<>(BOPBlocks.SPIKE_PLANT)
			.withTextures("betteroplenty:block/spike_plant"));

		dispatcher.addDispatch(new SpikeCustomModel<>(BOPBlocks.SPIKE_CUSTOM)
			.withTextures("betteroplenty:block/spike_custom"));

		dispatcher.addDispatch(new BlockModelCrossedSquaresShifted<>(BOPBlocks.LAVENDER)
			.withTextures("betteroplenty:block/lavender"));

		dispatcher.addDispatch(new BlockModelCrossedSquaresShifted<>(BOPBlocks.WHEAT_GRASS)
			.withTextures("betteroplenty:block/wheatgrass"));

		dispatcher.addDispatch(new BlockModelStandard<>(BOPBlocks.AMETHYST_ORE)
			.withTextures("betteroplenty:block/ore/amethyst/stone"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPBlocks.AMETHYST_BLOCK)
			.withTextures("betteroplenty:block/amethystblock"));

		Object[][] gems = {
			{BOPBlocks.RUBY_ORE, "ore/ruby/stone"}, {BOPBlocks.RUBY_BLOCK, "rubyblock"},
			{BOPBlocks.PERIDOT_ORE, "ore/peridot/stone"}, {BOPBlocks.PERIDOT_BLOCK, "peridotblock"},
			{BOPBlocks.TOPAZ_ORE, "ore/topaz/stone"}, {BOPBlocks.TOPAZ_BLOCK, "topazblock"},
			{BOPBlocks.TANZANITE_ORE, "ore/tanzanite/stone"}, {BOPBlocks.TANZANITE_BLOCK, "tanzaniteblock"},
			{BOPBlocks.MALACHITE_ORE, "ore/malachite/stone"}, {BOPBlocks.MALACHITE_BLOCK, "malachiteblock"},
			{BOPBlocks.SAPPHIRE_ORE, "ore/sapphire/stone"}, {BOPBlocks.SAPPHIRE_BLOCK, "sapphireblock"},
		};
		for (Object[] gem : gems) {
			@SuppressWarnings("unchecked")
			Block<net.minecraft.core.block.BlockLogic> block =
				(Block<net.minecraft.core.block.BlockLogic>) gem[0];
			dispatcher.addDispatch(new BlockModelStandard<>(block)
				.withTextures("betteroplenty:block/" + gem[1]));
		}

		String[] oreGems = {"amethyst", "ruby", "peridot", "topaz", "tanzanite", "malachite", "sapphire"};
		String[] oreHosts = {"basalt", "granite", "limestone", "permafrost"};
		for (int gi = 0; gi < oreGems.length; gi++) {
			for (String host : oreHosts) {
				Block<?> variant = com.betteroplenty.block.BOPOreVariants.variant(oreGems[gi], host);
				if (variant == null) {
					continue;
				}
				@SuppressWarnings("unchecked")
				Block<net.minecraft.core.block.BlockLogic> v =
					(Block<net.minecraft.core.block.BlockLogic>) variant;
				dispatcher.addDispatch(new BlockModelStandard<>(v)
					.withTextures("betteroplenty:block/ore/" + oreGems[gi] + "/" + host));
			}
		}

		dispatcher.addDispatch(new BlockModelStandard<>(BOPBlocks.HARD_ICE)
			.withTextures("betteroplenty:block/hardice"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPBlocks.CRAG_ROCK)
			.withTextures("betteroplenty:block/cragrock"));

		dispatcher.addDispatch(new BlockModelStandard<>(BOPBlocks.QUICKSAND)
			.withTextures("betteroplenty:block/quicksand"));

		dispatcher.addDispatch(new BlockModelAxisAligned<>(BOPBones.SMALL)
			.withTextures("betteroplenty:block/bones_small"));
		dispatcher.addDispatch(new BlockModelAxisAligned<>(BOPBones.MEDIUM)
			.withTextures("betteroplenty:block/bones_medium"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPBones.LARGE)
			.withTextures("betteroplenty:block/bones_large"));

		dispatcher.addDispatch(new BlockModelGrave(BOPGraves.GRAVE)
			.setAllTextures("betteroplenty:block/grave"));
		dispatcher.addDispatch(new BlockModelGrave(BOPGraves.GRAVE_TOP)
			.setAllTextures("betteroplenty:block/grave"));

		dispatcher.addDispatch(new BlockModelStandard<>(BOPPromisedLand.HOLY_STONE)
			.withTextures("betteroplenty:block/holystone"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPPromisedLand.HOLY_COBBLE)
			.withTextures("betteroplenty:block/holycobble"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPPromisedLand.HOLY_BRICKS)
			.withTextures("betteroplenty:block/holybrick"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPPromisedLand.HOLY_STONE_MOSSY)
			.withTextures("betteroplenty:block/holystonemossy"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPPromisedLand.HOLY_DIRT)
			.withTextures("betteroplenty:block/holydirt"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPPromisedLand.CRYSTAL)
			.withTextures("betteroplenty:block/crystal"));

		dispatcher.addDispatch(new BlockModelStandard<>(BOPPromisedLand.CLOUD)
			.withTextures("betteroplenty:block/cloud").onRenderLayer(1));

		dispatcher.addDispatch(new BlockModelCrossedSquaresShifted<>(BOPPromisedLand.HOLY_TALL_GRASS)
			.withTextures("betteroplenty:block/holytallgrass"));

		dispatcher.addDispatch(new BlockModelStandard<>(BOPPromisedLand.HOLY_GRASS)
			.withTextures("betteroplenty:block/holygrass_top", "betteroplenty:block/holydirt",
				"betteroplenty:block/holygrass_side"));

		dispatcher.addDispatch(new BlockModelStandard<>(BOPPromisedLand.PORTAL_PROMISED)
			.withTextures("betteroplenty:block/portal").onRenderLayer(1));

		dispatcher.addDispatch(new BlockModelStandard<>(BOPJungle.MYCELIUM)
			.withTextures("betteroplenty:block/mycel_top", "minecraft:block/dirt",
				"betteroplenty:block/mycel_side"));

		dispatcher.addDispatch(new BlockModelBamboo<>(BOPJungle.BAMBOO)
			.withTextures("betteroplenty:block/bambootop", "betteroplenty:block/bamboo"));

		dispatcher.addDispatch(new BlockModelStandard<>(BOPJungle.THATCHING)
			.withTextures("betteroplenty:block/bamboothatching"));

		dispatcher.addDispatch(new BlockModelStandard<>(BOPBlocks.RED_ROCK)
			.withTextures("betteroplenty:block/redrock"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPBlocks.RED_COBBLE)
			.withTextures("betteroplenty:block/redcobble"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPBlocks.RED_BRICK)
			.withTextures("betteroplenty:block/redbrick"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPBlocks.HARD_SAND)
			.withTextures("betteroplenty:block/hardsand"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPBlocks.HARD_DIRT)
			.withTextures("betteroplenty:block/harddirt"));

		dispatcher.addDispatch(new BlockModelStandard<>(BOPTerracotta.HARDENED_CLAY)
			.withTextures("betteroplenty:block/hardened_clay"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPTerracotta.STAINED_CLAY_ORANGE)
			.withTextures("betteroplenty:block/hardened_clay_stained_orange"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPTerracotta.STAINED_CLAY_RED)
			.withTextures("betteroplenty:block/hardened_clay_stained_red"));

		dispatcher.addDispatch(new BlockModelStandard<>(BOPWastes.ASH)
			.withTextures("betteroplenty:block/ashblock"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPWastes.ASH_STONE)
			.withTextures("betteroplenty:block/ashstone"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPWastes.DRIED_DIRT)
			.withTextures("betteroplenty:block/drieddirt"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPWastes.SMOLDERING_GRASS)
			.withTextures("betteroplenty:block/smolderinggrass_top",
				"betteroplenty:block/smolderinggrass_bottom",
				"betteroplenty:block/smolderinggrass_side"));

		dispatcher.addDispatch(new BlockModelCrossedSquares<>(BOPOrchard.APPLE_SAPLING)
			.withTextures("betteroplenty:block/sapling_apple"));
		dispatcher.addDispatch(new BlockModelAppleLeaves<>(BOPOrchard.APPLE_LEAVES));

		dispatcher.addDispatch(new BlockModelStandard<>(BOPNether.OVERGROWN_NETHERRACK)
			.withTextures("betteroplenty:block/overgrownnetherrack1",
				"betteroplenty:block/overgrownnetherrack3",
				"betteroplenty:block/overgrownnetherrack2"));

		dispatcher.addDispatch(new BlockModelStandard<>(BOPNether.FLESH)
			.withTextures("betteroplenty:block/flesh"));

		dispatcher.addDispatch(new BlockModelGiantMushroom<>(BOPNether.MUSHROOM_CAP_BROWN,
			"betteroplenty:block/mushroom_skin_brown"));
		dispatcher.addDispatch(new BlockModelGiantMushroom<>(BOPNether.MUSHROOM_CAP_RED,
			"betteroplenty:block/mushroom_skin_red"));

		dispatcher.addDispatch(new BlockModelGiantMushroom<>(BOPNether.MUSHROOM_STEM,
			"betteroplenty:block/mushroom_skin_stem"));

		for (int i = 0; i < BOPNether.crossModels().size(); i++) {
			dispatcher.addDispatch(new BlockModelCrossedSquares<>(BOPNether.crossModels().get(i))
				.withTextures(BOPNether.crossTextures().get(i)));
		}
		bindCropModels(dispatcher, BOPNether.cropModels(), BOPNether.cropModelKeys());

		BetterOPlenty.LOGGER.info("Bound block models for {} spike blocks and {} BOP blocks.", 3, 28);

		for (BOPWoodSet set : BOPWoodSet.registered()) {

			if (set.log != null) {
				dispatcher.addDispatch(new BlockModelAxisAligned<>(set.log)
					.withTextures(set.logHeartTexture, set.logSideTexture));
			}

			dispatcher.addDispatch(new BlockModelGenericLeaves<>(set.leaves, set.leavesModelKey));

			dispatcher.addDispatch(new BlockModelCrossedSquares<>(set.sapling)
				.withTextures(set.saplingTexture));

		}

		BetterOPlenty.LOGGER.info("Bound block models for {} BOP wood set(s).", BOPWoodSet.registered().size());

		bindFlowerModels(dispatcher);
		bindGroundCoverModels(dispatcher);
		bindFluidModels(dispatcher);
		bindGardenModels(dispatcher);
	}

	private void bindGardenModels(BlockModelDispatcher dispatcher) {
		dispatcher.addDispatch(new BlockModelStandard<>(BOPBlocks.LONG_GRASS)
			.withTextures("betteroplenty:block/longgrass1", "betteroplenty:block/longgrass3",
				"betteroplenty:block/longgrass2"));

		dispatcher.addDispatch(new BlockModelAxisAligned<>(BOPBlocks.BIG_FLOWER_STEM)
			.withTextures("betteroplenty:block/bigflowerstem_heart",
				"betteroplenty:block/bigflowerstem_side"));

		dispatcher.addDispatch(new BlockModelStandard<>(BOPBlocks.BIG_FLOWER_RED)
			.withTextures("betteroplenty:block/bigflowerred"));
		dispatcher.addDispatch(new BlockModelStandard<>(BOPBlocks.BIG_FLOWER_YELLOW)
			.withTextures("betteroplenty:block/bigfloweryellow"));

		BetterOPlenty.LOGGER.info("Bound block models for {} Garden block(s).", 4);
	}

	private void bindGroundCoverModels(BlockModelDispatcher dispatcher) {
		List<Block<?>> blocks = BOPPlants.crossModels();
		List<String> textures = BOPPlants.crossTextures();

		for (int i = 0; i < blocks.size(); i++) {
			Block<?> block = blocks.get(i);
			String model = BOPPlants.crossModelKeys().get(i);

			if (block == BOPPlants.REED) {
				dispatcher.addDispatch(new BlockModelWaterReed<>(block)
					.withTextures(textures.get(i)));

			} else if (takesGrassOffset(block)) {
				dispatcher.addDispatch(new BlockModelGenericShifted<>(block,
					BlockModelDispatcher.loadDataModel(model)).render3D(false));
			} else {
				dispatcher.addDispatch(new BlockModelGeneric<>(block,
					BlockModelDispatcher.loadDataModel(model)).render3D(false));
			}
		}

		bindCropModels(dispatcher, BOPPlants.cropModels(), BOPPlants.cropModelKeys());

		dispatcher.addDispatch(new BlockModelCropStages<>(
			com.betteroplenty.block.BOPCrops.TURNIP_CROP,
			com.betteroplenty.block.BOPCrops.turnipStageModels()));

		List<Block<?>> sheets = BOPPlants.sheetModels();

		List<String> sheetTextures = BOPPlants.sheetTextures();

		for (int i = 0; i < sheets.size(); i++) {

			if (sheets.get(i) == BOPPlants.CLOVER_PATCH) {
				dispatcher.addDispatch(carpet(sheets.get(i), sheetTextures.get(i)));
			} else {
				dispatcher.addDispatch(new BlockModelStandard<>(sheets.get(i))
					.withTextures(sheetTextures.get(i)));
			}
		}

		BetterOPlenty.LOGGER.info(
			"Bound block models for {} BOP ground-cover block(s), including {} face-hugging sheets "
				+ "(2 mosses, the willow drape and ivy).",
			blocks.size() + sheets.size(), sheets.size());

		bindCoralModels(dispatcher);
	}

	private static boolean shearsUnderPositionOffset(Block<?> block) {
		return block == BOPFlowers.SUNFLOWER
			|| block == BOPFlowers.SUNFLOWER_TOP
			|| block == BOPPlants.HIGH_GRASS
			|| block == BOPPlants.HIGH_GRASS_TOP
			|| block == BOPPlants.CATTAIL_BOTTOM
			|| block == BOPPlants.CATTAIL_TOP
			|| block == BOPCorals.KELP_BOTTOM
			|| block == BOPCorals.KELP_MIDDLE
			|| block == BOPCorals.KELP_TOP;
	}

	private static boolean takesGrassOffset(Block<?> block) {
		if (shearsUnderPositionOffset(block)) {
			return false;
		}
		return block == BOPPlants.SHORT_GRASS
			|| block == BOPPlants.MEDIUM_GRASS
			|| block == BOPPlants.DEAD_GRASS
			|| block == BOPPlants.DESERT_GRASS
			|| block == BOPPlants.DESERT_SPROUTS
			|| block == BOPPlants.DUNE_GRASS
			|| block == BOPPlants.SPROUT;
	}

	private void bindCropModels(BlockModelDispatcher dispatcher, List<Block<?>> blocks,
			List<String> modelKeys) {
		for (int i = 0; i < blocks.size(); i++) {

			boolean jittered = blocks.get(i) == BOPPlants.BARLEY;
			dispatcher.addDispatch(new BlockModelCrop<>(blocks.get(i), modelKeys.get(i), jittered));
		}

		if (!blocks.isEmpty()) {
			BetterOPlenty.LOGGER.info("Bound crop models for {} BOP plant(s).", blocks.size());
		}
	}

	private void bindCoralModels(BlockModelDispatcher dispatcher) {
		List<Block<?>> blocks = BOPCorals.crossModels();
		List<String> textures = BOPCorals.crossTextures();

		for (int i = 0; i < blocks.size(); i++) {
			dispatcher.addDispatch(new BlockModelCrossedSquares<>(blocks.get(i))
				.withTextures(textures.get(i)));
		}

		BetterOPlenty.LOGGER.info("Bound block models for {} BOP coral and kelp block(s).", blocks.size());

		bindFormationModels(dispatcher);
		bindHiveModels(dispatcher);
	}

	private void bindHiveModels(BlockModelDispatcher dispatcher) {
		List<Block<?>> blocks = BOPHive.cubeModels();
		List<String> textures = BOPHive.cubeTextures();

		for (int i = 0; i < blocks.size(); i++) {
			Block<?> block = blocks.get(i);
			if (block == BOPHive.HONEY_BLOCK) {
				dispatcher.addDispatch(new BlockModelStandard<>(block)
					.withTextures(textures.get(i)).onRenderLayer(1));
			} else {
				dispatcher.addDispatch(new BlockModelStandard<>(block)
					.withTextures(textures.get(i)));
			}
		}

		BetterOPlenty.LOGGER.info(
			"Bound block models for {} BOP hive block(s), the honey block on the alpha layer.",
			blocks.size());
	}

	private void bindFormationModels(BlockModelDispatcher dispatcher) {
		List<Block<?>> blocks = BOPFormations.crossModels();
		List<String> textures = BOPFormations.crossTextures();

		for (int i = 0; i < blocks.size(); i++) {
			dispatcher.addDispatch(new BlockModelCrossedSquares<>(blocks.get(i))
				.withTextures(textures.get(i)));
		}

		BetterOPlenty.LOGGER.info("Bound block models for {} BOP stone-formation block(s).", blocks.size());
	}

	private void bindFlowerModels(BlockModelDispatcher dispatcher) {
		for (BOPFlowers.Member member : BOPFlowers.registered()) {

			if (member.block() == BOPFlowers.CLOVER) {
				dispatcher.addDispatch(carpet(member.block(), member.textureKey()));

			} else if (member.block() == BOPFlowers.LILY_FLOWER) {
				dispatcher.addDispatch(new BlockModelHangingCross<>(member.block(),
						BOPFlowers.LILY_FLOWER_DROP)
					.withTextures(member.textureKey()));

			} else if (shearsUnderPositionOffset(member.block())) {
				dispatcher.addDispatch(new BlockModelCrossedSquares<>(member.block())
					.withTextures(member.textureKey()));
			} else {

				dispatcher.addDispatch(new BlockModelCrossedSquaresShifted<>(member.block())
					.withTextures(member.textureKey()));
			}
		}

		BetterOPlenty.LOGGER.info(
			"Bound block models for {} BOP flower(s) and mushroom(s); clover is a flat mat and the "
				+ "lily flower hangs onto its pad.", BOPFlowers.registered().size());
	}

	private static <T extends net.minecraft.core.block.BlockLogic> BlockModelStandard<T> carpet(
			Block<T> block, String texture) {
		return new BlockModelCarpet<>(block).withTextures(texture);
	}

	private void bindFluidModels(BlockModelDispatcher dispatcher) {
		String poisonStill = "betteroplenty:block/fluid/liquid_poison/still";
		String poisonFlowing = "betteroplenty:block/fluid/liquid_poison/flowing";
		dispatcher.addDispatch(new BlockModelFluid<>(BOPFluids.LIQUID_POISON_STILL, poisonStill, poisonFlowing)
			.onRenderLayer(1));
		dispatcher.addDispatch(new BlockModelFluid<>(BOPFluids.LIQUID_POISON_FLOWING, poisonStill, poisonFlowing)
			.onRenderLayer(1));

		String springStill = "betteroplenty:block/fluid/spring_water/still";
		String springFlowing = "betteroplenty:block/fluid/spring_water/flowing";
		dispatcher.addDispatch(new BlockModelFluid<>(BOPFluids.SPRING_WATER_STILL, springStill, springFlowing)
			.onRenderLayer(1));
		dispatcher.addDispatch(new BlockModelFluid<>(BOPFluids.SPRING_WATER_FLOWING, springStill, springFlowing)
			.onRenderLayer(1));

		String honeyStill = "betteroplenty:block/fluid/honey/still";
		String honeyFlowing = "betteroplenty:block/fluid/honey/flowing";
		dispatcher.addDispatch(new BlockModelFluid<>(BOPFluids.HONEY_STILL, honeyStill, honeyFlowing)
			.onRenderLayer(1));
		dispatcher.addDispatch(new BlockModelFluid<>(BOPFluids.HONEY_FLOWING, honeyStill, honeyFlowing)
			.onRenderLayer(1));

		BetterOPlenty.LOGGER.info("Bound block models for {} BOP fluid(s) ({} blocks, alpha layer).", 3, 6);
	}

	@Override
	public void initItemModels(ItemModelDispatcher dispatcher) {
		dispatcher.addDispatch(new ItemModelStandard(BOPItems.AMETHYST));

		for (Item food : new Item[]{
			com.betteroplenty.item.BOPFoods.BERRIES, com.betteroplenty.item.BOPFoods.SHROOM_POWDER,
			com.betteroplenty.item.BOPFoods.WILD_CARROTS, com.betteroplenty.item.BOPFoods.SUNFLOWER_SEEDS,
			com.betteroplenty.item.BOPFoods.SALAD_FRUIT, com.betteroplenty.item.BOPFoods.SALAD_VEGGIE,
			com.betteroplenty.item.BOPFoods.SALAD_SHROOM, com.betteroplenty.item.BOPFoods.EARTH,
			com.betteroplenty.item.BOPFoods.PERSIMMON, com.betteroplenty.item.BOPFoods.FILLED_HONEYCOMB,
			com.betteroplenty.item.BOPFoods.AMBROSIA, com.betteroplenty.item.BOPFoods.TURNIP,
		}) {
			dispatcher.addDispatch(new ItemModelStandard(food));
		}

		dispatcher.addDispatch(new ItemModelStandard(BOPItems.TURNIP_SEEDS));

		for (Item gem : new Item[]{
			BOPItems.RUBY, BOPItems.PERIDOT, BOPItems.TOPAZ,
			BOPItems.TANZANITE, BOPItems.MALACHITE, BOPItems.SAPPHIRE,
			BOPItems.GEM_STAR
		}) {
			dispatcher.addDispatch(new ItemModelStandard(gem));
		}

		for (Item part : new Item[]{
			BOPItems.ANCIENT_STAFF_HANDLE, BOPItems.ANCIENT_STAFF_POLE,
			BOPItems.ANCIENT_STAFF_TOPPER, BOPItems.ANCIENT_STAFF_DEPLETED
		}) {
			dispatcher.addDispatch(new ItemModelStandard(part));
		}
		dispatcher.addDispatch(new ItemModelStandard(BOPItems.ANCIENT_STAFF)
			.setDisplayPos("firstperson_righthand", ItemModelDispatcher.HANDHELD_FIRST_PERSON_RIGHT_HAND)
			.setDisplayPos("firstperson_lefthand", ItemModelDispatcher.HANDHELD_FIRST_PERSON_LEFT_HAND)
			.setDisplayPos("thirdperson_righthand", ItemModelDispatcher.HANDHELD_THIRD_PERSON_RIGHT_HAND)
			.setDisplayPos("thirdperson_lefthand", ItemModelDispatcher.HANDHELD_THIRD_PERSON_LEFT_HAND));

		for (Item tool : new Item[]{
			BOPItems.AMETHYST_PICKAXE, BOPItems.AMETHYST_SHOVEL, BOPItems.AMETHYST_AXE,
			BOPItems.AMETHYST_SWORD, BOPItems.AMETHYST_HOE,
			BOPItems.MUD_PICKAXE, BOPItems.MUD_SHOVEL, BOPItems.MUD_AXE,
			BOPItems.MUD_SWORD, BOPItems.MUD_HOE
		}) {
			dispatcher.addDispatch(new ItemModelStandard(tool)
				.setDisplayPos("firstperson_righthand", ItemModelDispatcher.HANDHELD_FIRST_PERSON_RIGHT_HAND)
				.setDisplayPos("firstperson_lefthand", ItemModelDispatcher.HANDHELD_FIRST_PERSON_LEFT_HAND)
				.setDisplayPos("thirdperson_righthand", ItemModelDispatcher.HANDHELD_THIRD_PERSON_RIGHT_HAND)
				.setDisplayPos("thirdperson_lefthand", ItemModelDispatcher.HANDHELD_THIRD_PERSON_LEFT_HAND));
		}

		for (Item armor : new Item[]{
			BOPItems.AMETHYST_HELMET, BOPItems.AMETHYST_CHESTPLATE,
			BOPItems.AMETHYST_LEGGINGS, BOPItems.AMETHYST_BOOTS,
			BOPItems.MUD_HELMET, BOPItems.MUD_CHESTPLATE,
			BOPItems.MUD_LEGGINGS, BOPItems.MUD_BOOTS,

			BOPFlowerBands.DULL, BOPFlowerBands.PLAIN,
			BOPFlowerBands.LUSH, BOPFlowerBands.EXOTIC,

			BOPItems.WADING_BOOTS, BOPItems.FLIPPERS
		}) {
			dispatcher.addDispatch(new ItemModelStandard(armor));
		}

		dispatcher.addDispatch(new ItemModelStandard(BOPItems.MUDBALL));

		dispatcher.addDispatch(new ItemModelStandard(BOPItems.BOP_RECORD));
		dispatcher.addDispatch(new ItemModelStandard(BOPItems.BOP_RECORD_MUD));
		dispatcher.addDispatch(new ItemModelStandard(BOPItems.DART));
		dispatcher.addDispatch(new ItemModelStandard(BOPItems.DART_BLOWER)
			.setDisplayPos("firstperson_righthand", ItemModelDispatcher.HANDHELD_FIRST_PERSON_RIGHT_HAND)
			.setDisplayPos("firstperson_lefthand", ItemModelDispatcher.HANDHELD_FIRST_PERSON_LEFT_HAND)
			.setDisplayPos("thirdperson_righthand", ItemModelDispatcher.HANDHELD_THIRD_PERSON_RIGHT_HAND)
			.setDisplayPos("thirdperson_lefthand", ItemModelDispatcher.HANDHELD_THIRD_PERSON_LEFT_HAND));

		for (Item scythe : new Item[]{
			BOPItems.WOOD_SCYTHE, BOPItems.STONE_SCYTHE, BOPItems.IRON_SCYTHE, BOPItems.GOLD_SCYTHE,
			BOPItems.DIAMOND_SCYTHE, BOPItems.MUD_SCYTHE, BOPItems.AMETHYST_SCYTHE,
			BOPItems.STEEL_SCYTHE
		}) {
			dispatcher.addDispatch(new ItemModelStandard(scythe)
				.setDisplayPos("firstperson_righthand", ItemModelDispatcher.HANDHELD_FIRST_PERSON_RIGHT_HAND)
				.setDisplayPos("firstperson_lefthand", ItemModelDispatcher.HANDHELD_FIRST_PERSON_LEFT_HAND)
				.setDisplayPos("thirdperson_righthand", ItemModelDispatcher.HANDHELD_THIRD_PERSON_RIGHT_HAND)
				.setDisplayPos("thirdperson_lefthand", ItemModelDispatcher.HANDHELD_THIRD_PERSON_LEFT_HAND));
		}

		for (Item misc : new Item[]{
			BOPItems.MUD_BRICK, BOPItems.ASH, BOPItems.EMPTY_HONEYCOMB, BOPItems.FLESH_CHUNK,
			BOPItems.CRYSTAL_SHARD, BOPItems.GHASTLY_SOUL, BOPItems.PIXIE_DUST
		}) {
			dispatcher.addDispatch(new ItemModelStandard(misc));
		}

		for (Item jar : new Item[]{
			BOPItems.JAR_HONEY, BOPItems.JAR_POISON, BOPItems.JAR_PIXIE
		}) {
			dispatcher.addDispatch(new ItemModelStandard(jar));
		}

		dispatcher.addDispatch(new ItemModelBucketAmethyst(
			BOPFluids.BUCKET_AMETHYST, BetterOPlenty.MOD_ID, ItemBucketAmethyst.MAX_CHARGES));

		BetterOPlenty.LOGGER.info("Bound item models for {} BOP items.", 65);
	}

	@Override
	public void initEntityModels(EntityRendererDispatcher dispatcher) {

		dispatcher.assignRenderer(EntityMudball.class, new EntityRendererSprite<>(BOPItems.MUDBALL));
		dispatcher.assignRenderer(EntityDart.class, new EntityRendererSprite<>(BOPItems.DART));

		dispatcher.assignRenderer(EntityBird.class, new MobRendererBird(0.0F));
		dispatcher.assignRenderer(EntityWasp.class, new MobRendererWasp(0.0F));

		dispatcher.assignRenderer(EntityPixie.class, new MobRendererPixie(0.0F));

		dispatcher.assignRenderer(EntityGlob.class, new MobRendererGlob(0.25F));

		dispatcher.assignRenderer(EntityPhantom.class, new MobRendererPhantom(0.0F));
	}

	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
	}

	@Override
	public void initBlockColors(BlockColorDispatcher dispatcher) {

		BlockColorBOP.BiomeTint grass = BiomeGenBase::getBiomeGrassColor;
		BlockColorBOP.BiomeTint foliage = BiomeGenBase::getBiomeFoliageColor;

		for (Block<?> block : new Block<?>[]{
			Blocks.GRASS, Blocks.TALLGRASS, Blocks.TALLGRASS_FERN, Blocks.ALGAE,
			Blocks.MOSS_STONE, Blocks.MOSS_BASALT, Blocks.MOSS_LIMESTONE, Blocks.MOSS_GRANITE
		}) {
			dispatcher.addDispatch(block, new BlockColorBOP(Colorizers.grass, grass));
		}

		for (Block<?> block : BOPPlants.grassTinted()) {
			dispatcher.addDispatch(block, new BlockColorBOP(Colorizers.grass, grass));
		}
		for (Block<?> block : BOPPlants.foliageTinted()) {
			dispatcher.addDispatch(block, new BlockColorBOP(Colorizers.oak, foliage));
		}

		dispatcher.addDispatch(BOPBlocks.WHEAT_GRASS, new BlockColorBOP(Colorizers.grass, grass));

		dispatcher.addDispatch(Blocks.LEAVES_OAK, new BlockColorBOP(Colorizers.oak, foliage));
		dispatcher.addDispatch(Blocks.LAYER_LEAVES_OAK, new BlockColorBOP(Colorizers.oak, foliage));
		dispatcher.addDispatch(Blocks.LEAVES_BIRCH, new BlockColorBOP(Colorizers.birch, foliage));
		dispatcher.addDispatch(Blocks.LEAVES_PINE, new BlockColorBOP(Colorizers.pine, foliage));

		dispatcher.addDispatch(Blocks.LEAVES_EUCALYPTUS, new BlockColorBOP(Colorizers.eucalyptus, foliage));
		dispatcher.addDispatch(Blocks.LEAVES_CACAO, new BlockColorBOP(Colorizers.cacao, foliage));
		dispatcher.addDispatch(Blocks.LEAVES_SHRUB, new BlockColorBOP(Colorizers.shrub, foliage));
		dispatcher.addDispatch(Blocks.LEAVES_THORN, new BlockColorBOP(Colorizers.thorn, foliage));
		dispatcher.addDispatch(Blocks.LEAVES_PALM, new BlockColorBOP(Colorizers.palm, foliage));

		int tinted = 0;
		for (BOPWoodSet set : BOPWoodSet.registered()) {
			if (set.colorizedLeaves) {
				dispatcher.addDispatch(set.leaves, new BlockColorBOP(Colorizers.oak, foliage));
				tinted++;
			}
		}

		dispatcher.addDispatch(Blocks.FLUID_WATER_STILL, new BlockColorWaterBOP());
		dispatcher.addDispatch(Blocks.FLUID_WATER_FLOWING, new BlockColorWaterBOP());

		BetterOPlenty.LOGGER.info(
			"Bound BOP biome tints (season-aware) for grass, foliage, water and {} colorized wood set(s).",
			tinted);
	}
}
