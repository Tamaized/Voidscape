package tamaized.voidscape.entity.ai.pawn;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import tamaized.voidscape.entity.EntityCorruptedPawnBoss;
import tamaized.voidscape.entity.ai.AITask;
import tamaized.voidscape.registry.ModDamageSource;

import java.util.Random;
import java.util.function.Predicate;

public class TankBuster extends AITask.RandomAITask.ChanceAITask<EntityCorruptedPawnBoss> {

	public TankBuster(float damage, boolean stick, Predicate<RandomSource> rand) {
		super(rand);
		next(next(new AITask.RepeatedAITask<>((boss, ai) -> {
			if (boss.aiTick == 0) {
				if (boss.tickCount % 60 == 0) {
					boss.aiTick = boss.tickCount + 20 * 6;
					boss.lockonTarget = boss.getTarget();
					boss.beStill = true;
					boss.markCasting(true);
					boss.setRayBits(0b011111111);
					boss.setRayTarget(boss.lockonTarget);
					boss.updateRayStart();
					boss.updateRayEnd(20 * 6);
				}
			} else if (boss.tickCount >= boss.aiTick) {
				if (boss.lockonTarget == null || !boss.lockonTarget.isAlive())
					boss.lockonTarget = boss.getTarget();
				if (boss.lockonTarget != null && boss.lockonTarget.isAlive())
					boss.lockonTarget.hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(boss), damage);
				boss.beStill = false;
				boss.markCasting(false);
				boss.setRayBits(0);
				boss.setRayTarget(null);
				boss.aiTick = 0;
				ai.finish();
			} else if (boss.lockonTarget != null && boss.lockonTarget.isAlive()) {
				boss.lookAt(boss.lockonTarget, 10F, 10F);
				if (!stick && boss.distanceToSqr(boss.lockonTarget) >= 100F) {
					boss.beStill = false;
					boss.markCasting(false);
					boss.setRayBits(0);
					boss.setRayTarget(null);
					boss.aiTick = 0;
					boss.level.playSound(null, boss.xo, boss.yo, boss.zo, SoundEvents.ENDER_DRAGON_GROWL, boss.getSoundSource(), 0.75F, 0.25F + boss.getRandom().nextFloat() * 0.5F);
					ai.finish();
				}
			}
		})));
	}

}
