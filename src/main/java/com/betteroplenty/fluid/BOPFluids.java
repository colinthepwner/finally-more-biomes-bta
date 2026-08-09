package com.betteroplenty.fluid;

import biomesoplenty.fluids.FluidHoney;
import biomesoplenty.fluids.FluidLiquidPoison;
import biomesoplenty.fluids.FluidSpringWater;
import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.res.ObfResources;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFluid;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.material.MaterialColor;
import net.minecraft.core.block.material.MaterialLiquid;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemBucket;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public final class BOPFluids {
	private BOPFluids() {}

	public static final Material LIQUID_POISON_MATERIAL =
		new MaterialLiquid(MaterialColor.paintedPurple).setConductivity(5).destroyOnPush();

	public static final Material SPRING_WATER_MATERIAL =
		new MaterialLiquid(MaterialColor.water).setConductivity(5).destroyOnPush();

	public static final Material HONEY_MATERIAL =
		new MaterialLiquid(MaterialColor.paintedYellow).setConductivity(5);

	private static final int FIRST_BLOCK_ID = 1600;

	private static final int FIRST_HONEY_BLOCK_ID = 2105;

	public static Block<BlockLogicFluid> LIQUID_POISON_STILL;
	public static Block<BlockLogicFluid> LIQUID_POISON_FLOWING;
	public static Block<BlockLogicFluid> SPRING_WATER_STILL;
	public static Block<BlockLogicFluid> SPRING_WATER_FLOWING;
	public static Block<BlockLogicFluid> HONEY_STILL;
	public static Block<BlockLogicFluid> HONEY_FLOWING;

	private static final int BUCKET_ITEM_ID = 18100;

	public static Item BUCKET_AMETHYST;

	public static NamespaceID SPRING_WATER_STATE;

	public static NamespaceID LIQUID_POISON_STATE;

	public static NamespaceID HONEY_STATE;

	public static void register() {

		BlockBuilder fluid = new BlockBuilder(BetterOPlenty.MOD_ID)
			.setHardness(100.0f)
			.setUseInternalLight()
			.setLightOpacity(3)
			.setDisableStats()
			.setTags(BlockTags.PLACE_OVERWRITES, BlockTags.NOT_IN_CREATIVE_MENU);

		LIQUID_POISON_STILL = fluid.clone()
			.setStatParent(() -> LIQUID_POISON_FLOWING)
			.build("fluid_liquid_poison_still", FIRST_BLOCK_ID,
				block -> new BlockLogicBOPFluidStill(block, LIQUID_POISON_MATERIAL, new FluidLiquidPoison(),
					() -> LIQUID_POISON_FLOWING, BOPFluidContact.LIQUID_POISON));

		LIQUID_POISON_FLOWING = fluid.clone()
			.build("fluid_liquid_poison_flowing", FIRST_BLOCK_ID + 1,
				block -> new BlockLogicBOPFluidFlowing(block, LIQUID_POISON_MATERIAL, new FluidLiquidPoison(),
					LIQUID_POISON_STILL, BOPFluidContact.LIQUID_POISON));

		SPRING_WATER_STILL = fluid.clone()
			.setStatParent(() -> SPRING_WATER_FLOWING)
			.build("fluid_spring_water_still", FIRST_BLOCK_ID + 2,
				block -> new BlockLogicBOPFluidStill(block, SPRING_WATER_MATERIAL, new FluidSpringWater(),
					() -> SPRING_WATER_FLOWING, BOPFluidContact.SPRING_WATER));

		SPRING_WATER_FLOWING = fluid.clone()
			.build("fluid_spring_water_flowing", FIRST_BLOCK_ID + 3,
				block -> new BlockLogicBOPFluidFlowing(block, SPRING_WATER_MATERIAL, new FluidSpringWater(),
					SPRING_WATER_STILL, BOPFluidContact.SPRING_WATER));

		HONEY_STILL = fluid.clone()
			.setLightOpacity(1)
			.setStatParent(() -> HONEY_FLOWING)
			.build("fluid_honey_still", FIRST_HONEY_BLOCK_ID,
				block -> new BlockLogicBOPFluidStill(block, HONEY_MATERIAL, new FluidHoney(),
					() -> HONEY_FLOWING, BOPFluidContact.HONEY));

		HONEY_FLOWING = fluid.clone()
			.setLightOpacity(1)
			.build("fluid_honey_flowing", FIRST_HONEY_BLOCK_ID + 1,
				block -> new BlockLogicBOPFluidFlowing(block, HONEY_MATERIAL, new FluidHoney(),
					HONEY_STILL, BOPFluidContact.HONEY));

		registerBucketStates();

		BUCKET_AMETHYST = new ItemBuilder(BetterOPlenty.MOD_ID)
			.build(new ItemBucketAmethyst(
				"bucket_amethyst", "betteroplenty:item/bucket_amethyst", BUCKET_ITEM_ID));

		BetterOPlenty.LOGGER.info(
			"Registered {} BOP fluid(s) as {} blocks, {} bucket state(s) and 1 bucket tier ({} charges).",
			3, 6, 3, ItemBucketAmethyst.MAX_CHARGES);
	}

	private static void registerBucketStates() {
		SPRING_WATER_STATE = ItemBucket.registerState(
			NamespaceID.fromPool(BetterOPlenty.MOD_ID, "spring_water"),
			new ItemBucket.BucketState("spring_water", 4, 50, SPRING_WATER_FLOWING, null, "liquid.splash"));

		LIQUID_POISON_STATE = ItemBucket.registerState(
			NamespaceID.fromPool(BetterOPlenty.MOD_ID, "liquid_poison"),
			new ItemBucket.BucketState("liquid_poison", LIQUID_POISON_FLOWING, null, "liquid.splash"));

		HONEY_STATE = ItemBucket.registerState(
			NamespaceID.fromPool(BetterOPlenty.MOD_ID, "honey"),
			new ItemBucket.BucketState("honey", HONEY_FLOWING, null, "liquid.splash"));
	}

	public static List<ItemBucket> allBuckets() {
		List<ItemBucket> buckets = new ArrayList<>();
		for (Item item : Item.itemsList) {
			if (item instanceof ItemBucket bucket) {
				buckets.add(bucket);
			}
		}
		return buckets;
	}

	public static void audit() {
		List<String> problems = new ArrayList<>();
		int combinations = 0;

		try {
			for (ItemBucket bucket : allBuckets()) {
				String namespace = bucket.namespaceID.namespace();

				String value = bucket.namespaceID.value();
				String prefix = value.substring(value.indexOf('/') + 1);

				for (NamespaceID stateId : ItemBucket.getRegisteredStateIds()) {
					String stateName = ItemBucket.getStateName(stateId);
					boolean empty = ItemBucket.STATE_EMPTY.equals(stateId);

					if (empty) {
						combinations++;
						checkTexture(problems, namespace, prefix, "empty");
					} else {
						for (int charge = 1; charge <= bucket.maxCharges; charge++) {
							combinations++;
							checkTexture(problems, namespace, prefix, stateName + "_" + charge);
						}
					}

					checkLang(problems, empty ? bucket.getKey()
						: bucket.getKey() + "." + stateName.replace("_", ""));
				}

				for (int charge = 1; charge <= bucket.maxCharges; charge++) {
					combinations++;
					checkTexture(problems, namespace, prefix, "waterboiling_" + charge);
				}
			}
		} catch (Throwable t) {
			BetterOPlenty.LOGGER.warn("Fluid audit could not complete; this is the audit's own problem, "
				+ "not the mod's.", t);
			return;
		}

		if (problems.isEmpty()) {
			BetterOPlenty.LOGGER.info("Fluid audit: {} bucket/state combinations across {} bucket(s) and "
					+ "{} state(s), all with art, a name and a desc.",
				combinations, allBuckets().size(), ItemBucket.getRegisteredStateIds().size());
		} else {
			BetterOPlenty.LOGGER.warn("Fluid audit found {} problem(s) across {} bucket/state "
				+ "combinations:", problems.size(), combinations);
			for (String problem : problems) {
				BetterOPlenty.LOGGER.warn("  - {}", problem);
			}
		}
	}

	private static void checkTexture(List<String> problems, String namespace, String prefix, String name) {
		String path = "/assets/" + namespace + "/textures/item/" + prefix + "/" + name + ".png";
		if (!ObfResources.has(path) && BOPFluids.class.getResource(path) == null) {
			problems.add(path + " is missing -- that slot renders as a magenta square");
		}
	}

	private static void checkLang(List<String> problems, String key) {
		I18n i18n = I18n.getInstance();
		if (i18n == null) {
			return;
		}
		if (key.concat(".name").equals(i18n.translateKey(key + ".name"))) {
			problems.add("missing " + key + ".name");
		}
		if (key.concat(".desc").equals(i18n.translateKey(key + ".desc"))) {
			problems.add("missing " + key + ".desc (rule 2 -- the Ctrl tooltip)");
		}
	}
}
