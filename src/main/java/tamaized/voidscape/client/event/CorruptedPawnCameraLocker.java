package tamaized.voidscape.client.event;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ViewportEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.entity.CorruptedPawnEntity;
import tamaized.voidscape.registry.ModDataAttachments;

@Component
public class CorruptedPawnCameraLocker {

	@Autowired
	private ModDataAttachments dataAttachments;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(ViewportEvent.ComputeCameraAngles.class, event -> {
			Entity camera = event.getCamera().getEntity();
			CorruptedPawnEntity hunt = camera.getData(dataAttachments.INSANITY).getHunter();
			if (hunt != null && hunt.isAlive() && !hunt.isRemoved()) {
				camera.lookAt(EntityAnchorArgument.Anchor.EYES, hunt.getEyePosition());
				event.setYaw(camera.getViewYRot((float) event.getPartialTick()));
				event.setPitch(camera.getViewXRot((float) event.getPartialTick()));
			}
		});
	}

}
