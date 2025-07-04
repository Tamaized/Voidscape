package tamaized.voidscape.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.asm.ASMHooks;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.registry.ModAttributes;
import tamaized.voidscape.registry.ModDamageSource;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.ModEffects;

@Component
public class VoidicDamageSourceHandler {

	@Autowired
	private ModDamageSource damageSource;

	@Autowired
	private ModAttributes attributes;

	@Autowired
	private ModDataAttachments dataAttachments;

	@Autowired
	private ModEffects effects;

	@PostConstruct(PostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(LivingDamageEvent.Pre.class, event -> {
			DamageState damageState;
			LivingEntity target = event.getEntity();
			if (!event.getSource().is(damageSource.VOIDIC) && (damageState = getDamageState(event.getSource())) != DamageState.NONE) {
				if (target.getHealth() <= event.getNewDamage())
					return;
				if (damageState == DamageState.MELEE && (event.getSource().isDirect() ? event.getSource().getDirectEntity() : event.getSource().getEntity()) instanceof LivingEntity attacker) {
					final float voidicMeleeDamage = (float) (attacker.getAttributeValue(attributes.VOIDIC_DMG) * (attacker instanceof Player p ? p.getAttackStrengthScale(0.5F) : 1F));
					if (voidicMeleeDamage > 0) {
						target.invulnerableTime = 0;
						target.hurt(damageSource.getEntityDamageSource(target.level(), damageSource.VOIDIC, attacker), voidicMeleeDamage);
					}
					final float infusion = (float) (attacker.getAttributeValue(attributes.VOIDIC_INFUSION) - 1F)
										   * (attacker instanceof Player p ? p.getAttackStrengthScale(0.5F) : 1F)
										   * Insanity.MAX_INFUSION;
					if (infusion > 0)
						target.getData(dataAttachments.INSANITY).addInfusion(infusion, target);
				} else if (damageState == DamageState.ARROW && event.getSource().getDirectEntity() instanceof AbstractArrow arrowEntity) {
					float voidic = arrowEntity.getData(dataAttachments.VOIDIC_ARROW);
					if (voidic > 0) {
						if (target.getHealth() <= event.getNewDamage())
							return;
						target.invulnerableTime = 0;
						target.hurt(damageSource.getEntityDamageSource(arrowEntity.level(), damageSource.VOIDIC, arrowEntity.getOwner()), voidic);
					}
					float infusion = arrowEntity.getData(dataAttachments.INFUSION_ARROW);
					if (infusion > 0) {
						target.getData(dataAttachments.INSANITY).addInfusion(infusion, target);
					}
				}
			} else if (event.getSource().is(damageSource.VOIDIC)) {
				if (target.hasEffect(effects.ICHOR)) {
					event.setNewDamage(event.getNewDamage() * 2F);
				}
				if (target.hasEffect(effects.FORTIFIED)) {
					event.setNewDamage(event.getNewDamage() * 0.25F);
					if (target.getRandom().nextInt(4) == 0) {
						target.removeEffect(effects.FORTIFIED);
					}
				}
				AttributeInstance attributeInstance = target.getAttribute(attributes.VOIDIC_RES);
				if (attributeInstance != null) {
					float res = (float) attributeInstance.getValue();
					if (res != 0)
						event.setNewDamage(event.getNewDamage() - res);
				}
			}
		});
	}

	private DamageState getDamageState(DamageSource source) {
		if (source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK))
			return DamageState.MELEE;
		if (source.is(DamageTypes.ARROW))
			return DamageState.ARROW;
		return DamageState.NONE;
	}

	private enum DamageState {
		MELEE, ARROW, NONE
	}

}
