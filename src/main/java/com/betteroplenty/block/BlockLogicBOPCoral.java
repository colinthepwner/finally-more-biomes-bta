package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.support.ISupport;
import net.minecraft.core.block.support.PartialSupport;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.IBonemealable;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBdc;

import java.util.function.Supplier;

public class BlockLogicBOPCoral extends BlockLogic implements IBonemealable {

	@FunctionalInterface
	public interface Anchor {
		boolean holds(@NotNull Block<?> block);
	}

	@NotNull private final Anchor anchor;
	@Nullable private final Supplier<Block<?>> promotesTo;
	@Nullable private final Supplier<Block<?>> dropAs;

	private boolean regrows;

	public BlockLogicBOPCoral(@NotNull Block<?> block, @NotNull Material material,
	                          @NotNull Anchor anchor,
	                          @Nullable Supplier<Block<?>> promotesTo,
	                          @Nullable Supplier<Block<?>> dropAs) {
		super(block, material);
		this.anchor = anchor;
		this.promotesTo = promotesTo;
		this.dropAs = dropAs;

		this.setBlockBounds(0.1, 0.0, 0.1, 0.9, 0.8, 0.9);
	}

	@NotNull
	public BlockLogicBOPCoral withRegrowth() {
		this.regrows = true;
		return this;
	}

	@Override
	public boolean onBonemealUsed(@NotNull ItemStack itemStack, @Nullable Player player, @NotNull World world,
								  @NotNull TilePosc tilePos, @NotNull Side side, double xHit, double yHit) {
		if (!this.regrows) {
			return false;
		}
		if (!world.isClientSide) {
			if (world.rand.nextFloat() < 0.45f) {
				new biomesoplenty.worldgen.WorldGenKelp(false)
					.generate(world, world.rand, tilePos.x(), tilePos.y(), tilePos.z());
			}
			if (player == null || player.getGamemode().hasBlockConsumption()) {
				itemStack.stackSize--;
			}
		}
		return true;
	}

	public boolean canStay(@NotNull World world, @NotNull TilePosc tilePos) {
		return this.anchor.holds(world.getBlockType(tilePos.add(0, -1, 0, new TilePos())));
	}

	@Override
	public boolean canPlaceAt(@NotNull World world, @NotNull TilePosc tilePos) {
		return world.getBlockType(tilePos).hasTag(BlockTags.IS_WATER) && this.canStay(world, tilePos);
	}

	@Override
	public void onNeighborChanged(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Block<?> neighbour) {
		if (this.promotesTo != null
			&& !BOPCorals.isKelp(world.getBlockType(tilePos.add(0, 1, 0, new TilePos())))) {
			world.setBlockType(tilePos, this.promotesTo.get());
			return;
		}

		if (!this.canStay(world, tilePos)) {
			this.block.dropBlockWithCause(world, EnumDropCause.WORLD, tilePos.x(), tilePos.y(),
				tilePos.z(), world.getBlockData(tilePos), null, null);
			world.setBlockType(tilePos, Blocks.FLUID_WATER_STILL);
		}
	}

	@Nullable
	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, int data,
	                                  @Nullable TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(this.dropAs == null ? this.block : this.dropAs.get())};
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
}
