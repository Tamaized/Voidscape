package tamaized.voidscape.client;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import tamaized.voidscape.block.BlockEtherealPlant;
import tamaized.voidscape.registry.ModBlocks;
import tamaized.voidscape.registry.ModItems;

import java.util.function.Consumer;

public final class TintHandler {

	private TintHandler() {

	}

	public static void setup(IEventBus bus) {
		bus.addListener((Consumer<ColorHandlerEvent.Block>) event -> {
			event.getBlockColors().register((state, tintGetter, pos, color) -> switch (state.getValue(BlockEtherealPlant.STATE)) {
				case VOID -> 0x872BFF;
				case NULL -> 0xFFFFFF;
				case OVERWORLD -> 0x88BF40;
				case NETHER -> 0xBF4040;
				case END -> 0xBF40A6;
			}, ModBlocks.PLANT.get());
		});
		bus.addListener((Consumer<ColorHandlerEvent.Item>) event -> {
			event.getItemColors().register((stack, color) -> {
				CompoundTag tag = stack.getTag();
				if (tag == null)
					return 0x872BFF;
				return switch (tag.getString("augment")) {
					default -> 0x872BFF;
					case "null" -> 0xFFFFFF;
					case "overworld" -> 0x88BF40;
					case "nether" -> 0xBF4040;
					case "end" -> 0xBF40A6;
				};
			}, ModItems.FRUIT.get());
			event.getItemColors().register((stack, color) -> 0x872BFF, ModBlocks.PLANT_ITEM.get());
		});
	}

}
