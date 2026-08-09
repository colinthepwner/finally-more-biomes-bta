package com.betteroplenty.client;

import com.betteroplenty.block.BlockLogicAppleLeaves;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGenericLeaves;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.useless.dragonfly.models.block.StaticBlockModel;

public class BlockModelAppleLeaves<T extends BlockLogic> extends BlockModelGenericLeaves<T> {

	private static final String KEY = "betteroplenty:block/leaves/apple";

	private static final int STAGES = BlockLogicAppleLeaves.MAX_GROWTH_STATE + 1;

	@NotNull
	private final StaticBlockModel[] fancyByStage = new StaticBlockModel[STAGES];
	@NotNull
	private final StaticBlockModel[] fastByStage = new StaticBlockModel[STAGES];
	@NotNull
	private final StaticBlockModel[] smartByStage = new StaticBlockModel[STAGES];

	public BlockModelAppleLeaves(@NotNull Block<T> block) {

		super(block, KEY + "0");

		for (int stage = 0; stage < STAGES; stage++) {
			this.fancyByStage[stage] = BlockModelDispatcher.loadDataModel(KEY + stage + "_fancy").asModel();
			this.fastByStage[stage] = BlockModelDispatcher.loadDataModel(KEY + stage + "_fast").asModel();
			this.smartByStage[stage] = BlockModelDispatcher.loadDataModel(KEY + stage + "_smart").asModel();
		}
	}

	private int stage(int data) {
		int stage = BlockLogicAppleLeaves.getGrowthRate(data);
		return stage < 0 ? 0 : Math.min(stage, STAGES - 1);
	}

	@NotNull
	@Override
	public StaticBlockModel getModelFromData(int data) {
		int stage = stage(data);
		return switch ((net.minecraft.client.option.enums.LeavesQuality)
			net.minecraft.client.option.GameSettings.LEAVES_QUALITY.value) {
			case FANCY, SMART -> this.fancyByStage[stage];
			case FAST -> this.fastByStage[stage];
		};
	}

	@NotNull
	@Override
	public StaticBlockModel getModel(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		int stage = stage(source.getBlockData(tilePos));
		return switch ((net.minecraft.client.option.enums.LeavesQuality)
			net.minecraft.client.option.GameSettings.LEAVES_QUALITY.value) {
			case FANCY -> this.fancyByStage[stage];
			case SMART -> this.smartByStage[stage];
			case FAST -> this.fastByStage[stage];
		};
	}
}
