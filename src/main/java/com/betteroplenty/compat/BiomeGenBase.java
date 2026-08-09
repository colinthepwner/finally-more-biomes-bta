package com.betteroplenty.compat;

import com.betteroplenty.entity.SpawnList;
import com.betteroplenty.world.BOPDecorations;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public abstract class BiomeGenBase extends Biome {

	@NotNull
	public final BiomeDecoratorBOP customBiomeDecorator = new BiomeDecoratorBOP();

	@NotNull
	public final BiomeDecoratorBOP theBiomeDecorator = customBiomeDecorator;

	public float rootHeight = 0.1f;

	public float heightVariation = 0.2f;

	@NotNull
	private final SpawnList monsterSpawns =
		new SpawnList("spawnableMonsterList", this.spawnableMonsterList, 1);

	@NotNull
	private final SpawnList creatureSpawns =
		new SpawnList("spawnableCreatureList", this.spawnableCreatureList,
			SpawnList.CREATURE_WEIGHT_SCALE);

	@NotNull
	private final SpawnList waterCreatureSpawns =
		new SpawnList("spawnableWaterCreatureList", this.spawnableWaterCreatureList, 1);

	@NotNull
	private final SpawnList ambientCreatureSpawns =
		new SpawnList("spawnableAmbientCreatureList", this.spawnableAmbientCreatureList, 1);

	@NotNull
	private final SpawnList caveCreatureSpawns =
		new SpawnList("spawnableCaveCreatureList", null, 1);

	public double seasonResist = 0.0;

	protected BiomeGenBase(@NotNull String key) {
		super(key);
	}

	@NotNull
	protected BiomeGenBase setMinMaxHeight(float min, float max) {
		this.rootHeight = min;
		this.heightVariation = max;
		return this;
	}

	public int getBiomeGrassColor() {
		return -1;
	}

	public int getBiomeFoliageColor() {
		return -1;
	}

	public int getSkyColorByTemp(float temperature) {
		return super.getSkyColor(temperature);
	}

	@Override
	public final int getSkyColor(float temperature) {
		return this.getSkyColorByTemp(temperature);
	}

	public int waterColorMultiplier = 0xFFFFFF;

	public int getBiomeWaterColor() {
		return this.waterColorMultiplier == 0xFFFFFF ? -1 : this.waterColorMultiplier;
	}

	public int getBiomeFogColor() {
		return -1;
	}

	public float getFogCloseness() {
		return 1.0F;
	}

	@NotNull
	public WorldFeatureInterface getRandomWorldGenForGrass(@NotNull Random random) {
		return BOPDecorations.defaultGrassFeature();
	}

	@NotNull
	public SpawnList spawnableMonsterList() {
		return this.monsterSpawns;
	}

	@NotNull
	public SpawnList spawnableCreatureList() {
		return this.creatureSpawns;
	}

	@NotNull
	public SpawnList spawnableWaterCreatureList() {
		return this.waterCreatureSpawns;
	}

	@NotNull
	public SpawnList spawnableAmbientCreatureList() {
		return this.ambientCreatureSpawns;
	}

	@NotNull
	public SpawnList spawnableCaveCreatureList() {
		return this.caveCreatureSpawns;
	}

	@NotNull
	public List<SpawnList> spawnLists() {
		return List.of(this.monsterSpawns, this.creatureSpawns, this.waterCreatureSpawns,
			this.ambientCreatureSpawns, this.caveCreatureSpawns);
	}

	public void registerExtraDecorations(@NotNull BOPDecorations.ExtraDecorationSink sink) {
	}
}
