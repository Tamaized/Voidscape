package tamaized.voidscape.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import tamaized.voidscape.Voidscape;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class DonatorHandler {

	public static final Map<UUID, DonatorSettings> settings = new HashMap<>();
	private static final String URL_DONATORS = "https://raw.githubusercontent.com/Tamaized/Voidscape/{BRANCH}/donator.properties";
	public static volatile List<UUID> donators = new ArrayList<>();
	private static boolean started = false;

	public static void start() {
		if (!started) {
			Voidscape.LOGGER.info("Starting Donator Handler");
			started = true;
			new ThreadDonators();
		}
	}

	public static void loadData(Properties props) {
		donators.clear();
		for (String s : props.stringPropertyNames()) {
			donators.add(UUID.fromString(s));
		}
		Voidscape.LOGGER.debug(donators);
	}

	public static final class DonatorSettings {
		public boolean enabled = true;
		public int color = 0xFFFFFFFF;

		public DonatorSettings(boolean enabled, int color) {
			this.enabled = enabled;
			this.color = color;
		}
	}

	private static class ThreadDonators extends Thread {

		public ThreadDonators() {
			setName("Voidscape Donator Loader");
			setDaemon(true);
			start();
		}

		@Override
		public void run() {
			Voidscape.LOGGER.info("Loading Data");
			try (InputStreamReader json = new InputStreamReader(new URL("https://api.github.com/repos/Tamaized/Voidscape").openConnection().getInputStream())) {
				String branch = new Gson().fromJson(json, JsonObject.class).get("default_branch").getAsString();
				URL url = new URL(URL_DONATORS.replace("{BRANCH}", branch));
				Voidscape.LOGGER.debug(url);
				Properties props = new Properties();
				InputStreamReader reader = new InputStreamReader(url.openStream());
				props.load(reader);
				loadData(props);
				Voidscape.LOGGER.info("Data Loaded");
			} catch (IOException e) {
				Voidscape.LOGGER.error("Could not load data");
			}
		}

	}
}