package com.betteroplenty.client;

import biomesoplenty.entities.EntityWasp;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.core.util.helper.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.models.entity.BoneTransform;
import org.useless.dragonfly.models.entity.StaticEntityModel;

public class MobRendererWasp extends MobRenderer<EntityWasp> {

	private static final float DROP_UNITS = -12.0F;

	private static final float WOBBLE_PITCH = 2.5F * (float)Math.PI / 180.0F;

	private static final float WOBBLE_ROLL = 1.5F * (float)Math.PI / 180.0F;

	public MobRendererWasp(float shadowSize) {
		super(shadowSize);
	}

	@Override
	protected void preRenderTransform(
		@NotNull EntityWasp entity, double x, double y, double z, float yaw, float partialTick) {

		super.preRenderTransform(entity, x, y, z, yaw, partialTick);
		GLRenderer.modelM4f().rotateY((float)Math.PI);
		GLRenderer.modelM4f().translate(0.0F, DROP_UNITS, 0.0F);
	}

	@Nullable
	@Override
	protected StaticEntityModel getAndSetupModelForLayer(
		@NotNull EntityWasp entity, float brightness, float partialTick, int layer) {

		StaticEntityModel model = this.getModel("main");
		model.resetBones();

		float t = this.getLimbPitch(entity, partialTick);

		int idPhase = entity.id % 10;

		wobble(model.getTransform("head"), t, 0.1F * idPhase);
		wobble(model.getTransform("thorax"), t, 0.075F * idPhase);
		wobble(model.getTransform("abdomen"), t, 0.6F * idPhase);

		float beat = MathHelper.cos(t * 1.7F) * (float)Math.PI * 0.25F;
		BoneTransform wingRight = model.getTransform("wing_right");
		BoneTransform wingLeft = model.getTransform("wing_left");
		wingRight.rotY = beat;
		wingRight.rotZ = beat;
		wingLeft.rotY = -beat;
		wingLeft.rotZ = -beat;

		return model;
	}

	private static void wobble(BoneTransform bone, float t, float speed) {
		bone.rotX = MathHelper.sin(t * speed) * WOBBLE_PITCH;
		bone.rotZ = MathHelper.cos(t * speed) * WOBBLE_ROLL;
	}
}
