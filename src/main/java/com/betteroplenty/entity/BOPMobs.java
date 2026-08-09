package com.betteroplenty.entity;

import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.animal.MobChicken;
import net.minecraft.core.entity.animal.MobCow;
import net.minecraft.core.entity.animal.MobDeer;
import net.minecraft.core.entity.animal.MobPig;
import net.minecraft.core.entity.animal.MobSheep;
import net.minecraft.core.entity.animal.MobSquid;
import net.minecraft.core.entity.animal.MobWolf;
import net.minecraft.core.entity.monster.MobCreeper;
import net.minecraft.core.entity.monster.MobGhast;
import net.minecraft.core.entity.monster.MobSlime;
import net.minecraft.core.entity.monster.MobSnowman;
import net.minecraft.core.entity.monster.MobScorpion;
import net.minecraft.core.entity.monster.MobSpider;
import net.minecraft.core.entity.monster.MobZombiePig;
import org.jetbrains.annotations.Nullable;

public final class BOPMobs {
	private BOPMobs() {}

	public static final Class<? extends Mob> WOLF = MobWolf.class;

	public static final Class<? extends Mob> SLIME = MobSlime.class;

	public static final Class<? extends Mob> SPIDER = MobSpider.class;

	public static final Class<? extends Mob> CREEPER = MobCreeper.class;

	public static final Class<? extends Mob> SNOWMAN = MobSnowman.class;

	public static final Class<? extends Mob> GHAST = MobGhast.class;

	public static final Class<? extends Mob> PIG_ZOMBIE = MobZombiePig.class;

	public static final Class<? extends Mob> SHEEP = MobSheep.class;
	public static final Class<? extends Mob> PIG = MobPig.class;
	public static final Class<? extends Mob> CHICKEN = MobChicken.class;
	public static final Class<? extends Mob> COW = MobCow.class;
	public static final Class<? extends Mob> SQUID = MobSquid.class;

	public static final Class<? extends Mob> HORSE = MobDeer.class;

	public static final Class<? extends Mob> CAVE_SPIDER = MobScorpion.class;

	public static final Class<? extends Mob> MOOSHROOM = MobCow.class;

	public static final Class<? extends Mob> MAGMA_CUBE = MobSlime.class;

	@Nullable
	public static final Class<? extends Mob> ENDERMAN = null;

	@Nullable
	public static final Class<? extends Mob> WITCH = null;

	@Nullable
	public static final Class<? extends Mob> OCELOT = null;

	@Nullable
	public static final Class<? extends Mob> BAT = null;

	@Nullable
	public static final Class<? extends Mob> TWILIGHT_FOREST = null;
}
