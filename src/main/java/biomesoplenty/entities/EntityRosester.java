package biomesoplenty.entities;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.animal.MobChicken;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;

public class EntityRosester extends MobChicken {

	private static ItemStack redDye() {
		return new ItemStack(Items.DYE, 1, 1);
	}

	private int timeUntilNextDye;

	public EntityRosester(World world) {
		super(world);
		this.setTextureIdentifier("betteroplenty", "rosester");

		this.setSize(0.3F, 0.7F);
		this.timeUntilNextDye = this.random.nextInt(6000) + 6000;

		this.mobDrops.add(new net.minecraft.core.WeightedRandomLootObject(redDye(), 0, 2));
	}

	@Override
	public int getMaxHealth() {
		return 4;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.layDye();
		this.growFlowers();
	}

	private void layDye() {
		if (!this.world.isClientSide && --this.timeUntilNextDye <= 0) {
			this.world.playSoundAtEntity(null, this, "mob.chickenplop", 1.0F,
				(this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

			this.dropItem(redDye(), 0.0F);
			this.timeUntilNextDye = this.random.nextInt(6000) + 6000;
		}
	}

	private void growFlowers() {
		if (this.world.isClientSide) {
			return;
		}
		for (int i = 0; i < 4; ++i) {
			int x = MathHelper.floor(this.x + (i % 2 * 2 - 1) * 0.25F);
			int y = MathHelper.floor(this.y);
			int z = MathHelper.floor(this.z + (i / 2 % 2 * 2 - 1) * 0.25F);

			if (this.world.isAirBlock(x, y, z)
				&& this.world.getBlockBiome(x, y, z).defaultTemperature > 0.3F
				&& Blocks.FLOWER_RED.canPlaceBlockAt(this.world, x, y, z)
				&& this.random.nextInt(300) == 0) {
				this.world.setBlock(x, y, z, Blocks.FLOWER_RED.id());
			}
		}
	}

}
