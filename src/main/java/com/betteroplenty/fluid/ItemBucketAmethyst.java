package com.betteroplenty.fluid;

import net.minecraft.core.item.ItemBucket;
import org.jetbrains.annotations.NotNull;

public class ItemBucketAmethyst extends ItemBucket {

	public static final int MAX_CHARGES = 2;

	public ItemBucketAmethyst(@NotNull String name, @NotNull String namespaceId, int id) {
		super(name, namespaceId, id, MAX_CHARGES);
	}
}
