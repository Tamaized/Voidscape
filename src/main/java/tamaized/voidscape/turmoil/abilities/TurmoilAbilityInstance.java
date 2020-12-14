package tamaized.voidscape.turmoil.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public final class TurmoilAbilityInstance {

	private final TurmoilAbility ability;
	private long lastCast;

	public TurmoilAbilityInstance(TurmoilAbility ability) {
		this.ability = ability;
	}

	public boolean canExecute(World level) {
		return cooldownRemaining(level) <= 0;
	}

	public void execute(LivingEntity caster) {
		if (!canExecute(caster.level))
			return;
		ability.execute(caster);
		lastCast = caster.level.getGameTime();
	}

	public long cooldownRemaining(World level) {
		return Math.max((ability.cooldown() - (level.getGameTime() - lastCast)), 0);
	}

	public float cooldownPercent(World level) {
		return MathHelper.clamp(cooldownRemaining(level) / ability.cooldown(), 0, 1);
	}

}
