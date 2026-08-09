package net.minecraft.core.world.generate;

import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;
import java.util.Random;

import java.util.function.Predicate;

public class BiomeGatedLargeFeature extends LargeFeature {
    private final LargeFeature delegate;
    private final Predicate<Biome> biomePredicate;

    public BiomeGatedLargeFeature(LargeFeature delegate, Predicate<Biome> biomePredicate) {
        this.delegate = delegate;
        this.biomePredicate = biomePredicate;
    }

    @Override
    protected void doGeneration(World world, Random random, int chunkX, int chunkZ, int x, int z, ChunkGeneratorResult data) {
        Biome biome = world.getBiomeProvider().getBiome(x, 128, z);
        if (biomePredicate.test(biome)) {
            delegate.doGeneration(world, random, chunkX, chunkZ, x, z, data);
        }
    }
}
