package tamaized.voidscape.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class MaddeningEffect extends MobEffect {

	public MaddeningEffect(MobEffectCategory type, int color) {
		super(type, color);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
		return true;
	}

	@Override
	public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplification) {
		if (mob instanceof Mob m && !(m.getTarget() instanceof Mob)) {
			serverLevel.getEntities(m, m.getBoundingBox().inflate(8D), e -> !(e instanceof Player) && e instanceof LivingEntity).stream()
				.findAny()
				.map(LivingEntity.class::cast)
				.ifPresent(m::setTarget);
		}
		return true;
	}
}
