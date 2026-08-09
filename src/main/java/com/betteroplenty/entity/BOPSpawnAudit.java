package com.betteroplenty.entity;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPBiomes;
import net.minecraft.core.data.registry.Registries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class BOPSpawnAudit {
	private BOPSpawnAudit() {}

	private static final int BTA_DEFAULT_PACK_SIZE = 4;

	public static void run() {
		selfTest();

		List<BiomeGenBase> registered = BOPBiomes.registered();
		if (registered.isEmpty()) {
			BetterOPlenty.LOGGER.error("Spawn audit ran before any biome was registered; every "
				+ "check below would pass against nothing. Fix the call site.");
			return;
		}

		List<String> bypassed = new ArrayList<>();
		List<String> summaries = new ArrayList<>();
		int declaredTotal = 0;
		int droppedTotal = 0;
		int inertTotal = 0;
		int packSizeIgnored = 0;
		int biomesWithSpawns = 0;

		for (BiomeGenBase biome : registered) {
			int declaredHere = 0;
			for (SpawnList list : biome.spawnLists()) {
				if (!list.isConsistent()) {
					bypassed.add(name(biome) + "." + list.upstreamName());
				}
				for (SpawnList.Declared entry : list.declared()) {
					declaredHere++;
					if (entry.isDropped()) {
						droppedTotal++;
					}
					if (list.isInert()) {
						inertTotal++;
					} else if (entry.maxGroup() != BTA_DEFAULT_PACK_SIZE) {
						packSizeIgnored++;
					}
				}
			}
			if (declaredHere > 0) {
				biomesWithSpawns++;
				declaredTotal += declaredHere;
				summaries.add(String.format("%s: %d entries, live weights m/c/w = %d/%d/%d",
					name(biome), declaredHere,
					biome.spawnableMonsterList().liveWeight(),
					biome.spawnableCreatureList().liveWeight(),
					biome.spawnableWaterCreatureList().liveWeight()));
			}
		}

		if (!bypassed.isEmpty()) {
			BetterOPlenty.LOGGER.error("Spawn lists written past the compat layer in {} place(s): "
					+ "{}. Those entries missed the weight scale and the BOPMobs remap -- the line "
					+ "wants spawnableXList().add(...), with the parentheses.",
				bypassed.size(), String.join(", ", bypassed));
			return;
		}

		if (declaredTotal == 0) {
			BetterOPlenty.LOGGER.info("Spawn lists: none of the {} ported biome(s) set their own "
					+ "spawns yet, so all {} inherit BTA's defaults. 81 of BOP's 92 biome classes "
					+ "do set them, so expect this line to change as the roster fills.",
				registered.size(), registered.size());
			return;
		}

		BetterOPlenty.LOGGER.info("Spawn lists: {} entries across {} of {} ported biome(s); "
				+ "{} dropped (no BTA mob), {} inert (cave-creature, no BTA category), "
				+ "{} group sizes BTA cannot honour. Details:\n    {}",
			declaredTotal, biomesWithSpawns, registered.size(), droppedTotal, inertTotal,
			packSizeIgnored, String.join("\n    ", summaries));
	}

	@NotNull
	private static String name(@NotNull BiomeGenBase biome) {
		String key = Registries.BIOMES.getKey(biome);
		return key == null ? biome.translationKey : key;
	}

	private static final class Probe extends BiomeGenBase {
		Probe() {
			super("audit.spawn_probe");
		}

		void bypassTheCompatLayer() {
			this.spawnableCreatureList.add(
				new net.minecraft.core.entity.SpawnListEntry(BOPMobs.WOLF, 5));
		}
	}

	private static void selfTest() {
		List<String> failures = new ArrayList<>();

		Probe fresh = new Probe();
		expect(failures, "inherited monster weight", 54, fresh.spawnableMonsterList().liveWeight());
		expect(failures, "inherited creature weight", 418, fresh.spawnableCreatureList().liveWeight());
		expect(failures, "inherited water weight", 10, fresh.spawnableWaterCreatureList().liveWeight());

		Probe adding = new Probe();
		adding.spawnableCreatureList().add(BOPMobs.WOLF, 5, 4, 4);
		double share = 50.0 / adding.spawnableCreatureList().liveWeight();
		if (Math.abs(share - 5.0 / 45.0) > 0.01) {
			failures.add(String.format("wolf share %.1f%% vs upstream 11.1%%", share * 100.0));
		}

		Probe clearing = new Probe();
		clearing.spawnableCreatureList().clear();
		clearing.spawnableCreatureList().add(BOPMobs.MOOSHROOM, 3, 4, 8);
		expect(failures, "cleared list size", 1, clearing.spawnableCreatureList().liveSize());
		expect(failures, "cleared list weight", 30, clearing.spawnableCreatureList().liveWeight());

		Probe dropping = new Probe();
		dropping.spawnableMonsterList().add(BOPMobs.ENDERMAN, 10, 1, 4);
		expect(failures, "dropped entry counted", 1, dropping.spawnableMonsterList().droppedCount());
		expect(failures, "dropped entry not spawned", 54, dropping.spawnableMonsterList().liveWeight());

		Probe cave = new Probe();
		cave.spawnableCaveCreatureList().add(BOPMobs.BAT, 10, 8, 8);
		expect(failures, "cave entry recorded", 1, cave.spawnableCaveCreatureList().declared().size());
		expect(failures, "cave entry inert", 0, cave.spawnableCaveCreatureList().liveSize());
		if (!cave.spawnableCaveCreatureList().isInert()) {
			failures.add("cave list claims to be backed by a BTA category");
		}

		Probe bypassed = new Probe();
		if (!bypassed.spawnableCreatureList().isConsistent()) {
			failures.add("bypass detector fires on an untouched list");
		}
		bypassed.bypassTheCompatLayer();
		if (bypassed.spawnableCreatureList().isConsistent()) {
			failures.add("bypass detector missed a raw add on the inherited field");
		}

		if (!failures.isEmpty()) {
			BetterOPlenty.LOGGER.error("Spawn mechanism self-test FAILED ({}): {}. Ported spawn "
					+ "lines are not doing what they read as -- fix this before porting a biome "
					+ "that sets spawns.",
				failures.size(), String.join("; ", failures));
			return;
		}
		BetterOPlenty.LOGGER.info("Spawn mechanism self-test: 12 checks pass -- BTA's inherited "
			+ "weights are 54/418/10 as read, the creature scale holds upstream's share, dropped "
			+ "mobs are counted, cave entries are inert, and a raw add is detected.");
	}

	private static void expect(@NotNull List<String> failures, @NotNull String what,
	                           int expected, int actual) {
		if (expected != actual) {
			failures.add(what + " = " + actual + ", expected " + expected);
		}
	}
}
