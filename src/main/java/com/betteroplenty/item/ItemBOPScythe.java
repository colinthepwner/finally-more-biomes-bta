package com.betteroplenty.item;

import com.betteroplenty.block.BOPPlants;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFlower;
import net.minecraft.core.block.BlockLogicLeavesBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public class ItemBOPScythe extends Item {

	public static final class Tier {
		final int radius;
		final int leafHeight;
		final int wearTrim;
		final int wearLeaves;
		final int wearCorner;

		private Tier(int radius, int leafHeight, int wearTrim, int wearLeaves, int wearCorner) {
			this.radius = radius;
			this.leafHeight = leafHeight;
			this.wearTrim = wearTrim;
			this.wearLeaves = wearLeaves;
			this.wearCorner = wearCorner;
		}

		public static final Tier PLAIN = new Tier(1, 0, 5, 4, 5);

		public static final Tier IRON = new Tier(2, 2, 7, 6, 8);

		public static final Tier DIAMOND = new Tier(3, 3, 8, 7, 9);

		public static final Tier AMETHYST = new Tier(3, 3, 9, 8, 10);

		public static final Tier STEEL = new Tier(4, 4, 9, 8, 10);
	}

	private final ToolMaterial material;
	private final Tier tier;

	public ItemBOPScythe(@NotNull String name, @NotNull String namespaceId, int id,
						 @NotNull ToolMaterial material, @NotNull Tier tier) {
		super(name, namespaceId, id);
		this.material = material;
		this.tier = tier;
		this.maxStackSize = 1;
		this.setMaxDamage(material.getDurability());
	}

	public Tier getTier() {
		return this.tier;
	}

	@Override
	public boolean onBlockDestroyed(@NotNull ItemStack selfStack, @NotNull World world,
									@NotNull Mob mob, @NotNull Block<?> removedBlock,
									@NotNull TilePosc blockPos, @NotNull Side side) {
		int x = blockPos.x();
		int y = blockPos.y();
		int z = blockPos.z();

		if (isLeaves(removedBlock)) {
			if (this.tier.leafHeight > 0) {
				this.trimLeaves(selfStack, mob, world, x, y, z, this.tier.radius);
				return true;
			}
			return false;
		}

		this.trimCutCorner(selfStack, mob, world, x, y, z, this.tier.radius);

		if (world.rand.nextInt(3) == 0) {
			this.trim(selfStack, mob, world, x, y, z, this.tier.radius - 1);
			return true;
		}

		return false;
	}

	private void trim(ItemStack stack, Mob mob, World world, int x, int y, int z, int radius) {
		for (int aX = -radius; aX <= radius; aX++) {
			for (int aY = 0; aY <= radius; aY++) {
				for (int aZ = -radius; aZ <= radius; aZ++) {
					this.mow(stack, mob, world, x + aX, y + aY, z + aZ, this.tier.wearTrim);
				}
			}
		}
	}

	private void trimCutCorner(ItemStack stack, Mob mob, World world, int x, int y, int z, int radius) {
		for (int aX = -radius; aX <= radius; aX++) {
			for (int aY = 0; aY <= radius; aY++) {
				for (int aZ = -radius; aZ <= radius; aZ++) {

					if (Math.abs(aX) + Math.abs(aZ) < radius * 2) {
						this.mow(stack, mob, world, x + aX, y + aY, z + aZ, this.tier.wearCorner);
					}
				}
			}
		}
	}

	private void trimLeaves(ItemStack stack, Mob mob, World world, int x, int y, int z, int radius) {
		TilePos pos = new TilePos();

		for (int aX = -radius; aX <= radius; aX++) {
			for (int aY = -radius; aY <= radius; aY++) {
				for (int aZ = -radius; aZ <= radius; aZ++) {
					pos.set(x + aX, y + aY, z + aZ);
					Block<?> block = world.getBlockType(pos);

					if (block == null || block == Blocks.AIR || !isLeaves(block)) {
						continue;
					}

					this.wear(stack, mob, world, this.tier.wearLeaves);
					this.dropAndClear(world, mob, block, pos);
				}
			}
		}
	}

	private void mow(ItemStack stack, Mob mob, World world, int x, int y, int z, int wear) {
		TilePos pos = new TilePos(x, y, z);
		Block<?> block = world.getBlockType(pos);

		if (block == null || block == Blocks.AIR) {
			return;
		}

		this.wear(stack, mob, world, wear);

		if (block == BOPPlants.SHORT_GRASS || block == BOPPlants.HIGH_GRASS_TOP) {
			this.dropAndClear(world, mob, block, pos);
		} else if (block == BOPPlants.MEDIUM_GRASS) {

			this.dropAndReplace(world, mob, block, pos, BOPPlants.SHORT_GRASS);
		} else if (block == Blocks.TALLGRASS) {

			this.dropAndReplace(world, mob, block, pos, BOPPlants.MEDIUM_GRASS);
		} else if (block != Blocks.ALGAE && Block.hasLogicClass(block, BlockLogicFlower.class)) {
			this.dropAndClear(world, mob, block, pos);
		}
	}

	private void wear(ItemStack stack, Mob mob, World world, int wear) {
		if (world.rand.nextInt(wear) == 0) {
			stack.damageItem(1, mob);
		}
	}

	private void drop(World world, Mob mob, Block<?> block, TilePosc pos) {
		block.dropWithCause(world, EnumDropCause.PROPER_TOOL, pos,
			world.getBlockData(pos), world.getTileEntity(pos),
			mob instanceof Player ? (Player) mob : null);
	}

	private void dropAndClear(World world, Mob mob, Block<?> block, TilePosc pos) {
		this.drop(world, mob, block, pos);
		world.setBlockWithNotify(pos.x(), pos.y(), pos.z(), 0);
	}

	private void dropAndReplace(World world, Mob mob, Block<?> block, TilePosc pos, Block<?> with) {
		this.drop(world, mob, block, pos);
		world.setBlockWithNotify(pos.x(), pos.y(), pos.z(), with.id());
	}

	private static boolean isLeaves(@NotNull Block<?> block) {
		return Block.hasLogicClass(block, BlockLogicLeavesBase.class);
	}

	@Override
	public float getStrVsBlock(@NotNull ItemStack selfStack, @NotNull Block<?> block) {
		return isLeaves(block) ? SHEAR_SPEED : super.getStrVsBlock(selfStack, block);
	}

	private static final float SHEAR_SPEED = 15.0F;

	public ToolMaterial getMaterial() {
		return this.material;
	}
}
