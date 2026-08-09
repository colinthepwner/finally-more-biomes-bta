package biomesoplenty.entities;

import com.betteroplenty.item.BOPItems;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.entity.monster.MobSlime;
import net.minecraft.core.item.Items;
import net.minecraft.core.world.World;

import java.util.List;

public class EntityGlob extends MobSlime {

	public static final int UPSTREAM_GROUP_SIZE = 1;

	public EntityGlob(World world) {
		this(world, false);
	}

	public EntityGlob(World world, boolean isSplit) {
		super(world, isSplit);

		this.setTextureIdentifier("betteroplenty", "glob");

		this.mobDrops.clear();
		this.mobDrops.add(new WeightedRandomLootObject(BOPItems.MUDBALL.getDefaultStack(), 0, 2));

		this.mobDrops.add(new WeightedRandomLootObject(Items.SLIMEBALL.getDefaultStack(), 1));
	}

	@Override
	protected void dropDeathItems() {
		super.dropDeathItems();
		if (this.random.nextInt(1000) == 0) {
			this.dropItem(BOPItems.BOP_RECORD_MUD.getDefaultStack(), 0.0F);
		}
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return UPSTREAM_GROUP_SIZE;
	}

	@Override
	public void setSlimeSize(int size) {
		this.entityData.set(DATA_SLIME_SIZE, (byte)size);
		this.setSize(0.6F * size, 0.6F * size);
		this.setHealthRaw(this.getMaxHealth());
		this.setPos(this.x, this.y, this.z);
	}

	@Override
	public void remove() {
		int size = this.getSlimeSize();

		if (!this.world.isClientSide && size > 1 && this.getHealth() <= 0) {
			int children = 2 + this.random.nextInt(3);

			for (int i = 0; i < children; i++) {
				float offsetX = (i % 2 - 0.5F) * size / 4.0F;
				float offsetZ = (i / 2 - 0.5F) * size / 4.0F;
				EntityGlob child = new EntityGlob(this.world, true);
				child.setSlimeSize(size / 2);
				child.moveTo(this.x + offsetX, this.y + 0.5, this.z + offsetZ,
					this.random.nextFloat() * 360.0F, 0.0F);
				this.world.entityJoinedWorld(child);
			}

			this.setHealthRaw(1);
		}

		super.remove();
	}

	@Override
	protected List<WeightedRandomLootObject> getMobDrops() {
		return this.mobDrops;
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F * this.getSlimeSize();
	}

	@Override
	public boolean canSpawnHere() {
		return this.random.nextInt(10) == 0 && super.canSpawnHere();
	}
}
