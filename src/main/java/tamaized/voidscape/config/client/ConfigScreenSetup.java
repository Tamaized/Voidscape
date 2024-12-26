package tamaized.voidscape.config.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;

@Component(dist = Dist.CLIENT)
public class ConfigScreenSetup {

	@Autowired(dist = Dist.CLIENT)
	private DonatorSettings donatorSettings;

	@Autowired(dist = Dist.CLIENT)
	private HexColorEditBoxFactory hexColorEditBoxFactory;

	@PostConstruct
	private void setup() {
		ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (mod, parent) -> new ConfigurationScreen(
			mod,
			parent,
			(context, s, element) -> s.equals("color") ? new ConfigurationScreen.ConfigurationSectionScreen.Element(
				element.name(),
				element.tooltip(),
				hexColorEditBoxFactory.make(donatorSettings.color),
				false
			) : element)
		);
	}

}
