package tamaized.voidscape.client;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.registry.ModBlocks;

@Component
public class TintHandler {

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(this::handleBlocks);
		bus.addListener(this::handleItems);
	}

	private void handleBlocks(RegisterColorHandlersEvent.Block event) {
		event.register((blockState, iBlockDisplayReader, blockPos, i) -> 0x331166, ModBlocks.ANTIROCK.get());
		event.register((blockState, iBlockDisplayReader, blockPos, i) -> 0x661133, ModBlocks.ASTRALROCK.get());
		event.register((blockState, iBlockDisplayReader, blockPos, i) -> 0x661133, ModBlocks.CRACKED_ASTRALROCK.get());
	}

	private void handleItems(RegisterColorHandlersEvent.Item event) {
		event.register((stack, tint) -> 0x331166, ModBlocks.ANTIROCK_ITEM.get());
		event.register((stack, tint) -> 0x661133, ModBlocks.ASTRALROCK_ITEM.get());
		event.register((stack, tint) -> 0x661133, ModBlocks.CRACKED_ASTRALROCK_ITEM.get());
	}

}
