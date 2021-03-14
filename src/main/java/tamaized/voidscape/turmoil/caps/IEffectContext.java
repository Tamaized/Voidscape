package tamaized.voidscape.turmoil.caps;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import tamaized.voidscape.Voidscape;

import java.util.Optional;

public interface IEffectContext {

	static final ResourceLocation ID = new ResourceLocation(Voidscape.MODID, "effectcontext");

	Optional<Context> context(Effect effect);

	EffectInstance add(EffectInstance effect, LivingEntity caster, float damage);

	void remove(Effect effect);

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
