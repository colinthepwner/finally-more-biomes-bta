package com.betteroplenty.mixin.client;

import com.betteroplenty.world.WorldTypeBOP;
import net.minecraft.client.gui.worldsettings.WorldTypeButtonComponent;
import net.minecraft.core.world.type.WorldTypeGroups;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = WorldTypeButtonComponent.class, remap = false)
public abstract class WorldTypeButtonDefaultMixin {
	@Redirect(method = {"resetValue", "isDefault"},
		at = @At(value = "FIELD", target = "Lnet/minecraft/core/world/type/WorldTypeGroups;DEFAULT:Lnet/minecraft/core/world/type/WorldTypeGroups$Group;"),
		require = 2)
	private WorldTypeGroups.Group betteroplenty$fmbDefault() {
		return WorldTypeBOP.default_group != null ? WorldTypeBOP.default_group : WorldTypeGroups.DEFAULT;
	}
}
