package tamaized.voidscape.datagen.generator;

import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.BlockModelProviderFactory;
import tamaized.voidscape.datagen.assets.bakedmodel.ItemModelProviderFactory;
import tamaized.voidscape.datagen.assets.blockstate.BlockStateProviderFactory;
import tamaized.voidscape.datagen.assets.lang.LangProviderFactory;
import tamaized.voidscape.datagen.assets.particle.ParticleProviderFactory;

@Component
public class AssetsGenerator {

	@Autowired
	private BlockModelProviderFactory blockModelProviderFactory;

	@Autowired
	private ItemModelProviderFactory itemModelProviderFactory;

	@Autowired
	private BlockStateProviderFactory blockStateProviderFactory;

	@Autowired
	private LangProviderFactory langProviderFactory;

	@Autowired
	private ParticleProviderFactory particleProviderFactory;

	public void generate(GatherDataEvent event) {
		event.getGenerator().addProvider(true, blockModelProviderFactory.make(event));
		event.getGenerator().addProvider(true, itemModelProviderFactory.make(event));
		event.getGenerator().addProvider(true, blockStateProviderFactory.make(event));
		event.getGenerator().addProvider(true, langProviderFactory.make(event));
		event.getGenerator().addProvider(true, particleProviderFactory.make(event));
	}

}
