package com.betteroplenty.mixin;

import com.betteroplenty.item.BOPItems;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.generate.feature.WorldFeatureDungeon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldFeatureDungeon.class, remap = false)
public abstract class WorldFeatureDungeonLootMixin {

	private static final double LOOT_WEIGHT = 5.0;

	@Inject(
		method = "<init>(IILjava/lang/String;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/WeightedRandomBag;addEntry(Ljava/lang/Object;D)V",
			ordinal = 14,
			shift = At.Shift.AFTER))
	private void betteroplenty$addWaterGearLoot(
			int blockIdWalls, int blockIdFloor, String mobOverride, CallbackInfo ci) {
		if (BOPItems.WADING_BOOTS == null || BOPItems.FLIPPERS == null) {
			return;
		}

		WorldFeatureDungeon self = (WorldFeatureDungeon) (Object) this;
		self.chestLoot.addEntry(
			new WeightedRandomLootObject(new ItemStack(BOPItems.WADING_BOOTS)), LOOT_WEIGHT);
		self.chestLoot.addEntry(
			new WeightedRandomLootObject(new ItemStack(BOPItems.FLIPPERS)), LOOT_WEIGHT);
	}
}
