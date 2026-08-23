package tamaized.voidscape.datagen.assets.particle;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.data.ParticleDescriptionProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.registry.ModParticles;

@Component
public class ParticleProviderFactory {

	@Autowired
	private ModParticles particles;

	public ParticleDescriptionProvider make(GatherDataEvent event) {
		return new ParticleDescriptionProvider(
			event.getGenerator().getPackOutput()
		) {
			@Override
			protected void addDescriptions() {
				spriteSet(particles.SPELL_CLOUD.get(), Identifier.fromNamespaceAndPath(Voidscape.MODID, "spell_cloud"));
			}
		};
	}

}
