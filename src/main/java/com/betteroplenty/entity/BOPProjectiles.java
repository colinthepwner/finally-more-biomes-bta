package com.betteroplenty.entity;

import biomesoplenty.entities.projectiles.EntityDart;
import biomesoplenty.entities.projectiles.EntityMudball;
import com.betteroplenty.BetterOPlenty;
import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.net.entity.EntityTracker;
import net.minecraft.core.net.entity.EntityTrackerEntry;
import net.minecraft.core.net.entity.ITrackedEntry;
import net.minecraft.core.net.entity.IVehicleEntry;
import net.minecraft.core.net.entity.NetEntityHandler;
import net.minecraft.core.net.packet.PacketAddEntity;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BOPProjectiles {
	private BOPProjectiles() {}

	private static final int TYPE_MUDBALL = 100;
	private static final int TYPE_DART = 101;

	private static final EntityDispatcher DISPATCHER = EntityDispatcher.getInstance();

	public static void register() {
		DISPATCHER.addMapping(EntityMudball.class,
			NamespaceID.getPermanent(BetterOPlenty.MOD_ID, "mudball"),
			EntityMudball::new,
			"entity.betteroplenty.mudball.name");

		DISPATCHER.addMapping(EntityDart.class,
			NamespaceID.getPermanent(BetterOPlenty.MOD_ID, "dart"),
			EntityDart::new,
			"entity.betteroplenty.dart.name");

		NetEntityHandler.registerNetworkEntry(new MudballEntry(), TYPE_MUDBALL);
		NetEntityHandler.registerNetworkEntry(new DartEntry(), TYPE_DART);

		verify();
	}

	private static void verify() {
		for (int type : new int[]{TYPE_MUDBALL, TYPE_DART}) {
			if (!NetEntityHandler.hasType(type)) {
				BetterOPlenty.LOGGER.error("Projectile network type {} did not register; the "
					+ "projectile will not appear on any client.", type);
			}
		}

		for (Class<?> projectile : new Class<?>[]{EntityMudball.class, EntityDart.class}) {
			EntityDispatcher.EntityDispatcherEntry<?> entry =
				EntityDispatcher.getInstance().entryForClass((Class) projectile);
			if (entry == null) {
				BetterOPlenty.LOGGER.error("Projectile {} registered but the dispatcher cannot find "
					+ "it by class; it will not survive a chunk save.", projectile.getSimpleName());
			}
		}

		BetterOPlenty.LOGGER.info("Registered 2 BOP projectiles (mudball #{}, dart #{}), both with "
			+ "a network entry.", TYPE_MUDBALL, TYPE_DART);
	}

	private static final class MudballEntry
			implements IVehicleEntry<EntityMudball>, ITrackedEntry<EntityMudball> {

		@NotNull
		@Override
		public Class<EntityMudball> getAppliedClass() {
			return EntityMudball.class;
		}

		@Override
		public int getTrackingDistance() {
			return 64;
		}

		@Override
		public int getMovementPacketDelay() {
			return 10;
		}

		@Override
		public boolean sendMotionUpdates() {
			return true;
		}

		@Override
		public void onEntityTracked(EntityTracker tracker, EntityTrackerEntry trackerEntry,
									EntityMudball trackedObject) {
		}

		@Override
		public Entity getEntity(World world, double x, double y, double z, int metadata,
								boolean hasVelocity, double xd, double yd, double zd,
								Entity owner, @Nullable CompoundTag tag) {
			return new EntityMudball(world, x, y, z);
		}

		@Override
		public PacketAddEntity getSpawnPacket(EntityTrackerEntry tracker, EntityMudball trackedObject) {
			return new PacketAddEntity(trackedObject);
		}
	}

	private static final class DartEntry
			implements IVehicleEntry<EntityDart>, ITrackedEntry<EntityDart> {

		@NotNull
		@Override
		public Class<EntityDart> getAppliedClass() {
			return EntityDart.class;
		}

		@Override
		public int getTrackingDistance() {
			return 64;
		}

		@Override
		public int getMovementPacketDelay() {
			return 20;
		}

		@Override
		public boolean sendMotionUpdates() {
			return false;
		}

		@Override
		public void onEntityTracked(EntityTracker tracker, EntityTrackerEntry trackerEntry,
									EntityDart trackedObject) {
		}

		@Override
		public Entity getEntity(World world, double x, double y, double z, int metadata,
								boolean hasVelocity, double xd, double yd, double zd,
								Entity owner, @Nullable CompoundTag tag) {
			return new EntityDart(world, x, y, z);
		}

		@Override
		public PacketAddEntity getSpawnPacket(EntityTrackerEntry tracker, EntityDart trackedObject) {
			Mob owner = trackedObject.owner;
			return new PacketAddEntity(trackedObject, 0, owner == null ? -1 : owner.id,
				trackedObject.xd, trackedObject.yd, trackedObject.zd);
		}
	}
}
