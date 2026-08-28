package tamaized.voidscape.client.item;

import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.registry.ModToolSetComponentDirectory;

@Component(dist = Dist.CLIENT)
public class CrossbowClientExtensions {

	@Autowired(dist = Dist.CLIENT)
	private ModToolSetComponentDirectory toolSets;

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(this::registerClientExtensions);
	}

	private void registerClientExtensions(RegisterClientExtensionsEvent event) {
		register(
			event,
			new CrossbowExtensions(),
			toolSets.astralToolSet().ASTRAL_XBOW,
			toolSets.corruptToolSet().CORRUPT_XBOW,
			toolSets.ichorToolSet().ICHOR_XBOW,
			toolSets.titaniteToolSet().TITANITE_XBOW,
			toolSets.voidicCrystalToolSet().VOIDIC_CRYSTAL_XBOW
		);
	}

	@SafeVarargs
	private void register(RegisterClientExtensionsEvent event, IClientItemExtensions extensions, DeferredHolder<Item, Item>... items) {
		for (DeferredHolder<Item, Item> item : items)
			event.registerItem(extensions, item.get());
	}

}
