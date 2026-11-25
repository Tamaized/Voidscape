package tamaized.voidscape.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.connector.fml.EventBusPostConstruct;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.entity.CorruptedPawnEntity;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.registry.ModEffects;

import java.util.List;

@Component
public class ConfusedPlayerAttackHandler {

	@Autowired
	private ModDataAttachments dataAttachments;

	@Autowired
	private ModEffects effects;

	private boolean redirecting;

	@EventBusPostConstruct(EventBusPostConstruct.Bus.GAME)
	private void setup(IEventBus bus) {
		bus.addListener(EventPriority.LOWEST, AttackEntityEvent.class, event -> {
			Player player = event.getEntity();

			if (!(player.level() instanceof ServerLevel level) || redirecting || player.getRandom().nextFloat() >= getConfusedAttackChance(player))
				return;

			event.setCanceled(true);
			level.playSound(null, player, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 1F, 0.5F + player.getRandom().nextFloat() * 0.1F);

			List<LivingEntity> targets = getTargets(level, player, event.getTarget());
			int index = player.getRandom().nextInt(targets.size() + 1);

			if (index < targets.size())
				redirect(player, targets.get(index));
			else
				hurtSelf(level, player);
		});
	}

	private float getConfusedAttackChance(Player player) {
		Insanity insanity = player.getData(dataAttachments.INSANITY);
		if (insanity.getHunter() != null)
			return 0F;
		float paranoia = insanity.getParanoia() / Insanity.MAX_PARANOIA;
		return Math.max(
			player.hasEffect(effects.MADDENED) ? 0.33F : 0F,
			paranoia > 0.75F ? Mth.map(paranoia, 0.75F, 1F, 0.10F, 0.33F) : 0F
		);
	}

	private List<LivingEntity> getTargets(ServerLevel level, Player player, Entity original) {
		ItemStack weapon = player.getMainHandItem();
		return level.getEntitiesOfClass(
			LivingEntity.class,
			player.getBoundingBox().inflate(player.getAttackRangeWith(weapon).effectiveMaxRange(player)),
			entity -> entity != player &&
				entity != original &&
				!(entity instanceof CorruptedPawnEntity) &&
				entity.isAlive() &&
				entity.isPickable() &&
				player.isWithinAttackRange(weapon, entity.getBoundingBox(), 0D) &&
				player.hasLineOfSight(entity)
		);
	}

	private void redirect(Player player, LivingEntity target) {
		redirecting = true;
		try {
			player.attack(target);
		} finally {
			redirecting = false;
		}
	}

	private void hurtSelf(ServerLevel level, Player player) {
		float damage = Math.min(player.getMaxHealth() * 0.15F * player.getAttackStrengthScale(0.5F), player.getHealth() - 1F);
		if (damage > 0F)
			player.hurtServer(level, player.damageSources().generic(), damage);
	}

}
