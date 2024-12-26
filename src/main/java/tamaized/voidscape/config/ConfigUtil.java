package tamaized.voidscape.config;

import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Bean;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

@Component
public class ConfigUtil {

	private final String suffix;

	@SuppressWarnings("unused")
	public ConfigUtil() {
		this(null);
	}

	public ConfigUtil(@Nullable String suffix) {
		this.suffix = suffix;
	}

	public String translationKey(String key) {
		return Voidscape.MODID + ".config." + (suffix == null ? "" : (suffix + ".")) + key;
	}

	@Bean("common")
	private static ConfigUtil commonSuffix() {
		return new ConfigUtil("common");
	}

	@Bean("client")
	private static ConfigUtil clientSuffix() {
		return new ConfigUtil("client");
	}

	@Bean("donatorSettings")
	private static ConfigUtil donatorSettingsSuffix() {
		return new ConfigUtil("client.donatorSettings");
	}

}
