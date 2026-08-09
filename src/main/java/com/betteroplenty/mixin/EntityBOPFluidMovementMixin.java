package com.betteroplenty.mixin;

import com.betteroplenty.fluid.BOPFluids;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.helper.MathHelper;
import org.joml.primitives.AABBd;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class, remap = false)
public abstract class EntityBOPFluidMovementMixin {

	@Unique
	private AABBd betteroplenty$fluidBox;

	@Unique
	private static Material[] betteroplenty$fluidMaterials;

	@Inject(method = "checkAndHandleWater(Z)Z", at = @At("RETURN"), cancellable = true)
	private void betteroplenty$bopFluidsCountAsLiquid(
		boolean addVelocity, CallbackInfoReturnable<Boolean> cir) {
		Entity self = (Entity) (Object) this;
		if (self.world == null) {
			return;
		}

		Material[] materials = betteroplenty$fluidMaterials;
		if (materials == null) {
			materials = new Material[]{
				BOPFluids.SPRING_WATER_MATERIAL, BOPFluids.LIQUID_POISON_MATERIAL
			};

			if (materials[0] != null && materials[1] != null) {
				betteroplenty$fluidMaterials = materials;
			}
		}

		AABBd box = this.betteroplenty$fluidBox;
		if (box == null) {
			box = this.betteroplenty$fluidBox = new AABBd();
		}

		MathHelper.aabbInsetBoundingBox(self.bb, 0.001, 0.001, 0.001, box);

		if (!self.world.isMaterialInBB(box, materials)) {
			return;
		}

		boolean inBOPFluid = false;
		for (Material material : materials) {
			if (self.world.handleMaterialAcceleration(box, material, self, addVelocity)) {
				inBOPFluid = true;
			}
		}

		if (inBOPFluid) {
			cir.setReturnValue(true);
		}
	}
}
