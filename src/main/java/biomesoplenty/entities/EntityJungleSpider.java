package biomesoplenty.entities;

import net.minecraft.core.entity.monster.MobSpider;
import net.minecraft.core.world.World;

public class EntityJungleSpider extends MobSpider {

	public static final int UPSTREAM_GROUP_SIZE = 6;

	public EntityJungleSpider(World world) {
		super(world);

		this.setTextureIdentifier("betteroplenty", "jungle_spider");
		this.setSize(0.4F, 0.3F);

		this.moveSpeed = 0.95F;
	}

	@Override
	public int getMaxHealth() {
		return 6;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return UPSTREAM_GROUP_SIZE;
	}

}
