package com.betteroplenty.mixin.server;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.world.WorldTypeBOP;
import com.betteroplenty.world.promised.DimensionPromisedLand;
import net.minecraft.core.world.Dimension;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MinecraftServer.class, remap = false)
public class MinecraftServerDimensionMixin {

	@Inject(
		method = "startServer",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/Dimension;init()V",
			shift = At.Shift.AFTER))
	private void betteroplenty$claimDimensionBeforeWorldsExist(CallbackInfoReturnable<Boolean> cir) {
		if (DimensionPromisedLand.register()) {
			BetterOPlenty.LOGGER.info("Claimed dimension {} before this server built its worlds, so "
					+ "it gets a WorldServer, a PlayerManager and an entity tracker like any other "
					+ "dimension. {} dimension(s) will be built.",
				DimensionPromisedLand.DIMENSION_ID, Dimension.getDimensionList().size());
		}

		WorldTypeBOP.registerWorldTypeGroup();
	}
}
