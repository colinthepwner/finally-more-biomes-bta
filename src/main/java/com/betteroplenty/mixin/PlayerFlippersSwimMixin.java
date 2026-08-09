package com.betteroplenty.mixin;

import com.betteroplenty.item.BOPItems;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerFlippersSwimMixin {

	private static final double SWIM_BOOST_HORIZONTAL = 1.125;

	private static final double SWIM_BOOST_VERTICAL = 1.1;

	@Inject(method = "moveEntityWithHeading(FF)V", at = @At("TAIL"))
	private void betteroplenty$flipperSwimBoost(
			float moveStrafing, float moveForward, CallbackInfo ci) {
		Player self = (Player) (Object) this;
		if (!self.isInWater()) {
			return;
		}

		ItemStack boots = self.getItemInArmorSlot(HumanArmorShape.BOOTS);
		if (boots == null || BOPItems.FLIPPERS == null || boots.itemID != BOPItems.FLIPPERS.id) {
			return;
		}

		self.xd *= SWIM_BOOST_HORIZONTAL;
		self.yd *= SWIM_BOOST_VERTICAL;
		self.zd *= SWIM_BOOST_HORIZONTAL;
	}
}
