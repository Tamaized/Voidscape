package tamaized.voidscape.registry;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.VoidCommands;

@Component
public class ModCommands {

	@Autowired
	private VoidCommands voidCommands;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void init(IEventBus bus) {
		bus.addListener(RegisterCommandsEvent.class, event -> event.getDispatcher().register(voidCommands.factory()));
	}

}
