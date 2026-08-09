package com.betteroplenty.world;

import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.data.BiomeRange;
import net.minecraft.core.world.biome.data.BiomeRangeMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public final class BOPBiomeRangeMap {

	private final Map<Biome, Set<BiomeRange>> ranges = new LinkedHashMap<>();

	private double[] altitudeEdges;
	private double[][] temperatureEdges;
	private double[][][] humidityEdges;
	private double[][][][] varietyEdges;
	private Biome[][][][] owners;
	private int gapCount;

	public void addRange(@NotNull Biome biome, BiomeRange... ranges) {
		if (ranges == null || ranges.length == 0 || this.owners != null) {
			return;
		}
		this.ranges.computeIfAbsent(biome, key -> new LinkedHashSet<>()).addAll(Arrays.asList(ranges));
	}

	public void clear() {
		this.ranges.clear();
		this.owners = null;
		this.altitudeEdges = null;
		this.temperatureEdges = null;
		this.humidityEdges = null;
		this.varietyEdges = null;
		this.gapCount = 0;
	}

	@Nullable
	public Set<BiomeRange> getRanges(@NotNull Biome biome) {
		Set<BiomeRange> set = this.ranges.get(biome);
		return set == null ? null : Collections.unmodifiableSet(set);
	}

	@NotNull
	public Set<Biome> allBiomes() {
		return Collections.unmodifiableSet(this.ranges.keySet());
	}

	public void lock() {
		if (this.ranges.isEmpty()) {
			throw new IllegalStateException("No BOP biome ranges registered; the world would have no biomes.");
		}

		List<BiomeRange> flat = new ArrayList<>();
		List<Biome> flatOwners = new ArrayList<>();
		for (Map.Entry<Biome, Set<BiomeRange>> entry : this.ranges.entrySet()) {
			for (BiomeRange range : entry.getValue()) {
				flat.add(range);
				flatOwners.add(entry.getKey());
			}
		}

		this.altitudeEdges = edges(flat, BiomeRange::getMinAltitude, BiomeRange::getMaxAltitude);
		int aSlots = this.altitudeEdges.length - 1;
		this.temperatureEdges = new double[aSlots][];
		this.humidityEdges = new double[aSlots][][];
		this.varietyEdges = new double[aSlots][][][];
		this.owners = new Biome[aSlots][][][];
		this.gapCount = 0;
		List<String> holes = new ArrayList<>();

		for (int a = 0; a < aSlots; a++) {
			double aMid = mid(this.altitudeEdges, a);
			List<BiomeRange> inLayer = overlapping(flat, aMid,
				BiomeRange::getMinAltitude, BiomeRange::getMaxAltitude);
			this.temperatureEdges[a] = edges(inLayer,
				BiomeRange::getMinTemperature, BiomeRange::getMaxTemperature);
			int tSlots = this.temperatureEdges[a].length - 1;
			this.humidityEdges[a] = new double[tSlots][];
			this.varietyEdges[a] = new double[tSlots][][];
			this.owners[a] = new Biome[tSlots][][];

			for (int t = 0; t < tSlots; t++) {
				double tMid = mid(this.temperatureEdges[a], t);
				List<BiomeRange> inBand = overlapping(inLayer, tMid,
					BiomeRange::getMinTemperature, BiomeRange::getMaxTemperature);
				this.humidityEdges[a][t] = edges(inBand,
					BiomeRange::getMinHumidity, BiomeRange::getMaxHumidity);
				int hSlots = this.humidityEdges[a][t].length - 1;
				this.varietyEdges[a][t] = new double[hSlots][];
				this.owners[a][t] = new Biome[hSlots][];

				for (int h = 0; h < hSlots; h++) {
					double hMid = mid(this.humidityEdges[a][t], h);
					List<BiomeRange> inCell = overlapping(inBand, hMid,
						BiomeRange::getMinHumidity, BiomeRange::getMaxHumidity);
					this.varietyEdges[a][t][h] = edges(inCell,
						BiomeRange::getMinVariety, BiomeRange::getMaxVariety);
					int vSlots = this.varietyEdges[a][t][h].length - 1;
					this.owners[a][t][h] = new Biome[vSlots];

					for (int v = 0; v < vSlots; v++) {
						double vMid = mid(this.varietyEdges[a][t][h], v);
						Biome owner = null;
						for (int i = 0; i < flat.size(); i++) {

							if (flat.get(i).contains(tMid, hMid, aMid, vMid)) {
								owner = flatOwners.get(i);
							}
						}
						if (owner == null) {
							this.gapCount++;
							if (holes.size() < 8) {
								holes.add(String.format("A %.4f  T %.4f  H %.4f  V %.4f",
									aMid, tMid, hMid, vMid));
							}
						}
						this.owners[a][t][h][v] = owner;
					}
				}
			}
		}

		if (this.gapCount > 0) {
			this.owners = null;
			throw new IllegalStateException("The BOP biome range map leaves " + this.gapCount
				+ " region(s) of the climate space with no biome; worldgen would produce null "
				+ "biomes there. First few: " + String.join(" | ", holes));
		}
	}

	public boolean hasGaps() {
		if (this.owners == null) {
			return true;
		}
		for (Biome[][][] layer : this.owners) {
			for (Biome[][] band : layer) {
				for (Biome[] cell : band) {
					for (Biome owner : cell) {
						if (owner == null) return true;
					}
				}
			}
		}
		return false;
	}

	@Nullable
	public Biome lookupBiome(double temperature, double humidity, double altitude, double variety) {
		if (this.owners == null) {
			return null;
		}
		int a = slot(this.altitudeEdges, altitude);
		int t = slot(this.temperatureEdges[a], temperature);
		int h = slot(this.humidityEdges[a][t], humidity);
		int v = slot(this.varietyEdges[a][t][h], variety);
		return this.owners[a][t][h][v];
	}

	public int rangeCount() {
		int n = 0;
		for (Set<BiomeRange> set : this.ranges.values()) {
			n += set.size();
		}
		return n;
	}

	@NotNull
	public String describe() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%-34s %-13s %-13s %-13s %-13s %s%n",
			"biome", "temperature", "humidity", "variety", "altitude", "share"));
		for (Map.Entry<Biome, Set<BiomeRange>> entry : this.ranges.entrySet()) {
			double total = 0.0;
			for (BiomeRange r : entry.getValue()) {
				total += (r.getMaxTemperature() - r.getMinTemperature()) *
					(r.getMaxHumidity() - r.getMinHumidity()) *
					(r.getMaxVariety() - r.getMinVariety()) *
					(r.getMaxAltitude() - r.getMinAltitude());
			}
			BiomeRange first = entry.getValue().iterator().next();
			sb.append(String.format("%-34s %.3f-%.3f  %.3f-%.3f  %.3f-%.3f  %.3f-%.3f  %5.2f%%%s%n",
				key(entry.getKey()),
				first.getMinTemperature(), first.getMaxTemperature(),
				first.getMinHumidity(), first.getMaxHumidity(),
				first.getMinVariety(), first.getMaxVariety(),
				first.getMinAltitude(), first.getMaxAltitude(),
				total * 100.0,
				entry.getValue().size() > 1 ? "  (+" + (entry.getValue().size() - 1) + " more boxes)" : ""));
		}
		return sb.toString();
	}

	public void writeDebugImage(@NotNull File target, int size) {
		double landAltitude = (BOPClimate.SEA_SHARE + 1.0) * 0.5;
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		for (int px = 0; px < size; px++) {
			for (int py = 0; py < size; py++) {
				double temperature = (px + 0.5) / size;
				double humidity = 1.0 - (py + 0.5) / size;
				Biome biome = lookupBiome(temperature, humidity, landAltitude, 0.5);
				image.setRGB(px, py, biome == null ? 0xFF00FF : biome.color);
			}
		}
		try {
			javax.imageio.ImageIO.write(image, "png", target);
			BetterOPlenty.LOGGER.info("Wrote biome range map slice to {}.", target.getAbsolutePath());
		} catch (Exception e) {
			BetterOPlenty.LOGGER.error("Failed to write the biome range map image.", e);
		}
	}

	private interface Bound {
		double of(BiomeRange range);
	}

	@NotNull
	private static double[] edges(@NotNull List<BiomeRange> ranges, @NotNull Bound min, @NotNull Bound max) {
		TreeSet<Double> set = new TreeSet<>();
		set.add(0.0);
		set.add(1.0);
		for (BiomeRange range : ranges) {
			double lo = min.of(range);
			double hi = max.of(range);
			if (lo > 0.0 && lo < 1.0) set.add(lo);
			if (hi > 0.0 && hi < 1.0) set.add(hi);
		}
		double[] out = new double[set.size()];
		int i = 0;
		for (double edge : set) {
			out[i++] = edge;
		}
		return out;
	}

	@NotNull
	private static List<BiomeRange> overlapping(@NotNull List<BiomeRange> ranges, double value,
	                                            @NotNull Bound min, @NotNull Bound max) {
		List<BiomeRange> out = new ArrayList<>();
		for (BiomeRange range : ranges) {
			if (value >= min.of(range) && value < max.of(range)) {
				out.add(range);
			}
		}
		return out;
	}

	private static double mid(@NotNull double[] edges, int slot) {
		return (edges[slot] + edges[slot + 1]) * 0.5;
	}

	private static int slot(@NotNull double[] edges, double value) {
		if (value <= edges[0]) return 0;
		int last = edges.length - 1;
		if (value >= edges[last]) return last - 1;
		int lo = 0, hi = last;
		while (hi - lo > 1) {
			int mid = (lo + hi) >>> 1;
			if (edges[mid] <= value) lo = mid; else hi = mid;
		}
		return lo;
	}

	@NotNull
	private static String key(@NotNull Biome biome) {
		String registryKey = Registries.BIOMES.getKey(biome);
		return registryKey == null ? biome.translationKey : registryKey;
	}
}
