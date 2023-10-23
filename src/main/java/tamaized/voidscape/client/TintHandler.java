package tamaized.voidscape.client;

import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import tamaized.voidscape.registry.ModBlocks;

import java.util.function.Consumer;

public final class TintHandler {

	private TintHandler() {

	}

	public static void setup(IEventBus bus) {
		bus.addListener((Consumer<RegisterColorHandlersEvent.Block>) event -> {
			event.register((blockState, iBlockDisplayReader, blockPos, i) -> 0x331166, ModBlocks.ANTIROCK.get());
			event.register((blockState, iBlockDisplayReader, blockPos, i) -> 0x661133, ModBlocks.ASTRALROCK.get());
			event.register((blockState, iBlockDisplayReader, blockPos, i) -> 0x661133, ModBlocks.CRACKED_ASTRALROCK.get());
		});
		bus.addListener((Consumer<RegisterColorHandlersEvent.Item>) event -> {
			event.register((stack, tint) -> 0x331166, ModBlocks.ANTIROCK_ITEM.get());
			event.register((stack, tint) -> 0x661133, ModBlocks.ASTRALROCK_ITEM.get());
			event.register((stack, tint) -> 0x661133, ModBlocks.CRACKED_ASTRALROCK_ITEM.get());
		});
	}

}
