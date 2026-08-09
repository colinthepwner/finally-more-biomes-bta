package com.betteroplenty.item;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.util.collection.NamespaceID;
import org.jetbrains.annotations.NotNull;

public final class BOPFlowerBands {
	private BOPFlowerBands() {}

	private static ArmorMaterial material(String tier) {
		return ArmorMaterial.register(
			new ArmorMaterial(NamespaceID.fromPool("betteroplenty", tier + "flowerband"), 0));
	}

	public static final ArmorMaterial DULL_MATERIAL = material("dull");
	public static final ArmorMaterial PLAIN_MATERIAL = material("plain");
	public static final ArmorMaterial LUSH_MATERIAL = material("lush");
	public static final ArmorMaterial EXOTIC_MATERIAL = material("exotic");

	public static Item DULL;
	public static Item PLAIN;
	public static Item LUSH;
	public static Item EXOTIC;

	@NotNull
	public static Item[] all() {
		return new Item[]{DULL, PLAIN, LUSH, EXOTIC};
	}
}
