package com.betteroplenty.block;

import biomesoplenty.worldgen.WorldGenNetherBush;
import biomesoplenty.worldgen.tree.WorldGenAcacia;
import biomesoplenty.worldgen.tree.WorldGenAutumn;
import biomesoplenty.worldgen.tree.WorldGenAutumn2;
import biomesoplenty.worldgen.tree.WorldGenBambooTree;
import biomesoplenty.worldgen.tree.WorldGenDeadTree2;
import biomesoplenty.worldgen.tree.WorldGenJacaranda;
import biomesoplenty.worldgen.tree.WorldGenMangrove;
import biomesoplenty.worldgen.tree.WorldGenMaple;
import biomesoplenty.worldgen.tree.WorldGenMystic2;
import biomesoplenty.worldgen.tree.WorldGenOminous1;
import biomesoplenty.worldgen.tree.WorldGenOminous2;
import biomesoplenty.worldgen.tree.WorldGenPersimmon;
import biomesoplenty.worldgen.tree.WorldGenPromisedTree;
import biomesoplenty.worldgen.tree.WorldGenRedwoodTree2;
import biomesoplenty.worldgen.tree.WorldGenTaiga9;
import biomesoplenty.worldgen.tree.WorldGenWillow;
import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.util.helper.DyeColor;

import java.util.ArrayList;
import java.util.List;

public final class BOPWoodSets {
	private BOPWoodSets() {}

	public static BOPWoodSet JACARANDA;

	public static BOPWoodSet ACACIA;

	public static BOPWoodSet DARK;

	public static BOPWoodSet DEAD;

	public static BOPWoodSet FIR;

	public static BOPWoodSet HELLBARK;

	public static BOPWoodSet HOLY;

	public static BOPWoodSet MAGIC;

	public static BOPWoodSet MANGROVE;

	public static BOPWoodSet REDWOOD;

	public static BOPWoodSet WILLOW;

	public static BOPWoodSet MAPLE;

	public static BOPWoodSet ORANGE_AUTUMN;

	public static BOPWoodSet YELLOW_AUTUMN;

	public static BOPWoodSet PERSIMMON;

	public static BOPWoodSet BAMBOO;

	public static void register() {
		JACARANDA = BOPWoodSet.species("jacaranda", 1500)

			.tree(random -> new WorldGenJacaranda(false))

			.planks(DyeColor.PINK)
			.register();

		ACACIA = BOPWoodSet.species("acacia", 1503)

			.tree(random -> new WorldGenAcacia(false))
			.colorizedLeaves()

			.planks(null)
			.register();

		DARK = BOPWoodSet.species("dark", 1506)

			.tree(random -> random.nextInt(8) == 0
				? new WorldGenOminous2()
				: new WorldGenOminous1(false))

			.planks(DyeColor.BROWN)
			.register();

		DEAD = BOPWoodSet.species("dead", 1509)

			.tree(random -> new WorldGenDeadTree2(false))

			.planks(DyeColor.BROWN)
			.register();

		FIR = BOPWoodSet.species("fir", 1512)

			.tree(random -> new WorldGenTaiga9(false))

			.planks(null)
			.register();

		HELLBARK = BOPWoodSet.species("hellbark", 1515)

			.tree(random -> new WorldGenNetherBush())

			.planks(DyeColor.ORANGE)
			.register();

		HOLY = BOPWoodSet.species("holy", 1518)

			.tree(random -> new WorldGenPromisedTree(false))

			.planks(DyeColor.ORANGE)
			.register();

		MAGIC = BOPWoodSet.species("magic", 1521)

			.tree(random -> new WorldGenMystic2(false))

			.leavesLogic((block, sapling) ->
				new BlockLogicLeavesMagic(block, Materials.LEAVES, sapling))

			.planks(DyeColor.BLUE)
			.register();

		MANGROVE = BOPWoodSet.species("mangrove", 1524)

			.tree(random -> new WorldGenMangrove(false))

			.growsOnSand()
			.colorizedLeaves()

			.planks(DyeColor.WHITE)
			.register();

		REDWOOD = BOPWoodSet.species("redwood", 1527)

			.tree(random -> new WorldGenRedwoodTree2(false))
			.colorizedLeaves()

			.planks(DyeColor.RED)
			.register();

		WILLOW = BOPWoodSet.species("willow", 1530)

			.tree(random -> new WorldGenWillow())
			.colorizedLeaves()

			.planks(DyeColor.GREEN)
			.register();

		MAPLE = BOPWoodSet.species("maple", 1533)

			.tree(random -> new WorldGenMaple(false))
			.noLog()
			.register();

		ORANGE_AUTUMN = BOPWoodSet.species("orangeautumn", 1536)

			.tree(random -> new WorldGenAutumn2(false))
			.noLog()
			.register();

		YELLOW_AUTUMN = BOPWoodSet.species("yellowautumn", 1539)

			.tree(random -> new WorldGenAutumn(false))
			.noLog()
			.register();

		PERSIMMON = BOPWoodSet.species("persimmon", 1930)

			.tree(random -> new WorldGenPersimmon(false))
			.noLog()
			.register();

		BAMBOO = BOPWoodSet.species("bamboo", 2006)

			.tree(random -> new WorldGenBambooTree(false))
			.noLog()
			.leavesLogic((block, sapling) ->
				new BlockLogicLeavesBamboo(block, Materials.LEAVES, sapling, () -> BOPJungle.BAMBOO))
			.register();

		logRegistration();
	}

	private static void logRegistration() {
		int blocks = 0;
		List<String> pending = new ArrayList<>();
		for (BOPWoodSet set : BOPWoodSet.registered()) {
			blocks += set.log != null ? 3 : 2;
			if (!set.hasTreeFeature()) {
				pending.add(set.key + " (" + set.pendingReason + ")");
			}
		}

		BetterOPlenty.LOGGER.info("Registered {} BOP wood set(s), {} blocks.",
			BOPWoodSet.registered().size(), blocks);

		if (!pending.isEmpty()) {
			BetterOPlenty.LOGGER.warn("{} of {} wood set(s) have no tree generator yet, so their "
					+ "saplings will not grow: {}. The blocks are registered so the generators can be "
					+ "written against them.",
				pending.size(), BOPWoodSet.registered().size(), String.join(", ", pending));
		}
	}
}
