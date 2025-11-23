package tamaized.voidscape.client.event;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.client.DonatorLayer;

@Component(dist = Dist.CLIENT)
public class EntityLayerRendererRegistration {

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(EntityRenderersEvent.AddLayers.class, event -> {
			event.getSkins().forEach(renderer -> {
				LivingEntityRenderer<Player, EntityModel<Player>> skin = event.getSkin(renderer);
				if (skin == null)
					return;
				skin.addLayer(new DonatorLayer<>(skin));
			});
		});
	}

}
