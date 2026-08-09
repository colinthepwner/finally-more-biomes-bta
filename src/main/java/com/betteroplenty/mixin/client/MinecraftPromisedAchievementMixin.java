package com.betteroplenty.mixin.client;

import com.betteroplenty.BOPAchievements;
import net.minecraft.client.Minecraft;
import net.minecraft.core.util.helper.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftPromisedAchievementMixin {

	@Inject(method = "usePortal", at = @At("TAIL"))
	private void betteroplenty$promisedLandAchievement(int dim, DyeColor portalColor,
													   CallbackInfo ci) {

		BOPAchievements.onDimensionEntered(((Minecraft) (Object) this).thePlayer, dim);
	}
}
