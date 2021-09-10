package tamaized.voidscape.entity.ai.pawn;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.entity.EntityCorruptedPawnBoss;
import tamaized.voidscape.entity.EntityCorruptedPawnTentacle;
import tamaized.voidscape.entity.ai.AITask;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.skills.TurmoilSkill;
import tamaized.voidscape.world.InstanceManager;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Bind extends AITask.RandomAITask.ChanceAITask<EntityCorruptedPawnBoss> {

	private long cooldown;

	public Bind(float health, long duration, float damage, Predicate<Random> rand) {
		super(rand);
		next(next(new RepeatedAITask<>((boss, ai) -> {
			if (cooldown == 0)
				cooldown = boss.tickCount + 20 * 30;
			if (cooldown > boss.tickCount) {
				ai.finish();
				return;
			}
			cooldown = boss.tickCount + 20 * 30;
			InstanceManager.findByLevel(boss.level).ifPresent(instance -> {
				List<Player> players = instance.players();
				List<Player> list = players.stream().filter(p -> p.getCapability(SubCapability.CAPABILITY).
						map(cap -> cap.get(Voidscape.subCapBind).
								map(bind -> !bind.isBound()).orElse(true) && cap.get(Voidscape.subCapTurmoilData).map(data -> data.classType() == TurmoilSkill.CoreType.Healer).
								orElse(false)).orElse(false)).collect(Collectors.toList());
				if (list.isEmpty())
					list = players.stream().filter(p -> p.getCapability(SubCapability.CAPABILITY).
							map(cap -> cap.get(Voidscape.subCapBind).
									map(bind -> !bind.isBound()).orElse(true) && cap.get(Voidscape.subCapTurmoilData).map(data -> data.classType() == TurmoilSkill.CoreType.Melee || data.classType() == TurmoilSkill.CoreType.Ranged).
									orElse(false)).orElse(false)).collect(Collectors.toList());
				if (list.isEmpty())
					list = players;
				list = list.stream().filter(Entity::canUpdate).collect(Collectors.toList());
				if (!list.isEmpty()) {
					Player player = list.size() == 1 ? list.get(0) : list.get(boss.getRandom().nextInt(list.size()));
					EntityCorruptedPawnTentacle tentacle = new EntityCorruptedPawnTentacle(boss.level, boss, player.position()).withHealth(health).explodes(duration, damage).markBinding(player);
					boss.level.addFreshEntity(tentacle);
				}
			});
			ai.finish();
		})));
	}

}
