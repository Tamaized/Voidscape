package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.effect.StandardEffect;

@Component
public class ModEffects {

	@Autowired
	private ModAttributes attributes;

	public final DeferredHolder<MobEffect, MobEffect> ICHOR = RegUtil.register(Registries.MOB_EFFECT, "ichor",
		() -> new StandardEffect("ichor", MobEffectCategory.HARMFUL, 0xFF7700, true));

	public final DeferredHolder<MobEffect, MobEffect> AURA = RegUtil.register(Registries.MOB_EFFECT, "aura",
		() -> new StandardEffect("aura", MobEffectCategory.BENEFICIAL, 0x7700FF, false));

	public final DeferredHolder<MobEffect, MobEffect> FORTIFIED = RegUtil.register(Registries.MOB_EFFECT, "fortified",
		() -> new StandardEffect("fortified", MobEffectCategory.BENEFICIAL, 0x00FFAA, false));

	public final DeferredHolder<MobEffect, MobEffect> TRAUMATIZED = RegUtil.register(Registries.MOB_EFFECT, "traumatized",
		(key) -> new StandardEffect("traumatized", MobEffectCategory.BENEFICIAL, 0x7700FF, false)
			.addAttributeModifier(
				attributes.VOIDIC_DMG, key.withPrefix("effect."), 5.0, AttributeModifier.Operation.ADD_VALUE
			)
			.addAttributeModifier(
				attributes.VOIDIC_ARROW_DMG, key.withPrefix("effect."), 5.0, AttributeModifier.Operation.ADD_VALUE
			)
			.addAttributeModifier(
				attributes.VOIDIC_RES, key.withPrefix("effect."), 5.0D, AttributeModifier.Operation.ADD_VALUE
			)
			.addAttributeModifier(
				attributes.VOIDIC_INFUSION, key.withPrefix("effect."), 0.15D, AttributeModifier.Operation.ADD_VALUE
			)
	);

}
