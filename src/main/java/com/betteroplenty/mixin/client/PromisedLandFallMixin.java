package com.betteroplenty.mixin.client;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.world.promised.DimensionPromisedLand;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.world.Dimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Mob.class, remap = false)
public abstract class PromisedLandFallMixin {

	@Inject(method = "outOfWorld", at = @At("HEAD"))
	private void betteroplenty$fallFromPromisedLand(CallbackInfo ci) {
		Mob self = (Mob) (Object) this;
		if (self.world == null || self.world.dimension != DimensionPromisedLand.PROMISED_LAND) {
			return;
		}

		Minecraft mc = Minecraft.getMinecraft();
		if (self != mc.thePlayer) {
			return;
		}

		if (mc.thePlayer.getGamemode().hasInvulnerablePlayer()) {
			return;
		}

		BetterOPlenty.LOGGER.info("Player fell out of the Promised Land; transferring to the "
			+ "Overworld and dropping them from the ceiling, upstream's own \"usually fatal\" arrival.");
		mc.usePortal(Dimension.OVERWORLD.id, null);

		Mob player = mc.thePlayer;
		double highY = player.world.getHeightBlocks();
		player.moveTo(player.x, highY, player.z, player.yRot, player.xRot);
		player.xd = 0.0;
		player.yd = 0.0;
		player.zd = 0.0;
	}
}
