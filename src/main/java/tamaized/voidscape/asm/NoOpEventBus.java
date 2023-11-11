package tamaized.voidscape.asm;

import net.neoforged.bus.BusBuilderImpl;
import net.neoforged.bus.EventBus;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;

public class NoOpEventBus extends EventBus {

	static final IEventBus INSTANCE = new NoOpEventBus(new BusBuilderImpl());

	public NoOpEventBus(BusBuilderImpl busBuilder) {
		super(busBuilder);
	}

	@Override
	public <T extends Event> T post(T event) {
		return event;
	}
}
