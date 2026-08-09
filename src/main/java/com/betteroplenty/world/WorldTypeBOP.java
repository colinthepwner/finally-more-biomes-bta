package com.betteroplenty.world;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.world.nether.WorldTypeNetherBOP;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.provider.BiomeProvider;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.generate.chunk.perlin.ChunkGeneratorPerlin;
import net.minecraft.core.world.generate.chunk.perlin.overworld.SurfaceGeneratorOverworld;
import net.minecraft.core.world.generate.CavesLargeFeature;
import net.minecraft.core.world.generate.LargeFeature;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.type.WorldTypeGroups;
import net.minecraft.core.world.type.WorldTypes;
import net.minecraft.core.world.type.overworld.WorldTypeOverworld;
import org.jetbrains.annotations.NotNull;

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

		WorldTypeGroups.Group group = new WorldTypeGroups.Group(BOP);

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

		BetterOPlenty.LOGGER.info(
			"Published BOP world type: {} groups available, {} dimensions seeded, Nether = {}.",
			WorldTypeGroups.GROUPS.size(), dimensions,
			WorldTypeNetherBOP.NETHER_BOP != null ? "BOP's" : "BTA's (BOP Nether type missing!)");
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
			new SurfaceGeneratorOverworld(world),
			new LargeFeature[]{new CavesLargeFeature()}
		) {};
	}

	@Override
	public boolean isValidSpawn(World world, int x, int y, int z) {
		return world.getBlockId(x, y, z) == Blocks.SAND.id()
			|| world.getBlockId(x, y, z) == Blocks.GRASS.id();
	}

}
