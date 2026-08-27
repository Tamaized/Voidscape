package tamaized.voidscape.event;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.registry.tool.VoidToolLadder;

@Component
public class VoidToolHarvestRestrictor {

	@Autowired
	private VoidToolLadder ladder;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(PlayerEvent.HarvestCheck.class, event -> {
			if (!event.canHarvest())
				return;
			int required = ladder.requiredLevel(event.getTargetBlock());
			if (required >= 0)
				event.setCanHarvest(ladder.toolLevel(event.getEntity().getMainHandItem()) >= required);
		});
	}

}
