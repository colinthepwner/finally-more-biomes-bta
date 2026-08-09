package com.betteroplenty.mixin;

import com.betteroplenty.item.BOPItems;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(value = WorldFeatureLabyrinth.class, remap = false)
public abstract class WorldFeatureLabyrinthLootMixin {

	private static final double LOOT_WEIGHT = 5.0;

	@Inject(
		method = "place(Lnet/minecraft/core/world/World;Ljava/util/Random;III)Z",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/WeightedRandomBag;addEntry(Ljava/lang/Object;D)V",
			ordinal = 17,
			shift = At.Shift.AFTER))
	private void betteroplenty$addWaterGearLoot(
			World world, Random random, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
		if (BOPItems.WADING_BOOTS == null || BOPItems.FLIPPERS == null) {
			return;
		}

		WorldFeatureLabyrinth self = (WorldFeatureLabyrinth) (Object) this;
		self.chestLoot.addEntry(
			new WeightedRandomLootObject(new ItemStack(BOPItems.WADING_BOOTS)), LOOT_WEIGHT);
		self.chestLoot.addEntry(
			new WeightedRandomLootObject(new ItemStack(BOPItems.FLIPPERS)), LOOT_WEIGHT);
	}
}
