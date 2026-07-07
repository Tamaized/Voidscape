package tamaized.voidscape.client.effect;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.effect.StandardEffect;
import tamaized.voidscape.registry.ModEffects;

@Component(dist = Dist.CLIENT)
public class ClientMobEffectExtensionsHandler {

	@Autowired(dist = Dist.CLIENT)
	private ModEffects effects;

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(RegisterClientExtensionsEvent.class, event -> event.registerMobEffect(new IClientMobEffectExtensions() {
			@Override
			public boolean renderInventoryIcon(MobEffectInstance instance, AbstractContainerScreen<?> screen, GuiGraphicsExtractor guiGraphics, int x, int y, int blitOffset) {
				if (!(instance.getEffect().value() instanceof StandardEffect standardEffect))
					return false;

				guiGraphics.blitSprite(
					RenderPipelines.GUI_TEXTURED,
					standardEffect.getTexture(),
					x, y + 7,
					18, 18
				);

				return true;
			}

			@Override
			public boolean renderGuiIcon(MobEffectInstance instance, Gui gui, GuiGraphicsExtractor guiGraphics, int x, int y, float z, float alpha) {
				if (!(instance.getEffect().value() instanceof StandardEffect standardEffect))
					return false;

				guiGraphics.blitSprite(
					RenderPipelines.GUI_TEXTURED,
					standardEffect.getTexture(),
					x + 3, y + 3,
					18, 18,
					alpha
				);

				return true;
			}
		}, effects.AURA, effects.FORTIFIED, effects.ICHOR, effects.TRAUMATIZED));
	}

}
