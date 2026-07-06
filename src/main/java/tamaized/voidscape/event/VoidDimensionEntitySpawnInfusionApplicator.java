package tamaized.voidscape.event;

import net.minecraft.world.entity.EntitySpawnReason;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.entity.IEthereal;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.util.LevelUtil;

@Component
public class VoidDimensionEntitySpawnInfusionApplicator {

	@Autowired
	private LevelUtil levelUtil;

	@Autowired
	private ModDataAttachments dataAttachments;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(FinalizeSpawnEvent.class, event -> {
			if (event.getSpawnType() == EntitySpawnReason.NATURAL &&
				!(event.getEntity() instanceof IEthereal) &&
				levelUtil.isInVoidDimension(event.getEntity().level())) {
				event.getEntity().getData(dataAttachments.INSANITY).addInfusion(event.getEntity().getRandom().nextInt(200) + 100, event.getEntity());
			}
		});
	}

}
