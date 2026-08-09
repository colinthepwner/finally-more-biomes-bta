package com.betteroplenty.world.promised;

import com.betteroplenty.BetterOPlenty;
import com.betteroplenty.block.BOPPlants;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicPortal;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PromisedArrivalIsland {
	private PromisedArrivalIsland() {}

	public static final int MIN_ARRIVAL_Y = 130;

	public static final int STAFF_ARRIVAL_Y = 145;

	private static final int CLEARANCE = 3;

	private static final int ANCHOR_OFFSET = -8;

	private static final int PORTAL_X = 5;
	private static final int PORTAL_Y = 8;
	private static final int PORTAL_Z = 7;

	@NotNull
	private static Block<?> paletteBlock(int index) {
		if (PromisedArrivalTemplate.PALETTE_SIZE != 12) {
			throw new IllegalStateException(
				"Promised Land arrival island: the generated template declares "
					+ PromisedArrivalTemplate.PALETTE_SIZE + " palette entries and this switch "
					+ "answers 12. Re-run tools/transcribe_promised_arrival.py and update both.");
		}
		return switch (index) {
			case 0 -> Blocks.DIRT;
			case 1 -> Blocks.GRASS;
			case 2 -> Blocks.BLOCK_QUARTZ;
			case 3 -> Blocks.BRICK_QUARTZ;
			case 4 -> Blocks.STAIRS_BRICK_QUARTZ;
			case 5 -> Blocks.SLAB_BRICK_QUARTZ;
			case 6 -> BOPPlants.IVY;
			case 7 -> Blocks.LOG_OAK;
			case 8 -> Blocks.LEAVES_OAK;
			case 9 -> Blocks.TALLGRASS_FERN;
			case 10 -> Blocks.FLOWER_RED;
			case 11 -> Blocks.FLOWER_YELLOW;
			default -> throw new IllegalStateException(
				"Promised Land arrival island: palette index " + index + " has no block");
		};
	}

	public static int arrivalY(@NotNull World world, int originX, int originZ) {
		return arrivalY(world, originX, originZ, MIN_ARRIVAL_Y);
	}

	public static int arrivalY(@NotNull World world, int originX, int originZ, int floor) {
		int top = 0;
		for (int dx = 0; dx < PromisedArrivalTemplate.SIZE_X; dx++) {
			for (int dz = 0; dz < PromisedArrivalTemplate.SIZE_Z; dz++) {
				top = Math.max(top, world.getHeightValue(originX + dx, originZ + dz));
			}
		}
		int y = Math.max(floor, top + CLEARANCE);
		return Math.min(y, world.getWorldType().getMaxY(world) - PromisedArrivalTemplate.SIZE_Y);
	}

	public static int originX(@NotNull Entity entity) {
		return MathHelper.floor(entity.x) + ANCHOR_OFFSET;
	}

	public static int originZ(@NotNull Entity entity) {
		return MathHelper.floor(entity.z) + ANCHOR_OFFSET;
	}

	public static boolean siteIsClear(@NotNull World world, int originX, int originY, int originZ) {
		TilePos pos = new TilePos();
		for (int dx = 0; dx < PromisedArrivalTemplate.SIZE_X; dx++) {
			for (int dy = 0; dy < PromisedArrivalTemplate.SIZE_Y; dy++) {
				for (int dz = 0; dz < PromisedArrivalTemplate.SIZE_Z; dz++) {
					if (world.getBlockType(pos.set(originX + dx, originY + dy, originZ + dz))
							!= Blocks.AIR) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static boolean build(@NotNull World world, @NotNull Entity entity,
								@Nullable DyeColor portalColor) {
		return build(world, entity, portalColor, MIN_ARRIVAL_Y);
	}

	public static boolean build(@NotNull World world, @NotNull Entity entity,
								@Nullable DyeColor portalColor, int floor) {
		int originX = originX(entity);
		int originZ = originZ(entity);
		int originY = arrivalY(world, originX, originZ, floor);

		int placed = 0;
		TilePos pos = new TilePos();
		boolean savedNoNeighborUpdate = world.noNeighborUpdate;
		world.noNeighborUpdate = true;
		try {
			for (String level : PromisedArrivalTemplate.LEVELS) {
				for (int i = 0; i + 4 < level.length(); i += 5) {
					int dx = level.charAt(i) - 'A';
					int dy = level.charAt(i + 1) - 'A';
					int dz = level.charAt(i + 2) - 'A';
					int palette = level.charAt(i + 3) - 'A';
					int data = level.charAt(i + 4) - 'A';
					world.setBlockTypeDataNotify(
						pos.set(originX + dx, originY + dy, originZ + dz),
						paletteBlock(palette), data);
					placed++;
				}
			}
		} finally {
			world.noNeighborUpdate = savedNoNeighborUpdate;
		}

		Block<BlockLogicPortal> portal = com.betteroplenty.block.BOPPromisedLand.PORTAL_PROMISED;
		boolean lit = portal != null && portal.getLogic().tryToCreatePortal(
			world,
			new TilePos(originX + PORTAL_X + 1, originY + PORTAL_Y + 1, originZ + PORTAL_Z),
			portalColor);

		if (lit) {
			BetterOPlenty.LOGGER.info(
				"Built the Promised Land arrival island at ({}, {}, {}) -- {} blocks, portal lit.",
				originX, originY, originZ, placed);
		} else {
			BetterOPlenty.LOGGER.error(
				"Built the Promised Land arrival island at ({}, {}, {}) but its portal did NOT "
					+ "ignite; the template's quartz frame must have moved.",
				originX, originY, originZ);
		}
		return lit;
	}
}
