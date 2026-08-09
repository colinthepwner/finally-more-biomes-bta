package com.betteroplenty.world;

import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.BiomeTags;
import net.minecraft.core.world.biome.data.BiomeRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class BOPClimate {
	private BOPClimate() {}

	private static final Tag<Biome> SNOW = BiomeTags.HAS_SURFACE_SNOW;
	private static final Tag<Biome> ICE = BiomeTags.HAS_SURFACE_ICE;
	private static final Tag<Biome> DEEPER_SNOW = BiomeTags.DEEPER_SNOW;

	public static final class Planned {

		@NotNull public final String id;

		public final float temperature;

		public final float rainfall;

		public final float minHeight, maxHeight;

		@Nullable public final String parent;

		public final double share;

		@NotNull public final List<Tag<Biome>> tags;

		private Planned(@NotNull String id, float temperature, float rainfall, float minHeight,
		                float maxHeight, @Nullable String parent, double share,
		                @NotNull List<Tag<Biome>> tags) {
			this.id = id;
			this.temperature = temperature;
			this.rainfall = rainfall;
			this.minHeight = minHeight;
			this.maxHeight = maxHeight;
			this.parent = parent;
			this.share = share;
			this.tags = tags;
		}

		@Override
		public String toString() {
			return id + "(t=" + temperature + ", r=" + rainfall + ", share=" + share + ")";
		}
	}

	public static final class Cell {
		@NotNull public final Planned owner;
		public final double tMin, tMax, hMin, hMax, vMin, vMax;

		private Cell(@NotNull Planned owner, double tMin, double tMax,
		             double hMin, double hMax, double vMin, double vMax) {
			this.owner = owner;
			this.tMin = tMin; this.tMax = tMax;
			this.hMin = hMin; this.hMax = hMax;
			this.vMin = vMin; this.vMax = vMax;
		}

		@NotNull
		public BiomeRange toRange() {
			return new BiomeRange(tMin, tMax, hMin, hMax, SEA_SHARE, 1.0, vMin, vMax);
		}

		public double volume() {
			return (tMax - tMin) * (hMax - hMin) * (vMax - vMin);
		}
	}

	public static final double SEA_SHARE = seaShare();

	private static double seaShare() {
		String override = System.getProperty("betteroplenty.seaShare");
		if (override == null) {
			return 0.40;
		}
		try {
			double value = Double.parseDouble(override);
			if (value < 0.0 || value > 0.95) {
				throw new NumberFormatException("outside 0..0.95");
			}
			return value;
		} catch (NumberFormatException e) {
			return 0.40;
		}
	}

	@NotNull
	public static BiomeRange oceanRange() {
		return new BiomeRange(0.0, 1.0, 0.0, 1.0, 0.0, SEA_SHARE, 0.0, 1.0);
	}

	private static final Map<String, Planned> ROSTER = new LinkedHashMap<>();

	@SafeVarargs
	private static void plan(@NotNull String id, float temperature, float rainfall,
	                         float minHeight, float maxHeight, @Nullable String parent,
	                         double share, Tag<Biome>... tags) {
		Planned previous = ROSTER.put(id, new Planned(id, temperature, rainfall, minHeight,
			maxHeight, parent, share, List.of(tags)));
		if (previous != null) {
			throw new IllegalStateException("Duplicate planned biome id '" + id + "'");
		}
	}

	static {
		plan("alps",                   0.0f,  0.5f,   2.0f,  3.0f, null,               0.003968, SNOW, ICE, DEEPER_SNOW);
		plan("alps_base",              0.0f,  0.5f,   0.2f,  0.5f, "alps",             0.003968);
		plan("alps_forest",            0.0f,  0.5f,   1.0f,  2.0f, "alps",             0.003968, SNOW, ICE, DEEPER_SNOW);
		plan("coniferous_forest_snow", 0.0f,  0.5f,   0.3f,  0.6f, null,               0.011905, SNOW, ICE, DEEPER_SNOW);
		plan("frost_forest",           0.0f,  0.5f,   0.3f,  0.4f, null,               0.011905, SNOW, ICE, DEEPER_SNOW);
		plan("glacier",                0.0f,  0.5f,   0.4f,  0.8f, null,               0.011905, SNOW, ICE, DEEPER_SNOW);
		plan("icy_hills",              0.0f,  0.5f,   0.3f,  0.8f, null,               0.011905, SNOW, ICE, DEEPER_SNOW);
		plan("polar",                  0.0f,  0.5f,  -0.5f,  0.0f, null,               0.011905, SNOW, ICE, DEEPER_SNOW);
		plan("arctic",                0.05f,  0.5f,   0.2f,  0.2f, null,               0.011905, SNOW, ICE);
		plan("dead_forest_snow",      0.05f,  0.8f,   0.2f,  0.7f, null,               0.011905, SNOW, ICE);
		plan("taiga",                 0.05f,  0.8f,   0.3f,  0.5f, null,               0.007937, SNOW, ICE);
		plan("taiga_hills",           0.05f,  0.8f,   0.8f,  1.0f, "taiga",            0.003968, SNOW, ICE);
		plan("extreme_hills",          0.2f,  0.3f,   1.0f,  2.0f, null,               0.011905);
		plan("maple_woods",            0.2f,  0.8f,   0.3f,  0.6f, null,               0.011905);
		plan("tundra",                 0.2f,  0.8f,   0.1f,  0.3f, null,               0.011905);
		plan("crag",                   0.4f,  0.2f,   2.0f,  3.0f, null,               0.011905);
		plan("birch_forest",           0.4f,  0.3f,   0.1f,  0.2f, null,               0.011905);
		plan("fen",                    0.4f,  0.4f,   0.2f,  0.4f, null,               0.011905);
		plan("grove",                  0.4f,  0.8f,   0.3f,  0.4f, null,               0.011905);
		plan("jade_cliffs",            0.5f,  0.1f,   0.5f,  1.5f, null,               0.011905);
		plan("mountain",               0.5f,  0.1f,   1.0f,  1.5f, null,               0.011905);
		plan("autumn_hills",           0.5f,  0.2f,   0.5f,  0.8f, null,               0.011905);
		plan("coniferous_forest",      0.5f,  0.4f,   0.4f,  0.9f, null,               0.011905);
		plan("highland",               0.5f,  0.5f,   0.9f,  1.9f, null,               0.011905);
		plan("hot_springs",            0.5f,  0.7f,   0.2f,  0.5f, null,               0.011905);
		plan("shield",                 0.5f,  0.8f,   0.1f,  0.3f, null,               0.011905);
		plan("bayou",                  0.5f,  0.9f,   0.1f,  0.3f, null,               0.011905);
		plan("marsh",                  0.5f,  0.9f,   0.2f,  0.2f, null,               0.011905);
		plan("silkglades",             0.5f,  0.9f,   0.3f,  0.3f, null,               0.011905);
		plan("moor",                   0.5f,  1.0f,   0.7f,  0.8f, null,               0.011905);
		plan("shrubland",              0.6f, 0.05f,   0.2f,  0.2f, null,               0.007937);
		plan("shrubland_forest",       0.6f, 0.05f,   0.2f,  0.2f, "shrubland",        0.003968);
		plan("thicket",                0.6f,  0.2f,   0.2f,  0.2f, null,               0.011905);
		plan("boreal_forest",          0.6f,  0.7f,   0.2f,  1.0f, null,               0.011905);
		plan("field",                  0.6f,  0.7f,   0.3f,  0.3f, null,               0.005952);
		plan("field_forest",           0.6f,  0.7f,   0.3f,  0.3f, "field",            0.005952);
		plan("lavender_fields",        0.6f,  0.7f,   0.3f,  0.3f, null,               0.011905);
		plan("spruce_woods",           0.6f,  0.7f,   0.1f,  0.2f, null,               0.011905);
		plan("grassland",              0.7f,  0.7f,   0.2f,  0.5f, null,               0.011905);
		plan("meadow",                 0.7f,  0.7f,   0.1f,  0.2f, null,               0.005952);
		plan("meadow_forest",          0.7f,  0.7f,   0.1f,  0.2f, "meadow",           0.005952);
		plan("cherry_blossom_grove",   0.7f,  0.8f,   0.3f,  0.4f, null,               0.011905);
		plan("deciduous_forest",       0.7f,  0.8f,   0.1f,  0.2f, null,               0.011905);
		plan("forest",                 0.7f,  0.8f,   0.1f,  0.2f, null,               0.007937);
		plan("forest_hills",           0.7f,  0.8f,   0.8f,  1.0f, "forest",           0.003968);
		plan("garden",                 0.7f,  0.8f,   0.3f,  0.4f, null,               0.011905);
		plan("seasonal_forest",        0.7f,  0.8f,   0.3f,  0.7f, null,               0.005952);
		plan("seasonal_spruce_forest", 0.7f,  0.8f,   0.6f,  0.9f, "seasonal_forest",  0.005952);
		plan("temperate_rainforest",   0.7f,  0.8f,   0.2f,  0.6f, null,               0.011905);
		plan("timber",                 0.7f,  0.8f,   0.3f,  0.4f, null,               0.007937);
		plan("timber_thin",            0.7f,  0.8f,   0.3f,  0.4f, "timber",           0.003968);
		plan("lush_swamp",             0.7f,  1.0f,   0.2f,  0.3f, null,               0.011905);
		plan("outback",                0.8f, 0.05f,   0.3f,  0.4f, null,               0.011905);
		plan("heathland",              0.8f,  0.1f,   0.3f,  0.4f, null,               0.011905);
		plan("lush_desert",            0.8f,  0.2f,   0.2f,  0.9f, null,               0.011905);
		plan("canyon",                 0.8f,  0.4f,   1.5f,  2.0f, null,               0.005952);
		plan("canyon_ravine",          0.8f,  0.4f,   0.3f,  0.4f, "canyon",           0.005952);
		plan("chaparral",              0.8f,  0.4f,   0.3f,  0.6f, null,               0.011905);
		plan("orchard",                0.8f,  0.4f,   0.1f,  0.2f, null,               0.011905);
		plan("pasture",                0.8f,  0.4f,   0.3f,  0.4f, null,               0.003968);
		plan("pasture_meadow",         0.8f,  0.4f,   0.3f,  0.4f, "pasture",          0.003968);
		plan("pasture_thin",           0.8f,  0.4f,   0.3f,  0.4f, "pasture",          0.003968);
		plan("plains",                 0.8f,  0.4f,   0.1f,  0.2f, null,               0.011905);
		plan("redwood_forest",         0.8f,  0.4f,   0.3f,  0.4f, null,               0.011905);
		plan("overgrown_greens",       0.8f,  0.8f,   0.3f,  0.4f, null,               0.011905);
		plan("bog",                    0.8f,  0.9f,   0.3f,  0.3f, null,               0.011905);
		plan("dead_swamp",             0.8f,  0.9f,   0.1f,  0.2f, null,               0.011905);
		plan("mangrove",               0.8f,  0.9f,   0.1f,  0.3f, null,               0.011905);
		plan("ominous_woods",          0.8f,  0.9f,   0.1f,  0.3f, null,               0.007937);
		plan("ominous_woods_thick",    0.8f,  0.9f,   0.4f,  0.8f, "ominous_woods",    0.003968);
		plan("quagmire",               0.8f,  0.9f,   0.2f,  0.3f, null,               0.011905);
		plan("sludgepit",              0.8f,  0.9f,   0.1f,  0.3f, null,               0.011905);
		plan("swampland",              0.8f,  0.9f,   0.1f,  0.3f, null,               0.011905);
		plan("wetland",                0.8f,  0.9f,   0.3f,  0.5f, null,               0.011905);
		plan("prairie",                0.9f,  0.6f,   0.3f,  0.4f, null,               0.011905);
		plan("oasis",                  0.9f,  0.7f,   0.3f,  0.4f, null,               0.011905);
		plan("fungi_forest",           0.9f,  1.0f,   0.2f,  0.5f, null,               0.011905);
		plan("mystic_grove",           0.9f,  1.0f,   0.3f,  0.8f, null,               0.007937);
		plan("mystic_grove_thin",      0.9f,  1.0f,   0.1f,  0.3f, "mystic_grove",     0.003968);
		plan("scrubland",              1.2f, 0.05f,   0.3f,  0.5f, null,               0.011905);
		plan("dead_forest",            1.2f,  0.1f,   0.2f,  0.7f, null,               0.011905);
		plan("bamboo_forest",          1.2f,  0.9f,   0.2f,  0.4f, null,               0.011905);
		plan("jungle",                 1.2f,  0.9f,   0.4f,  0.5f, null,               0.007937);
		plan("jungle_hills",           1.2f,  0.9f,   0.9f,  1.2f, "jungle",           0.003968);
		plan("sacred_springs",         1.2f,  0.9f,   0.4f,  1.2f, null,               0.011905);
		plan("tropical_rainforest",    1.2f,  0.9f,   0.3f,  0.7f, null,               0.011905);
		plan("savanna",                1.5f,  0.1f,   0.3f,  0.4f, null,               0.008929);
		plan("savanna_plateau",        1.5f,  0.1f,   0.9f,  0.9f, "savanna",          0.002976);
		plan("woodland",               1.7f,  0.2f,   0.3f,  0.7f, null,               0.011905);
		plan("badlands",               2.0f, 0.05f,   0.3f,  0.9f, null,               0.011905);
		plan("brushland",              2.0f, 0.05f,   0.3f,  0.3f, null,               0.011905);
		plan("deadlands",              2.0f, 0.05f,   0.1f,  0.5f, null,               0.011905);
		plan("desert",                 2.0f, 0.05f,   0.2f,  0.3f, null,               0.011905);
		plan("dunes",                  2.0f, 0.05f,   0.5f,  1.3f, null,               0.011905);
		plan("mesa",                   2.0f, 0.05f,   0.4f,  2.0f, null,               0.011905);
		plan("steppe",                 2.0f, 0.05f,   0.3f,  0.4f, null,               0.011905);
		plan("volcano",                2.0f, 0.05f,   0.6f,  1.2f, null,               0.011905);
		plan("wasteland",              2.0f, 0.05f,   0.3f,  0.4f, null,               0.011905);
		plan("rainforest",             2.0f,  2.0f,   0.2f,  1.8f, null,               0.011905);
		plan("tropics",                2.0f,  2.0f,   0.0f,  0.4f, null,               0.007937);
		plan("tropics_mountain",       2.0f,  2.0f,   0.7f,  1.2f, "tropics",          0.003968);
	}

	@NotNull
	public static List<Planned> roster() {
		return List.copyOf(ROSTER.values());
	}

	public static final class Resist {
		private Resist() {}

		public static final double SEASONAL = 0.65;

		public static final double DEAD = 0.80;

		public static final double MYSTIC = 0.85;

		public static final double FROZEN = 0.5;

		public static final double NONE = 0.0;
	}

	private static final Map<String, Double> RESIST = new LinkedHashMap<>();

	private static void resist(double value, String... ids) {
		for (String id : ids) {
			Double previous = RESIST.put(id, value);
			if (previous != null) {
				throw new IllegalStateException("Biome '" + id + "' is in two resistance groups");
			}
		}
	}

	static {
		resist(Resist.SEASONAL,
			"autumn_hills", "seasonal_forest", "seasonal_spruce_forest", "timber", "timber_thin");

		resist(Resist.DEAD,
			"deadlands", "wasteland", "dead_forest", "dead_forest_snow", "dead_swamp",
			"volcano", "crag", "badlands", "mesa", "dunes");

		resist(Resist.MYSTIC,
			"mystic_grove", "mystic_grove_thin", "ominous_woods", "ominous_woods_thick",
			"fungi_forest", "sacred_springs", "cherry_blossom_grove");

		resist(Resist.FROZEN,
			"arctic", "glacier", "icy_hills", "frost_forest", "tundra", "polar");
	}

	public static boolean seasonsNormally(@NotNull String id) {
		return !RESIST.containsKey(id);
	}

	public static double seasonResist(@NotNull String id) {
		return RESIST.getOrDefault(id, Resist.NONE);
	}

	@NotNull
	public static Map<String, Double> seasonResistances() {
		return Map.copyOf(RESIST);
	}

	@Nullable
	public static Planned planned(@NotNull String id) {
		return ROSTER.get(id);
	}

	@NotNull
	public static List<Tag<Biome>> tagsFor(@NotNull String id) {
		Planned planned = ROSTER.get(id);
		return planned == null ? List.of() : planned.tags;
	}

	public static final class Axis {
		@NotNull private final double[] knots;

		private Axis(@NotNull double[] knots) {
			this.knots = knots;
		}

		public double share(double value) {
			if (value <= knots[0]) return 0.0;
			int last = knots.length - 1;
			if (value >= knots[last]) return 1.0;
			int lo = 0, hi = last;
			while (hi - lo > 1) {
				int mid = (lo + hi) >>> 1;
				if (knots[mid] <= value) lo = mid; else hi = mid;
			}
			double span = knots[hi] - knots[lo];
			double frac = span <= 0.0 ? 0.0 : (value - knots[lo]) / span;
			return (lo + frac) / last;
		}
	}

	public static final Axis TEMPERATURE = new Axis(new double[]{
		-0.15000, 0.07159, 0.13042, 0.17297, 0.20286, 0.22716, 0.24831, 0.26732,
		0.28476, 0.30166, 0.31855, 0.33536, 0.35226, 0.36921, 0.38628, 0.40352,
		0.42070, 0.43796, 0.45521, 0.47245, 0.48977, 0.50707, 0.52428, 0.54150,
		0.55866, 0.57579, 0.59301, 0.61025, 0.62752, 0.64488, 0.66235, 0.67995,
		0.69762, 0.71536, 0.73301, 0.75052, 0.76800, 0.78546, 0.80282, 0.82022,
		0.83750, 0.85485, 0.87231, 0.88981, 0.90733, 0.92477, 0.94224, 0.95960,
		0.97699, 0.99438, 1.01168, 1.02887, 1.04594, 1.06300, 1.07991, 1.09679,
		1.11372, 1.13110, 1.15004, 1.17117, 1.19555, 1.22535, 1.26784, 1.32682,
		1.55000});

	public static final Axis HUMIDITY = new Axis(new double[]{
		-0.37000, -0.12713, -0.06871, -0.02821, 0.00193, 0.02681, 0.04853, 0.06824,
		0.08655, 0.10396, 0.12102, 0.13791, 0.15476, 0.17170, 0.18871, 0.20576,
		0.22299, 0.24020, 0.25751, 0.27484, 0.29223, 0.30960, 0.32692, 0.34417,
		0.36136, 0.37852, 0.39566, 0.41290, 0.43019, 0.44752, 0.46494, 0.48248,
		0.50004, 0.51757, 0.53510, 0.55248, 0.56983, 0.58710, 0.60435, 0.62149,
		0.63868, 0.65592, 0.67315, 0.69049, 0.70788, 0.72530, 0.74273, 0.76010,
		0.77741, 0.79460, 0.81169, 0.82866, 0.84557, 0.86237, 0.87927, 0.89626,
		0.91371, 0.93199, 0.95161, 0.97343, 0.99838, 1.02858, 1.06933, 1.12793,
		1.37000});

	public static final Axis VARIETY = new Axis(new double[]{
		-0.38000, -0.13087, -0.07223, -0.03123, -0.00076, 0.02428, 0.04632, 0.06619,
		0.08461, 0.10212, 0.11924, 0.13616, 0.15302, 0.17001, 0.18708, 0.20415,
		0.22134, 0.23863, 0.25607, 0.27349, 0.29096, 0.30847, 0.32592, 0.34320,
		0.36050, 0.37778, 0.39504, 0.41240, 0.42977, 0.44724, 0.46478, 0.48242,
		0.50007, 0.51779, 0.53542, 0.55299, 0.57040, 0.58782, 0.60519, 0.62248,
		0.63974, 0.65703, 0.67438, 0.69182, 0.70930, 0.72682, 0.74421, 0.76156,
		0.77884, 0.79599, 0.81303, 0.83006, 0.84704, 0.86394, 0.88081, 0.89786,
		0.91539, 0.93378, 0.95349, 0.97543, 1.00046, 1.03069, 1.07143, 1.12999,
		1.37000});

	public static final Axis LANDFORM = new Axis(new double[]{
		-0.35714, -0.35121, -0.34523, -0.33920, -0.33287, -0.32627, -0.31957, -0.31267,
		-0.30573, -0.29882, -0.29205, -0.28505, -0.27802, -0.27103, -0.26394, -0.25671,
		-0.24925, -0.24163, -0.23375, -0.22582, -0.21768, -0.20937, -0.20093, -0.19236,
		-0.18372, -0.17459, -0.16489, -0.15494, -0.14475, -0.13395, -0.12265, -0.11101,
		-0.09916, -0.08805, -0.07689, -0.06506, -0.05250, -0.03968, -0.02620, -0.01210,
		0.00278, 0.01778, 0.02909, 0.03967, 0.05077, 0.06142, 0.07187, 0.08409,
		0.09599, 0.10454, 0.11047, 0.11746, 0.12434, 0.12500, 0.12500, 0.12500,
		0.12500, 0.12500, 0.12500, 0.12500, 0.12500, 0.12500, 0.12500, 0.12500,
		0.12500});

	private static List<Cell> cells;

	@NotNull
	public static synchronized List<Cell> partition() {
		if (cells != null) {
			return cells;
		}
		List<Cell> out = new ArrayList<>(ROSTER.size());

		double tCursor = 0.0;
		int i = 0;
		List<Planned> all = new ArrayList<>(ROSTER.values());
		while (i < all.size()) {
			float bandTemperature = all.get(i).temperature;
			int bandEnd = i;
			double bandShare = 0.0;
			while (bandEnd < all.size() && all.get(bandEnd).temperature == bandTemperature) {
				bandShare += all.get(bandEnd).share;
				bandEnd++;
			}
			double tMin = tCursor;
			double tMax = bandEnd == all.size() ? 1.0 : tCursor + bandShare;
			tCursor = tMax;

			List<List<Planned>> families = new ArrayList<>();
			int j = i;
			while (j < bandEnd) {
				float cellRainfall = all.get(j).rainfall;
				int cellEnd = j;
				while (cellEnd < bandEnd && all.get(cellEnd).rainfall == cellRainfall) {
					cellEnd++;
				}

				families.addAll(familiesOf(all.subList(j, cellEnd)));
				j = cellEnd;
			}

			double hCursor = 0.0;
			for (int f = 0; f < families.size(); f++) {
				List<Planned> family = families.get(f);
				double familyShare = 0.0;
				for (Planned member : family) {
					familyShare += member.share;
				}

				double hMin = hCursor;
				double hMax = f == families.size() - 1 ? 1.0 : hCursor + familyShare / bandShare;
				hCursor = hMax;

				double vCursor = 0.0;
				for (int k = 0; k < family.size(); k++) {
					Planned planned = family.get(k);
					double vMin = vCursor;
					double vMax = k == family.size() - 1
						? 1.0
						: vCursor + planned.share / (familyShare == 0.0 ? 1.0 : familyShare);
					vCursor = vMax;
					out.add(new Cell(planned, tMin, tMax, hMin, hMax, vMin, vMax));
				}
			}
			i = bandEnd;
		}

		cells = Collections.unmodifiableList(out);
		return cells;
	}

	@NotNull
	private static List<List<Planned>> familiesOf(@NotNull List<Planned> inCell) {
		Map<String, List<Planned>> byHead = new LinkedHashMap<>();
		for (Planned planned : inCell) {
			boolean parentIsHere = planned.parent != null
				&& inCell.stream().anyMatch(other -> other.id.equals(planned.parent));
			String head = parentIsHere ? planned.parent : planned.id;
			byHead.computeIfAbsent(head, key -> new ArrayList<>()).add(planned);
		}

		Comparator<Planned> byHeight = Comparator
			.comparingDouble((Planned p) -> p.minHeight)
			.thenComparing(p -> p.id);

		List<List<Planned>> families = new ArrayList<>(byHead.values());
		for (List<Planned> family : families) {
			family.sort(byHeight);
		}
		families.sort(Comparator
			.comparingDouble((List<Planned> family) -> family.get(0).minHeight)
			.thenComparing(family -> family.get(0).id));
		return families;
	}
}
