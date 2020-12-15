package tamaized.voidscape.turmoil.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.SubCapability;

public final class TurmoilAbilityInstance {

	private final TurmoilAbility ability;
	private long lastCast;

	public TurmoilAbilityInstance(TurmoilAbility ability) {
		this.ability = ability;
	}

	public boolean canAfford(LivingEntity caster) {
		return caster.getCapability(SubCapability.CAPABILITY).map(resolve -> resolve.get(Voidscape.subCapTurmoilStats).map(stats -> stats.getVoidicPower() >= ability.getCost()).get()).orElse(false);
	}

	public boolean canExecute(LivingEntity caster) {
		return cooldownRemaining(caster.level) <= 0 && canAfford(caster);
	}

	public void execute(LivingEntity caster) {
		if (!canExecute(caster))
			return;
		ability.execute(caster);
		caster.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> stats.setVoidicPower(stats.getVoidicPower() - ability.getCost())));
		lastCast = caster.level.getGameTime();
	}

	public long cooldownRemaining(World level) {
		return Math.max((ability.cooldown() - (level.getGameTime() - lastCast)), 0);
	}

	public float cooldownPercent(World level) {
		return MathHelper.clamp(cooldownRemaining(level) / ability.cooldown(), 0, 1);
	}

}
