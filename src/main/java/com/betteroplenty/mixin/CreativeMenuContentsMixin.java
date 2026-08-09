package com.betteroplenty.mixin;

import com.betteroplenty.fluid.BOPFluids;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemBucket;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.CreativeMenuContents;
import net.minecraft.core.util.collection.NamespaceID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = CreativeMenuContents.class, remap = false)
public abstract class CreativeMenuContentsMixin {

	@Inject(
		method = "addMiscTools(Ljava/util/List;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/player/inventory/CreativeMenuContents;"
				+ "addBucketVariants(Ljava/util/List;Lnet/minecraft/core/item/Item;)V",
			ordinal = 1))
	private static void betteroplenty$addAmethystBucketVariants(
		List<ItemStack> list, CallbackInfo ci) {
		Item item = BOPFluids.BUCKET_AMETHYST;

		if (!(item instanceof ItemBucket bucket)) {
			return;
		}

		list.add(new ItemStack(item));
		for (NamespaceID stateId : ItemBucket.getRegisteredStateIds()) {
			if (!ItemBucket.STATE_EMPTY.equals(stateId)) {
				ItemStack stack = new ItemStack(item, 1);
				ItemBucket.setState(stack, stateId);
				ItemBucket.setCharges(stack, bucket.maxCharges);
				list.add(stack);
			}
		}
		CreativeMenuContents.newLine(list);
	}
}
