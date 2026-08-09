package com.betteroplenty.agent;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.season.Season;
import net.minecraft.core.world.season.SeasonManager;
import net.minecraft.core.world.season.SeasonManagerCycle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenMainMenu;
import net.minecraft.client.gui.ScreenPause;
import net.minecraft.client.gui.achievements.ScreenAchievements;
import net.minecraft.client.gui.achievements.data.AchievementPage;
import net.minecraft.client.gui.achievements.data.AchievementPageRegistry;
import net.minecraft.client.gui.container.ScreenInventory;
import net.minecraft.client.gui.container.ScreenInventoryCreative;
import net.minecraft.client.gui.toasts.GuiElementToastsHud;
import com.betteroplenty.client.BiomeBlendBOP;
import com.betteroplenty.client.BlockColorBOP;
import com.betteroplenty.client.MeshProfile;
import net.minecraft.client.option.GameSettings;
import net.minecraft.core.world.weather.Weather;
import net.minecraft.core.world.weather.Weathers;
import net.minecraft.core.player.inventory.menu.MenuInventoryCreative;
import net.minecraft.client.util.helper.ScreenShot;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.IArmorItem;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.gamemode.Gamemodes;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.provider.ChunkProvider;
import net.minecraft.core.world.pos.ChunkPos;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.settings.WorldConfiguration;
import net.minecraft.core.world.type.WorldTypeGroups;

import com.betteroplenty.world.DensityGeneratorBOP;
import com.betteroplenty.world.WorldTypeBOP;

final class AgentController {

	private static final int TP_DECORATED_RADIUS = 2;

	private static final int TP_GENERATED_RADIUS = TP_DECORATED_RADIUS + 1;

	private static final int TP_SETTLE_TICKS = 5;

	private static final double TP_SETTLE_TOLERANCE = 3.0;

	private static final int PENDING_TIMEOUT_TICKS = 20 * 120;

	private static final int SEASON_SETTLE_TICKS = 4;

	private static final int SCREENSHOT_SETTLE_FRAMES = 60;

	private final String worldName;
	private final long seed;
	private final File dir;
	private final String worldTypeKey;
	private final AgentBridge bridge;

	private final java.util.ArrayDeque<JsonObject> queue = new java.util.ArrayDeque<>();

	private boolean worldRequested;
	private boolean settingsApplied;

	private JsonObject pending;
	private int pendingTicks;

	private int tpX, tpZ;
	private int tpPlacedY;

	private int tpExactY = Integer.MIN_VALUE;
	private int tpSettleTicks;

	private int settleFrames = -1;
	private boolean captureIssued;

	private boolean keepTime;
	private Set<String> screenshotsBefore;
	private double hoverX, hoverY, hoverZ;

	private static final class SilentToastHud extends GuiElementToastsHud {
		SilentToastHud(Minecraft mc) {
			super(mc);
		}

		@Override
		public void render(float partialTick) {

		}
	}

	private GuiElementToastsHud toastHud;

	private boolean pinned;
	private double pinX, pinY, pinZ;
	private float pinYaw, pinPitch;

	AgentController(String worldName, long seed, File dir, String worldTypeKey) {
		this.worldName = worldName;
		this.seed = seed;
		this.dir = dir;
		this.worldTypeKey = worldTypeKey == null ? "" : worldTypeKey.trim();
		this.bridge = new AgentBridge(dir);
	}

	void tick(Minecraft mc) {
		if (!settingsApplied) {

			GameSettings.PAUSE_ON_LOST_FOCUS.value = false;
			GameSettings.RENDER_DISTANCE.value = 16;
			settingsApplied = true;
		}

		if (mc.currentWorld == null) {
			autoLoad(mc);
			return;
		}

		if (mc.currentScreen instanceof ScreenPause || mc.currentScreen instanceof ScreenMainMenu) {
			mc.displayScreen(null);
		}

		if (mc.thePlayer == null) {
			return;
		}

		queue.addAll(bridge.poll());

		if (pending != null) {
			continuePending(mc);
			return;
		}

		while (pending == null && !queue.isEmpty()) {
			execute(mc, queue.removeFirst());
		}
	}

	private void autoLoad(Minecraft mc) {
		if (worldRequested || !(mc.currentScreen instanceof ScreenMainMenu)) {
			return;
		}
		worldRequested = true;

		File save = new File(mc.getMinecraftDir(), "saves" + File.separator + worldName);
		if (new File(save, "level.dat").isFile()) {
			AgentMode.log("Loading existing world '" + worldName + "'.");

			mc.playerController = new net.minecraft.client.player.controller.PlayerControllerSP(mc);
			mc.startWorld(worldName);
			return;
		}

		WorldTypeGroups.Group group = chooseWorldTypeGroup();

		WorldConfiguration config = new WorldConfiguration();
		config.setWorldName(worldName);
		config.setNumericSeed(seed);
		config.setStringSeed("");
		config.setGamemode(Gamemodes.CREATIVE);
		config.setCheatsEnabled(true);
		config.setWorldTypeGroup(group);
		AgentMode.log("Creating world '" + worldName + "' (seed " + seed + ", overworld type "
			+ Registries.WORLD_TYPES.getKey(group.get(Dimension.OVERWORLD)) + ").");
		mc.createAndStartWorld(config);
	}

	private WorldTypeGroups.Group chooseWorldTypeGroup() {
		if (!worldTypeKey.isEmpty()) {
			StringBuilder known = new StringBuilder();
			for (WorldTypeGroups.Group candidate : WorldTypeGroups.GROUPS) {
				String key = Registries.WORLD_TYPES.getKey(candidate.get(Dimension.OVERWORLD));
				known.append(key).append(' ');
				if (worldTypeKey.equalsIgnoreCase(key)) {
					return candidate;
				}
			}
			throw new IllegalStateException("betteroplenty.agent.worldtype='" + worldTypeKey
				+ "' matches no registered overworld world type. Registered: " + known);
		}

		for (WorldTypeGroups.Group candidate : WorldTypeGroups.GROUPS) {
			if (candidate.get(Dimension.OVERWORLD) == WorldTypeBOP.BOP) {
				return candidate;
			}
		}
		throw new IllegalStateException("No world type group has BOP as its overworld type; "
			+ "was WorldTypeBOP.registerWorldTypeGroup skipped?");
	}

	private void execute(Minecraft mc, JsonObject command) {
		long id = command.has("id") ? command.get("id").getAsLong() : -1;
		String cmd = command.has("cmd") ? command.get("cmd").getAsString() : null;
		AgentMode.log("Command " + id + ": " + command);
		try {
			if (cmd == null) {
				fail(id, "missing 'cmd'");
			} else switch (cmd) {
				case "locate_biome" -> locateBiome(mc, id, command);
				case "dimension" -> dimension(mc, id, command);
				case "tp" -> beginTp(mc, id, command);
				case "screenshot" -> beginScreenshot(mc, id, command);
				case "dump" -> dump(mc, id, command);
				case "give" -> give(mc, id, command);
				case "place_block" -> placeBlock(mc, id, command);
				case "break_block" -> breakBlock(mc, id, command);
				case "interact" -> interact(mc, id, command);
				case "use_item" -> useItem(mc, id, command);
				case "use_on" -> useOn(mc, id, command);
				case "look" -> look(mc, id, command);
				case "screen" -> screen(mc, id, command);
				case "gamemode" -> gamemode(mc, id, command);
				case "equip" -> equip(mc, id, command);
				case "hold" -> hold(mc, id, command);
				case "state" -> state(mc, id, command);
				case "spawn" -> spawn(mc, id, command);
				case "clear_entities" -> clearEntities(mc, id, command);
				case "season" -> season(mc, id, command);
				case "time" -> time(mc, id, command);
				case "weather" -> weather(mc, id, command);
				case "colors" -> colors(mc, id, command);
				case "perf" -> perf(mc, id, command);
				case "quit" -> quit(mc, id);
				default -> fail(id, "unknown cmd '" + cmd + "'");
			}
		} catch (RuntimeException e) {

			fail(id, e.toString());
		}
	}

