package tamaized.voidscape.client.event;

import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ScreenEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.client.entity.render.state.ChestEquipmentRenderStateExtension;

@Component(dist = Dist.CLIENT)
public class SmithingPreviewHandler {

	@Autowired(dist = Dist.CLIENT)
	private ChestEquipmentRenderStateExtension chestEquipmentRenderStateExtension;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(ScreenEvent.Render.Pre.class, event -> {
			if (event.getScreen() instanceof SmithingScreen smithing) {
				smithing.armorStandPreview.elytraRotX = (float) (Math.PI / 12);
				smithing.armorStandPreview.elytraRotY = 0F;
				smithing.armorStandPreview.elytraRotZ = (float) (-Math.PI / 12);
				chestEquipmentRenderStateExtension.applyElytra(smithing.armorStandPreview);
			}
		});
	}

}
