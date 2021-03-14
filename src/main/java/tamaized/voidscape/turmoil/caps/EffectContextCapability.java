package tamaized.voidscape.turmoil.caps;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EffectContextCapability implements IEffectContext {

	private Map<Effect, Context> map = new HashMap<>();

	@Override
	public Optional<Context> context(Effect effect) {
		return Optional.ofNullable(map.get(effect));
	}

	@Override
	public EffectInstance add(EffectInstance effect, LivingEntity caster, float damage) {
		map.putIfAbsent(effect.getEffect(), new Context(caster, damage));
		return effect;
	}

	@Override
	public void remove(Effect effect) {
		map.remove(effect);
	}
}
