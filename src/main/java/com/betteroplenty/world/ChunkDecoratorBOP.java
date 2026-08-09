package com.betteroplenty.world;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.mixin.ChunkDecoratorSnowInvoker;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.compat.DecorationWindow;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;
import net.minecraft.core.world.generate.chunk.ChunkFeatureDecorator;
import net.minecraft.core.world.generate.chunk.perlin.overworld.ChunkDecoratorOverworld;
import net.minecraft.core.world.pos.TilePos;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChunkDecoratorBOP implements ChunkDecorator {

	@NotNull
	private final ChunkDecoratorOverworld vanillaSteps;

	@NotNull
	private final BOPFeatures bopFeatures;

	@NotNull
	private final World world;

	public ChunkDecoratorBOP(@NotNull World world) {
		this.world = world;
		this.vanillaSteps = new OverworldSteps(world);
		this.bopFeatures = new BOPFeatures(world);

		this.bopFeatures.registerDecorations();
	}

	@Override
	public void decorate(@NotNull Chunk chunk) {

		DecorationWindow.open(this.world, chunk.pos.x(), chunk.pos.z());
		try {

			BOPSuppression.suppressUngatedVanilla = true;
			BOPSuppression.deferSnow = true;
			try {
				this.vanillaSteps.decorate(chunk);
			} finally {
				BOPSuppression.suppressUngatedVanilla = false;
				BOPSuppression.deferSnow = false;
			}

			this.bopFeatures.decorate(chunk);

			this.applyDeferredSnow(chunk);
		} finally {

			DecorationWindow.close(this.world, chunk.pos.x(), chunk.pos.z());

			int minX = chunk.pos.x() * 16;
			int minZ = chunk.pos.z() * 16;
			this.world.markBlocksDirty(
				new TilePos(minX, this.world.getWorldType().getMinY(this.world), minZ),
				new TilePos(minX + 31, this.world.getWorldType().getMaxY(this.world), minZ + 31));
		}
	}

	private void applyDeferredSnow(@NotNull Chunk chunk) {
		ChunkDecoratorSnowInvoker snow = (ChunkDecoratorSnowInvoker) this.vanillaSteps;
		int chunkX = chunk.pos.x();
		int chunkZ = chunk.pos.z();
		int x = chunkX * 16;
		int z = chunkZ * 16;
		int oceanY = this.world.getWorldType().getOceanY();

		for (int dx = x + 8; dx < x + 8 + 16; dx++) {
			for (int dz = z + 8; dz < z + 8 + 16; dz++) {
				try {
					snow.betteroplenty$applySnowAndIceForColumn(dx, dz, oceanY);
				} catch (Throwable failed) {
					BetterOPlenty.LOGGER.error("Deferred snow column ({}, {}) failed at chunk "
						+ "({}, {}); continuing.", dx, dz, chunkX, chunkZ, failed);
				}
			}
		}

		for (int dx = x; dx < x + 32; dx++) {
			for (int dz = z; dz < z + 32; dz++) {
				int ownerChunkX = dx - 8 >> 4;
				int ownerChunkZ = dz - 8 >> 4;
				if ((ownerChunkX == chunkX && ownerChunkZ == chunkZ)
					|| !this.world.isChunkLoaded(ownerChunkX, ownerChunkZ)) {
					continue;
				}
				if (!this.world.getChunkFromChunkCoords(ownerChunkX, ownerChunkZ).isTerrainPopulated) {
					continue;
				}
				if (!this.world.isChunkLoaded(dx >> 4, dz >> 4)) {
					continue;
				}
				try {
					snow.betteroplenty$applySnowAndIceForColumn(dx, dz, oceanY);
				} catch (Throwable failed) {
					BetterOPlenty.LOGGER.error("Deferred spillover snow column ({}, {}) failed at "
						+ "chunk ({}, {}); continuing.", dx, dz, chunkX, chunkZ, failed);
				}
			}
		}
	}

	private static final class OverworldSteps extends ChunkDecoratorOverworld {
		private OverworldSteps(@NotNull World world) {
			super(world, 0);
		}
	}

	private static final class BOPFeatures extends ChunkFeatureDecorator {

		@NotNull
		private final ChunkBiomeWindow window;

		private BOPFeatures(@NotNull World world) {
			super(world);
			this.window = new ChunkBiomeWindow(world);
		}

		private boolean registered;

		@Override
		public void registerDecorations() {

			if (this.registered) {
				return;
			}
			this.registered = true;

			List<BiomeGenBase> biomes = new ArrayList<>(BOPBiomes.registered());
			biomes.addAll(BOPBiomes.promisedLand());
			if (biomes.isEmpty()) {
				BetterOPlenty.LOGGER.warn("No BOP biomes registered; chunks will generate undecorated.");
				return;
			}

			int decorations = 0;

			for (BOPDecorations.Entry entry : BOPDecorations.entries()) {

				Map<Integer, List<Biome>> byValue = new LinkedHashMap<>();

				for (BiomeGenBase biome : biomes) {
					int count = entry.counter().applyAsInt(biome.customBiomeDecorator);

					if (count < 0 || (count == 0 && !entry.liveAtZero())) {
						continue;
					}
					byValue.computeIfAbsent(count, k -> new ArrayList<>()).add(biome);
				}

				for (Map.Entry<Integer, List<Biome>> group : byValue.entrySet()) {
					int count = group.getKey();
					Biome[] mask = group.getValue().toArray(new Biome[0]);

					this.register(
						BetterOPlenty.MOD_ID + ":" + entry.id() + "_" + count,

						BiomeGatedDecoration.of(entry.feature().get(), mask, entry.selector(),
							entry.method().create(count), this.window));
					decorations++;
				}
			}

			int extras = 0;
			for (BiomeGenBase biome : biomes) {
				final int[] seq = {0};
				biome.registerExtraDecorations((id, feature, selector, method) -> {
					this.register(

						biome.getRegistryKey() + "/" + id + "_" + seq[0]++,
						BiomeGatedDecoration.of(feature.get(), new Biome[]{biome}, selector, method,
							this.window));
				});
				extras += seq[0];
			}
			decorations += extras;

			BetterOPlenty.LOGGER.info(
				"Registered {} BOP decorations across {} biomes ({} from {} counters, {} from decorate() overrides), "
					+ "running after BTA's own overworld decoration.",
				decorations, biomes.size(), decorations - extras, BOPDecorations.entries().size(), extras);
		}

		@Override
		public void postDecorate(@NotNull World world, @NotNull Chunk chunk) {

		}
	}
}
