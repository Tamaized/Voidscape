package tamaized.voidscape.client;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.registry.block.OreBlocks;
import tamaized.voidscape.registry.block.SpireBlocks;

@Component
public class TintHandler {

	@Autowired
	private SpireBlocks spireBlocks;

	@Autowired
	private OreBlocks oreBlocks;

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(this::handleBlocks);
		bus.addListener(this::handleItems);
	}

	private void handleBlocks(RegisterColorHandlersEvent.Block event) {
		event.register((blockState, iBlockDisplayReader, blockPos, i) -> 0x331166, spireBlocks.ANTIROCK.get());
		event.register((blockState, iBlockDisplayReader, blockPos, i) -> 0x661133, spireBlocks.ASTRALROCK.get());
		event.register((blockState, iBlockDisplayReader, blockPos, i) -> 0x661133, oreBlocks.CRACKED_ASTRALROCK.get());
	}

	private void handleItems(RegisterColorHandlersEvent.Item event) {
		event.register((stack, tint) -> 0x331166, spireBlocks.ANTIROCK_ITEM.get());
		event.register((stack, tint) -> 0x661133, spireBlocks.ASTRALROCK_ITEM.get());
		event.register((stack, tint) -> 0x661133, oreBlocks.CRACKED_ASTRALROCK_ITEM.get());
	}

}
