package tamaized.voidscape.client;

import net.minecraft.client.color.block.BlockTintSources;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.connector.fml.EventBusPostConstruct;
import tamaized.beanification.connector.fml.RequiresDist;
import tamaized.voidscape.registry.block.OreBlocks;
import tamaized.voidscape.registry.block.SpireBlocks;

import java.util.List;

@Component
@RequiresDist(Dist.CLIENT)
public class TintHandler {

	@Autowired
	@RequiresDist(Dist.CLIENT)
	private SpireBlocks spireBlocks;

	@Autowired
	@RequiresDist(Dist.CLIENT)
	private OreBlocks oreBlocks;

	@EventBusPostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(this::handleBlocks);
	}

	private void handleBlocks(RegisterColorHandlersEvent.BlockTintSources event) {
		event.register(List.of(BlockTintSources.constant(0x331166)), spireBlocks.ANTIROCK.get());
		event.register(List.of(BlockTintSources.constant(0x661133)), spireBlocks.ASTRALROCK.get());
		event.register(List.of(BlockTintSources.constant(0x661133)), oreBlocks.CRACKED_ASTRALROCK.get());
	}

}
