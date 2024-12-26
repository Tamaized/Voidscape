package tamaized.voidscape.network;

import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
public class DonatorHandler {

	private final Object lock_settings = new Object();
	private final Map<UUID, Settings> settings = new HashMap<>();
	private final URI URL_DONATORS = URI.create("https://gh.tamaized.com/Tamaized/Voidscape/donator.properties");
	private CompletableFuture<List<UUID>> donators;

	@PostConstruct
	private void start() {
		Voidscape.LOGGER.info("Starting Donator Handler");
		donators = CompletableFuture.supplyAsync(this::run);
	}

	public List<UUID> getDonators() {
		return donators.join();
	}

	public boolean isDonator(UUID uuid) {
		return donators.join().contains(uuid);
	}

	public Optional<Settings> getSettings(UUID donator) {
		synchronized (lock_settings) {
			return Optional.ofNullable(settings.get(donator));
		}
	}

	public void updateSettings(UUID donator, Settings settings) {
		synchronized (lock_settings) {
			if (!isDonator(donator))
				return;
			this.settings.put(donator, settings);
		}
	}

	private List<UUID> run() {
		Voidscape.LOGGER.info("Loading donor data");
		try (InputStreamReader data = new InputStreamReader(URL_DONATORS.toURL().openConnection().getInputStream())) {
			Properties props = new Properties();
			props.load(data);
			List<UUID> result = props.stringPropertyNames().stream().map(UUID::fromString).toList();
			Voidscape.LOGGER.info("Donor data loaded");
			return result;
		} catch (IOException e) {
			Voidscape.LOGGER.error("Could not load donor data");
		}

		return Collections.emptyList();
	}

	public record Settings(boolean enabled, int color) {

	}

}