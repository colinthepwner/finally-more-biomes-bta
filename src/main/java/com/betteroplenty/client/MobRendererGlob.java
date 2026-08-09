package com.betteroplenty.client;

import biomesoplenty.entities.EntityGlob;
import net.minecraft.client.render.entity.MobRendererSlime;
import net.minecraft.core.entity.monster.MobSlime;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.models.entity.StaticEntityModel;

public class MobRendererGlob extends MobRendererSlime {

	public MobRendererGlob(float shadowSize) {
		super(shadowSize);
	}

	@Nullable
	@Override
	protected StaticEntityModel getAndSetupModelForLayer(
		@NotNull MobSlime entity, float brightness, float partialTick, int layer) {

		StaticEntityModel model = super.getAndSetupModelForLayer(entity, brightness, partialTick, layer);

		this.bindTexture(entity.getEntityTexture());
		return model;
	}
}
