package tamaized.voidscape.entity.ai.wrath;

import tamaized.voidscape.entity.EntityVoidsWrathBoss;
import tamaized.voidscape.entity.ai.AITask;

import java.util.function.Predicate;

public class Heal extends AITask.RepeatedPendingAITask<EntityVoidsWrathBoss> {

	public Heal(Predicate<EntityVoidsWrathBoss> condition) {
		super((boss, ai) -> {
			if (boss.aiTick < (20 * 10)) {
				boss.heal(0.05f); // 10 HP
				boss.aiTick++;
			} else {
				boss.noKnockback = false;
				boss.armor = false;
				boss.beStill = false;
				boss.markGlowing(false);
				ai.finish();
			}
		}, condition);
	}

	private void run(EntityVoidsWrathBoss boss, AITask<EntityVoidsWrathBoss> ai) {

	}

	@Override
	protected void trigger(EntityVoidsWrathBoss boss) {
		boss.aiTick = 0;
		boss.noKnockback = true;
		boss.armor = true;
		boss.beStill = true;
		boss.markGlowing(true);
	}
}
