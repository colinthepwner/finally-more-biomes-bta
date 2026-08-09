package com.betteroplenty.client;

public final class MeshProfile {

	private MeshProfile() {}

	public static long rebuildNanos;

	public static long rebuildCalls;

	public static long rebuildsBuilt;

	private static long startNanos;

	public static void begin() {
		startNanos = System.nanoTime();
	}

	public static void end(boolean built) {
		rebuildNanos += System.nanoTime() - startNanos;
		rebuildCalls++;
		if (built) {
			rebuildsBuilt++;
		}
	}

	public static void reset() {
		rebuildNanos = 0;
		rebuildCalls = 0;
		rebuildsBuilt = 0;
	}
}