	private void locateBiome(Minecraft mc, long id, JsonObject command) {
		String key = required(command, "biome").getAsString();
		int maxRadius = command.has("max_radius") ? command.get("max_radius").getAsInt() : 10000;
		Biome target = Registries.BIOMES.getItem(key);
		if (target == null) {
			fail(id, "unknown biome '" + key + "'");
			return;
		}

		boolean wantInterior = !command.has("interior") || command.get("interior").getAsBoolean();

		int originX = (int) mc.thePlayer.x;
		int originZ = (int) mc.thePlayer.z;
		var provider = mc.currentWorld.getBiomeProvider();
		DensityGeneratorBOP density = wantInterior
			? new DensityGeneratorBOP(mc.currentWorld) : null;
		final int step = 16;
		int[] firstHit = null;
		for (int ring = 0; ring * step <= maxRadius; ring++) {
			for (int dx = -ring; dx <= ring; dx++) {
				for (int dz = -ring; dz <= ring; dz++) {
					if (Math.max(Math.abs(dx), Math.abs(dz)) != ring) {
						continue;
					}
					int x = originX + dx * step;
					int z = originZ + dz * step;
					if (provider.getBiome(x, 64, z) != target) {
						continue;
					}
					if (firstHit == null) {
						firstHit = new int[]{x, z};
					}
					if (!wantInterior || isUsableSite(provider, density, target, x, z)) {
						JsonObject result = ok(id);
						result.addProperty("found", true);
						result.addProperty("x", x);
						result.addProperty("z", z);
						result.addProperty("distance",
							(int) Math.hypot(x - originX, z - originZ));
						result.addProperty("interior", wantInterior);
						bridge.write(result);
						return;
					}
				}
			}
		}

		JsonObject result = ok(id);
		if (firstHit != null) {

			result.addProperty("found", true);
			result.addProperty("x", firstHit[0]);
			result.addProperty("z", firstHit[1]);
			result.addProperty("distance", (int) Math.hypot(firstHit[0] - originX, firstHit[1] - originZ));
			result.addProperty("interior", false);
			result.addProperty("warning", "no interior site within " + maxRadius
				+ "; this is an edge cell, so a colour read here is blended with a neighbour and the"
				+ " coastline pass may have relabelled it");
		} else {
			result.addProperty("found", false);
			result.addProperty("searched_radius", maxRadius);
		}
		bridge.write(result);
	}

	private static final int INTERIOR_RADIUS = 8;

	private static boolean isUsableSite(
			net.minecraft.core.world.biome.provider.BiomeProvider provider,
			DensityGeneratorBOP density, Biome target, int x, int z) {

		for (int dx = -INTERIOR_RADIUS; dx <= INTERIOR_RADIUS; dx += 4) {
			for (int dz = -INTERIOR_RADIUS; dz <= INTERIOR_RADIUS; dz += 4) {
				if (provider.getBiome(x + dx, 64, z + dz) != target) {
					return false;
				}
			}
		}

		int gridX = Math.floorDiv(x, 4) - 4;
		int gridZ = Math.floorDiv(z, 4) - 4;
		int[] profile = density.waterProfile(gridX, gridZ, 9, COASTLINE_DEPTHS);
		for (int sample : profile) {
			if (sample != 0) {
				return false;
			}
		}
		return true;
	}

	private static final int[] COASTLINE_DEPTHS = {112, 120, 128};

	private static final int PORTAL_COOLDOWN_TICKS = 10;

	private void dimension(Minecraft mc, long id, JsonObject command) {
		JsonElement wanted = required(command, "dimension");
		Int2ObjectMap<Dimension> dimensions = Dimension.getDimensionList();

		int target = -1;
		if (wanted.getAsJsonPrimitive().isNumber()) {
			target = wanted.getAsInt();
			if (!dimensions.containsKey(target)) {
				fail(id, "no dimension with id " + target + "; registered: " + dimensionNames(dimensions));
				return;
			}
		} else {

			String name = wanted.getAsString();
			for (Int2ObjectMap.Entry<Dimension> entry : dimensions.int2ObjectEntrySet()) {
				Dimension dim = entry.getValue();
				if (dim.languageKey.equalsIgnoreCase(name) || dim.getTranslatedName().equalsIgnoreCase(name)) {
					target = entry.getIntKey();
					break;
				}
			}
			if (target < 0) {
				fail(id, "unknown dimension '" + name + "'; registered: " + dimensionNames(dimensions));
				return;
			}
		}

		int current = mc.currentWorld.dimension.id;
		mc.thePlayer.dimension = current;

		if (current == target) {

			JsonObject same = ok(id);
			describeDimension(mc, same, target, dimensions);
			same.addProperty("changed", false);
			bridge.write(same);
			return;
		}

		mc.thePlayer.timeUntilPortal = PORTAL_COOLDOWN_TICKS;
		mc.usePortal(target, null);

		JsonObject result = ok(id);
		describeDimension(mc, result, target, dimensions);
		result.addProperty("changed", true);
		bridge.write(result);
	}

	private void describeDimension(Minecraft mc, JsonObject result, int target,
								   Int2ObjectMap<Dimension> dimensions) {
		result.addProperty("dimension", dimensions.get(target).getTranslatedName());
		result.addProperty("id_num", target);
		result.addProperty("x", (int) Math.floor(mc.thePlayer.x));
		result.addProperty("y", (int) Math.floor(mc.thePlayer.y));
		result.addProperty("z", (int) Math.floor(mc.thePlayer.z));

		result.addProperty("world_type",
			mc.currentWorld == null ? null : Registries.WORLD_TYPES.getKey(mc.currentWorld.getWorldType()));
	}

	private static String dimensionNames(Int2ObjectMap<Dimension> dimensions) {
		StringBuilder names = new StringBuilder();
		for (Int2ObjectMap.Entry<Dimension> entry : dimensions.int2ObjectEntrySet()) {
			if (names.length() > 0) {
				names.append(", ");
			}
			names.append(entry.getValue().languageKey)
				.append(" / \"").append(entry.getValue().getTranslatedName())
				.append("\" (").append(entry.getIntKey()).append(")");
		}
		return names.toString();
	}

	private void beginTp(Minecraft mc, long id, JsonObject command) {
		tpX = required(command, "x").getAsInt();
		tpZ = required(command, "z").getAsInt();

		tpExactY = command.has("y") && !command.get("y").isJsonNull()
			? command.get("y").getAsInt() : Integer.MIN_VALUE;

		mc.thePlayer.moveTo(tpX + 0.5, 250.0, tpZ + 0.5, mc.thePlayer.yRot, 30.0F);
		tpPlacedY = Integer.MIN_VALUE;
		tpSettleTicks = 0;
		setPending(command);
	}

	private void beginScreenshot(Minecraft mc, long id, JsonObject command) {

		if (command.has("view")) {
			String view = command.get("view").getAsString();
			GameSettings.THIRD_PERSON_VIEW.value = switch (view) {
				case "third" -> 1;
				case "front" -> 2;
				default -> 0;
			};
		}

		if (command.has("fullbright")) {
			mc.fullbright = command.get("fullbright").getAsBoolean();

			if (mc.renderGlobal != null) {
				mc.renderGlobal.allChanged();
			}
		}

		if (command.has("immersive")) {
			int level = command.get("immersive").getAsInt();
			GameSettings.IMMERSIVE_MODE.set(level);
			if (level >= 2 && !(mc.guiToasts instanceof SilentToastHud)) {
				toastHud = mc.guiToasts;
				mc.guiToasts = new SilentToastHud(mc);
			} else if (level < 2 && toastHud != null) {
				mc.guiToasts = toastHud;
				toastHud = null;
			}
		}

		if (pinned) {
			hoverX = pinX;
			hoverY = pinY;
			hoverZ = pinZ;
		} else {
			int px = (int) Math.floor(mc.thePlayer.x);
			int pz = (int) Math.floor(mc.thePlayer.z);
			hoverX = px + 0.5;

			int floor = landingY(mc.currentWorld, px, pz);
			double hover = floor + 18.0;
			if (mc.currentWorld.getWorldType().hasCeiling()) {
				int maxY = mc.currentWorld.getHeightBlocks() - 1;
				int headroom = floor;
				while (headroom < maxY && mc.currentWorld.isAirBlock(px, headroom + 1, pz)) {
					headroom++;
				}
				hover = Math.min(hover, headroom);
			}
			hoverY = Math.min(hover, 250.0);
			hoverZ = pz + 0.5;
		}

		if (command.has("keep_time")) {
			keepTime = command.get("keep_time").getAsBoolean();
		}
		screenshotsBefore = listScreenshots();
		settleFrames = SCREENSHOT_SETTLE_FRAMES;
		captureIssued = false;
		setPending(command);
	}

