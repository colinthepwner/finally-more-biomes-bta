package com.betteroplenty.fluid.client;

import net.minecraft.client.render.item.model.ItemModelBucket;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemBucket;
import net.minecraft.core.util.collection.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ItemModelBucketAmethyst extends ItemModelBucket {

	public static final String TEXTURE_FOLDER = "bucket_amethyst";

	public ItemModelBucketAmethyst(@NotNull Item item, @NotNull String namespace, int maxCharges) {

		super(item, false);
		Set<NamespaceID> validStates = ItemBucket.getRegisteredStateIds();
		this.initializeIcons(namespace, TEXTURE_FOLDER, maxCharges, validStates);
	}
}
