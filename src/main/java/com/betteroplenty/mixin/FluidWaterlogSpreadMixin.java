package com.betteroplenty.mixin;

import com.betteroplenty.block.BOPWaterloggable;
import net.minecraft.core.block.BlockLogicFluidFlowing;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockLogicFluidFlowing.class)
public class FluidWaterlogSpreadMixin {

	@Inject(method = "canSpreadTo", at = @At("HEAD"), cancellable = true)
	private void betteroplenty$keepWaterloggedPlants(World world, TilePos tilePos,
			CallbackInfoReturnable<Boolean> cir) {
		if (BOPWaterloggable.isWaterloggedPlant(world, tilePos)) {
			cir.setReturnValue(false);
		}
	}
}
