package com.betteroplenty.entity;

import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.SpawnListEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SpawnList {

	public static final int CREATURE_WEIGHT_SCALE = 10;

	public record Declared(@Nullable Class<? extends Mob> mob, int weight, int minGroup, int maxGroup) {

		public boolean isDropped() {
			return this.mob == null;
		}
	}

	@NotNull
	private final String upstreamName;

	@Nullable
	private final List<SpawnListEntry> live;

	private final int weightScale;

	@NotNull
	private final List<Declared> declared = new ArrayList<>();

	private int expectedSize;

	public SpawnList(@NotNull String upstreamName, @Nullable List<SpawnListEntry> live, int weightScale) {
		this.upstreamName = upstreamName;
		this.live = live;
		this.weightScale = weightScale;
		this.expectedSize = live == null ? 0 : live.size();
	}

	@NotNull
	public SpawnList clear() {
		if (this.live != null) {
			this.live.clear();
			this.expectedSize = 0;
		}
		return this;
	}

	@NotNull
	public SpawnList add(@Nullable Class<? extends Mob> mob, int weight, int minGroup, int maxGroup) {
		this.declared.add(new Declared(mob, weight, minGroup, maxGroup));
		if (mob == null || this.live == null) {
			return this;
		}
		this.live.add(new SpawnListEntry(mob, weight * this.weightScale));
		this.expectedSize = this.live.size();
		return this;
	}

	@NotNull
	public String upstreamName() {
		return this.upstreamName;
	}

	@NotNull
	public List<Declared> declared() {
		return Collections.unmodifiableList(this.declared);
	}

	public int droppedCount() {
		int dropped = 0;
		for (Declared d : this.declared) {
			if (d.isDropped()) {
				dropped++;
			}
		}
		return dropped;
	}

	public boolean isInert() {
		return this.live == null;
	}

	public boolean isConsistent() {
		return this.live == null || this.live.size() == this.expectedSize;
	}

	public int liveSize() {
		return this.live == null ? 0 : this.live.size();
	}

	public int liveWeight() {
		if (this.live == null) {
			return 0;
		}
		int total = 0;
		for (SpawnListEntry entry : this.live) {
			total += entry.spawnFrequency;
		}
		return total;
	}
}
