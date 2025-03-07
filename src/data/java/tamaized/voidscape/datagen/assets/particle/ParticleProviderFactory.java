package tamaized.voidscape.datagen.assets.particle;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;
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
			event.getGenerator().getPackOutput(),
			event.getExistingFileHelper()
		) {
			@Override
			protected void addDescriptions() {
				sprite(particles.SPELL_CLOUD.get(), ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "spell_cloud"));
			}
		};
	}

}
