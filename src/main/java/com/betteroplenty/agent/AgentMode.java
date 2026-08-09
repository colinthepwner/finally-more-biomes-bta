package com.betteroplenty.agent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import net.minecraft.client.Minecraft;

public final class AgentMode {

	public static final boolean ACTIVE = Boolean.getBoolean("betteroplenty.agent");

	public static final long DEFAULT_SEED = 3633286825302357475L;

	private static AgentController controller;
	private static PrintWriter log;
	private static boolean failed;

	private AgentMode() {
	}

	public static void tick(Minecraft mc) {
		if (!ACTIVE || failed) {
			return;
		}
		try {
			controller().tick(mc);
		} catch (Throwable t) {

			failed = true;
			log("FATAL: agent controller failed, standing down: " + t);
			for (StackTraceElement e : t.getStackTrace()) {
				log("  at " + e);
			}
		}
	}

	public static void onFrame(Minecraft mc) {
		if (!ACTIVE || failed || controller == null) {
			return;
		}
		try {
			controller.frame(mc);
		} catch (Throwable t) {
			failed = true;
			log("FATAL: agent frame hook failed, standing down: " + t);
		}
	}

	private static AgentController controller() {
		if (controller == null) {
			String world = System.getProperty("betteroplenty.agent.world");
			if (world == null || world.trim().isEmpty()) {
				throw new IllegalStateException(
					"betteroplenty.agent=true but betteroplenty.agent.world is unset; "
						+ "agent mode requires a world name so parallel clients never collide.");
			}
			long seed = Long.getLong("betteroplenty.agent.seed", DEFAULT_SEED);
			File dir = new File(System.getProperty(
				"betteroplenty.agent.dir", "agent" + File.separator + world));
			if (!dir.isDirectory() && !dir.mkdirs()) {
				throw new IllegalStateException("Cannot create agent dir " + dir.getAbsolutePath());
			}
			String worldType = System.getProperty("betteroplenty.agent.worldtype", "").trim();
			openLog(new File(dir, "agent.log"));
			log("Agent mode up: world='" + world + "', seed=" + seed
				+ ", dir=" + dir.getAbsolutePath()
				+ (worldType.isEmpty() ? "" : ", worldtype=" + worldType));
			controller = new AgentController(world, seed, dir, worldType);
		}
		return controller;
	}

	private static void openLog(File file) {
		try {
			log = new PrintWriter(new FileWriter(file, true), true);
		} catch (IOException e) {
			throw new IllegalStateException("Cannot open " + file.getAbsolutePath(), e);
		}
	}

	static void log(String message) {
		if (log != null) {
			log.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
				+ " " + message);
		}
	}
}
