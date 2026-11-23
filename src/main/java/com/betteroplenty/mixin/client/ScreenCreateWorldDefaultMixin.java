package com.betteroplenty.mixin.client;

import com.betteroplenty.world.WorldTypeBOP;
import net.minecraft.client.gui.ScreenCreateWorld;
import net.minecraft.core.world.settings.WorldConfiguration;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ScreenCreateWorld.class, remap = false)
public abstract class ScreenCreateWorldDefaultMixin {
	@Shadow @Final private WorldConfiguration worldConfiguration;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void betteroplenty$fmbByDefault(CallbackInfo ci) {
		if (WorldTypeBOP.default_group != null) worldConfiguration.setWorldTypeGroup(WorldTypeBOP.default_group);
	}
}
