package com.betteroplenty.fluid;

import net.minecraft.core.util.helper.DamageType;

public final class BOPDamageTypes {
	private BOPDamageTypes() {}

	public static final DamageType POISON = new DamageType(
		"damagetype.betteroplenty.poison", false, false, "minecraft:gui/hud/protection_combat");

	public static final DamageType WITHER = new DamageType(
		"damagetype.betteroplenty.wither", false, false, "minecraft:gui/hud/protection_combat");

	public static final DamageType SICKNESS = new DamageType(
		"damagetype.betteroplenty.sickness", false, false, "minecraft:gui/hud/protection_combat");
}
