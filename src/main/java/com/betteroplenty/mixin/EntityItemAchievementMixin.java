package com.betteroplenty.mixin;

import com.betteroplenty.BOPAchievements;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityItem.class, remap = false)
public abstract class EntityItemAchievementMixin {

	@Unique
	private int betteroplenty$stackSizeBeforeTouch;

	@Inject(method = "playerTouch(Lnet/minecraft/core/entity/player/Player;)V", at = @At("HEAD"))
	private void betteroplenty$recordStackSize(Player player, CallbackInfo ci) {
		EntityItem self = (EntityItem) (Object) this;
		this.betteroplenty$stackSizeBeforeTouch = self.item == null ? 0 : self.item.stackSize;
	}

	@Inject(method = "playerTouch(Lnet/minecraft/core/entity/player/Player;)V", at = @At("TAIL"))
	private void betteroplenty$awardPickupAchievements(Player player, CallbackInfo ci) {
		EntityItem self = (EntityItem) (Object) this;
		if (self.item == null || self.item.stackSize >= this.betteroplenty$stackSizeBeforeTouch) {
			return;
		}
		BOPAchievements.onItemPickedUp(player, self.item);
	}
}
