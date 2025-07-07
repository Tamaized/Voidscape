package tamaized.voidscape.event;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.entity.CorruptedPawnEntity;

@Component
public class CorruptedPawnAntiModConflictTicker {

	@PostConstruct(PostConstruct.Bus.GAME)
	private void test(IEventBus bus) {
		bus.addListener(EventPriority.LOWEST, true, EntityTickEvent.Pre.class, event -> {
			if (event.getEntity() instanceof CorruptedPawnEntity && event.isCanceled())
				event.setCanceled(false);
		});
	}

}
