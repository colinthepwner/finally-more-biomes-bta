package com.betteroplenty.world.promised;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.block.BOPPromisedLand;
import com.betteroplenty.world.BOPBiomes;
import com.betteroplenty.world.ChunkDecoratorBOP;
import net.minecraft.core.world.generate.chunk.perlin.drift.TerrainGeneratorDrift;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.provider.BiomeProvider;
import net.minecraft.core.world.generate.CavesLargeFeature;
import net.minecraft.core.world.generate.LargeFeature;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.generate.chunk.perlin.ChunkGeneratorPerlin;
import net.minecraft.core.world.generate.chunk.perlin.overworld.SurfaceGeneratorOverworld;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.type.WorldTypes;
import net.minecraft.core.world.type.overworld.WorldTypeOverworld;
import net.minecraft.core.world.weather.Weathers;
import org.jetbrains.annotations.NotNull;

public class WorldTypePromisedLand extends WorldTypeOverworld {

	public static WorldType PROMISED_LAND;

	public WorldTypePromisedLand(WorldType.Properties properties) {
		super(properties);
	}

	public static void register() {
		PROMISED_LAND = WorldTypes.register(
			BetterOPlenty.MOD_ID + ":bop.promised",
			new WorldTypePromisedLand(
				WorldTypeOverworld.defaultProperties("worldType.betteroplenty.bop.promised")
					.fillerBlock(BOPPromisedLand.HOLY_STONE)
					.defaultWeather(Weathers.OVERWORLD_CLEAR)
					.bounds(0, 255, 0)
					.portalBounds(0, 255)));

		BetterOPlenty.LOGGER.info("Registered world type '{}' (the Promised Land).",
			BetterOPlenty.MOD_ID + ":bop.promised");
	}

	@Override
	public float getCelestialAngle(World world, long tick, float partialTick) {
		return 0.0F;
	}

	@Override
	public int getSkyDarken(World world, long tick, float partialTick) {
		return 0;
	}

	@Override
	public boolean mayRespawn() {
		return false;
	}

	@Override
	public void onWorldCreation(World world) {
		super.onWorldCreation(world);
		world.setWorldTime(78000L);
	}

	@NotNull
	@Override
	public BiomeProvider createBiomeProvider(World world) {
		return new BiomeProviderPromisedLand(world);
	}

	@NotNull
	@Override
	public Biome[] allBiomes() {
		return BOPBiomes.promisedLand().toArray(new Biome[0]);
	}

	@Override
	public ChunkGenerator createChunkGenerator(World world) {
		BetterOPlenty.LOGGER.info("Creating Promised Land chunk generator for dimension {}.",
			world.dimension);

		return new ChunkGeneratorPerlin(
			world,
			new ChunkDecoratorBOP(world),
			new TerrainGeneratorDrift(world),
			new SurfaceGeneratorOverworld(world),
			new LargeFeature[]{new CavesLargeFeature()}
		) {};
	}
}
