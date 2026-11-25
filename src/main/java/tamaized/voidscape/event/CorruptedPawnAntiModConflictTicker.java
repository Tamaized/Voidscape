package tamaized.voidscape.event;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import tamaized.beanification.Component;
import tamaized.beanification.connector.fml.EventBusPostConstruct;
import tamaized.voidscape.entity.CorruptedPawnEntity;

@Component
public class CorruptedPawnAntiModConflictTicker {

	@EventBusPostConstruct(EventBusPostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(EventPriority.LOWEST, true, EntityTickEvent.Pre.class, event -> {
			if (event.getEntity() instanceof CorruptedPawnEntity && event.isCanceled())
				event.setCanceled(false);
		});
	}

}
