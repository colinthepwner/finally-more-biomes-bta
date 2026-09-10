package com.betteroplenty.world;

import com.betteroplenty.BOPDimensionId;
import com.betteroplenty.BetterOPlenty;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class WorldTypeBOP extends WorldTypeOverworld {

	public static WorldType BOP;

	public static WorldType BOP_AMPLIFIED;

	public static WorldType BOP_WOVEN;

	public static WorldType BOP_WOVEN_AMPLIFIED;

	private static WorldType[] overworldTypes() {
		return new WorldType[]{BOP, BOP_AMPLIFIED, BOP_WOVEN, BOP_WOVEN_AMPLIFIED};
	}

	private final boolean woven;

	private static boolean groupPublished = false;

	public WorldTypeBOP(WorldType.Properties properties) {
		this(properties, false);
	}

	public WorldTypeBOP(WorldType.Properties properties, boolean woven) {
		super(properties);
		this.woven = woven;
	}

	private static WorldType.Properties amplified(String translationKey) {
		return WorldTypeOverworld.defaultProperties(translationKey)
			.bounds(0, 255, 64)
			.portalBounds(0, 255);
	}

	public static void register() {
		BOP = WorldTypes.register(
			BetterOPlenty.MOD_ID + ":bop.default",
			new WorldTypeBOP(
				WorldTypeOverworld.defaultProperties("worldType.betteroplenty.bop")
					.portalBounds(0, 255)
			)
		);

		BOP_AMPLIFIED = WorldTypes.register(
			BetterOPlenty.MOD_ID + ":bop.amplified",
			new WorldTypeBOP(amplified("worldType.betteroplenty.bop.amplified"))
		);

		BOP_WOVEN = WorldTypes.register(
			BetterOPlenty.MOD_ID + ":bop.woven",
			new WorldTypeBOP(
				WorldTypeOverworld.defaultProperties("worldType.betteroplenty.bop.woven")
					.portalBounds(0, 255),
				true
			)
		);

		BOP_WOVEN_AMPLIFIED = WorldTypes.register(
			BetterOPlenty.MOD_ID + ":bop.woven.amplified",
			new WorldTypeBOP(amplified("worldType.betteroplenty.bop.woven.amplified"), true)
		);

		BetterOPlenty.LOGGER.info("Registered 4 world types: '{}' and '{}' (256 tall, ocean at "
				+ "Y=128), '{}' and '{}' (amplified, ocean at Y=64); two of the four weave BTA's "
				+ "biomes in.",
			BetterOPlenty.MOD_ID + ":bop.default", BetterOPlenty.MOD_ID + ":bop.woven",
			BetterOPlenty.MOD_ID + ":bop.amplified",
			BetterOPlenty.MOD_ID + ":bop.woven.amplified");
	}

	public static void registerWorldTypeGroup() {
		if (groupPublished) {
			return;
		}
		int dimensions = Dimension.getDimensionList().size();
		if (dimensions == 0) {
			BetterOPlenty.LOGGER.error(
				"Dimensions are not registered yet; the BOP world type group would crash on world "
					+ "creation. Skipping registration.");
			return;
		}

		List<Integer> plugged = plugIdSpaceHoles();
		List<WorldTypeGroups.Group> groups = new ArrayList<>();
		int builtInGroups;
		try {

			builtInGroups = WorldTypeGroups.GROUPS.size();

			for (WorldType overworld : overworldTypes()) {
				if (overworld == null) {
					continue;
				}
				groups.add(new WorldTypeGroups.Group(overworld) {
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
				});
			}
		} catch (NullPointerException | ExceptionInInitializerError sparse) {

			BetterOPlenty.LOGGER.error("Could not build the BOP world type group: BTA's "
				+ "WorldTypeGroups.Group constructor walks dimension ids 0..size-1 and requires "
				+ "every one of them to exist, and this instance's id space has gaps. Registered "
				+ "ids: {}. The Biomes O' Plenty world type will not appear in the Create World "
				+ "screen until whichever mod owns the highest id there registers a contiguous "
				+ "one. Nothing else in this mod is affected.", BOPDimensionId.describeIdSpace(),
				sparse);
			try {
				healExistingGroups();
			} catch (Throwable alsoSparse) {

				BetterOPlenty.LOGGER.error("The world type group list could not be built either, "
					+ "so there was nothing to heal.", alsoSparse);
			}
			return;
		} finally {
			unplugIdSpaceHoles(plugged);
		}

		for (int i = 0; i < groups.size(); i++) {
			WorldTypeGroups.Group group = groups.get(i);
			WorldType overworld = overworldTypes()[i];
			for (Dimension dimension : Dimension.getDimensionList().values()) {
				WorldType type;
				if (dimension == Dimension.OVERWORLD) {

					type = overworld;
				} else if (dimension == Dimension.NETHER && WorldTypeNetherBOP.NETHER_BOP != null) {
					type = WorldTypeNetherBOP.NETHER_BOP;
				} else {
					type = dimension.defaultWorldType;
				}
				group.with(dimension, type);
			}
			WorldTypeGroups.GROUPS.add(group);
		}
		groupPublished = true;

		List<String> seeded = healExistingGroups();

		BetterOPlenty.LOGGER.info(
			"Published {} BOP world types: {} groups available ({} of them BTA's own), {} dimensions "
				+ "seeded, Nether = {}; all groups answer for all dimensions{}.",
			groups.size(), WorldTypeGroups.GROUPS.size(), builtInGroups, dimensions,
			WorldTypeNetherBOP.NETHER_BOP != null ? "BOP's" : "BTA's (BOP Nether type missing!)",
			seeded.isEmpty() ? "" : " after healing");
	}

	private static List<String> healExistingGroups() {

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
		return seeded;
	}

	private static List<Integer> plugIdSpaceHoles() {
		Int2ObjectMap<Dimension> visible = Dimension.getDimensionList();
		int highest = -1;
		for (var entry : visible.int2ObjectEntrySet()) {
			highest = Math.max(highest, entry.getIntKey());
		}
		List<Integer> holes = new ArrayList<>();
		for (int id = 0; id < highest; id++) {
			if (!visible.containsKey(id)) {
				holes.add(id);
			}
		}
		if (holes.isEmpty()) {
			return holes;
		}

		BetterOPlenty.LOGGER.error("This instance's dimension ids are not contiguous: {} registered, "
			+ "with {} missing. BTA's WorldTypeGroups.Group constructor walks ids 0..size-1 and "
			+ "requires every one to exist, so as things stand ANY mod that builds a world type "
			+ "group from here on crashes -- including BTA's own, if something touches "
			+ "WorldTypeGroups late. Filling the gap(s) with the overworld for the length of this "
			+ "mod's own group construction and putting the list back afterwards. Only a mod "
			+ "claiming a contiguous id can make this go away for good -- and that includes this "
			+ "one, which holds a high id only while a world here has terrain saved under it.",
			BOPDimensionId.describeIdSpace(), holes);

		try {
			Int2ObjectMap<Dimension> live = mutableDimensionList();
			for (int id : holes) {
				live.put(id, Dimension.OVERWORLD);
			}
			return holes;
		} catch (ReflectiveOperationException | RuntimeException e) {
			BetterOPlenty.LOGGER.error("Could not reach Dimension.dimensionList to fill the gap(s); "
				+ "the group construction after this will fail.", e);
			return new ArrayList<>();
		}
	}

	private static void unplugIdSpaceHoles(List<Integer> plugged) {
		if (plugged.isEmpty()) {
			return;
		}
		try {
			Int2ObjectMap<Dimension> live = mutableDimensionList();
			for (int id : plugged) {
				live.remove(id);
			}
			BetterOPlenty.LOGGER.info("Dimension id gap(s) {} filled for the group construction and "
				+ "removed again; the dimension list is as it was.", plugged);
		} catch (ReflectiveOperationException | RuntimeException e) {

			BetterOPlenty.LOGGER.error("Could not remove the temporary dimension list entries {}. "
				+ "Those ids now point at the overworld. Restart the game before creating or "
				+ "loading a world.", plugged, e);
		}
	}

	@SuppressWarnings("unchecked")
	private static Int2ObjectMap<Dimension> mutableDimensionList() throws ReflectiveOperationException {
		Field field = Dimension.class.getDeclaredField("dimensionList");
		field.setAccessible(true);
		return (Int2ObjectMap<Dimension>) field.get(null);
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
		if (this.woven) {
			BOPWovenClimate.logPartition();
		}
		return new BiomeProviderBOP(world, this.woven);
	}

	@NotNull
	@Override
	public Biome[] allBiomes() {
		return this.woven ? BOPWovenClimate.allBiomes() : BOPBiomes.allBiomes();
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
