package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.VoidDimensionSpecialEffectsFactory;

@Component
public class ModDimensions {

	@Autowired(dist = Dist.CLIENT)
	private VoidDimensionSpecialEffectsFactory voidDimensionSpecialEffectsFactory;

	public final ResourceKey<Level> VOID = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "void"));

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(RegisterDimensionSpecialEffectsEvent.class, event -> event.register(
			VOID.location(),
			voidDimensionSpecialEffectsFactory.make()
		));
	}

}
