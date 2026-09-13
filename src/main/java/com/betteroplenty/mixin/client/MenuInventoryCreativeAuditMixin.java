package com.betteroplenty.mixin.client;

import com.betteroplenty.BOPAudit;
import net.minecraft.core.player.inventory.menu.MenuInventoryCreative;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MenuInventoryCreative.class, remap = false, priority = 2000)
public abstract class MenuInventoryCreativeAuditMixin {

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void betteroplenty$auditCreativeMenu(CallbackInfo ci) {
		BOPAudit.auditCreativeMenu();
	}
}
