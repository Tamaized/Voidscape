package tamaized.voidscape.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ConfigureMainRenderTargetEvent;
import tamaized.beanification.Component;
import tamaized.beanification.connector.fml.EventBusPostConstruct;
import tamaized.beanification.connector.fml.RequiresDist;

@Component
@RequiresDist(Dist.CLIENT)
public class SetupClientRenderingConfiguration {

	@EventBusPostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(ConfigureMainRenderTargetEvent.class, ConfigureMainRenderTargetEvent::enableStencil);
	}

}
