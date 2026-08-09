package com.betteroplenty.mixin;

import com.betteroplenty.BOPAchievements;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.SlotResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SlotResult.class, remap = false)
public abstract class SlotResultAchievementMixin {

	@Shadow
	private Player thePlayer;

	@Inject(method = "onTake(Lnet/minecraft/core/item/ItemStack;)V", at = @At("TAIL"))
	private void betteroplenty$awardCraftAchievements(ItemStack itemStack, CallbackInfo ci) {
		BOPAchievements.onCrafted(this.thePlayer, itemStack);
	}
}
