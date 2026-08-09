package com.betteroplenty.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicTallGrass;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.IBonemealable;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.ItemToolShears;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BlockLogicBOPGroundCover extends BlockLogicTallGrass implements IBonemealable {

	@FunctionalInterface
	public interface Soil {
		boolean grows(@NotNull Block<?> block);
	}

	@FunctionalInterface
	public interface Contact {
		void apply(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Entity entity);
	}

	@Nullable private Soil soil;
	@Nullable private Supplier<net.minecraft.core.item.Item> dropItem;
	@Nullable private Contact contact;
	private boolean needsLight = true;
	private boolean hangsFromAbove;
	private boolean waterBeside;
	private int harvestBite;
	private int handDropOneIn;
	@Nullable private Supplier<Block<?>> stacksOn;
	@Nullable private Supplier<Block<?>> dropAs;
	@Nullable private Supplier<net.minecraft.core.item.Item> pickCrop;
	@Nullable private Supplier<Block<?>> pickSpent;
	@Nullable private Supplier<Block<?>> tallGrowthBottom;
	@Nullable private Supplier<Block<?>> tallGrowthTop;
	private boolean cactusGrowth;

	public BlockLogicBOPGroundCover(@NotNull Block<?> block) {
		super(block);
	}

	@NotNull
	public BlockLogicBOPGroundCover withSoil(@NotNull Soil soil) {
		this.soil = soil;
		return this;
	}

	@NotNull
	public BlockLogicBOPGroundCover withoutLight() {
		this.needsLight = false;
		return this;
	}

	@NotNull
	public BlockLogicBOPGroundCover hangingFromAbove() {
		this.hangsFromAbove = true;
		this.needsLight = false;
		return this;
	}

	@NotNull
	public BlockLogicBOPGroundCover withWaterBeside() {
		this.waterBeside = true;
		return this;
	}

	@NotNull
	public BlockLogicBOPGroundCover stackingOnItself(@NotNull Supplier<Block<?>> self) {
		this.stacksOn = self;
		return this;
	}

	@NotNull
	public BlockLogicBOPGroundCover withContact(@NotNull Contact contact) {
		this.contact = contact;
		return this;
	}

	@NotNull
	public BlockLogicBOPGroundCover withBounds(double minX, double minY, double minZ,
											   double maxX, double maxY, double maxZ) {
		this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
		return this;
	}

	@NotNull
	public BlockLogicBOPGroundCover withHarvestBite(int damage) {
		this.harvestBite = damage;
		return this;
	}

	@NotNull
	public BlockLogicBOPGroundCover withHandDrop(int oneIn) {
		this.handDropOneIn = oneIn;
		return this;
	}

	@NotNull
	public BlockLogicBOPGroundCover withDropItem(@NotNull Supplier<net.minecraft.core.item.Item> drop) {
		this.dropItem = drop;
		return this;
	}

	@NotNull
	public BlockLogicBOPGroundCover withDropAs(@NotNull Supplier<Block<?>> drop) {
		this.dropAs = drop;
		return this;
	}

	@NotNull
	public BlockLogicBOPGroundCover withPickable(@NotNull Supplier<net.minecraft.core.item.Item> crop,
												 @NotNull Supplier<Block<?>> spent) {
		this.pickCrop = crop;
		this.pickSpent = spent;
		return this;
	}

	@NotNull
	public BlockLogicBOPGroundCover withTallGrowth(@NotNull Supplier<Block<?>> bottom,
												   @NotNull Supplier<Block<?>> top) {
		this.tallGrowthBottom = bottom;
		this.tallGrowthTop = top;
		return this;
	}

	@NotNull
	public BlockLogicBOPGroundCover withCactusGrowth() {
		this.cactusGrowth = true;
		return this;
	}

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player,
								@Nullable net.minecraft.core.util.helper.Side side,
								double xHit, double yHit) {
		if (this.pickCrop == null || this.pickSpent == null) {
			return super.onInteracted(world, tilePos, player, side, xHit, yHit);
		}
		return this.pick(world, tilePos, player);
	}

	private boolean pick(@NotNull World world, @NotNull TilePosc tilePos, @Nullable Player player) {
		if (player != null) {
			world.playSoundAtEntity(player, player, "item.pickup", 1.0f, 1.0f);
		}

		if (!world.isClientSide) {
			world.dropItem(tilePos, new ItemStack(this.pickCrop.get(), 1));
			world.setBlockWithNotify(tilePos.x(), tilePos.y(), tilePos.z(), this.pickSpent.get().id());
		}
		return true;
	}

	@Override
	public void onActivatorInteracted(@NotNull World world, @NotNull TilePosc tilePos,
									  @NotNull net.minecraft.core.block.entity.TileEntityActivator activator,
									  @NotNull net.minecraft.core.util.helper.Direction direction) {
		if (this.pickCrop == null || this.pickSpent == null) {
			super.onActivatorInteracted(world, tilePos, activator, direction);
			return;
		}
		this.pick(world, tilePos, null);
	}

	@Override
	protected boolean mayPlaceOn(@NotNull Block<?> block) {
		if (this.stacksOn != null && block == this.stacksOn.get()) {
			return true;
		}
		return this.soil == null ? super.mayPlaceOn(block) : this.soil.grows(block);
	}

	@Override
	public boolean canStay(@NotNull World world, @NotNull TilePosc tilePos) {
		if (this.needsLight && world.getFullBlockLightValue(tilePos) < 8 && !world.canBlockSeeSky(tilePos)) {
			return false;
		}
		return this.hasSupport(world, tilePos);
	}

	@Override
	public boolean canPlaceAt(@NotNull World world, @NotNull TilePosc tilePos) {
		Block<?> here = world.getBlockType(tilePos);
		if (here != null && here != Blocks.AIR && !here.hasTag(BlockTags.PLACE_OVERWRITES)) {
			return false;
		}
		return this.hasSupport(world, tilePos);
	}

	private boolean hasSupport(@NotNull World world, @NotNull TilePosc tilePos) {
		TilePos query = new TilePos();
		TilePos anchor = this.hangsFromAbove ? tilePos.up(query) : tilePos.down(query);
		if (!this.mayPlaceOn(world.getBlockType(anchor))) {
			return false;
		}
		return !this.waterBeside || hasWaterBeside(world, anchor);
	}

	private static boolean hasWaterBeside(@NotNull World world, @NotNull TilePosc soilPos) {
		TilePos query = new TilePos();
		return world.getBlockType(soilPos.add(-1, 0, 0, query)).hasTag(BlockTags.IS_WATER)
			|| world.getBlockType(soilPos.add(1, 0, 0, query)).hasTag(BlockTags.IS_WATER)
			|| world.getBlockType(soilPos.add(0, 0, -1, query)).hasTag(BlockTags.IS_WATER)
			|| world.getBlockType(soilPos.add(0, 0, 1, query)).hasTag(BlockTags.IS_WATER);
	}

	@Nullable
	@Override
	public ItemStack[] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, int data,
									  @Nullable TileEntity tileEntity) {
		boolean byHand = dropCause != EnumDropCause.SILK_TOUCH && dropCause != EnumDropCause.PICK_BLOCK;
		if (byHand && (this.handDropOneIn <= 0 || world.rand.nextInt(this.handDropOneIn) != 0)) {
			return null;
		}
		if (this.dropItem != null) {
			net.minecraft.core.item.Item item = this.dropItem.get();
			if (item != null) {
				return new ItemStack[]{new ItemStack(item)};
			}
		}
		Block<?> drop = this.dropAs == null ? this.block : this.dropAs.get();
		return new ItemStack[]{new ItemStack(drop)};
	}

	@Override
	public void onEntityCollision(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Entity entity) {
		if (this.contact != null && !world.isClientSide && entity instanceof Mob) {
			this.contact.apply(world, tilePos, entity);
		}
	}

	@Override
	public void onHarvest(@NotNull World world, @NotNull Player player, @NotNull TilePosc tilePos,
						  int data, @Nullable TileEntity tileEntity) {
		super.onHarvest(world, player, tilePos, data, tileEntity);

		if (this.harvestBite <= 0 || world.isClientSide) {
			return;
		}
		ItemStack held = player.inventory.getCurrentItem();
		if (held == null || !(held.getItem() instanceof ItemToolShears)) {
			player.hurt(null, this.harvestBite, DamageType.COMBAT);
		}
	}

	@Override
	public boolean onBonemealUsed(@NotNull ItemStack itemStack, @Nullable Player player, @NotNull World world,
								  @NotNull TilePosc tilePos, @NotNull Side side, double xHit, double yHit) {
		if (this.tallGrowthBottom != null && this.tallGrowthTop != null) {
			if (!world.isClientSide) {

				if (world.rand.nextFloat() < 0.45f && this.canStay(world, tilePos)) {
					TilePos above = tilePos.up(new TilePos());
					world.setBlockTypeNotify(tilePos, this.tallGrowthBottom.get());
					world.setBlockTypeNotify(above, this.tallGrowthTop.get());
				}
				if (player == null || player.getGamemode().hasBlockConsumption()) {
					itemStack.stackSize--;
				}
			}
			return true;
		}

		if (this.cactusGrowth) {
			if (!world.isClientSide) {
				if (world.rand.nextFloat() < 0.45f) {

					int height = 1 + world.rand.nextInt(world.rand.nextInt(3) + 1);
					TilePos cell = new TilePos(tilePos);
					for (int i = 0; i < height; i++) {
						cell.set(tilePos.x(), tilePos.y() + i, tilePos.z());
						if (Blocks.CACTUS.getLogic().canStay(world, cell)) {
							world.setBlockTypeNotify(cell, Blocks.CACTUS);
						}
					}
				}
				if (player == null || player.getGamemode().hasBlockConsumption()) {
					itemStack.stackSize--;
				}
			}
			return true;
		}

		return false;
	}
}
