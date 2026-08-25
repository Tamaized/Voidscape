package tamaized.voidscape.client;

import net.minecraft.client.color.block.BlockTintSources;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.registry.block.OreBlocks;
import tamaized.voidscape.registry.block.SpireBlocks;

import java.util.List;

@Component
public class TintHandler {

	@Autowired
	private SpireBlocks spireBlocks;

	@Autowired
	private OreBlocks oreBlocks;

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(this::handleBlocks);
	}

	private void handleBlocks(RegisterColorHandlersEvent.BlockTintSources event) {
		event.register(List.of(BlockTintSources.constant(0x331166)), spireBlocks.ANTIROCK.get());
		event.register(List.of(BlockTintSources.constant(0x661133)), spireBlocks.ASTRALROCK.get());
		event.register(List.of(BlockTintSources.constant(0x661133)), oreBlocks.CRACKED_ASTRALROCK.get());
	}

}
