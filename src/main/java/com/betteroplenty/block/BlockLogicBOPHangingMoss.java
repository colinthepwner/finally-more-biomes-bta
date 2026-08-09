package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.support.ISupport;
import net.minecraft.core.block.support.PartialSupport;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

import java.util.function.Predicate;

public class BlockLogicBOPHangingMoss extends BlockLogic {

	private static final int SOUTH = 1;
	private static final int WEST = 2;
	private static final int NORTH = 4;
	private static final int EAST = 8;

	private static final double THICKNESS = 0.0625;

	@NotNull private final Predicate<Block<?>> anchor;

	private final boolean dropsByHand;

	private final boolean climbable;

	private BlockLogicBOPHangingMoss(@NotNull Block<?> block, @NotNull Predicate<Block<?>> anchor,
									 boolean dropsByHand, boolean climbable) {
		super(block, Materials.PLANT);
		this.anchor = anchor;
		this.dropsByHand = dropsByHand;
		this.climbable = climbable;
	}

	@NotNull
	public static BlockLogicBOPHangingMoss caveMoss(@NotNull Block<?> block,
													@NotNull java.util.function.Supplier<Block<?>[]> logs) {
		return new BlockLogicBOPHangingMoss(block, target -> {
			if (target == Blocks.LOG_OAK || target == Blocks.STONE) {
				return true;
			}
			for (Block<?> log : logs.get()) {
				if (target == log) {
					return true;
				}
			}
			return false;
		}, true, false);
	}

	@NotNull
	public static BlockLogicBOPHangingMoss treeMoss(@NotNull Block<?> block) {
		return drape(block, false);
	}

	@NotNull
	public static BlockLogicBOPHangingMoss drape(@NotNull Block<?> block, boolean climbable) {
		return new BlockLogicBOPHangingMoss(block,
			target -> target != null && target != Blocks.AIR
				&& target.getLogic().isCubeShaped() && target.getMaterial().blocksMotion(),
			false, climbable);
	}

	@Override
	public boolean isClimbable(@NotNull World world, @NotNull TilePosc tilePos) {
		return this.climbable;
	}

	@NotNull
	@Override
	public AABBdc getBoundsFromState(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		int data = source.getBlockData(tilePos);

		double minX = 1.0, minY = 1.0, minZ = 1.0;
		double maxX = 0.0, maxY = 0.0, maxZ = 0.0;
		boolean attached = data > 0;

		if ((data & WEST) != 0) {
			maxX = Math.max(maxX, THICKNESS);
			minX = 0.0;
			minY = 0.0;
			maxY = 1.0;
			minZ = 0.0;
			maxZ = 1.0;
			attached = true;
		}

		if ((data & EAST) != 0) {
			minX = Math.min(minX, 1.0 - THICKNESS);
			maxX = 1.0;
			minY = 0.0;
			maxY = 1.0;
			minZ = 0.0;
			maxZ = 1.0;
			attached = true;
		}

		if ((data & NORTH) != 0) {
			maxZ = Math.max(maxZ, THICKNESS);
			minZ = 0.0;
			minX = 0.0;
			maxX = 1.0;
			minY = 0.0;
			maxY = 1.0;
			attached = true;
		}

		if ((data & SOUTH) != 0) {
			minZ = Math.min(minZ, 1.0 - THICKNESS);
			maxZ = 1.0;
			minX = 0.0;
			maxX = 1.0;
			minY = 0.0;
			maxY = 1.0;
			attached = true;
		}

		if (!attached && this.anchor.test(source.getBlockType(tilePos.up(new TilePos())))) {
			minY = Math.min(minY, 1.0 - THICKNESS);
			maxY = 1.0;
			minX = 0.0;
			maxX = 1.0;
			minZ = 0.0;
			maxZ = 1.0;
		}

		return new AABBd(minX, minY, minZ, maxX, maxY, maxZ);
	}

	@Nullable
	@Override
	public AABBdc getCollisionAABB(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		return null;
	}

	@Override
	public boolean isSolidRender() {
		return false;
	}

	@Override
	public boolean isCubeShaped() {
		return false;
	}

	@NotNull
	@Override
	public ISupport getSupport(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side) {
		return PartialSupport.INSTANCE;
	}

	@Override
	public boolean canPlaceOnSide(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side) {
		TilePos query = new TilePos();
		return switch (side) {
			case TOP -> this.anchor.test(world.getBlockType(tilePos.up(query)));
			case NORTH, SOUTH, WEST, EAST ->
				this.anchor.test(world.getBlockType(tilePos.add(side.opposite().direction, query)));
			default -> false;
		};
	}

	@Override
	public int getPlacedData(@Nullable Player player, @NotNull ItemStack itemStack, @NotNull World world,
							 @NotNull TilePosc tilePos, @NotNull Side side, double xHit, double yHit) {
		return attachmentData(side);
	}

	public static int attachmentData(@NotNull Side side) {
		return bit(side.opposite().direction);
	}

	private static int bit(@NotNull Direction direction) {
		return switch (direction) {
			case SOUTH -> SOUTH;
			case WEST -> WEST;
			case NORTH -> NORTH;
			case EAST -> EAST;
			default -> 0;
		};
	}

	@Override
	public boolean canPlaceAt(@NotNull World world, @NotNull TilePosc tilePos) {
		TilePos query = new TilePos();
		for (Direction direction : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST}) {
			if (this.anchor.test(world.getBlockType(tilePos.add(direction, query)))) {
				return true;
			}
		}
		return this.anchor.test(world.getBlockType(tilePos.up(query)));
	}

	@Override
	public boolean canStay(@NotNull World world, @NotNull TilePosc tilePos) {
		return this.settle(world, tilePos, false);
	}

	@Override
	public void onNeighborChanged(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Block<?> block) {
		if (!world.isClientSide && !this.settle(world, tilePos, true)) {
			this.dropWithCause(world, EnumDropCause.WORLD, tilePos, world.getBlockData(tilePos), null, null);
			world.setBlockTypeNotify(tilePos, Blocks.AIR);
		}
	}

	private boolean settle(@NotNull World world, @NotNull TilePosc tilePos, boolean write) {
		int data = world.getBlockData(tilePos);
		int kept = data;
		TilePos query = new TilePos();

		if (data > 0) {
			for (Direction direction : new Direction[]{Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST}) {
				int mask = bit(direction);
				if ((data & mask) == 0) {
					continue;
				}
				boolean held = this.anchor.test(world.getBlockType(tilePos.add(direction, query)));
				boolean hungFromAbove = world.getBlockType(tilePos.up(query)) == this.block
					&& (world.getBlockData(tilePos.up(query)) & mask) != 0;
				if (!held && !hungFromAbove) {
					kept &= ~mask;
				}
			}
		}

		if (kept == 0 && !this.anchor.test(world.getBlockType(tilePos.up(query)))) {
			return false;
		}

		if (write && kept != data) {
			world.setBlockDataNotify(tilePos, kept);
		}
		return true;
	}

	@Nullable
	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, int data,
									  @Nullable TileEntity tileEntity) {
		if (this.dropsByHand) {
			return dropCause == EnumDropCause.IMPROPER_TOOL ? null : new ItemStack[]{new ItemStack(this.block)};
		}
		return switch (dropCause) {
			case SILK_TOUCH, PICK_BLOCK -> new ItemStack[]{new ItemStack(this.block)};
			default -> null;
		};
	}
}
