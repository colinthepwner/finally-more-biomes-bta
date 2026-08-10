package com.betteroplenty.world;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.world.nether.WorldTypeNetherBOP;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.provider.BiomeProvider;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.generate.chunk.perlin.ChunkGeneratorPerlin;
import net.minecraft.core.world.generate.CavesLargeFeature;
import net.minecraft.core.world.generate.LargeFeature;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.type.WorldTypeGroups;
import net.minecraft.core.world.type.WorldTypes;
import net.minecraft.core.world.type.overworld.WorldTypeOverworld;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WorldTypeBOP extends WorldTypeOverworld {

	public static WorldType BOP;

	public WorldTypeBOP(WorldType.Properties properties) {
		super(properties);
	}

	public static void register() {
		BOP = WorldTypes.register(
			BetterOPlenty.MOD_ID + ":bop.default",
			new WorldTypeBOP(
				WorldTypeOverworld.defaultProperties("worldType.betteroplenty.bop")
					.portalBounds(0, 255)
			)
		);

		BetterOPlenty.LOGGER.info("Registered world type '{}' (256 tall, ocean at Y=128).",
			BetterOPlenty.MOD_ID + ":bop.default");
	}

	public static void registerWorldTypeGroup() {
		int dimensions = Dimension.getDimensionList().size();
		if (dimensions == 0) {
			BetterOPlenty.LOGGER.error(
				"Dimensions are not registered yet; the BOP world type group would crash on world "
					+ "creation. Skipping registration.");
			return;
		}

		WorldTypeGroups.Group group = new WorldTypeGroups.Group(BOP) {
			@Override
			public WorldType get(Dimension dimension) {
				try {
					return super.get(dimension);
				} catch (NullPointerException hole) {
					WorldType fallback = dimension.defaultWorldType;
					if (fallback == null) {
						throw new IllegalStateException("The BOP world type group has no world "
							+ "type for dimension " + dimensionId(dimension) + ", and that "
							+ "dimension declares no default of its own. It was registered after "
							+ "Finally More Biomes published its world type, by a mod that did "
							+ "not seed the existing groups.", hole);
					}
					with(dimension, fallback);
					BetterOPlenty.LOGGER.warn("BOP world type group: dimension {} was registered "
						+ "after the group was built; seeded its default world type '{}' so world "
						+ "creation cannot crash on it.", dimensionId(dimension),
						Registries.WORLD_TYPES.getKey(fallback));
					return fallback;
				}
			}
		};

		for (Dimension dimension : Dimension.getDimensionList().values()) {
			WorldType type;
			if (dimension == Dimension.OVERWORLD) {
				type = BOP;
			} else if (dimension == Dimension.NETHER && WorldTypeNetherBOP.NETHER_BOP != null) {
				type = WorldTypeNetherBOP.NETHER_BOP;
			} else {
				type = dimension.defaultWorldType;
			}
			group.with(dimension, type);
		}
		WorldTypeGroups.GROUPS.add(group);

		List<String> seeded = new ArrayList<>();
		for (WorldTypeGroups.Group candidate : WorldTypeGroups.GROUPS) {
			for (Dimension dimension : Dimension.getDimensionList().values()) {
				try {
					candidate.get(dimension);
				} catch (NullPointerException hole) {
					WorldType fallback = dimension.defaultWorldType;
					if (fallback == null) {
						BetterOPlenty.LOGGER.error("World type group '{}' has no entry for "
							+ "dimension {}, which declares no default world type either. "
							+ "Creating a world with that group will crash; nothing sane to "
							+ "seed.", describe(candidate), dimensionId(dimension));
						continue;
					}
					candidate.with(dimension, fallback);
					seeded.add("'" + describe(candidate) + "' had no entry for dimension "
						+ dimensionId(dimension) + ", seeded '"
						+ Registries.WORLD_TYPES.getKey(fallback) + "'");
				}
			}
		}
		if (!seeded.isEmpty()) {
			BetterOPlenty.LOGGER.warn("World type groups: {} hole(s) seeded -- a mod initialised "
				+ "the world-type list before every dimension was registered, which would have "
				+ "crashed world creation with an NPE in WorldTypeGroups.Group.get: {}.",
				seeded.size(), String.join("; ", seeded));
		}

		BetterOPlenty.LOGGER.info(
			"Published BOP world type: {} groups available, {} dimensions seeded, Nether = {}; "
				+ "all groups answer for all dimensions{}.",
			WorldTypeGroups.GROUPS.size(), dimensions,
			WorldTypeNetherBOP.NETHER_BOP != null ? "BOP's" : "BTA's (BOP Nether type missing!)",
			seeded.isEmpty() ? "" : " after healing");
	}

	private static String describe(WorldTypeGroups.Group group) {
		return String.valueOf(Registries.WORLD_TYPES.getKey(group.get(Dimension.OVERWORLD)));
	}

	private static int dimensionId(Dimension dimension) {
		for (var entry : Dimension.getDimensionList().int2ObjectEntrySet()) {
			if (entry.getValue() == dimension) {
				return entry.getIntKey();
			}
		}
		return -1;
	}

	@NotNull
	@Override
	public BiomeProvider createBiomeProvider(World world) {
		return new BiomeProviderBOP(world);
	}

	@NotNull
	@Override
	public Biome[] allBiomes() {
		return BOPBiomes.allBiomes();
	}

	@Override
	public ChunkGenerator createChunkGenerator(World world) {
		BetterOPlenty.LOGGER.info("Creating BOP chunk generator for dimension {}.", world.dimension);
		return new ChunkGeneratorPerlin(
			world,
			new ChunkDecoratorBOP(world),
			new TerrainGeneratorBOP(world),

			new SurfaceGeneratorBOP(world),
			new LargeFeature[]{new CavesLargeFeature()}
		) {};
	}

	@Override
	public boolean isValidSpawn(World world, int x, int y, int z) {
		return world.getBlockId(x, y, z) == Blocks.SAND.id()
			|| world.getBlockId(x, y, z) == Blocks.GRASS.id();
	}

}
