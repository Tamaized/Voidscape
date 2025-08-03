package tamaized.voidscape.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.effect.StandardEffect;

@Component
public class ModEffects {

	@Autowired
	private ModAttributes attributes;

	private final DeferredRegister<MobEffect> REGISTRY = RegUtil.create(Registries.MOB_EFFECT);

	public final DeferredHolder<MobEffect, MobEffect> ICHOR = REGISTRY.register("ichor", () -> new StandardEffect("ichor", MobEffectCategory.HARMFUL, 0xFF7700, true));
	public final DeferredHolder<MobEffect, MobEffect> AURA = REGISTRY.register("aura", () -> new StandardEffect("aura", MobEffectCategory.BENEFICIAL, 0x7700FF, false));
	public final DeferredHolder<MobEffect, MobEffect> FORTIFIED = REGISTRY.register("fortified", () -> new StandardEffect("fortified", MobEffectCategory.BENEFICIAL, 0x00FFAA, false));
	public final DeferredHolder<MobEffect, MobEffect> TRAUMATIZED = REGISTRY.register("traumatized", () -> new StandardEffect("traumatized", MobEffectCategory.BENEFICIAL, 0x7700FF, false)
		.addAttributeModifier(
			attributes.VOIDIC_DMG, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "effect.traumatized"), 5.0, AttributeModifier.Operation.ADD_VALUE
		)
		.addAttributeModifier(
			attributes.VOIDIC_ARROW_DMG, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "effect.traumatized"), 5.0, AttributeModifier.Operation.ADD_VALUE
		)
		.addAttributeModifier(
			attributes.VOIDIC_RES, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "effect.traumatized"), 5.0D, AttributeModifier.Operation.ADD_VALUE
		)
		.addAttributeModifier(
			attributes.VOIDIC_INFUSION, ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "effect.traumatized"), 0.15D, AttributeModifier.Operation.ADD_VALUE
		)
	);

}
