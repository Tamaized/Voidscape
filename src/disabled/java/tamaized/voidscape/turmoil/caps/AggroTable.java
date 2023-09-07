package tamaized.voidscape.turmoil.caps;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.SubCapability;

import java.util.Comparator;
import java.util.Map;
import java.util.WeakHashMap;

public class AggroTable implements IAggroTable {

	private Map<LivingEntity, DoubleValue> table = new WeakHashMap<>();

	@Override
	public void tick(Mob entity) {
		table.entrySet().stream().max(Comparator.comparingDouble(o -> o.getValue().value())).ifPresent(e -> entity.setTarget(e.getKey()));
		table.entrySet().removeIf(e -> e.getValue().sub(1.0D) <= 0 || e.
				getKey().getCapability(SubCapability.CAPABILITY).map(cap -> cap.get(Voidscape.subCapTurmoilTracked).map(data -> data.incapacitated).orElse(false)).orElse(false));
	}

	@Override
	public void addHate(LivingEntity attacker, double hate, boolean existing) {
		if (hate <= 0 || (attacker instanceof Player && (((Player) attacker).isCreative() || attacker.isSpectator())))
			return;
		if (table.containsKey(attacker))
			table.get(attacker).add(hate);
		else if (!existing)
			table.put(attacker, new DoubleValue(hate));
	}

	@Override
	public void mulHate(LivingEntity attacker, double hate) {
		if (hate <= 1)
			return;
		if (table.containsKey(attacker))
			table.get(attacker).mul(hate);
		else
			table.put(attacker, new DoubleValue(hate));
	}

	@Override
	public void placeAtTop(LivingEntity entity) {
		table.put(entity, new DoubleValue(table.entrySet().stream().max(Comparator.comparingDouble(o -> o.getValue().value())).
				map(livingEntityDoubleValueEntry -> livingEntityDoubleValueEntry.getValue().value()).orElse(1.0D)));
	}

	@Override
	public void remove(LivingEntity entity) {
		table.remove(entity);
	}

	@Override
	public boolean hasHate(LivingEntity entity) {
		return table.containsKey(entity);
	}

	static class DoubleValue {
		double val;

		DoubleValue(double d) {
			val = d;
		}

		double add(double d) {
			return val += d;
		}

		double sub(double d) {
			return val -= d;
		}

		double mul(double d) {
			return val *= d;
		}

		double value() {
			return val;
		}
	}
}
