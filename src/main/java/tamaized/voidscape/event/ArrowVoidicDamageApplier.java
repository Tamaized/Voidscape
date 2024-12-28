package tamaized.voidscape.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModDataAttachments;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ArrowVoidicDamageApplier {

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private ModDataAttachments dataAttachments;

	private final Map<UUID, AttributeStates> OWNER_STATES = new ConcurrentHashMap<>();

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(ArrowLooseEvent.class, event -> {
			Player player = event.getEntity();
			OWNER_STATES.put(player.getUUID(), new AttributeStates(
				(float) player.getAttributeValue(attributes.VOIDIC_ARROW_DMG),
				((float) player.getAttributeValue(attributes.VOIDIC_INFUSION) - 1F) * Insanity.MAX_INFUSION
			));
		});
		bus.addListener(EntityJoinLevelEvent.class, event -> {
			if (event.getEntity() instanceof AbstractArrow arrow) {
				Entity entity = arrow.getOwner();
				if (entity instanceof Player player) {
					AttributeStates states = OWNER_STATES.get(player.getUUID());
					if (states != null) {
						if (states.voidic() > 0)
							arrow.setData(dataAttachments.VOIDIC_ARROW.get(), states.voidic());
						if (states.infusion() > 0)
							arrow.setData(dataAttachments.INFUSION_ARROW.get(), states.infusion());
						return;
					}
				}
				if (entity instanceof LivingEntity shooter) {
					float voidic = (float) shooter.getAttributeValue(attributes.VOIDIC_ARROW_DMG);
					if (voidic > 0)
						arrow.setData(dataAttachments.VOIDIC_ARROW, voidic);
					final float infusion = ((float) shooter.getAttributeValue(attributes.VOIDIC_INFUSION) - 1F) * Insanity.MAX_INFUSION;
					if (infusion > 0)
						event.getEntity().setData(dataAttachments.INFUSION_ARROW, infusion);
				}
			}
		});
	}

	private record AttributeStates(float voidic, float infusion) {

	}

}