	private void setPending(JsonObject command) {
		pending = command;
		pendingTicks = 0;
	}

	private void continuePending(Minecraft mc) {
		long id = pending.has("id") ? pending.get("id").getAsLong() : -1;
		if (++pendingTicks > PENDING_TIMEOUT_TICKS) {
			String cmd = pending.get("cmd").getAsString();
			pending = null;
			settleFrames = -1;
			fail(id, cmd + " timed out after " + PENDING_TIMEOUT_TICKS + " ticks");
			return;
		}

		switch (pending.get("cmd").getAsString()) {
			case "tp" -> continueTp(mc, id);
			case "screenshot" -> continueScreenshot(mc, id);
			case "season" -> continueSeason(mc, id);
			case "interact" -> continueInteract(mc, id);
			default -> pending = null;
		}
	}

	private void continueTp(Minecraft mc, long id) {
		ChunkProvider provider = mc.currentWorld.getChunkProvider();
		int centerX = tpX >> 4;
		int centerZ = tpZ >> 4;

		for (int dx = -TP_GENERATED_RADIUS; dx <= TP_GENERATED_RADIUS; dx++) {
			for (int dz = -TP_GENERATED_RADIUS; dz <= TP_GENERATED_RADIUS; dz++) {
				provider.provideChunk(new ChunkPos(centerX + dx, centerZ + dz), true);
			}
		}
		for (int dx = -TP_DECORATED_RADIUS; dx <= TP_DECORATED_RADIUS; dx++) {
			for (int dz = -TP_DECORATED_RADIUS; dz <= TP_DECORATED_RADIUS; dz++) {
				ChunkPos pos = new ChunkPos(centerX + dx, centerZ + dz);
				if (!provider.isChunkLoaded(pos)) {
					return;
				}
				Chunk chunk = provider.provideChunk(pos, true);
				if (!chunk.isTerrainPopulated) {
					return;
				}
			}
		}

		int surface = tpExactY != Integer.MIN_VALUE
			? tpExactY - 1 : landingY(mc.currentWorld, tpX, tpZ);

		if (tpPlacedY != surface + 1
			|| Math.abs(mc.thePlayer.y - (surface + 1.0)) > TP_SETTLE_TOLERANCE) {
			mc.thePlayer.moveTo(tpX + 0.5, surface + 1.0, tpZ + 0.5, mc.thePlayer.yRot, 20.0F);
			tpPlacedY = surface + 1;
			tpSettleTicks = 0;
			return;
		}
		if (++tpSettleTicks < TP_SETTLE_TICKS) {
			return;
		}

		Biome biome = mc.currentWorld.getBlockBiome(new TilePos(tpX, surface, tpZ));

		pending = null;
		JsonObject result = ok(id);
		result.addProperty("x", tpX);
		result.addProperty("y", surface + 1);
		result.addProperty("z", tpZ);
		result.addProperty("biome", biome == null ? null : Registries.BIOMES.getKey(biome));
		result.addProperty("waited_ticks", pendingTicks);
		bridge.write(result);
	}

