package tamaized.voidscape.entity.ai.pawn;

import net.minecraft.world.phys.Vec3;
import tamaized.voidscape.entity.EntityCorruptedPawnBoss;
import tamaized.voidscape.entity.EntityCorruptedPawnTentacle;
import tamaized.voidscape.entity.ai.AITask;

import java.util.function.Predicate;

public class TentacleFall extends AITask.PendingAITask<EntityCorruptedPawnBoss> {

	public TentacleFall(int extra, float health, long duration, float damage, Predicate<EntityCorruptedPawnBoss> condition) {
		super((boss, ai) -> {
			if (!boss.isInvulnerable()) {
				boss.setInvulnerable(true);
				boss.beStill = true;
				boss.markCasting(true);
				boss.setRayBits(0);
				boss.setRayTarget(null);
				boss.lockonTarget = null;
				int left = extra + 1;
				int disable = 0;
				while (left > 0) {
					int num = boss.getRandom().nextInt(8);
					if (boss.tentacleIndicies.contains(num))
						continue;
					boss.tentacleIndicies.add(num);
					disable |= 1 << num;
					left--;
					Vec3 pos = EntityCorruptedPawnBoss.TENTACLE_POSITIONS[num];
					EntityCorruptedPawnTentacle tentacle = new EntityCorruptedPawnTentacle(boss.level, boss, pos).withHealth(health).explodes(duration, damage);
					boss.tentacles.add(tentacle);
					boss.level.addFreshEntity(tentacle);
				}
				boss.disableTentacles(disable);
				boss.teleportHome();
			} else {
				boss.tentacles.removeIf(t -> !t.isAlive());
				if (boss.tentacles.isEmpty()) {
					int t = 0;
					for (Integer i : boss.tentacleIndicies)
						t |= 1 << i;
					boss.tentacleIndicies.clear();
					boss.enableTentacles(t);
					boss.markCasting(false);
					boss.setInvulnerable(false);
					boss.teleportHome();
					boss.beStill = false;
					ai.finish();
				} else
					boss.setInvulnerable(true);
			}
		}, condition);
	}
}
