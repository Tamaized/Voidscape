package tamaized.voidscape.registry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;
import tamaized.voidscape.turmoil.caps.IEffectContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModEffects {

	private static final List<Effect> CONTEXT_EFFECTS = new ArrayList<>();
	private static final DeferredRegister<Effect> REGISTRY = RegUtil.create(ForgeRegistries.POTIONS);
	public static final RegistryObject<ToggleEffect> FIRE_ARROW = REGISTRY.register("fire_arrow", () -> new ToggleEffect(EffectType.BENEFICIAL, 0xFFAA00, TurmoilAbility.Toggle.ArrowShot));
	public static final RegistryObject<DotEffect> TRAUMATIZE = REGISTRY.register("traumatize", () -> context(new DotEffect(EffectType.HARMFUL, 0x7700FF)));
	public static final RegistryObject<Effect> BULWARK = REGISTRY.register("bulwark", () -> new ToggleEffect(EffectType.BENEFICIAL, 0x00FFFF).
			addAttributeModifier(ModAttributes.VOIDIC_RES.get(), "360ac01a-0be0-4c85-a078-bbc0cf90a6e1", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL));
	public static final RegistryObject<Effect> ADRENALINE = REGISTRY.register("adrenaline", () -> new ToggleEffect(EffectType.BENEFICIAL, 0xFF7700).
			addAttributeModifier(ModAttributes.VOIDIC_RES.get(), "777e0d84-3984-4502-973e-851766de8bc7", 0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL));
	public static final RegistryObject<Effect> TUNNEL_VISION = REGISTRY.register("tunnel_vision", () -> context(new StandardEffect(EffectType.BENEFICIAL, 0xFF0000)));

	private static <T extends Effect> T context(T effect) {
		CONTEXT_EFFECTS.add(effect);
		return effect;
	}

	public static boolean hasContext(Effect effect) {
		return CONTEXT_EFFECTS.contains(effect);
	}

	public static void apply(LivingEntity entity, ToggleEffect effect, int duration, int amp) {
		if (effect.toggle() != TurmoilAbility.Toggle.None) {
			List<Effect> remove = new ArrayList<>();
			for (EffectInstance e : entity.getActiveEffects())
				if (e.getEffect() instanceof ToggleEffect && ((ToggleEffect) e.getEffect()).toggle() == effect.toggle())
					remove.add(e.getEffect());
			remove.forEach(entity::removeEffect);
		}
		entity.addEffect(new EffectInstance(effect, duration, amp));
	}

	public static void dot(LivingEntity caster, LivingEntity entity, DotEffect effect, int duration, int amp, float damage) {
		EffectInstance instance = new EffectInstance(effect, duration, amp);
		entity.getCapability(SubCapability.CAPABILITY_EFFECTCONTEXT).ifPresent(cap -> cap.add(instance, caster, damage));
	}

	public static void target(LivingEntity caster, LivingEntity entity, StandardEffect effect, int duration, int amp) {
		EffectInstance instance = new EffectInstance(effect, duration, amp);
		entity.getCapability(SubCapability.CAPABILITY_EFFECTCONTEXT).ifPresent(cap -> cap.add(instance, caster, 0));
	}

	public static void classload() {

	}

	public static class ToggleEffect extends Effect {

		private final TurmoilAbility.Toggle toggle;

		private ToggleEffect(EffectType type, int color) {
			this(type, color, TurmoilAbility.Toggle.None);
		}

		private ToggleEffect(EffectType type, int color, TurmoilAbility.Toggle toggle) {
			super(type, color);
			this.toggle = toggle;
		}

		public TurmoilAbility.Toggle toggle() {
			return toggle;
		}

	}

	public static class DotEffect extends Effect {

		private final int hurtTick;

		private DotEffect(EffectType type, int color) {
			this(type, color, 20);
		}

		private DotEffect(EffectType type, int color, int hurtTick) {
			super(type, color);
			this.hurtTick = hurtTick;
		}

		@Override
		public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
			return p_76397_1_ % hurtTick == 0;
		}

		@Override
		public void applyEffectTick(LivingEntity entity, int amp) {
			Optional<IEffectContext.Context> context = entity.getCapability(SubCapability.CAPABILITY_EFFECTCONTEXT).map(cap -> cap.context(this)).orElse(Optional.empty());
			if (context.isPresent())
				entity.hurt(ModDamageSource.VOIDIC_WITH_ENTITY.apply(context.get().source()), context.get().amount());
			else
				entity.hurt(ModDamageSource.VOIDIC, 1F);
		}
	}

	public static class StandardEffect extends Effect {

		private StandardEffect(EffectType type, int color) {
			super(type, color);
		}
	}

}
