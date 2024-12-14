package tamaized.voidscape.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.effect.StandardEffect;

@Component
public class ModEffects {

	private final DeferredRegister<MobEffect> REGISTRY = RegUtil.create(Registries.MOB_EFFECT);

	public final Holder<MobEffect> ICHOR = REGISTRY.register("ichor", () -> new StandardEffect("ichor", MobEffectCategory.HARMFUL, 0xFF7700, true));
	public final Holder<MobEffect> AURA = REGISTRY.register("aura", () -> new StandardEffect("aura", MobEffectCategory.BENEFICIAL, 0x7700FF, false));
	public final Holder<MobEffect> FORTIFIED = REGISTRY.register("fortified", () -> new StandardEffect("fortified", MobEffectCategory.BENEFICIAL, 0x00FFAA, false));

}
