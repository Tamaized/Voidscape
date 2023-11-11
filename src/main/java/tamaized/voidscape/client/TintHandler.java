package tamaized.voidscape.client;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import tamaized.voidscape.registry.ModBlocks;

public final class TintHandler {

	private TintHandler() {

	}

	public static void setup(IEventBus bus) {
		bus.addListener(RegisterColorHandlersEvent.Block.class, event -> {
			event.register((blockState, iBlockDisplayReader, blockPos, i) -> 0x331166, ModBlocks.ANTIROCK.get());
			event.register((blockState, iBlockDisplayReader, blockPos, i) -> 0x661133, ModBlocks.ASTRALROCK.get());
			event.register((blockState, iBlockDisplayReader, blockPos, i) -> 0x661133, ModBlocks.CRACKED_ASTRALROCK.get());
		});
		bus.addListener(RegisterColorHandlersEvent.Item.class, event -> {
			event.register((stack, tint) -> 0x331166, ModBlocks.ANTIROCK_ITEM.get());
			event.register((stack, tint) -> 0x661133, ModBlocks.ASTRALROCK_ITEM.get());
			event.register((stack, tint) -> 0x661133, ModBlocks.CRACKED_ASTRALROCK_ITEM.get());
		});
	}

}
