package com.betteroplenty.mixin.client;

import com.betteroplenty.client.BOPItemIcons;
import net.minecraft.client.render.item.model.ItemModelBlock;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemModelBlock.class, remap = false)
public abstract class ItemModelBlockIconMixin {

	@Inject(method = "getIcon", at = @At("HEAD"), cancellable = true)
	private void betteroplenty$bespokeInventorySprite(Entity entity, ItemStack itemStack,
			CallbackInfoReturnable<IconCoordinate> cir) {
		Item item = itemStack.getItem();
		if (!(item instanceof ItemBlock)) {
			return;
		}
		IconCoordinate icon = BOPItemIcons.iconFor(((ItemBlock<?>) item).getBlock());
		if (icon != null) {
			cir.setReturnValue(icon);
		}
	}
}
