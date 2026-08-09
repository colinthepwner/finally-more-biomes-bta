package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicGrass;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.IBonemealable;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BlockLogicHolyGrass extends BlockLogicGrass implements IBonemealable {

	@NotNull private final Supplier<Block<?>> tallGrass;

	public BlockLogicHolyGrass(@NotNull Block<?> block, @NotNull Block<?> dirt,
							   @NotNull Supplier<Block<?>> tallGrass) {
		super(block, dirt);
		this.tallGrass = tallGrass;
	}

	@Override
	public boolean onBonemealUsed(@NotNull ItemStack itemStack, @Nullable Player player, @NotNull World world,
								  @NotNull TilePosc tilePos, @NotNull Side side, double xHit, double yHit) {
		if (!world.isClientSide) {
			if (player == null || player.getGamemode().hasBlockConsumption()) {
				itemStack.stackSize--;
			}

			Block<?> grass = this.tallGrass.get();
			TilePos p = new TilePos(tilePos.x(), tilePos.y() + 1, tilePos.z());
			for (int i1 = 0; i1 < 128; i1++) {
				for (int i2 = 0; i2 < i1 / 16; i2++) {
					p.x = p.x + (world.rand.nextInt(3) - 1);
					p.y = p.y + (world.rand.nextInt(3) - 1) * world.rand.nextInt(3) / 2;
					p.z = p.z + (world.rand.nextInt(3) - 1);
				}
				if (world.getBlockType(p) == Blocks.AIR
						&& (world.getFullBlockLightValue(p) >= 8 || world.canBlockSeeSky(p))
						&& grass.getLogic().canPlaceAt(world, p)) {
					world.setBlockTypeNotify(p, grass);
				}
			}
		}
		return true;
	}
}
