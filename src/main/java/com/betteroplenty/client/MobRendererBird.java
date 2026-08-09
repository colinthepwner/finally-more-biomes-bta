package com.betteroplenty.client;

import biomesoplenty.entities.EntityBird;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.core.util.helper.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.models.entity.BoneTransform;
import org.useless.dragonfly.models.entity.StaticEntityModel;

public class MobRendererBird extends MobRenderer<EntityBird> {

	public MobRendererBird(float shadowSize) {
		super(shadowSize);
	}

	@Nullable
	@Override
	protected StaticEntityModel getAndSetupModelForLayer(
		@NotNull EntityBird entity, float brightness, float partialTick, int layer) {

		StaticEntityModel model = this.getModel("main");
		model.resetBones();

		BoneTransform wingRight = model.getTransform("wing_right");
		BoneTransform wingLeft = model.getTransform("wing_left");

		if (this.isGliding(entity)) {
			wingRight.rotZ = 0.0;
			wingLeft.rotZ = 0.0;
		} else {
			float flap = MathHelper.cos(this.getLimbPitch(entity, partialTick) * 1.7F) *
				(float)Math.PI * 0.25F;
			wingRight.rotZ = flap;
			wingLeft.rotZ = -flap;
		}

		return model;
	}

	private boolean isGliding(EntityBird entity) {
		int x = MathHelper.floor(entity.x);
		int y = MathHelper.floor(entity.y);
		int z = MathHelper.floor(entity.z);
		return entity.y <= entity.yo && entity.world.isAirBlock(x, y - 1, z);
	}
}
