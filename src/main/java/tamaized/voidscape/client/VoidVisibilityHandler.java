package tamaized.voidscape.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.state.LightmapRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import org.joml.Vector3f;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.connector.fml.EventBusPostConstruct;
import tamaized.beanification.connector.fml.RequiresDist;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.util.LevelUtil;

@Component
@RequiresDist(Dist.CLIENT)
public class VoidVisibilityHandler {

	@Autowired
	@RequiresDist(Dist.CLIENT)
	private ModAttributes attributes;

	@Autowired
	@RequiresDist(Dist.CLIENT)
	private LevelUtil levelUtil;

	@EventBusPostConstruct(EventBusPostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(RenderFrameEvent.Pre.class, _ -> {
			LocalPlayer player = Minecraft.getInstance().player;
			if (player == null || !levelUtil.isInVoidDimension(Minecraft.getInstance().level))
				return;

			float visibility = (float) player.getAttributeValue(attributes.VOIDIC_VISIBILITY);
			if (visibility <= 0F)
				return;

			LightmapRenderState lightmap = Minecraft.getInstance().gameRenderer.getGameRenderState().lightmapRenderState;
			lightmap.ambientColor = new Vector3f(visibility, visibility, visibility);
		});
	}

}
