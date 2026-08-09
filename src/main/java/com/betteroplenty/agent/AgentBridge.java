package com.betteroplenty.agent;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

final class AgentBridge {

	private static final Gson GSON = new Gson();

	private final File commands;
	private final File results;
	private long offset;

	AgentBridge(File dir) {
		this.commands = new File(dir, "commands.jsonl");
		this.results = new File(dir, "results.jsonl");
	}

	List<JsonObject> poll() {
		if (!commands.isFile() || commands.length() <= offset) {
			return List.of();
		}
		List<JsonObject> out = new ArrayList<>();
		try (RandomAccessFile raf = new RandomAccessFile(commands, "r")) {
			raf.seek(offset);
			long lineStart = offset;
			StringBuilder line = new StringBuilder();
			int c;
			while ((c = raf.read()) != -1) {
				if (c == '\n') {
					String text = new String(line.toString().getBytes(StandardCharsets.ISO_8859_1),
						StandardCharsets.UTF_8).trim();
					lineStart = raf.getFilePointer();
					line.setLength(0);
					if (!text.isEmpty()) {
						parse(text, out);
					}
				} else if (c != '\r') {
					line.append((char) c);
				}
			}

			offset = lineStart;
		} catch (IOException e) {
			AgentMode.log("ERROR reading commands.jsonl: " + e);
		}
		return out;
	}

	private void parse(String text, List<JsonObject> out) {
		try {
			out.add(JsonParser.parseString(text).getAsJsonObject());
		} catch (RuntimeException e) {
			AgentMode.log("Malformed command line: " + text);
			JsonObject error = new JsonObject();
			error.addProperty("ok", false);
			error.addProperty("error", "malformed command: " + e.getMessage());
			write(error);
		}
	}

	void write(JsonObject result) {
		try (FileWriter w = new FileWriter(results, StandardCharsets.UTF_8, true)) {
			w.write(GSON.toJson(result));
			w.write('\n');
		} catch (IOException e) {
			AgentMode.log("ERROR writing results.jsonl: " + e);
		}
	}
}
