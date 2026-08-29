package tamaized.voidscape.client.event;

import net.minecraft.client.ScrollWheelHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.joml.Vector2i;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.data.QuiverContents;
import tamaized.voidscape.item.QuiverItem;
import tamaized.voidscape.network.server.ServerPacketQuiverScroll;
import tamaized.voidscape.registry.ModItemComponents;

@Component(dist = Dist.CLIENT)
public class QuiverScrollHandler {

	private final ScrollWheelHandler scrollWheelHandler = new ScrollWheelHandler();

	@Autowired(dist = Dist.CLIENT)
	private ModItemComponents components;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(ScreenEvent.MouseScrolled.Pre.class, event -> {
			if (!(event.getScreen() instanceof AbstractContainerScreen<?> screen))
				return;
			Slot slot = screen.getHoveredSlot();
			if (slot == null)
				return;
			ItemStack stack = slot.getItem();
			if (!(stack.getItem() instanceof QuiverItem))
				return;
			QuiverContents contents = stack.get(components.QUIVER_CONTENTS);
			if (contents == null || contents.view().size() < 2)
				return;
			Vector2i wheel = scrollWheelHandler.onMouseScroll(event.getScrollDeltaX(), event.getScrollDeltaY());
			int direction = wheel.y == 0 ? -wheel.x : wheel.y;
			if (direction == 0)
				return;
			QuiverContents.Mutable mutable = contents.toMutableCopy();
			mutable.rotate(direction);
			stack.set(components.QUIVER_CONTENTS, mutable.toImmutable());
			ClientPacketDistributor.sendToServer(new ServerPacketQuiverScroll(slot.index, direction));
			event.setCanceled(true);
		});
	}

}
