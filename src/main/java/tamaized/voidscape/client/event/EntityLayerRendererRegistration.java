package tamaized.voidscape.client.event;

import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.Avatar;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.renderstate.AvatarRenderStateModifier;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import org.jspecify.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.client.armor.ArmorOverlayLayer;
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
			if (skin != null) {
				skin.addLayer(new ShroudWingLayer<>(skin));
				addArmorOverlay(skin);
			}
			addArmorOverlay(event.getMannequinRenderer(renderer));
		});
		event.getEntityTypes().forEach(type -> addArmorOverlay(event.getRenderer(type)));
	}

	private void addArmorOverlay(@Nullable EntityRenderer<?, ?> renderer) {
		if (renderer instanceof LivingEntityRenderer<?, ?, ?> living && living.getModel() instanceof HumanoidModel)
			addArmorOverlayLayer(living);
	}

	@SuppressWarnings("unchecked")
	private <S extends HumanoidRenderState, M extends EntityModel<? super S>> void addArmorOverlayLayer(LivingEntityRenderer<?, ?, ?> renderer) {
		// Yucky
		LivingEntityRenderer<?, S, M> humanoid = (LivingEntityRenderer<?, S, M>) renderer;
		humanoid.addLayer(new ArmorOverlayLayer<>(humanoid));
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
