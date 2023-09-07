package tamaized.voidscape.entity.ai.pawn;

import tamaized.voidscape.entity.EntityCorruptedPawnBoss;
import tamaized.voidscape.entity.ai.AITask;
import tamaized.voidscape.registry.ModDamageSource;

public class AutoAttack extends AITask.RepeatedAITask<EntityCorruptedPawnBoss> {

	public AutoAttack(float damage) {
		super((boss, ai) -> {
			if (boss.aiTick == 0) {
				if (boss.tickCount % 60 == 0) {
					boss.aiTick = boss.tickCount + 20 * 3;
					boss.lockonTarget = boss.getTarget();
					boss.setRayBits(0b100000000);
					boss.setRayTarget(boss.lockonTarget);
					boss.updateRayStart();
					boss.updateRayEnd(20 * 3);
				}
			} else if (boss.tickCount >= boss.aiTick) {
				if (boss.lockonTarget == null || !boss.lockonTarget.isAlive())
					boss.lockonTarget = boss.getTarget();
				if (boss.lockonTarget != null && boss.lockonTarget.isAlive())
					boss.lockonTarget.hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(boss), damage);
				clean(boss);
				ai.finish();
			}
		});
	}

	@Override
	public void clear(EntityCorruptedPawnBoss boss) {
		super.clear(boss);
		clean(boss);
	}

	private static void clean(EntityCorruptedPawnBoss boss) {
		boss.setRayBits(0);
		boss.setRayTarget(null);
		boss.aiTick = 0;
	}

}
