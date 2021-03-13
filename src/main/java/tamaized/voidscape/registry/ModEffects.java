package tamaized.voidscape.registry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;

import java.util.ArrayList;
import java.util.List;

public class ModEffects {

	private static final DeferredRegister<net.minecraft.potion.Effect> REGISTRY = RegUtil.create(ForgeRegistries.POTIONS);

	public static final RegistryObject<Effect> FIRE_ARROW = REGISTRY.register("fire_arrow", () -> new Effect(EffectType.BENEFICIAL, 0xFFAA00, TurmoilAbility.Toggle.ArrowShot));

	public static class Effect extends net.minecraft.potion.Effect {

		private final TurmoilAbility.Toggle toggle;

		private Effect(EffectType type, int color) {
			this(type, color, TurmoilAbility.Toggle.None);
		}

		private Effect(EffectType type, int color, TurmoilAbility.Toggle toggle) {
			super(type, color);
			this.toggle = toggle;
		}

		public TurmoilAbility.Toggle toggle() {
			return toggle;
		}

	}

	public static void apply(LivingEntity entity, Effect effect, int duration, int amp) {
		if (effect.toggle() != TurmoilAbility.Toggle.None) {
			List<net.minecraft.potion.Effect> remove = new ArrayList<>();
			for (EffectInstance e : entity.getActiveEffects())
				if (e.getEffect() instanceof Effect && ((Effect) e.getEffect()).toggle() == effect.toggle())
					remove.add(e.getEffect());
			remove.forEach(entity::removeEffect);
		}
		entity.addEffect(new EffectInstance(effect, duration, amp));
	}

	public static void classload() {

	}

}
