package com.betteroplenty.mixin;

import com.betteroplenty.world.promised.DimensionPromisedLand;
import net.minecraft.core.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Mob.class, remap = false)
public abstract class PromisedLandFallCommonMixin {

	@Inject(method = "outOfWorld", at = @At("HEAD"), cancellable = true)
	private void betteroplenty$cancelVoidDamageInPromisedLand(CallbackInfo ci) {
		Mob self = (Mob) (Object) this;
		if (self.world != null && self.world.dimension == DimensionPromisedLand.PROMISED_LAND) {
			ci.cancel();
		}
	}
}
