package com.betteroplenty.world.nether;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.compat.BiomeGenBase;
import com.betteroplenty.world.BOPBiomes;
import com.betteroplenty.world.BOPDecorations;
import com.betteroplenty.world.BiomeGatedDecoration;
import com.betteroplenty.world.ChunkBiomeWindow;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.generate.chunk.perlin.nether.ChunkDecoratorNether;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChunkDecoratorNetherBOP extends ChunkDecoratorNether {

	@NotNull
	private final ChunkBiomeWindow window;

	public ChunkDecoratorNetherBOP(@NotNull World world) {
		super(world);
		this.window = new ChunkBiomeWindow(world);
	}

	@Override
	public void registerDecorations() {

		super.registerDecorations();

		List<BiomeGenBase> biomes = BOPBiomes.nether();
		if (biomes.isEmpty()) {
			BetterOPlenty.LOGGER.warn(
				"No BOP Nether biomes registered; the Nether will generate as BTA's own.");
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
					BetterOPlenty.MOD_ID + ":nether/" + entry.id() + "_" + count,
					BiomeGatedDecoration.of(entry.feature().get(), mask, entry.selector(),
						entry.method().create(count), this.window));
				decorations++;
			}
		}

		int extras = 0;
		for (BiomeGenBase biome : biomes) {
			final int[] seq = {0};
			biome.registerExtraDecorations((id, feature, selector, method) ->
				this.register(
					biome.getRegistryKey() + "/" + id + "_" + seq[0]++,
					BiomeGatedDecoration.of(feature.get(), new Biome[]{biome}, selector, method,
						this.window)));
			extras += seq[0];
		}
		decorations += extras;

		BetterOPlenty.LOGGER.info(
			"Registered {} BOP Nether decorations across {} biomes ({} from counters, {} from "
				+ "decorate() overrides), on top of BTA's own.",
			decorations, biomes.size(), decorations - extras, extras);
	}
}
