package com.betteroplenty.client;

import com.betteroplenty.compat.BiomeGenBase;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.render.block.color.BlockColorWater;
import net.minecraft.client.render.colorizer.Colorizers;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import net.minecraft.core.world.type.tag.WorldTypeTags;
import org.jetbrains.annotations.NotNull;

public class BlockColorWaterBOP extends BlockColorWater {

	@NotNull
	private final BlockColorBOP bop =
		new BlockColorBOP(Colorizers.water, BiomeGenBase::getBiomeWaterColor);

	public BlockColorWaterBOP() {
		super(Colorizers.water);
	}

	@Override
	public int getWorldColor(@NotNull WorldSource source, @NotNull TilePosc tilePos, int tintIndex) {

		if (!GameSettings.BIOME_WATER.value || source.getWorldType().hasTag(WorldTypeTags.RETRO)) {
			return super.getWorldColor(source, tilePos, tintIndex);
		}

		return this.bop.getWorldColor(source, tilePos, tintIndex);
	}
}
