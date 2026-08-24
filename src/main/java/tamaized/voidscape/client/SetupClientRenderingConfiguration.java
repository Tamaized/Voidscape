package tamaized.voidscape.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ConfigureMainRenderTargetEvent;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;

@Component(dist = Dist.CLIENT)
public class SetupClientRenderingConfiguration {

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(ConfigureMainRenderTargetEvent.class, ConfigureMainRenderTargetEvent::enableStencil);
	}

}
