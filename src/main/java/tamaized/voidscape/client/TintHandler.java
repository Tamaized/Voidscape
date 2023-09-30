package tamaized.voidscape.client;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.block.EtherealPlantBlock;
import tamaized.voidscape.registry.ModBlocks;
import tamaized.voidscape.registry.ModItems;

import java.util.function.Consumer;

public final class TintHandler {

	private TintHandler() {

	}

	public static void setup(IEventBus bus) {
		bus.addListener((Consumer<RegisterColorHandlersEvent.Block>) event -> {
			event.register((blockState, iBlockDisplayReader, blockPos, i) -> 0x331166, ModBlocks.ANTIROCK.get());
		});
	}

}
