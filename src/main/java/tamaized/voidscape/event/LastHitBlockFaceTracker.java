package tamaized.voidscape.event;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import tamaized.beanification.Component;
import tamaized.beanification.connector.fml.EventBusPostConstruct;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class LastHitBlockFaceTracker {

	private final Map<UUID, Direction> LAST_HIT_BLOCK_FACE = new HashMap<>();

	@EventBusPostConstruct(EventBusPostConstruct.Bus.GAME)
	private void postConstruct(IEventBus bus) {
		bus.addListener(PlayerInteractEvent.LeftClickBlock.class, event -> {
			if (event.getFace() == null)
				return;
			LAST_HIT_BLOCK_FACE.put(event.getEntity().getUUID(), event.getFace());
		});
		bus.addListener(PlayerEvent.PlayerLoggedOutEvent.class, event -> LAST_HIT_BLOCK_FACE.remove(event.getEntity().getUUID()));
	}

	public Optional<Direction> get(Player player) {
		return Optional.ofNullable(LAST_HIT_BLOCK_FACE.get(player.getUUID()));
	}

}
