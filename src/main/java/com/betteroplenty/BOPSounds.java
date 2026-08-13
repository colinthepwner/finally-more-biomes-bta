package com.betteroplenty;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.sound.SoundTypes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class BOPSounds {
	private BOPSounds() {}

	private static final String SOUNDS_JSON = "/assets/betteroplenty/sounds/sounds.json";

	public static void register() {
		SoundTypes.loadSoundsJson(BetterOPlenty.MOD_ID);

		List<String> declared = readEventKeys();
		if (declared.isEmpty()) {
			BetterOPlenty.LOGGER.error("Could not read {} to check the sound registration; BOP's "
				+ "sounds may be silent in multiplayer.", SOUNDS_JSON);
			return;
		}

		List<String> unresolved = new ArrayList<>();
		for (String key : declared) {
			if (SoundTypes.getSoundId(BetterOPlenty.MOD_ID + ":" + key) == -1) {
				unresolved.add(key);
			}
		}

		if (unresolved.isEmpty()) {
			BetterOPlenty.LOGGER.info("Registered {} BOP sound event(s) with BTA's sound registry, so "
					+ "they can be sent to a multiplayer client (they were silent on a server until "
					+ "0.1.11; the audio itself still comes from the asset bridge).",
				declared.size());
		} else {
			BetterOPlenty.LOGGER.error("{} of {} BOP sound event(s) did not register and will be "
					+ "silent in multiplayer: {}.",
				unresolved.size(), declared.size(), String.join(", ", unresolved));
		}
	}

	private static List<String> readEventKeys() {
		try (InputStream in = BOPSounds.class.getResourceAsStream(SOUNDS_JSON)) {
			if (in == null) {
				return List.of();
			}
			JsonObject root = JsonParser
				.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8))
				.getAsJsonObject();
			return new ArrayList<>(root.keySet());
		} catch (Exception e) {
			BetterOPlenty.LOGGER.error("Could not parse {}.", SOUNDS_JSON, e);
			return List.of();
		}
	}
}
