package tamaized.voidscape.asm;

import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;

public class NoOpEventBus extends EventBus {

	static final IEventBus INSTANCE = new NoOpEventBus(BusBuilder.builder());

	public NoOpEventBus(BusBuilder busBuilder) {
		super(busBuilder);
	}

	@Override
	public boolean post(Event event) {
		return false;
	}
}
