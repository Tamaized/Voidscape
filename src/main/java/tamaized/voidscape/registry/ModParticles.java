package tamaized.voidscape.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Component;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.particle.ParticleTypeSpellCloud;

import java.util.function.Supplier;

@Component
public class ModParticles {

	private final DeferredRegister<ParticleType<?>> REGISTRY = RegUtil.create(Registries.PARTICLE_TYPE);

	public final Supplier<ParticleType<ParticleTypeSpellCloud.Options>> SPELL_CLOUD = REGISTRY.register("spell_cloud", ParticleTypeSpellCloud::new);

}
