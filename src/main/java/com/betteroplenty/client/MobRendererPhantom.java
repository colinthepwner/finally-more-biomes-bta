package com.betteroplenty.client;

import biomesoplenty.entities.EntityPhantom;
import net.minecraft.client.render.entity.MobRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.models.entity.StaticEntityModel;

public class MobRendererPhantom extends MobRenderer<EntityPhantom> {

	public MobRendererPhantom(float shadowSize) {
		super(shadowSize);
	}

	@Nullable
	@Override
	protected StaticEntityModel getAndSetupModelForLayer(
		@NotNull EntityPhantom entity, float brightness, float partialTick, int layer) {
		return null;
	}
}
