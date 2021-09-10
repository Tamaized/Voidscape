package tamaized.voidscape.turmoil.caps;


import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EffectContextCapability implements IEffectContext {

	private Map<MobEffect, Context> map = new HashMap<>();

	@Override
	public Optional<Context> context(MobEffect effect) {
		return Optional.ofNullable(map.get(effect));
	}

	@Override
	public MobEffectInstance add(MobEffectInstance effect, LivingEntity caster, float damage) {
		map.put(effect.getEffect(), new Context(caster, damage));
		return effect;
	}

	@Override
	public void remove(MobEffect effect) {
		map.remove(effect);
	}
}
