package tamaized.voidscape.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModEffects;
import tamaized.voidscape.registry.tool.set.EntropicToolSet;

@Component
public class MaddeningDamageSourceHandler {

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private ModEffects effects;

	@Autowired
	private EntropicToolSet entropicToolSet;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(LivingDamageEvent.Post.class, event -> {
			Entity attacker = event.getSource().getEntity();
			if (attacker != null)
				applyWithChance(attacker, event.getEntity().getAttributeValue(attributes.MADDENING));
		});
		bus.addListener(EventPriority.LOWEST, LivingShieldBlockEvent.class, event -> {
			if (event.getBlocked() && event.getBlockedDamage() > 0) {
				ItemStack shield = event.getEntity().getItemBlockingWith();
				Entity attacker = event.getDamageSource().getEntity();
				if (shield != null && !shield.isEmpty() && shield.is(entropicToolSet.ENTROPIC_SHIELD) && attacker != null) {
					applyWithChance(attacker, 0.25D);
				}
			}
		});
	}

	public void applyWithChance(Entity target, double chance) {
		if (chance > 0 && target.getRandom().nextDouble() <= chance) {
			if (target instanceof LivingEntity e) {
				e.addEffect(new MobEffectInstance(effects.MADDENED, 20 * 15));
			}
		}
	}

}
