package com.betteroplenty.client;

import biomesoplenty.entities.EntityPixie;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.core.util.helper.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.models.entity.BoneTransform;
import org.useless.dragonfly.models.entity.StaticEntityModel;

public class MobRendererPixie extends MobRenderer<EntityPixie> {

	private static final int GLOW_LAYER = 1;

	public MobRendererPixie(float shadowSize) {
		super(shadowSize);
	}

	@Override
	protected int maxRenderLayer(@NotNull EntityPixie entity) {
		return GLOW_LAYER;
	}

	@Nullable
	@Override
	protected StaticEntityModel getAndSetupModelForLayer(
		@NotNull EntityPixie entity, float brightness, float partialTick, int layer) {

		if (layer == GLOW_LAYER) {

			GLRenderer.setLightmapCoord2i(15, 15);
			GLRenderer.enableState(State.BLEND);
			GLRenderer.setBlendFunc(BlendFactor.ONE, BlendFactor.ONE);
			GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}

		StaticEntityModel model = this.getModel("main");
		model.resetBones();

		float beat = MathHelper.cos(this.getLimbPitch(entity, partialTick) * 1.7F) *
			(float)Math.PI * 0.5F;
		BoneTransform wingRight = model.getTransform("wing_right");
		BoneTransform wingLeft = model.getTransform("wing_left");
		wingRight.rotY = -beat;
		wingLeft.rotY = beat;

		return model;
	}
}
