package tamaized.voidscape.client.tooltip;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.tooltip.QuiverTooltip;

@Component(dist = Dist.CLIENT)
public class ModTooltips {

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(RegisterClientTooltipComponentFactoriesEvent.class, event -> {
			event.register(QuiverTooltip.class, data -> new ClientQuiverTooltip(data.contents()));
		});
	}

}
