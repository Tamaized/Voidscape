package tamaized.voidscape.client.event;

import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.renderstate.AvatarRenderStateModifier;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.client.entity.render.layer.ShroudWingLayer;
import tamaized.voidscape.client.entity.render.state.ShroudWingLayerRenderStateExtension;

@Component(dist = Dist.CLIENT)
public class EntityLayerRendererRegistration {

	@Autowired(dist = Dist.CLIENT)
	private ShroudWingLayerRenderStateExtension shroudWingLayerRenderStateExtension;

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(this::addLayers);
		bus.addListener(this::modifyState);
	}

	private void addLayers(EntityRenderersEvent.AddLayers event) {
		event.getSkins().forEach(renderer -> {
			AvatarRenderer<AbstractClientPlayer> skin = event.getPlayerRenderer(renderer);
			if (skin == null)
				return;
			skin.addLayer(new ShroudWingLayer<>(skin));
		});
	}

	private void modifyState(RegisterRenderStateModifiersEvent event) {
		event.registerAvatarEntityModifier(new AvatarRenderStateModifier() {
			@Override
			public <T extends Avatar & ClientAvatarEntity> void accept(T avatar, AvatarRenderState renderState) {
				shroudWingLayerRenderStateExtension.apply(avatar, renderState);
			}
		});
	}

}
