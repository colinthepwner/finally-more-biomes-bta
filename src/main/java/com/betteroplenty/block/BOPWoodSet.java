package com.betteroplenty.block;

import com.betteroplenty.BetterOPlenty;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicLeavesBase;
import net.minecraft.core.block.BlockLogicLog;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.BlockSounds;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class BOPWoodSet {

	private static final List<BOPWoodSet> REGISTERED = new ArrayList<>();

	@FunctionalInterface
	public interface TreeFeature {
		@NotNull WorldFeature get(@NotNull Random random);
	}

	@FunctionalInterface
	public interface LeavesLogic {
		@NotNull BlockLogicLeavesBase create(@NotNull Block<BlockLogicLeavesBase> block,
											 @NotNull Block<BlockLogicSaplingBOP> sapling);
	}

	@NotNull public final String key;

	@Nullable public final Block<BlockLogicLog> log;
	@NotNull public final Block<BlockLogicLeavesBase> leaves;
	@NotNull public final Block<BlockLogicSaplingBOP> sapling;

	public final boolean colorizedLeaves;

	@Nullable public final String pendingReason;

	@Nullable public final DyeColor plankColor;

	@NotNull public final String logHeartTexture;
	@NotNull public final String logSideTexture;
	@NotNull public final String saplingTexture;

	@NotNull public final String leavesModelKey;

	private BOPWoodSet(Builder b,
					   @Nullable Block<BlockLogicLog> log,
					   @NotNull Block<BlockLogicLeavesBase> leaves,
					   @NotNull Block<BlockLogicSaplingBOP> sapling) {
		this.key = b.key;
		this.log = log;
		this.leaves = leaves;
		this.sapling = sapling;
		this.colorizedLeaves = b.colorizedLeaves;
		this.pendingReason = b.pendingReason;
		this.plankColor = b.plankColor;
		this.logHeartTexture = texture(b.logHeartTexture);
		this.logSideTexture = texture(b.logSideTexture);
		this.saplingTexture = texture(b.saplingTexture);
		this.leavesModelKey = BetterOPlenty.MOD_ID + ":block/leaves/" + b.key;
	}

	private static String texture(String name) {
		return BetterOPlenty.MOD_ID + ":block/" + name;
	}

	@NotNull
	public ItemStack planks(int count) {
		return this.plankColor == null
			? new ItemStack(Blocks.PLANKS_OAK, count)
			: new ItemStack(Blocks.PLANKS_OAK_PAINTED, count, this.plankColor.blockMeta);
	}

	public boolean hasTreeFeature() {
		return this.pendingReason == null;
	}

	public static void joinItemGroups() {
		List<ItemStack> logs = Registries.ITEM_GROUPS.getItem("minecraft:logs");
		List<ItemStack> leaves = Registries.ITEM_GROUPS.getItem("minecraft:leaves");
		if (logs == null || leaves == null) {
			BetterOPlenty.LOGGER.error("BTA's log/leaf item groups do not exist yet, so BOP logs will "
				+ "not smelt to charcoal. Is joinItemGroups() being called before AFTER_GAME_START?");
			return;
		}

		int joinedLogs = 0;
		for (BOPWoodSet set : REGISTERED) {

			if (set.log != null) {
				logs.add(new ItemStack(set.log));
				joinedLogs++;
			}
			leaves.add(new ItemStack(set.leaves));
		}

		BetterOPlenty.LOGGER.info(
			"Joined BTA item groups: {} BOP log(s) into minecraft:logs, so they now smelt to charcoal "
				+ "and blast to scorched log. Also {} leaf block(s) into minecraft:leaves, which "
				+ "nothing in BTA 8.0.1 consumes -- classification only, no behaviour gained.",
			joinedLogs, REGISTERED.size());
	}

	@NotNull
	public static Builder species(@NotNull String key, int baseId) {
		return new Builder(key, baseId);
	}

	@NotNull
	public static List<BOPWoodSet> registered() {
		return REGISTERED;
	}

	public static final class Builder {
		private final String key;
		private final int baseId;

		private String logHeartTexture;
		private String logSideTexture;
		private String saplingTexture;

		@Nullable private TreeFeature tree;
		@Nullable private String pendingReason;
		@Nullable private DyeColor plankColor;
		private boolean plankColorSet;
		private boolean hasLog = true;
		private boolean colorizedLeaves;
		private boolean growsOnSand;
		@Nullable private LeavesLogic leavesLogic;

		private Builder(String key, int baseId) {
			this.key = key;
			this.baseId = baseId;

			this.logHeartTexture = "log_" + key + "_heart";
			this.logSideTexture = "log_" + key + "_side";
			this.saplingTexture = "sapling_" + key;

		}

		@NotNull
		public Builder tree(@NotNull TreeFeature tree) {
			this.tree = tree;
			this.pendingReason = null;
			return this;
		}

		@NotNull
		public Builder treePending(@NotNull String reason) {
			this.pendingReason = reason;
			this.tree = null;
			return this;
		}

		@NotNull
		public Builder noLog() {
			this.hasLog = false;
			return this;
		}

		@NotNull
		public Builder colorizedLeaves() {
			this.colorizedLeaves = true;
			return this;
		}

		@NotNull
		public Builder leavesLogic(@NotNull LeavesLogic leavesLogic) {
			this.leavesLogic = leavesLogic;
			return this;
		}

		@NotNull
		public Builder logTextures(@NotNull String heart, @NotNull String side) {
			this.logHeartTexture = heart;
			this.logSideTexture = side;
			return this;
		}

		@NotNull
		public Builder saplingTexture(@NotNull String texture) {
			this.saplingTexture = texture;
			return this;
		}

		@NotNull
		public Builder growsOnSand() {
			this.growsOnSand = true;
			return this;
		}

		@NotNull
		public Builder planks(@Nullable DyeColor color) {
			this.plankColor = color;
			this.plankColorSet = true;
			return this;
		}

		@NotNull
		public BOPWoodSet register() {
			if (this.tree == null && this.pendingReason == null) {
				throw new IllegalStateException("Wood set '" + this.key + "' has no tree generator; "
					+ "call tree(...) before register(), or treePending(...) to say what it waits on");
			}

			if (this.hasLog && !this.plankColorSet) {
				throw new IllegalStateException("Wood set '" + this.key + "' has not said which BTA "
					+ "plank it cuts into; call planks(DyeColor) before register()");
			}
			TreeFeature treeFeature = this.tree;
			boolean saplingGrowsOnSand = this.growsOnSand;

			Block<BlockLogicSaplingBOP> sapling = builder()
				.setBlockSound(BlockSounds.GRASS)
				.setHardness(0.0f)
				.setTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLANTABLE_IN_JAR)

				.setCreativeInventoryPlacement(new CreativeInventoryPlacement.After(() -> Blocks.SAPLING_OAK))
				.build(this.key + "_sapling", this.baseId + 2,
					block -> new BlockLogicSaplingBOP(block, this.key, treeFeature, saplingGrowsOnSand));

			LeavesLogic leavesLogic = this.leavesLogic != null
				? this.leavesLogic
				: (block, sap) -> new BlockLogicLeavesBase(block, Materials.LEAVES, sap);

			Block<BlockLogicLeavesBase> leaves = builder()
				.setBlockSound(BlockSounds.GRASS)
				.setHardness(0.2f)

				.setLightOpacity(1)
				.setFlammability(30, 60)

				.setTags(BlockTags.SHEARS_DO_SILK_TOUCH, BlockTags.MINEABLE_BY_AXE,
					BlockTags.MINEABLE_BY_HOE, BlockTags.MINEABLE_BY_SWORD, BlockTags.MINEABLE_BY_SHEARS)
				.setCreativeInventoryPlacement(new CreativeInventoryPlacement.After(() -> Blocks.LEAVES_PALM))
				.build(this.key + "_leaves", this.baseId + 1,
					block -> leavesLogic.create(block, sapling));

			Block<BlockLogicLog> log = !this.hasLog ? null : builder()
				.setBlockSound(BlockSounds.WOOD)
				.setHardness(2.0f)
				.setFlammability(15, 10)
				.setTags(BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_AXE)
				.setCreativeInventoryPlacement(new CreativeInventoryPlacement.After(() -> Blocks.LOG_PALM))
				.build(this.key + "_log", this.baseId, BlockLogicLog::new);

			BOPWoodSet set = new BOPWoodSet(this, log, leaves, sapling);
			REGISTERED.add(set);
			return set;
		}

		private static BlockBuilder builder() {
			return new BlockBuilder(BetterOPlenty.MOD_ID);
		}
	}
}
