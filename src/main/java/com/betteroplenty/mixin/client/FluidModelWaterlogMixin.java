package com.betteroplenty.mixin.client;

import com.betteroplenty.block.BOPWaterloggable;
import net.minecraft.client.render.block.model.BlockModelFluid;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.joml.primitives.AABBdc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockModelFluid.class)
public class FluidModelWaterlogMixin {

	@Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
	private void betteroplenty$continuousSeaOverWaterloggedPlants(WorldSource source, AABBdc bounds,
			TilePosc tilePos, Side side, CallbackInfoReturnable<Boolean> cir) {
		if (BOPWaterloggable.isWaterloggedPlant(source, tilePos)) {
			cir.setReturnValue(false);
		}
	}
}
