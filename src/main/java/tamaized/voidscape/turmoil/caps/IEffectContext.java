package tamaized.voidscape.turmoil.caps;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import tamaized.voidscape.Voidscape;

import java.util.Optional;

public interface IEffectContext {

	static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "effectcontext");

	Optional<Context> context(MobEffect effect);

	MobEffectInstance add(MobEffectInstance effect, LivingEntity caster, float damage);

	void remove(MobEffect effect);

	class Context {
		private final LivingEntity source;
		private final float amount;

		Context(LivingEntity source, float amount) {
			this.source = source;
			this.amount = amount;
		}

		public LivingEntity source() {
			return source;
		}

		public float amount() {
			return amount;
		}

	}

}
