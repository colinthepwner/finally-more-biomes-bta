package com.betteroplenty.world.nether;

import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.provider.BiomeProvider;
import net.minecraft.core.world.generate.CavesLargeFeature;
import net.minecraft.core.world.generate.LargeFeature;
import net.minecraft.core.world.generate.LavaFloeLargeFeature;
import net.minecraft.core.world.generate.RubyglassFloeLargeFeature;
import net.minecraft.core.world.generate.ShelfLargeFeature;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.generate.chunk.perlin.ChunkGeneratorPerlin;
import net.minecraft.core.world.generate.chunk.perlin.nether.SurfaceGeneratorNether;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.type.WorldTypes;
import net.minecraft.core.world.type.nether.WorldTypeNether;
import org.jetbrains.annotations.NotNull;

public class WorldTypeNetherBOP extends WorldTypeNether {

	public static WorldType NETHER_BOP;

	public WorldTypeNetherBOP(WorldType.Properties properties) {
		super(properties);
	}

	public static void register() {
		NETHER_BOP = WorldTypes.register(
			BetterOPlenty.MOD_ID + ":bop.nether",
			new WorldTypeNetherBOP(
				WorldTypeNether.defaultProperties("worldType.betteroplenty.bop.nether")));

		BetterOPlenty.LOGGER.info(
			"Registered world type '{}' (BTA's Nether geometry: 256 tall, lava sea at Y=96).",
			BetterOPlenty.MOD_ID + ":bop.nether");
	}

	@NotNull
	@Override
	public BiomeProvider createBiomeProvider(World world) {
		return new BiomeProviderNetherBOP(world);
	}

	@NotNull
	@Override
	public Biome[] allBiomes() {

		return BOPNetherClimate.allBiomes();
	}

	@Override
	public ChunkGenerator createChunkGenerator(World world) {
		BetterOPlenty.LOGGER.info("Creating BOP Nether chunk generator for dimension {}.", world.dimension);

		return new ChunkGeneratorPerlin(
			world,
			new ChunkDecoratorNetherBOP(world),
			new TerrainGeneratorNetherBOP(world),
			new SurfaceGeneratorNether(world),
			new LargeFeature[]{
				new CavesLargeFeature(64, 256),
				new net.minecraft.core.world.generate.BiomeGatedLargeFeature(new LavaFloeLargeFeature(), b -> !BOPNetherClimate.isBOPNether(b)),
				new net.minecraft.core.world.generate.BiomeGatedLargeFeature(new RubyglassFloeLargeFeature(), b -> !BOPNetherClimate.isBOPNether(b)),
				new net.minecraft.core.world.generate.BiomeGatedLargeFeature(new ShelfLargeFeature(), b -> !BOPNetherClimate.isBOPNether(b))
			}
		) {};
	}
}
