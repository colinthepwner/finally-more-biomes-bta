package biomesoplenty.worldgen.tree;

import com.betteroplenty.block.BOPBlocks;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;

public class WorldGenDeadTree3 extends WorldGenDeadTree {

	public WorldGenDeadTree3(boolean par1) {
		super(par1);
	}

	@Override
	boolean validTreeLocation() {
		int ground = getBlockId(this.worldObj, this.basePos[0], this.basePos[1] - 1, this.basePos[2]);

		if (!Blocks.hasTag(ground, BlockTags.GROWS_TREES) && ground != BOPBlocks.RED_ROCK.id()) {
			return false;
		}

		int[] from = new int[] {this.basePos[0], this.basePos[1], this.basePos[2]};
		int[] to = new int[] {this.basePos[0], this.basePos[1] + this.heightLimit - 1, this.basePos[2]};
		int clear = this.checkBlockLine(from, to);

		if (clear == -1) {
			return true;
		} else if (clear < 6) {
			return false;
		} else {
			this.heightLimit = clear;
			return true;
		}
	}
}