	private void continueScreenshot(Minecraft mc, long id) {

		if (!captureIssued) {
			return;
		}
		File fresh = newestNewScreenshot();
		if (fresh == null || fresh.length() == 0) {
			return;
		}

		String label = pending.has("label")
			? pending.get("label").getAsString().replaceAll("[^A-Za-z0-9._-]", "_")
			: "screenshot";
		File dest = new File(dir, label + "-" + id + ".png");
		try {
			Files.copy(fresh.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			pending = null;
			fail(id, "screenshot taken but copy failed: " + e);
			return;
		}

		pending = null;
		JsonObject result = ok(id);
		result.addProperty("path", dest.getAbsolutePath());
		bridge.write(result);
	}

	void frame(Minecraft mc) {
		if (pending == null || settleFrames < 0 || mc.currentWorld == null || mc.thePlayer == null) {
			return;
		}
		if (settleFrames == SCREENSHOT_SETTLE_FRAMES && !keepTime) {

			long time = mc.currentWorld.getWorldTime();
			mc.currentWorld.setWorldTime(time + ((6000 - time % 24000) + 24000) % 24000);
		}

		mc.thePlayer.moveTo(hoverX, hoverY, hoverZ,
			pinned ? pinYaw : mc.thePlayer.yRot, pinned ? pinPitch : 32.0F);
		if (--settleFrames > 0) {
			return;
		}

		ScreenShot.saveScreenshot(mc, mc.getMinecraftDir(),
			mc.gameWindow.getWidthPixels(), mc.gameWindow.getHeightPixels());
		captureIssued = true;
		settleFrames = -1;
	}

	private Set<String> listScreenshots() {
		File[] files = new File(Minecraft.getMinecraft().getMinecraftDir(), "screenshots").listFiles();
		Set<String> names = new HashSet<>();
		if (files != null) {
			for (File f : files) {
				names.add(f.getName());
			}
		}
		return names;
	}

	private File newestNewScreenshot() {
		File[] files = new File(Minecraft.getMinecraft().getMinecraftDir(), "screenshots").listFiles();
		File newest = null;
		if (files != null) {
			for (File f : files) {
				if (f.getName().endsWith(".png") && !screenshotsBefore.contains(f.getName())
					&& (newest == null || f.lastModified() > newest.lastModified())) {
					newest = f;
				}
			}
		}
		return newest;
	}

	private void spawn(Minecraft mc, long id, JsonObject command) {
		String key = required(command, "entity").getAsString();
		World world = mc.currentWorld;

		double x = command.has("x") ? command.get("x").getAsDouble() : mc.thePlayer.x;
		double z = command.has("z") ? command.get("z").getAsDouble() : mc.thePlayer.z;
		double y = command.has("y") ? command.get("y").getAsDouble()
			: world.getHeightValue((int) Math.floor(x), (int) Math.floor(z)) + 6.0;
		int count = command.has("count") ? command.get("count").getAsInt() : 1;
		count = Math.min(Math.max(count, 1), 16);

		int spawned = 0;
		for (int i = 0; i < count; i++) {
			Entity entity = EntityDispatcher.getInstance().createEntityInWorld(key, world);
			if (entity == null) {
				fail(id, "unknown or unregistered entity '" + key + "'");
				return;
			}

			double jitterX = i == 0 ? 0.0 : (world.rand.nextDouble() - 0.5) * 3.0;
			double jitterZ = i == 0 ? 0.0 : (world.rand.nextDouble() - 0.5) * 3.0;
			entity.moveTo(x + 0.5 + jitterX, y, z + 0.5 + jitterZ, world.rand.nextFloat() * 360.0F, 0.0F);
			if (world.entityJoinedWorld(entity)) {
				spawned++;
			}
		}

		JsonObject result = ok(id);
		result.addProperty("entity", key);
		result.addProperty("spawned", spawned);
		result.addProperty("x", x + 0.5);
		result.addProperty("y", y);
		result.addProperty("z", z + 0.5);
		bridge.write(result);
	}

	private void season(Minecraft mc, long id, JsonObject command) {
		String want = required(command, "season").getAsString().trim().toLowerCase(Locale.ROOT);
		World world = mc.currentWorld;

		SeasonManager manager = world.getSeasonManager();
		if (manager == null) {
			fail(id, "this world has no season manager");
			return;
		}
		if (!(manager instanceof SeasonManagerCycle cycle)) {
			fail(id, "season manager is " + manager.getClass().getSimpleName()
				+ ", not a cycle -- this world's season cannot be moved");
			return;
		}

		List<Season> seasons = cycle.getSeasons();
		Season target = null;
		StringBuilder known = new StringBuilder();
		for (Season season : seasons) {
			String seasonId = season.getId().toLowerCase(Locale.ROOT);
			if (known.length() > 0) {
				known.append(", ");
			}
			known.append(season.getId());

			if (seasonId.equals(want)
				|| seasonId.endsWith("." + want)
				|| seasonId.endsWith(":" + want)) {
				target = season;
			}
		}
		if (target == null) {
			fail(id, "unknown season '" + want + "' -- this world has: " + known);
			return;
		}

		long offset = 0L;
		for (Season season : seasons) {
			if (season == target) {
				break;
			}
			offset += cycle.getSeasonLengthTicks(season);
		}

		long yearLength = cycle.getYearLengthTicks();
		long now = world.getWorldTime();
		long targetTime = (now - Math.floorMod(now, yearLength))
			+ offset + (cycle.getSeasonLengthTicks(target) / 2L);
		while (targetTime <= now) {
			targetTime += yearLength;
		}
		world.setWorldTime(targetTime);

		setPending(command);
	}

	private void continueSeason(Minecraft mc, long id) {
		if (pendingTicks < SEASON_SETTLE_TICKS) {
			return;
		}
		SeasonManager manager = mc.currentWorld.getSeasonManager();
		Season now = manager == null ? null : manager.getCurrentSeason();
		String requested = pending.get("season").getAsString();
		pending = null;

		JsonObject result = ok(id);
		result.addProperty("requested", requested);
		result.addProperty("season", now == null ? "none" : now.getId());
		result.addProperty("world_time", mc.currentWorld.getWorldTime());
		if (manager instanceof SeasonManagerCycle cycle) {
			result.addProperty("day_in_season", cycle.getDayInSeason());
		}
		bridge.write(result);
	}

	private void colors(Minecraft mc, long id, JsonObject command) {
		World world = mc.currentWorld;
		int x = MathHelper.floor(mc.thePlayer.x);
		int z = MathHelper.floor(mc.thePlayer.z);
		int y = command.has("y") ? command.get("y").getAsInt() : world.getHeightValue(x, z);
		TilePos pos = new TilePos(x, y, z);

		BlockColorDispatcher dispatcher = BlockColorDispatcher.getInstance();
		JsonObject result = ok(id);
		result.addProperty("x", x);
		result.addProperty("y", y);
		result.addProperty("z", z);

		Biome biome = world.getBlockBiome(x, y, z);
		result.addProperty("biome", biome == null ? "null"
			: String.valueOf(Registries.BIOMES.getKey(biome)));

		SeasonManager manager = world.getSeasonManager();
		Season season = manager == null ? null : manager.getCurrentSeason();
		result.addProperty("season", season == null ? "none" : season.getId());

		net.minecraft.client.render.block.color.BlockColor grass = dispatcher.getDispatch(Blocks.GRASS);
		net.minecraft.client.render.block.color.BlockColor foliage = dispatcher.getDispatch(Blocks.LEAVES_OAK);
		net.minecraft.client.render.block.color.BlockColor water = dispatcher.getDispatch(Blocks.FLUID_WATER_STILL);

		result.addProperty("grass", hex(grass.getWorldColor(world, pos, 0)));
		result.addProperty("foliage", hex(foliage.getWorldColor(world, pos, 0)));
		result.addProperty("water", hex(water.getWorldColor(world, pos, 0)));
		result.addProperty("grass_via", grass.getClass().getSimpleName());
		result.addProperty("foliage_via", foliage.getClass().getSimpleName());
		result.addProperty("water_via", water.getClass().getSimpleName());

		bridge.write(result);
	}

	private static String hex(int color) {
		return String.format("#%06X", color & 0xFFFFFF);
	}

	private void dump(Minecraft mc, long id, JsonObject command) {
		int radius = command.has("radius") ? command.get("radius").getAsInt() : 16;
		radius = Math.min(Math.max(radius, 1), 64);
		World world = mc.currentWorld;
		int px = (int) Math.floor(mc.thePlayer.x);
		int pz = (int) Math.floor(mc.thePlayer.z);

		Map<String, Long> blockCounts = new TreeMap<>();
		Map<String, Long> biomeCounts = new TreeMap<>();
		int minSurface = Integer.MAX_VALUE;
		int maxSurface = Integer.MIN_VALUE;
		long air = 0;

		String locateKey = command.has("locate") ? command.get("locate").getAsString() : null;
		Block<?> locateBlock = locateKey == null ? null : resolveBlock(locateKey);
		if (locateKey != null && locateBlock == null) {
			fail(id, "unknown block '" + locateKey + "'");
			return;
		}
		int foundX = 0, foundY = -1, foundZ = 0;
		long bestDistance = Long.MAX_VALUE;

		for (int x = px - radius; x <= px + radius; x++) {
			for (int z = pz - radius; z <= pz + radius; z++) {
				int surface = world.getHeightValue(x, z);
				minSurface = Math.min(minSurface, surface);
				maxSurface = Math.max(maxSurface, surface);
				Biome biome = world.getBlockBiome(new TilePos(x, Math.max(surface - 1, 0), z));
				if (biome != null) {
					biomeCounts.merge(Registries.BIOMES.getKey(biome), 1L, Long::sum);
				}
				for (int y = 0; y < world.getHeightBlocks(); y++) {
					int blockId = world.getBlockId(x, y, z);
					if (blockId == 0) {
						air++;
						continue;
					}
					Block<?> block = Blocks.getBlock(blockId);
					blockCounts.merge(block == null ? "unknown:" + blockId
						: block.namespaceId().toString(), 1L, Long::sum);
					if (locateBlock != null && block == locateBlock) {
						long dx = x - px;
						long dy = y - (long) Math.floor(mc.thePlayer.y);
						long dz = z - pz;
						long distance = dx * dx + dy * dy + dz * dz;
						if (distance < bestDistance) {
							bestDistance = distance;
							foundX = x;
							foundY = y;
							foundZ = z;
						}
					}
				}
			}
		}

		JsonObject payload = new JsonObject();
		if (locateBlock != null) {
			JsonObject located = new JsonObject();
			located.addProperty("block", locateBlock.namespaceId().toString());
			located.addProperty("found", foundY >= 0);
			if (foundY >= 0) {
				located.addProperty("x", foundX);
				located.addProperty("y", foundY);
				located.addProperty("z", foundZ);
			}
			payload.add("located", located);
		}
		payload.addProperty("center_x", px);
		payload.addProperty("center_z", pz);
		payload.addProperty("radius", radius);
		payload.addProperty("air", air);
		payload.addProperty("surface_min", minSurface);
		payload.addProperty("surface_max", maxSurface);
		JsonObject blocks = new JsonObject();
		blockCounts.forEach(blocks::addProperty);
		payload.add("blocks", blocks);
		JsonObject biomes = new JsonObject();
		biomeCounts.forEach(biomes::addProperty);
		payload.add("biome_columns", biomes);

		if (command.has("chunks") && command.get("chunks").getAsBoolean()) {
			JsonObject chunks = new JsonObject();
			ChunkProvider provider = world.getChunkProvider();
			for (int cx = (px - radius) >> 4; cx <= (px + radius) >> 4; cx++) {
				for (int cz = (pz - radius) >> 4; cz <= (pz + radius) >> 4; cz++) {
					ChunkPos pos = new ChunkPos(cx, cz);
					String state = !provider.isChunkLoaded(pos) ? "unloaded"
						: (provider.provideChunk(pos, true).isTerrainPopulated
							? "populated" : "generated");
					chunks.addProperty(cx + "," + cz, state);
				}
			}
			payload.add("chunk_population", chunks);
		}

		JsonObject result = ok(id);
		String rendered = payload.toString();
		if (rendered.length() <= 16 * 1024) {
			result.add("dump", payload);
		} else {
			File out = new File(dir, "dump-" + id + ".json");
			try (FileWriter w = new FileWriter(out, StandardCharsets.UTF_8)) {
				w.write(rendered);
			} catch (IOException e) {
				fail(id, "dump too large to inline and file write failed: " + e);
				return;
			}
			result.addProperty("dump_path", out.getAbsolutePath());
		}
		bridge.write(result);
	}

	private void quit(Minecraft mc, long id) {

		bridge.write(ok(id));
		AgentMode.log("Quit: saving world and shutting down.");

		mc.shutdown();
	}

	private void interact(Minecraft mc, long id, JsonObject command) {
		int x = required(command, "x").getAsInt();
		int y = required(command, "y").getAsInt();
		int z = required(command, "z").getAsInt();
		String expect = command.has("expect") ? command.get("expect").getAsString() : null;
		this.interactExpect = expect;
		this.interactBefore = expect == null ? 0 : countInInventory(mc, expect);
		this.interactBlockBefore = blockNameAt(mc.currentWorld, x, y, z);
		this.interactPos = new TilePos(x, y, z);

		emptyHand(mc);
		this.interactHandled = mc.playerController.useOrPlaceItemStackOnTile(
			mc.thePlayer, mc.currentWorld, null, this.interactPos, Side.TOP, 0.5, 0.5);

		this.interactTicks = command.has("ticks") ? command.get("ticks").getAsInt() : 20;
		setPending(command);
	}

	private void continueInteract(Minecraft mc, long id) {
		if (pendingTicks < this.interactTicks) {
			return;
		}
		pending = null;

		JsonObject result = ok(id);
		result.addProperty("handled", this.interactHandled);
		result.addProperty("block_was", this.interactBlockBefore);
		if (this.interactClearedSlot) {
			result.addProperty("cleared_slot", true);
		}
		result.addProperty("block_now", blockNameAt(mc.currentWorld,
			this.interactPos.x(), this.interactPos.y(), this.interactPos.z()));
		if (this.interactExpect != null) {
			int now = countInInventory(mc, this.interactExpect);
			result.addProperty("expect", this.interactExpect);
			result.addProperty("gained", now - this.interactBefore);
			result.addProperty("held_total", now);
		}
		bridge.write(result);
	}

	private void emptyHand(Minecraft mc) {
		var inventory = mc.thePlayer.inventory;
		for (int slot = 0; slot < 9; slot++) {
			if (inventory.mainInventory[slot] == null) {
				inventory.setCurrentSlot(slot, true);
				this.interactClearedSlot = false;
				return;
			}
		}
		inventory.setItem(inventory.getCurrentSlot(), null);
		this.interactClearedSlot = true;
	}

	private static int countInInventory(Minecraft mc, String key) {
		int total = 0;
		for (ItemStack stack : mc.thePlayer.inventory.mainInventory) {
			if (stack != null && stack.getItem() != null
					&& key.equals(stack.getItem().namespaceID.toString())) {
				total += stack.stackSize;
			}
		}
		return total;
	}

	private String interactExpect;
	private String interactBlockBefore;
	private TilePos interactPos = new TilePos(0, 0, 0);
	private int interactBefore;
	private int interactTicks;
	private boolean interactHandled;
	private boolean interactClearedSlot;

	private void placeBlock(Minecraft mc, long id, JsonObject command) {
		int x = required(command, "x").getAsInt();
		int y = required(command, "y").getAsInt();
		int z = required(command, "z").getAsInt();
		String key = required(command, "block").getAsString();
		Block<?> block = resolveBlock(key);
		if (block == null) {
			fail(id, "unknown block '" + key + "'");
			return;
		}

		ItemStack stack = new ItemStack(block, 64);
		mc.thePlayer.inventory.setItem(mc.thePlayer.inventory.getCurrentSlot(), stack);
		boolean placed = mc.playerController.useOrPlaceItemStackOnTile(
			mc.thePlayer, mc.currentWorld, stack, new TilePos(x, y - 1, z), Side.TOP, 0.5, 1.0);
		JsonObject result = ok(id);
		result.addProperty("placed", placed);
		result.addProperty("block_at_target", blockNameAt(mc.currentWorld, x, y, z));
		bridge.write(result);
	}

	private void breakBlock(Minecraft mc, long id, JsonObject command) {
		int x = required(command, "x").getAsInt();
		int y = required(command, "y").getAsInt();
		int z = required(command, "z").getAsInt();
		String before = blockNameAt(mc.currentWorld, x, y, z);

		boolean keepHeld = command.has("keep_held") && command.get("keep_held").getAsBoolean();
		if (!keepHeld) {
			mc.thePlayer.inventory.setItem(mc.thePlayer.inventory.getCurrentSlot(), null);
		}
		ItemStack held = mc.thePlayer.getCurrentEquippedItem();
		int damageBefore = held == null ? 0 : held.getMetadata();

		boolean removed = mc.playerController.destroyBlock(new TilePos(x, y, z), Side.TOP);

		JsonObject result = ok(id);
		result.addProperty("removed", removed);
		result.addProperty("block_was", before);
		if (keepHeld) {
			ItemStack after = mc.thePlayer.getCurrentEquippedItem();
			result.addProperty("held", after == null ? null : after.getItem().namespaceID.toString());

			result.addProperty("damage_taken", after == null ? 0 : after.getMetadata() - damageBefore);
		}
		bridge.write(result);
	}

	private void look(Minecraft mc, long id, JsonObject command) {
		if (command.has("pin") && !command.get("pin").getAsBoolean()) {
			boolean was = pinned;
			pinned = false;
			JsonObject cleared = ok(id);
			cleared.addProperty("pinned", false);
			cleared.addProperty("was_pinned", was);

			cleared.addProperty("x", mc.thePlayer.x);
			cleared.addProperty("y", mc.thePlayer.y);
			cleared.addProperty("z", mc.thePlayer.z);
			bridge.write(cleared);
			return;
		}
		pinYaw = required(command, "yaw").getAsFloat();
		pinPitch = required(command, "pitch").getAsFloat();
		pinX = command.has("x") ? command.get("x").getAsDouble() : mc.thePlayer.x;
		pinY = command.has("y") ? command.get("y").getAsDouble() : mc.thePlayer.y;
		pinZ = command.has("z") ? command.get("z").getAsDouble() : mc.thePlayer.z;
		pinned = true;
		mc.thePlayer.moveTo(pinX, pinY, pinZ, pinYaw, pinPitch);
		JsonObject result = ok(id);
		result.addProperty("pinned", true);
		result.addProperty("x", pinX);
		result.addProperty("y", pinY);
		result.addProperty("z", pinZ);
		result.addProperty("yaw", pinYaw);
		result.addProperty("pitch", pinPitch);
		bridge.write(result);
	}

	private void screen(Minecraft mc, long id, JsonObject command) {
		String which = command.has("screen") ? command.get("screen").getAsString() : "none";
		if (!which.equals("none") && !which.equals("inventory") && !which.equals("mob_spawning")
			&& !which.equals("achievements")) {
			fail(id, "unknown screen '" + which
				+ "'; expected \"inventory\", \"achievements\", \"mob_spawning\" or \"none\"");
			return;
		}

		if (which.equals("achievements")) {
			achievements(mc, id, command);
			return;
		}

		if (which.equals("mob_spawning")) {
			int scroll = command.has("scroll") ? command.get("scroll").getAsInt() : 0;
			mc.displayScreen(new ScreenMobSpawningProbe(scroll));
			JsonObject result = ok(id);
			result.addProperty("screen", mc.currentScreen == null ? null
				: mc.currentScreen.getClass().getSimpleName());
			result.addProperty("scroll", scroll);
			result.addProperty("mob_rows", ScreenMobSpawningProbe.namedMobCount());
			bridge.write(result);
			return;
		}

		MenuInventoryCreative creative =
			mc.thePlayer.inventoryMenu instanceof MenuInventoryCreative menu ? menu : null;
		if (creative != null && command.has("search")) {
			creative.searchPage(command.get("search").getAsString());
		}

		mc.displayScreen(which.equals("none") ? null
			: mc.thePlayer.getGamemode() == Gamemodes.CREATIVE
				? new ScreenInventoryCreative(mc.thePlayer)
				: new ScreenInventory(mc.thePlayer));

		if (creative != null && command.has("page")) {
			creative.setInventoryStatus(command.get("page").getAsInt(), creative.searchText);
		}

		JsonObject result = ok(id);
		result.addProperty("screen",
			mc.currentScreen == null ? null : mc.currentScreen.getClass().getSimpleName());
		if (creative != null) {
			result.addProperty("search", creative.searchText);
			result.addProperty("page", creative.page);
			result.addProperty("max_page", creative.maxPage);
			result.addProperty("matches", countCreativeMatches(creative));
		}
		bridge.write(result);
	}

	private void time(Minecraft mc, long id, JsonObject command) {
		if (mc.currentWorld == null) {
			fail(id, "no world");
			return;
		}
		final long DAY = 24000L;
		long wanted;
		if (command.has("time") && command.get("time").getAsJsonPrimitive().isNumber()) {
			wanted = Math.floorMod(command.get("time").getAsLong(), DAY);
		} else {
			String want = command.has("time") ? command.get("time").getAsString() : "";
			Long named = switch (want.toLowerCase(Locale.ROOT)) {
				case "dawn", "sunrise" -> 0L;
				case "day", "noon", "midday" -> 6000L;
				case "dusk", "sunset" -> 12000L;
				case "night", "midnight" -> 18000L;
				default -> null;
			};
			if (named == null) {
				fail(id, "time wants a tick 0-23999 or one of dawn, day/noon, dusk, night/midnight;"
					+ " got '" + want + "'");
				return;
			}
			wanted = named;
		}

		long now = mc.currentWorld.getWorldTime();
		long target = (now - Math.floorMod(now, DAY)) + wanted;
		while (target <= now) {
			target += DAY;
		}
		mc.currentWorld.setWorldTime(target);

		JsonObject result = ok(id);
		result.addProperty("was", now);
		result.addProperty("was_time_of_day", Math.floorMod(now, DAY));
		result.addProperty("world_time", target);
		result.addProperty("time_of_day", wanted);
		result.addProperty("advanced_ticks", target - now);

		result.addProperty("daylight", wanted < 12000L);
		bridge.write(result);
	}

	private void clearEntities(Minecraft mc, long id, JsonObject command) {
		if (mc.currentWorld == null || mc.thePlayer == null) {
			fail(id, "no world");
			return;
		}
		double radius = command.has("radius") ? command.get("radius").getAsDouble() : 96.0;
		double limit = radius * radius;
		double px = mc.thePlayer.x;
		double pz = mc.thePlayer.z;

		Map<String, Integer> kinds = new TreeMap<>();
		int removed = 0;

		for (Entity entity : new ArrayList<>(mc.currentWorld.entities)) {
			if (entity == null || entity == mc.thePlayer || entity.removed) {
				continue;
			}
			double dx = entity.x - px;
			double dz = entity.z - pz;
			if (dx * dx + dz * dz > limit) {
				continue;
			}

			kinds.merge(entity.getClass().getSimpleName(), 1, Integer::sum);
			entity.remove();
			removed++;
		}

		JsonObject result = ok(id);
		result.addProperty("removed", removed);
		result.addProperty("radius", radius);

		result.addProperty("remaining", mc.currentWorld.entities.size());
		JsonObject byKind = new JsonObject();
		kinds.forEach(byKind::addProperty);
		result.add("kinds", byKind);
		bridge.write(result);
	}

	private void weather(Minecraft mc, long id, JsonObject command) {
		if (mc.currentWorld == null) {
			fail(id, "no world");
			return;
		}
		JsonElement wanted = required(command, "weather");
		Weather target = null;
		if (wanted.getAsJsonPrimitive().isNumber()) {
			target = Weathers.getWeather(wanted.getAsInt());
		} else {
			String want = wanted.getAsString().toLowerCase(Locale.ROOT);

			for (Weather candidate : Weathers.WEATHERS) {
				if (candidate == null) {
					continue;
				}
				String candidateKey = candidate.getLanguageKey().toLowerCase(Locale.ROOT);
				if (candidateKey.equals(want) || candidateKey.endsWith("." + want)) {
					if (target == null || candidateKey.length() < target.getLanguageKey().length()) {
						target = candidate;
					}
				}
			}
		}
		if (target == null) {
			StringBuilder known = new StringBuilder();
			for (Weather candidate : Weathers.WEATHERS) {
				if (candidate != null) {
					known.append(known.length() == 0 ? "" : ", ").append(candidate.getLanguageKey());
				}
			}
			fail(id, "unknown weather '" + wanted + "'; registered: " + known);
			return;
		}

		long duration = command.has("duration") ? command.get("duration").getAsLong() : 240_000L;
		boolean clear = target.getLightLevelSubtracted() == 0 && !target.isDamp();
		float intensity = command.has("intensity")
			? command.get("intensity").getAsFloat() : (clear ? 0.0F : 1.0F);
		float power = command.has("power")
			? command.get("power").getAsFloat() : (clear ? 0.0F : 1.0F);

		Weather was = mc.currentWorld.getCurrentWeather();
		mc.currentWorld.getWeatherManager()
			.overrideWeather(target, target, duration, intensity, power);

		JsonObject result = ok(id);
		result.addProperty("weather", target.getLanguageKey());
		result.addProperty("was", was == null ? null : was.getLanguageKey());
		result.addProperty("intensity", intensity);
		result.addProperty("power", power);
		result.addProperty("duration", duration);

		result.addProperty("light_subtracted", target.getLightLevelSubtracted());
		result.addProperty("fog_distance_multiplier", target.getFogDistanceMultiplier());
		bridge.write(result);
	}

	private void gamemode(Minecraft mc, long id, JsonObject command) {
		String wanted = command.has("mode") ? command.get("mode").getAsString() : "";
		Gamemode mode = switch (wanted.toLowerCase(Locale.ROOT)) {
			case "survival" -> Gamemodes.SURVIVAL;
			case "creative" -> Gamemodes.CREATIVE;
			case "hardcore" -> Gamemodes.HARDCORE;
			case "adventure" -> Gamemodes.ADVENTURE;
			case "spectator" -> Gamemodes.SPECTATOR;
			default -> null;
		};
		if (mode == null) {
			fail(id, "unknown gamemode '" + wanted
				+ "'; expected survival, creative, hardcore, adventure or spectator");
			return;
		}

		String was = mc.thePlayer.getGamemode().getId();
		mc.thePlayer.setGamemode(mode);

		JsonObject result = ok(id);
		result.addProperty("gamemode", mode.getId());
		result.addProperty("has_item_drops", mode.hasItemDrops());
		result.addProperty("was", was);
		bridge.write(result);
	}

	private void achievements(Minecraft mc, long id, JsonObject command) {
		List<AchievementPage> pages = AchievementPageRegistry.getInstance().getPages();
		if (pages.isEmpty()) {
			fail(id, "no achievement pages are registered");
			return;
		}

		int index = 0;
		if (command.has("page")) {
			String wanted = command.get("page").getAsString();
			index = -1;
			try {
				index = Integer.parseInt(wanted.trim());
			} catch (NumberFormatException notAnIndex) {
				String needle = wanted.toLowerCase(Locale.ROOT);
				for (int i = 0; i < pages.size(); i++) {
					if (pages.get(i).getName().toLowerCase(Locale.ROOT).contains(needle)) {
						index = i;
						break;
					}
				}
			}
			if (index < 0 || index >= pages.size()) {
				StringBuilder have = new StringBuilder();
				for (AchievementPage page : pages) {
					have.append(have.length() == 0 ? "" : ", ").append('"').append(page.getName()).append('"');
				}
				fail(id, "no achievement page matches '" + wanted + "'; registered pages are " + have);
				return;
			}
		}

		ScreenAchievements screen = new ScreenAchievements(mc.currentScreen, mc.statsCounter);

		mc.displayScreen(screen);

		if (index > 0) {
			int top = 24;
			int bottom = screen.height - 28;
			int listHeight = 20 * pages.size();
			int listY = listHeight < bottom - top ? top + (bottom - top - listHeight) / 2 : top;

			screen.mouseClicked(screen.width / 8, listY + 20 * index + 10, 0);
		}

		JsonArray names = new JsonArray();
		for (AchievementPage page : pages) {
			names.add(page.getName());
		}

		JsonObject result = ok(id);
		result.addProperty("screen",
			mc.currentScreen == null ? null : mc.currentScreen.getClass().getSimpleName());
		result.add("pages", names);
		result.addProperty("page", index);
		result.addProperty("page_name", pages.get(index).getName());
		result.addProperty("entries", pages.get(index).getAchievementEntries().size());
		bridge.write(result);
	}

	private static int countCreativeMatches(MenuInventoryCreative creative) {
		String needle = creative.searchText.toLowerCase();
		net.minecraft.core.lang.I18n i18n = net.minecraft.core.lang.I18n.getInstance();
		int matches = 0;
		for (ItemStack stack : MenuInventoryCreative.creativeContents) {
			if (stack != null
				&& i18n.translateKey(stack.getItemKey() + ".name").toLowerCase().contains(needle)) {
				matches++;
			}
		}
		return matches;
	}

	private void hold(Minecraft mc, long id, JsonObject command) {
		int slot = mc.thePlayer.inventory.getCurrentSlot();
		JsonObject result = ok(id);
		result.addProperty("slot", slot);

		if (!command.has("item") || command.get("item").isJsonNull()) {
			mc.thePlayer.inventory.setItem(slot, null);
			result.add("holding", com.google.gson.JsonNull.INSTANCE);
			bridge.write(result);
			return;
		}

		String key = required(command, "item").getAsString();
		Item item = resolveItem(key);
		if (item == null) {
			fail(id, "unknown item '" + key + "'");
			return;
		}

		int count = command.has("count") ? Math.max(1, Math.min(64, command.get("count").getAsInt())) : 1;
		ItemStack stack = new ItemStack(item, count);
		mc.thePlayer.inventory.setItem(slot, stack);

		result.addProperty("holding", item.namespaceID.toString());
		result.addProperty("count", stack.stackSize);
		result.addProperty("damage", stack.getMetadata());
		result.addProperty("max_damage", item.getMaxDamage());
		bridge.write(result);
	}

	private void useItem(Minecraft mc, long id, JsonObject command) {
		int slot = mc.thePlayer.inventory.getCurrentSlot();
		ItemStack before = mc.thePlayer.inventory.getItem(slot);
		if (before == null) {
			fail(id, "nothing in hand -- use 'hold' first");
			return;
		}

		String heldBefore = before.getItem().namespaceID.toString();
		boolean used = mc.playerController.useItemStackOnNothing(
			mc.thePlayer, mc.currentWorld, before);

		ItemStack after = mc.thePlayer.inventory.getItem(slot);
		JsonObject result = ok(id);
		result.addProperty("used", used);
		result.addProperty("holding_before", heldBefore);
		if (after == null) {
			result.add("holding_after", com.google.gson.JsonNull.INSTANCE);
		} else {
			result.addProperty("holding_after", after.getItem().namespaceID.toString());
			result.addProperty("count", after.stackSize);
			result.addProperty("damage", after.getMetadata());
		}
		result.addProperty("changed", after == null || !heldBefore.equals(
			after.getItem().namespaceID.toString()));
		bridge.write(result);
	}

	private void useOn(Minecraft mc, long id, JsonObject command) {
		int x = required(command, "x").getAsInt();
		int y = required(command, "y").getAsInt();
		int z = required(command, "z").getAsInt();

		String sideKey = command.has("side") ? command.get("side").getAsString() : "top";
		Side side = resolveSide(sideKey);
		if (side == null) {
			fail(id, "unknown side '" + sideKey
				+ "' -- one of top/bottom/north/south/east/west");
			return;
		}

		var inventory = mc.thePlayer.inventory;
		JsonObject result = ok(id);

		if (command.has("item") && !command.get("item").isJsonNull()) {
			String key = command.get("item").getAsString();
			Item item = resolveItem(key);
			if (item == null) {
				fail(id, "unknown item '" + key + "'");
				return;
			}
			int count = command.has("count")
				? Math.max(1, Math.min(64, command.get("count").getAsInt())) : 64;
			int data = command.has("data") ? command.get("data").getAsInt() : 0;

			int slot = -1;
			for (int candidate = 0; candidate < 9; candidate++) {
				if (inventory.mainInventory[candidate] == null) {
					slot = candidate;
					break;
				}
			}
			if (slot < 0) {
				slot = inventory.getCurrentSlot();
				ItemStack displaced = inventory.getItem(slot);
				result.addProperty("replaced", displaced == null ? "nothing"
					: displaced.getItem().namespaceID.toString() + " x" + displaced.stackSize);
			} else {
				result.addProperty("slot_was_empty", true);
			}
			inventory.setCurrentSlot(slot, true);
			inventory.setItem(slot, new ItemStack(item, count, data));
		}

		int slot = inventory.getCurrentSlot();
		ItemStack held = inventory.getItem(slot);
		if (held == null) {
			fail(id, "nothing in hand -- pass 'item', or use 'hold' first");
			return;
		}
		result.addProperty("slot", slot);
		result.addProperty("item", held.getItem().namespaceID.toString());
		result.addProperty("data", held.getMetadata());
		int countBefore = held.stackSize;
		result.addProperty("count_before", countBefore);

		TilePos target = new TilePos(x, y, z);
		TilePos above = new TilePos(x, y + 1, z);
		String blockWas = blockNameAt(mc.currentWorld, x, y, z);
		int dataWas = mc.currentWorld.getBlockData(target);
		String aboveWas = blockNameAt(mc.currentWorld, x, y + 1, z);
		int aboveDataWas = mc.currentWorld.getBlockData(above);

		boolean sneak = command.has("sneak") && command.get("sneak").getAsBoolean();
		boolean wasSneaking = mc.thePlayer.isSneaking();
		if (sneak) {
			mc.thePlayer.setSneaking(true);
			result.addProperty("sneak", true);
		}
		boolean handled;
		try {
			handled = mc.playerController.useOrPlaceItemStackOnTile(
				mc.thePlayer, mc.currentWorld, held, target, side, 0.5, 0.5);
		} finally {
			if (sneak) {
				mc.thePlayer.setSneaking(wasSneaking);
			}
		}

		if (held.stackSize <= 0) {
			inventory.mainInventory[slot] = null;
		}

		ItemStack after = inventory.getItem(slot);
		int countAfter = after == null ? 0 : after.stackSize;
		String blockNow = blockNameAt(mc.currentWorld, x, y, z);
		int dataNow = mc.currentWorld.getBlockData(target);
		String aboveNow = blockNameAt(mc.currentWorld, x, y + 1, z);
		int aboveDataNow = mc.currentWorld.getBlockData(above);

		result.addProperty("handled", handled);
		result.addProperty("side", sideKey);
		result.addProperty("count_after", countAfter);
		result.addProperty("consumed", countBefore - countAfter);
		result.addProperty("block_was", blockWas);
		result.addProperty("data_was", dataWas);
		result.addProperty("block_now", blockNow);
		result.addProperty("data_now", dataNow);
		result.addProperty("above_was", aboveWas);
		result.addProperty("above_data_was", aboveDataWas);
		result.addProperty("above_now", aboveNow);
		result.addProperty("above_data_now", aboveDataNow);
		result.addProperty("changed", !blockWas.equals(blockNow) || dataWas != dataNow
			|| !aboveWas.equals(aboveNow) || aboveDataWas != aboveDataNow);
		bridge.write(result);
	}

	private static Side resolveSide(String key) {
		return switch (key.toLowerCase(Locale.ROOT)) {
			case "top", "up" -> Side.TOP;
			case "bottom", "down" -> Side.BOTTOM;
			case "north" -> Side.NORTH;
			case "south" -> Side.SOUTH;
			case "west" -> Side.WEST;
			case "east" -> Side.EAST;
			default -> null;
		};
	}

	private void equip(Minecraft mc, long id, JsonObject command) {
		for (HumanArmorShape slot : HumanArmorShape.values()) {
			mc.thePlayer.setItemInArmorSlot(slot, null);
		}

		JsonObject equipped = new JsonObject();
		if (command.has("items")) {
			for (com.google.gson.JsonElement element : command.getAsJsonArray("items")) {
				String key = element.getAsString();
				Item item = resolveItem(key);
				if (item == null) {
					fail(id, "unknown item '" + key + "'");
					return;
				}
				if (!(item instanceof IArmorItem<?> armor)
					|| !(armor.getArmorShape() instanceof HumanArmorShape slot)) {
					fail(id, "'" + key + "' is not human armour");
					return;
				}
				mc.thePlayer.setItemInArmorSlot(slot, new ItemStack(item, 1));
				equipped.addProperty(slot.name(), item.namespaceID.toString());
			}
		}

		JsonObject protection = new JsonObject();
		for (DamageType type : DamageType.values()) {
			protection.addProperty(type.getLanguageKey(),
				mc.thePlayer.getTotalProtectionAmount(type));
		}

		JsonObject result = ok(id);
		result.add("equipped", equipped);
		result.add("protection", protection);
		bridge.write(result);
	}

	private void state(Minecraft mc, long id, JsonObject command) {
		JsonObject result = ok(id);
		result.addProperty("x", mc.thePlayer.x);
		result.addProperty("y", mc.thePlayer.y);
		result.addProperty("z", mc.thePlayer.z);
		result.addProperty("on_ground", mc.thePlayer.onGround);
		result.addProperty("in_water", mc.thePlayer.isInWater());
		result.addProperty("in_lava", mc.thePlayer.isInLava());
		result.addProperty("in_acid", mc.thePlayer.isInAcid());

		JsonObject motion = new JsonObject();
		motion.addProperty("x", mc.thePlayer.xd);
		motion.addProperty("y", mc.thePlayer.yd);
		motion.addProperty("z", mc.thePlayer.zd);
		result.add("motion", motion);

		result.addProperty("fall_distance", mc.thePlayer.fallDistance);
		result.addProperty("air", mc.thePlayer.airSupply);
		result.addProperty("health", mc.thePlayer.getHealth());
		result.addProperty("block_at_feet", blockNameAt(mc.currentWorld,
			(int) Math.floor(mc.thePlayer.x),
			(int) Math.floor(mc.thePlayer.y),
			(int) Math.floor(mc.thePlayer.z)));
		bridge.write(result);
	}

	private void give(Minecraft mc, long id, JsonObject command) {
		String key = required(command, "item").getAsString();
		int count = command.has("count") ? command.get("count").getAsInt() : 1;
		boolean equip = command.has("equip") && command.get("equip").getAsBoolean();

		Item item = resolveItem(key);
		if (item == null) {

			Block<?> asBlock = resolveBlock(key);
			fail(id, asBlock != null
				? "'" + key + "' is a block, not an item -- use place_block, or give its item form"
				: "unknown item '" + key + "'");
			return;
		}

		ItemStack stack = new ItemStack(item, Math.max(1, count));
		JsonObject result = ok(id);
		result.addProperty("item", item.namespaceID.toString());
		result.addProperty("count", stack.stackSize);

		if (equip) {
			if (!(item instanceof ItemArmor<?> armor)
				|| !(armor.getArmorShape() instanceof HumanArmorShape shape)) {
				fail(id, "'" + key + "' is not wearable armour, so it cannot be equipped");
				return;
			}

			mc.thePlayer.setItemInArmorSlot(shape, stack);
			result.addProperty("equipped", true);
			result.addProperty("slot", shape.name());
		} else {
			mc.thePlayer.inventory.setItem(mc.thePlayer.inventory.getCurrentSlot(), stack);
			result.addProperty("equipped", false);
			result.addProperty("slot", "hotbar:" + mc.thePlayer.inventory.getCurrentSlot());
		}
		bridge.write(result);
	}

	private static Item resolveItem(String key) {
		String alt = null;
		int colon = key.indexOf(':');
		if (colon > 0 && !key.substring(colon + 1).startsWith("item/")) {
			alt = key.substring(0, colon) + ":item/" + key.substring(colon + 1);
		}
		for (Item item : Item.itemsList) {
			if (item == null) {
				continue;
			}
			String nsid = item.namespaceID.toString();
			if (nsid.equals(key) || nsid.equals(alt)) {
				return item;
			}
		}
		return null;
	}

	private static Block<?> resolveBlock(String key) {
		String alt = null;
		int colon = key.indexOf(':');
		if (colon > 0 && !key.substring(colon + 1).startsWith("block/")) {
			alt = key.substring(0, colon) + ":block/" + key.substring(colon + 1);
		}
		for (Block<?> block : Blocks.blockMap.values()) {
			String nsid = block.namespaceId().toString();
			if (nsid.equals(key) || nsid.equals(alt)) {
				return block;
			}
		}
		return null;
	}

	private static int landingY(World world, int x, int z) {
		int maxY = world.getHeightBlocks() - 1;
		int y = Math.min(world.getHeightValue(x, z), maxY);

		if (y >= maxY || world.getBlockId(x, Math.min(y + 1, maxY), z) != 0) {
			while (y > 1 && world.getBlockId(x, y, z) != 0) {
				y--;
			}
		}

		for (; y > 1; y--) {
			if (world.getBlockId(x, y, z) == 0 || world.getBlockMaterial(x, y, z).isLiquid()) {
				continue;
			}
			if (y + 2 <= maxY
				&& world.getBlockId(x, y + 1, z) == 0
				&& world.getBlockId(x, y + 2, z) == 0) {
				return y;
			}
		}

		return world.getHeightValue(x, z);
	}

	private static String blockNameAt(World world, int x, int y, int z) {
		int blockId = world.getBlockId(x, y, z);
		if (blockId == 0) {
			return "minecraft:air";
		}
		Block<?> block = Blocks.getBlock(blockId);
		return block == null ? "unknown:" + blockId : block.namespaceId().toString();
	}

	private static com.google.gson.JsonElement required(JsonObject command, String field) {
		if (!command.has(field)) {
			throw new IllegalArgumentException("missing '" + field + "'");
		}
		return command.get(field);
	}

	private static JsonObject ok(long id) {
		JsonObject result = new JsonObject();
		result.addProperty("id", id);
		result.addProperty("ok", true);
		return result;
	}

	private void perf(Minecraft mc, long id, JsonObject command) {
		boolean remesh = command.has("remesh") && command.get("remesh").getAsBoolean();
		boolean reset = remesh || (command.has("reset") && command.get("reset").getAsBoolean());

		if (reset) {
			MeshProfile.reset();
			BlockColorBOP.resetCounters();
		}
		if (remesh && mc.renderGlobal != null) {
			mc.renderGlobal.allChanged();
		}

		long built = MeshProfile.rebuildsBuilt;
		long nanos = MeshProfile.rebuildNanos;
		long hits = BlockColorBOP.memoHits;
		long misses = BlockColorBOP.memoMisses;

		JsonObject result = ok(id);
		result.addProperty("remesh", remesh);
		result.addProperty("reset", reset);
		result.addProperty("rebuild_calls", MeshProfile.rebuildCalls);
		result.addProperty("rebuilds_built", built);
		result.addProperty("rebuild_ms", nanos / 1.0e6);
		result.addProperty("ms_per_rebuild", built == 0 ? 0.0 : nanos / 1.0e6 / built);
		result.addProperty("memo_hits", hits);
		result.addProperty("memo_misses", misses);
		result.addProperty("hit_rate", hits + misses == 0 ? 0.0 : (double) hits / (hits + misses));
		result.addProperty("blends_run", misses);
		result.addProperty("perf_control", BiomeBlendBOP.PERF_CONTROL);
		result.addProperty("render_distance", GameSettings.RENDER_DISTANCE.value);
		result.addProperty("fps_line", mc.lineDebug == null ? "" : mc.lineDebug);
		bridge.write(result);
	}

	private void fail(long id, String message) {
		AgentMode.log("Command " + id + " failed: " + message);
		JsonObject result = new JsonObject();
		result.addProperty("id", id);
		result.addProperty("ok", false);
		result.addProperty("error", message);
		bridge.write(result);
	}
}
