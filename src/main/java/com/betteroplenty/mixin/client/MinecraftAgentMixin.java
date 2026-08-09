package com.betteroplenty.mixin.client;

import com.betteroplenty.agent.AgentMode;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, remap = false)
public abstract class MinecraftAgentMixin {

	@Inject(method = "runTick", at = @At("TAIL"))
	private void betteroplenty$agentTick(CallbackInfo ci) {
		AgentMode.tick((Minecraft) (Object) this);
	}

	@Inject(method = "screenshotListener", at = @At("HEAD"))
	private void betteroplenty$agentFrame(CallbackInfo ci) {
		AgentMode.onFrame((Minecraft) (Object) this);
	}
}
