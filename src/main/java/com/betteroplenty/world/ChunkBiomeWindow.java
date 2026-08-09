package com.betteroplenty.world;

import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.pos.TilePos;
import org.jetbrains.annotations.NotNull;

public final class ChunkBiomeWindow {

	public static final int SPAN = 24;

	private static final int MAX_DISTINCT = 16;

	@NotNull
	private final World world;

	private final Biome[] cells = new Biome[SPAN * SPAN];
	private final Biome[] distinct = new Biome[MAX_DISTINCT];
	private int distinctCount;
	private boolean distinctOverflowed;

	private final TilePos scratch = new TilePos();

	private boolean filled;
	private int originX;
	private int originZ;
	private int sampleY;

	private static boolean warnedOutOfWindow;

	public ChunkBiomeWindow(@NotNull World world) {
		this.world = world;
	}

	public boolean moveTo(int worldX, int worldZ, int sampleY) {
		if (this.filled && this.originX == worldX && this.originZ == worldZ && this.sampleY == sampleY) {
			return false;
		}
		this.originX = worldX;
		this.originZ = worldZ;
		this.sampleY = sampleY;
		this.distinctCount = 0;
		this.distinctOverflowed = false;

		for (int dx = 0; dx < SPAN; dx++) {
			for (int dz = 0; dz < SPAN; dz++) {
				this.scratch.set(worldX + dx, sampleY, worldZ + dz);
				Biome biome = this.world.getBlockBiome(this.scratch);
				this.cells[dx * SPAN + dz] = biome;
				this.remember(biome);
			}
		}
		this.filled = true;
		return true;
	}

	public int originX() {
		return this.originX;
	}

	public int originZ() {
		return this.originZ;
	}

	private void remember(Biome biome) {
		if (this.distinctOverflowed) {
			return;
		}
		for (int i = 0; i < this.distinctCount; i++) {
			if (this.distinct[i] == biome) {
				return;
			}
		}
		if (this.distinctCount == MAX_DISTINCT) {
			this.distinctOverflowed = true;
			return;
		}
		this.distinct[this.distinctCount++] = biome;
	}

	public boolean containsAny(@NotNull Biome[] mask) {
		if (this.distinctOverflowed) {
			for (Biome cell : this.cells) {
				for (Biome wanted : mask) {
					if (cell == wanted) {
						return true;
					}
				}
			}
			return false;
		}
		for (int i = 0; i < this.distinctCount; i++) {
			Biome here = this.distinct[i];
			for (Biome wanted : mask) {
				if (here == wanted) {
					return true;
				}
			}
		}
		return false;
	}

	@NotNull
	public Biome biomeAt(int x, int z) {
		int dx = x - this.originX;
		int dz = z - this.originZ;
		if (dx >= 0 && dx < SPAN && dz >= 0 && dz < SPAN) {
			return this.cells[dx * SPAN + dz];
		}
		if (!warnedOutOfWindow) {
			warnedOutOfWindow = true;
			BetterOPlenty.LOGGER.warn(
				"A decoration anchored at ({}, {}) fell outside the {}x{} biome window at ({}, {}). "
					+ "A position selector is producing X/Z outside [origin, origin+{}); the mask "
					+ "early-out in ChunkBiomeWindow.containsAny is no longer exact and features "
					+ "can be skipped. Widen ChunkBiomeWindow.SPAN to cover it.",
				x, z, SPAN, SPAN, this.originX, this.originZ, SPAN);
		}
		this.scratch.set(x, this.sampleY, z);
		return this.world.getBlockBiome(this.scratch);
	}
}
