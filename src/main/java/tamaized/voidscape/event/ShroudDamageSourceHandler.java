package tamaized.voidscape.event;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.registry.ModDataAttachments;

@Component
public class ShroudDamageSourceHandler {

	@Autowired
	private ModDataAttachments attachments;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(
			EventPriority.LOWEST,
			LivingDamageEvent.Pre.class,
			event -> event.setNewDamage(event.getEntity().getData(attachments.SHROUD.get()).onDamage(event.getEntity(), event.getNewDamage()))
		);
	}

}
