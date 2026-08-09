package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicLeavesBase;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.IBonemealable;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.Supplier;

public class BlockLogicAppleLeaves extends BlockLogicLeavesBase
	implements IBonemealable, BlockLogic.MatcherDataEquivalency.Masked {

	public static final int MASK_GROWTH_DATA = 240;

	public static final int MAX_GROWTH_STATE = 3;

	private static final int RIPEN_RATE = 25;

	public BlockLogicAppleLeaves(@NotNull Block<?> block, @NotNull Supplier<Block<?>> sapling) {

		super(block, Materials.LEAVES, sapling.get());
	}

	public static int getGrowthRate(int meta) {
		return (meta & MASK_GROWTH_DATA) >> 4;
	}

	public static int setGrowthRate(int meta, int growthRate) {
		return meta & ~MASK_GROWTH_DATA | growthRate << 4 & MASK_GROWTH_DATA;
	}

	@Override
	public void updateTick(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Random rand,
						   boolean isRandomTick) {
		super.updateTick(world, tilePos, rand, isRandomTick);
		if (world.isClientSide) {
			return;
		}

		int rate = RIPEN_RATE;
		if (world.getSeasonManager().getCurrentSeason() != null) {

			rate = Math.max(1,
				MathHelper.floor_float(rate * world.getSeasonManager().getCurrentSeason().cropGrowthFactor));
		}

		if (rand.nextInt(rate) != 0) {
			return;
		}

		int meta = world.getBlockData(tilePos);
		int stage = getGrowthRate(meta);

		if (stage < MAX_GROWTH_STATE) {
			world.setBlockDataNotify(tilePos, setGrowthRate(meta, stage + 1));
		}
	}

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player,
								@Nullable Side side, double xHit, double yHit) {
		return this.harvest(world, tilePos, player);
	}

	@Override
	public void onAttacked(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player,
						   @NotNull Side side, double xHit, double yHit) {
		this.onInteracted(world, tilePos, player, side, xHit, yHit);
	}

	@Override
	public void onActivatorInteracted(@NotNull World world, @NotNull TilePosc tilePos,
									  @NotNull TileEntityActivator activator, @NotNull Direction direction) {
		this.harvest(world, tilePos, null);
	}

	public boolean harvest(@NotNull World world, @NotNull TilePosc tilePos, @Nullable Player player) {
		int meta = world.getBlockData(tilePos);
		if (getGrowthRate(meta) < MAX_GROWTH_STATE) {
			return false;
		}

		if (player != null) {
			world.playSoundAtEntity(player, player, "item.pickup", 1.0f, 1.0f);
		}
		if (!world.isClientSide) {

			world.dropItem(tilePos, new ItemStack(Items.FOOD_APPLE, 1));
		}
		world.setBlockDataNotify(tilePos, setGrowthRate(meta, 0));
		return true;
	}

	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, int data,
									  @Nullable TileEntity tileEntity) {
		if (dropCause == EnumDropCause.PICK_BLOCK || dropCause == EnumDropCause.SILK_TOUCH) {
			return new ItemStack[]{new ItemStack(this.block)};
		}

		ItemStack[] sapling = super.getBreakResult(world, dropCause, data, tileEntity);

		boolean apple = switch (getGrowthRate(data)) {
			case 3 -> true;
			case 2 -> world.rand.nextInt(16) == 0;
			case 1 -> world.rand.nextInt(48) == 0;
			default -> world.rand.nextInt(80) == 0;
		};

		if (!apple) {
			return sapling;
		}
		ItemStack fruit = new ItemStack(Items.FOOD_APPLE, 1);
		if (sapling == null || sapling.length == 0) {
			return new ItemStack[]{fruit};
		}
		return new ItemStack[]{sapling[0], fruit};
	}

	@Override
	public boolean onBonemealUsed(@NotNull ItemStack itemStack, @Nullable Player player,
								  @NotNull World world, @NotNull TilePosc tilePos,
								  @NotNull Side side, double xHit, double yHit) {
		int meta = world.getBlockData(tilePos);
		if (getGrowthRate(meta) >= MAX_GROWTH_STATE) {
			return false;
		}
		if (!world.isClientSide) {
			world.setBlockDataNotify(tilePos, setGrowthRate(meta, MAX_GROWTH_STATE));
			if (player == null || player.getGamemode().hasBlockConsumption()) {
				itemStack.stackSize--;
			}
		}
		return true;
	}

	@Override
	public int getMatcherDataEquivalencyMask() {
		return ~BlockLogicLeavesBase.MASK_DECAY_DATA;
	}
}
