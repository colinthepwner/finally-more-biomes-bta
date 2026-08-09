package com.betteroplenty.item;

import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.DamageType;

public final class BOPMaterials {
	private BOPMaterials() {}

	public static final ToolMaterial AMETHYST_TOOL = new ToolMaterial()
		.setDurability(768)
		.setEfficiency(6.5f, 10.0f)
		.setMiningLevel(2);

	public static final ArmorMaterial AMETHYST_ARMOR = ArmorMaterial.register(
		new ArmorMaterial(NamespaceID.fromPool("betteroplenty", "amethyst"), 400)
			.withProtectionPercentage(DamageType.COMBAT, 52.0f)
			.withProtectionPercentage(DamageType.BLAST, 50.0f)
			.withProtectionPercentage(DamageType.FIRE, 50.0f)
			.withProtectionPercentage(DamageType.FALL, 50.0f)

			.withProtectionPercentage(DamageType.GENERIC, 120.0f));

	public static final ToolMaterial MUD_TOOL = new ToolMaterial()
		.setDurability(32)
		.setEfficiency(0.5f, 1.0f)
		.setMiningLevel(0);

	public static final ArmorMaterial MUD_ARMOR = ArmorMaterial.register(
		new ArmorMaterial(NamespaceID.fromPool("betteroplenty", "mud"), 72)
			.withProtectionPercentage(DamageType.COMBAT, 11.0f)
			.withProtectionPercentage(DamageType.BLAST, 11.0f)
			.withProtectionPercentage(DamageType.FIRE, 11.0f)
			.withProtectionPercentage(DamageType.FALL, 11.0f)

			.withProtectionPercentage(DamageType.DROWN, 120.0f));

	public static final ArmorMaterial WADING_BOOTS_ARMOR = ArmorMaterial.register(
		new ArmorMaterial(NamespaceID.fromPool("betteroplenty", "wadingboots"), 0));

	public static final ArmorMaterial FLIPPERS_ARMOR = ArmorMaterial.register(
		new ArmorMaterial(NamespaceID.fromPool("betteroplenty", "flippers"), 0));

}
