package tamaized.voidscape.datagen.generator;

import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.ModelProviderFactory;
import tamaized.voidscape.datagen.assets.equipment.EquipmentProviderFactory;
import tamaized.voidscape.datagen.assets.lang.LangProviderFactory;
import tamaized.voidscape.datagen.assets.particle.ParticleProviderFactory;

@Component
public class AssetsGenerator {

	@Autowired
	private ModelProviderFactory modelProviderFactory;

	@Autowired
	private LangProviderFactory langProviderFactory;

	@Autowired
	private ParticleProviderFactory particleProviderFactory;

	@Autowired
	private EquipmentProviderFactory equipmentProviderFactory;

	public void generate(GatherDataEvent.Client event) {
		event.getGenerator().addProvider(true, modelProviderFactory.make(event));
		event.getGenerator().addProvider(true, langProviderFactory.make(event));
		event.getGenerator().addProvider(true, particleProviderFactory.make(event));
		event.getGenerator().addProvider(true, equipmentProviderFactory.make(event));
	}

}
