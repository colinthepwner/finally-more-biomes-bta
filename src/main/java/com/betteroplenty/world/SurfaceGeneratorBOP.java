package com.betteroplenty.world;

import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.BiomeTags;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;
import net.minecraft.core.world.generate.chunk.perlin.overworld.SurfaceGeneratorOverworld;
import net.minecraft.core.world.noise.FractalNoise3D;
import net.minecraft.core.world.noise.ImprovedPerlinNoise;
import net.minecraft.core.world.noise.Noise3D;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SurfaceGeneratorBOP extends SurfaceGeneratorOverworld {

	private final World world;
	private final Noise3D beachNoise;
	private final Noise3D soilNoise;
	private final Noise3D mainNoise;

	public SurfaceGeneratorBOP(World world) {

		super(world);
		this.world = world;
		this.beachNoise = new FractalNoise3D<>(ImprovedPerlinNoise.genOctaves(world.getRandomSeed(), 4, 40));
		this.soilNoise = new FractalNoise3D<>(ImprovedPerlinNoise.genOctaves(world.getRandomSeed(), 4, 44));
		this.mainNoise = new FractalNoise3D<>(ImprovedPerlinNoise.genOctaves(world.getRandomSeed(), 8, 32));
	}

	@Override
	public void generateSurface(@NotNull Chunk chunk, @NotNull ChunkGeneratorResult result) {
		int oceanY = this.world.getWorldType().getOceanY();
		int minY = this.world.getWorldType().getMinY(this.world);
		int maxY = this.world.getWorldType().getMaxY(this.world);
		int chunkX = chunk.pos.x;
		int chunkZ = chunk.pos.z;
		int oceanBlock = this.world.getWorldType().getOceanBlockIds()[0];
		int worldFillBlock = this.world.getWorldType().getFillerBlockId();
		Random rand = new Random(chunkX * 341873128712L + chunkZ * 132897987541L);
		double beachScale = 0.03125;
		double[] sandBeachNoise = this.beachNoise.getRegion(null, chunkX * 16, chunkZ * 16, 0.0, 16, 16, 1, beachScale, beachScale, 1.0);
		double[] gravelBeachNoise = this.beachNoise.getRegion(null, chunkX * 16, 109.0134, chunkZ * 16, 16, 1, 16, beachScale, 1.0, beachScale);
		double[] soilThicknessNoise = this.soilNoise
			.getRegion(null, chunkX * 16, chunkZ * 16, 0.0, 16, 16, 1, beachScale * 2.0, beachScale * 2.0, beachScale * 2.0);
		double[] stoneLayerNoiseBasalt = this.soilNoise
			.getRegion(null, chunkX * 16, chunkZ * 16, 0.0, 16, 16, 1, beachScale * 4.0, beachScale * 4.0, beachScale * 4.0);
		double[] stoneLayerNoiseGranite = this.mainNoise
			.getRegion(null, chunkX * 16, chunkZ * 16, 0.0, 16, 16, 1, beachScale * 4.0, beachScale * 4.0, beachScale * 4.0);
		double[] stoneLayerNoiseLimestone = this.beachNoise
			.getRegion(null, chunkX * 16, chunkZ * 16, 0.0, 16, 16, 1, beachScale * 4.0, beachScale * 4.0, beachScale * 4.0);

		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {
				boolean generateSandBeach = sandBeachNoise[z + x * 16] + rand.nextDouble() * 0.2 > 0.0;
				boolean generateGravelBeach = gravelBeachNoise[z + x * 16] + rand.nextDouble() * 0.2 > 3.0;
				int soilThickness = (int) (soilThicknessNoise[z + x * 16] / 3.0 + 3.0 + rand.nextDouble() * 0.25);
				boolean generateBasaltLayer = stoneLayerNoiseBasalt[z + x * 16] + rand.nextDouble() * 0.2 > 0.0;
				boolean generateGraniteLayer = stoneLayerNoiseGranite[z + x * 16] + rand.nextDouble() * 0.2 > 2.0;
				boolean generateLimestoneLayer = stoneLayerNoiseLimestone[z + x * 16] + rand.nextDouble() * 0.2 > 3.0;

				int basaltThicknessLevel = (int) (stoneLayerNoiseBasalt[z + x] + rand.nextDouble() * 0.5);
				int graniteThicknessLevel = (int) (stoneLayerNoiseGranite[z + x] + rand.nextDouble() * 0.5);
				int limestoneThicknessLevel = (int) (stoneLayerNoiseLimestone[z + x] + rand.nextDouble() * 0.5);

				int currentLayerDepth = -1;
				short topBlock = -1;
				short fillerBlock = -1;
				Biome lastBiome = null;

				for (int y = maxY; y >= minY; y--) {
					Biome biome = chunk.getBlockBiome(x, y, z);
					if (biome == null) {
						biome = this.world.getBiomeProvider().getBiome(chunkX * 16 + x, y >> 3, chunkZ * 16 + z);
					}

					int block = result.getBlock(x, y, z);
					if ((biome != lastBiome || topBlock == -1 || fillerBlock == -1) && block == 0) {
						topBlock = (short) biome.getSurfaceProperties().getTopBlock().id();
						fillerBlock = (short) biome.getSurfaceProperties().getFillerBlock().id();
					}

					lastBiome = biome;
					if (block == 0) {
						currentLayerDepth = -1;
					} else if (block == worldFillBlock) {
						if (currentLayerDepth == -1) {
							if (soilThickness <= 0) {
								topBlock = 0;
								fillerBlock = (short) worldFillBlock;
							} else {
								boolean biomeGeneratesMud = biome.hasTag(BiomeTags.HAS_SURFACE_MUD);
								if (y >= minY + oceanY - 4 && y <= minY + oceanY + 1) {
									topBlock = (short) biome.getSurfaceProperties().getTopBlock().id();
									fillerBlock = (short) biome.getSurfaceProperties().getFillerBlock().id();

									if (!(biome instanceof BiomeGenBase)) {
										if (biomeGeneratesMud) {
											topBlock = (short) Blocks.MUD.id();
											fillerBlock = (short) Blocks.MUD.id();
										} else if (generateGravelBeach) {
											topBlock = 0;
											fillerBlock = (short) Blocks.GRAVEL.id();
										} else if (generateSandBeach) {
											topBlock = (short) Blocks.SAND.id();
											fillerBlock = (short) Blocks.SAND.id();
										}
									}
								} else if (y <= oceanY && biomeGeneratesMud && !(biome instanceof BiomeGenBase)) {
									topBlock = (short) Blocks.MUD.id();
									fillerBlock = (short) Blocks.MUD.id();
								}
							}

							if (y < minY + oceanY && topBlock == 0) {
								topBlock = (short) oceanBlock;
							}

							currentLayerDepth = soilThickness;
							if (y >= minY + oceanY - 1) {
								result.setBlock(x, y, z, topBlock);
							} else {
								result.setBlock(x, y, z, fillerBlock);
							}
						} else if (currentLayerDepth <= 0) {
							if (y >= minY + basaltThicknessLevel - rand.nextInt(3)
								&& y <= minY + 30 + basaltThicknessLevel - rand.nextInt(3) && generateBasaltLayer) {
								result.setBlock(x, y, z, Blocks.BASALT.id());
							} else if (biome == Biomes.OVERWORLD_GLACIER
								&& y >= minY + 56 + graniteThicknessLevel / 4 - rand.nextInt(3) && y <= maxY) {
								result.setBlock(x, y, z, Blocks.PERMAFROST.id());
							} else if (biome == Biomes.OVERWORLD_TUNDRA
								&& y >= minY + 56 + graniteThicknessLevel / 4 - rand.nextInt(3)
								&& y <= minY + 92 + graniteThicknessLevel / 8 - rand.nextInt(3)) {
								result.setBlock(x, y, z, Blocks.PERMAFROST.id());
							} else if (y >= minY + 64 + graniteThicknessLevel - rand.nextInt(3)
								&& y <= minY + 128 + graniteThicknessLevel - rand.nextInt(3)
								&& generateGraniteLayer) {
								result.setBlock(x, y, z, Blocks.GRANITE.id());
							} else if (y >= minY + 64 + limestoneThicknessLevel - rand.nextInt(3)
								&& y <= minY + 128 + limestoneThicknessLevel - rand.nextInt(3)
								&& generateLimestoneLayer) {
								result.setBlock(x, y, z, Blocks.LIMESTONE.id());
							}
						} else {
							if (currentLayerDepth > 0) {
								currentLayerDepth--;
								result.setBlock(x, y, z, fillerBlock);
							}

							if (currentLayerDepth == 0) {
								SubSurfaceLayer subSurfaceLayer =
									this.getSubSurfaceLayer(biome, fillerBlock, y, minY, oceanY, rand);
								if (subSurfaceLayer != null) {
									currentLayerDepth = subSurfaceLayer.depth;
									fillerBlock = subSurfaceLayer.block;
								}
							}
						}
					}
				}
			}
		}
	}
}
