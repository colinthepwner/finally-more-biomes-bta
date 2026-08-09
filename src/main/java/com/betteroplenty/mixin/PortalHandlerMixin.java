package com.betteroplenty.mixin;

import com.betteroplenty.world.promised.DimensionPromisedLand;
import com.betteroplenty.world.promised.PromisedArrivalIsland;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.PortalHandler;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PortalHandler.class, remap = false)
public class PortalHandlerMixin {

	@Inject(
		method = "generatePortal(Lnet/minecraft/core/world/World;Lnet/minecraft/core/entity/Entity;"
			+ "Lnet/minecraft/core/util/helper/DyeColor;Lnet/minecraft/core/world/Dimension;"
			+ "Lnet/minecraft/core/world/Dimension;)Z",
		at = @At("HEAD"), cancellable = true)
	private void betteroplenty$promisedLandArrival(World world, Entity entity, DyeColor portalColor,
												   Dimension oldDim, Dimension newDim,
												   CallbackInfoReturnable<Boolean> cir) {
		if (newDim == null || newDim != DimensionPromisedLand.PROMISED_LAND) {
			return;
		}
		cir.setReturnValue(PromisedArrivalIsland.build(world, entity, portalColor));
	}
}
