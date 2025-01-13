package tamaized.voidscape.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.regutil.RegUtil;
import tamaized.voidscape.client.particle.ParticleSpellCloud;
import tamaized.voidscape.particle.ParticleTypeSpellCloud;

import java.util.function.Supplier;

@Component
public class ModParticles {

	private final DeferredRegister<ParticleType<?>> REGISTRY = RegUtil.create(Registries.PARTICLE_TYPE);

	public final Supplier<ParticleType<ParticleTypeSpellCloud.Options>> SPELL_CLOUD = REGISTRY.register("spell_cloud", ParticleTypeSpellCloud::new);

	@PostConstruct
	private void setup(IEventBus bus) {
		bus.addListener(RegisterParticleProvidersEvent.class, event -> {
			event.registerSpriteSet(SPELL_CLOUD.get(), ParticleSpellCloud.Factory::new);
		});
	}

}
