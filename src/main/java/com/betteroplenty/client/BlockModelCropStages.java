package com.betteroplenty.client;

import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import org.jetbrains.annotations.NotNull;
import org.useless.dragonfly.models.block.StaticBlockModel;

public class BlockModelCropStages<T extends BlockLogic> extends BlockModelGeneric<T> {

	private final StaticBlockModel[] models;

	public BlockModelCropStages(@NotNull Block<T> block, @NotNull String[] modelKeys) {
		super(block, BlockModelDispatcher.loadDataModel(modelKeys[0]));
		this.models = new StaticBlockModel[modelKeys.length];
		for (int i = 0; i < modelKeys.length; i++) {
			this.models[i] = BlockModelDispatcher.loadDataModel(modelKeys[i]).asModel();
		}

		render3D(false);
	}

	@NotNull
	@Override
	public StaticBlockModel getModelFromData(int data) {
		return this.models[data % this.models.length];
	}
}
