package com.betteroplenty.mixin.server;

import com.betteroplenty.world.WorldTypeBOP;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.type.WorldTypes;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MinecraftServer.class, remap = false)
public abstract class MinecraftServerWorldTypeMixin {
	@Redirect(method = "startServer",
		at = @At(value = "FIELD", target = "Lnet/minecraft/core/world/type/WorldTypes;OVERWORLD_EXTENDED:Lnet/minecraft/core/world/type/WorldType;"))
	private WorldType betteroplenty$fmbByDefault() {
		return WorldTypeBOP.BOP != null ? WorldTypeBOP.BOP : WorldTypes.OVERWORLD_EXTENDED;
	}
}
