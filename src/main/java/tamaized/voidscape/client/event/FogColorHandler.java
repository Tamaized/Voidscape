package tamaized.voidscape.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ViewportEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.connector.fml.EventBusPostConstruct;
import tamaized.beanification.connector.fml.RequiresDist;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.util.LevelUtil;

@Component
@RequiresDist(Dist.CLIENT)
public class FogColorHandler {

	@Autowired
	@RequiresDist(Dist.CLIENT)
	private LevelUtil levelUtil;

	@Autowired
	@RequiresDist(Dist.CLIENT)
	private ModDataAttachments dataAttachments;

	@EventBusPostConstruct(EventBusPostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(ViewportEvent.ComputeFogColor.class, event -> {
			if (Minecraft.getInstance().level != null && levelUtil.isInVoidDimension(Minecraft.getInstance().level)) {
				event.setRed(0.04F);
				event.setGreen(0.03F);
				event.setBlue(0.05F);
				if (Minecraft.getInstance().player != null)
					event.setRed(Mth.clamp(Minecraft.getInstance().player.getData(dataAttachments.INSANITY).getParanoia() / 1200F, 0.04F, 1F));
			}
		});
	}

}
