package com.betteroplenty.res;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.InflaterInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ObfResources {

	private static final Logger LOGGER = LoggerFactory.getLogger("betteroplenty");

	private static final String MANIFEST = "/betteroplenty.dat";

	private static final byte[] KEY = {
		(byte) 0x9E, (byte) 0x37, (byte) 0x79, (byte) 0xB9, (byte) 0x7F, (byte) 0x4A, (byte) 0x7C,
		(byte) 0x15, (byte) 0xF3, (byte) 0x9C, (byte) 0xC0, (byte) 0x60, (byte) 0x5C, (byte) 0xED,
		(byte) 0xC8, (byte) 0x34, (byte) 0x1B, (byte) 0x2D, (byte) 0xB1, (byte) 0x4A, (byte) 0xCE,
		(byte) 0xD6, (byte) 0x76, (byte) 0x1F, (byte) 0x8E, (byte) 0x77, (byte) 0x22, (byte) 0x03,
		(byte) 0x64, (byte) 0x4C, (byte) 0xAB, (byte) 0x39,
	};

	private static final Map<String, String> PHYSICAL;

	public static final boolean ACTIVE;

	static {
		Map<String, String> physical = null;
		try {
			physical = load();
		} catch (Throwable t) {

			LOGGER.error("Resource manifest present but unreadable; running without it.", t);
		}
		PHYSICAL = physical == null ? Collections.emptyMap() : physical;
		ACTIVE = !PHYSICAL.isEmpty();
		if (ACTIVE) {
			LOGGER.info("Packed resources: {} entries.", PHYSICAL.size());
		}
	}

	private ObfResources() {
	}

	public static boolean has(String path) {
		return ACTIVE && PHYSICAL.containsKey(path);
	}

	public static InputStream open(String path) {
		if (!ACTIVE) {
			return null;
		}
		String hashed = PHYSICAL.get(path);
		if (hashed == null) {
			return null;
		}
		try (InputStream in = ObfResources.class.getResourceAsStream(hashed)) {
			if (in == null) {
				LOGGER.error("Manifest maps {} to {}, which is not in the jar.", path, hashed);
				return null;
			}
			byte[] bytes = readAll(in);
			fold(bytes, seed(hashed));
			return new ByteArrayInputStream(bytes);
		} catch (IOException e) {
			LOGGER.error("Failed to read packed resource {}", path, e);
			return null;
		}
	}

	public static String[] list(String directory, boolean subDirectories) {
		if (!ACTIVE) {
			return null;
		}
		String dir = directory.endsWith("/") ? directory : directory + "/";
		List<String> names = new ArrayList<>();
		for (String logical : PHYSICAL.keySet()) {
			if (!logical.startsWith(dir) || !logical.endsWith(".png")) {
				continue;
			}
			String relative = logical.substring(dir.length());
			if (subDirectories || relative.indexOf('/') < 0) {
				names.add(relative);
			}
		}
		return names.toArray(new String[0]);
	}

	private static Map<String, String> load() throws IOException {
		byte[] folded;
		try (InputStream in = ObfResources.class.getResourceAsStream(MANIFEST)) {
			if (in == null) {
				return null;
			}
			folded = readAll(in);
		}
		fold(folded, 0);

		byte[] plain;
		try (InputStream in = new InflaterInputStream(new ByteArrayInputStream(folded))) {
			plain = readAll(in);
		}

		Map<String, String> map = new HashMap<>();
		for (String line : new String(plain, StandardCharsets.UTF_8).split("\n")) {
			int bar = line.indexOf('|');
			if (bar > 0) {
				map.put(line.substring(0, bar), line.substring(bar + 1));
			}
		}
		return map;
	}

	private static void fold(byte[] bytes, int seed) {
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] ^= KEY[(i + seed) % KEY.length];
		}
	}

	private static int seed(String hashed) {
		int h = 0;
		for (int i = 0; i < hashed.length(); i++) {
			h = h * 31 + hashed.charAt(i);
		}
		return Math.floorMod(h, KEY.length);
	}

	private static byte[] readAll(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(Math.max(in.available(), 1024));
		byte[] buffer = new byte[8192];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
		return out.toByteArray();
	}
}
