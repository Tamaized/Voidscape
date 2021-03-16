package tamaized.voidscape.turmoil.caps;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

import java.util.Comparator;
import java.util.Map;
import java.util.WeakHashMap;

public class AggroTable implements IAggroTable {

	private Map<LivingEntity, DoubleValue> table = new WeakHashMap<>();

	@Override
	public void tick(MobEntity entity) {
		table.entrySet().stream().max(Comparator.comparingDouble(o -> o.getValue().value())).ifPresent(e -> entity.setTarget(e.getKey()));
		table.values().removeIf(v -> v.sub(1.0D) <= 0);
	}

	@Override
	public void addHate(LivingEntity attacker, double hate) {
		if (hate <= 0)
			return;
		if (table.containsKey(attacker))
			table.get(attacker).add(hate);
		else
			table.put(attacker, new DoubleValue(hate));
	}

	@Override
	public void placeAtTop(LivingEntity entity) {
		table.put(entity, new DoubleValue(table.entrySet().stream().max(Comparator.comparingDouble(o -> o.getValue().value())).
				map(livingEntityDoubleValueEntry -> livingEntityDoubleValueEntry.getValue().value()).orElse(1.0D)));
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

		double value() {
			return val;
		}
	}
}
