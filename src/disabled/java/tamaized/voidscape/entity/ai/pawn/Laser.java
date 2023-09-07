package tamaized.voidscape.entity.ai.pawn;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import tamaized.voidscape.entity.EntityCorruptedPawnBoss;
import tamaized.voidscape.entity.ai.AITask;
import tamaized.voidscape.registry.ModDamageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Laser extends AITask.RandomAITask.ChanceAITask<EntityCorruptedPawnBoss> {

	public Laser(float damage, Predicate<RandomSource> rand) {
		super(rand);
		next(next(new RepeatedAITask<>((boss, ai) -> {
			if (boss.aiTick == 0) {
				if (boss.tickCount % 60 == 0) {
					boss.aiTick = boss.tickCount + 20 * 16;
					boss.lockonTarget = boss.getRandomTarget();
					boss.beStill = true;
					boss.markSpin(true);
					boss.updateSpinStart();
					boss.updateSpinEnd(20 * 16);
				}
			} else if (boss.tickCount == boss.aiTick - (20 * 2)) {
				List<Player> toDamage = new ArrayList<>();
				Vec3 vec = boss.getViewVector(1F).scale(18);
				AABB box1 = new AABB(new BlockPos(vec.scale(3F/18F).add(boss.getPosition(1F)))).move(-0.5F, -0.5F, -0.5F).inflate(3F);
				AABB box2 = new AABB(new BlockPos(vec.scale(9F/18F).add(boss.getPosition(1F)))).move(-0.5F, -0.5F, -0.5F).inflate(3F);
				AABB box3 = new AABB(new BlockPos(vec.scale(15F/18F).add(boss.getPosition(1F)))).move(-0.5F, -0.5F, -0.5F).inflate(3F);
				toDamage.addAll(boss.level.getEntitiesOfClass(Player.class, box1, e -> !toDamage.contains(e)));
				toDamage.addAll(boss.level.getEntitiesOfClass(Player.class, box2, e -> !toDamage.contains(e)));
				toDamage.addAll(boss.level.getEntitiesOfClass(Player.class, box3, e -> !toDamage.contains(e)));
				toDamage.forEach(e -> e.hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(boss), damage));
			} else if (boss.tickCount == boss.aiTick - (20 * 4)) {
				boss.level.playSound(null, boss.xo, boss.yo, boss.zo, SoundEvents.BEACON_POWER_SELECT, boss.getSoundSource(), 4F, 0.25F + boss.getRandom().nextFloat() * 0.5F);
			} else if (boss.lockonTarget != null && boss.lockonTarget.isAlive() && boss.tickCount < boss.aiTick - (20 * 5)) {
				boss.lookAt(boss.lockonTarget, 0.5F, 0.5F);
			} else if (boss.tickCount >= boss.aiTick) {
				clean(boss);
				ai.finish();
			}
		})));
	}

	@Override
	public void clear(EntityCorruptedPawnBoss boss) {
		super.clear(boss);
		clean(boss);
	}

	private void clean(EntityCorruptedPawnBoss boss) {
		boss.beStill = false;
		boss.lockonTarget = null;
		boss.markSpin(false);
		boss.aiTick = 0;
	}

}
