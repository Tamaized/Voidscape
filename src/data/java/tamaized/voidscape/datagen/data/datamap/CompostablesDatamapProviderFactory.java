package tamaized.voidscape.datagen.data.datamap;

import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;
import tamaized.voidscape.registry.ModBlockComponentDirectory;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class CompostablesDatamapProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private ModBlockComponentDirectory blocks;

	public DataMapProvider make(GatherDataEvent event) {
		return new DataMapProvider(
			event.getGenerator().getPackOutput(),
			registryProvider.retrieve(event)
		) {
			@Override
			protected void gather() {
				this.builder(NeoForgeDataMaps.COMPOSTABLES)
					.add(blocks.thunderForestBiomeBlocks().THUNDER_WART_ITEM, new Compostable(0.85F), false)
					.add(blocks.thunderForestBiomeBlocks().THUNDER_ROOTS_ITEM, new Compostable(0.65F), false)
					.add(blocks.thunderForestBiomeBlocks().THUNDER_FUNGUS_ITEM, new Compostable(0.65F), false)
					.add(blocks.thunderForestBiomeBlocks().THUNDER_VINES_ITEM, new Compostable(0.50F), false)
					.add(items.etherealFruitItems().ETHEREAL_FRUIT_VOID, new Compostable(0.30F), false)
					.add(items.etherealFruitItems().ETHEREAL_FRUIT_NULL, new Compostable(0.30F), false)
					.add(items.etherealFruitItems().ETHEREAL_FRUIT_OVERWORLD, new Compostable(0.30F), false)
					.add(items.etherealFruitItems().ETHEREAL_FRUIT_NETHER, new Compostable(0.30F), false)
					.add(items.etherealFruitItems().ETHEREAL_FRUIT_END, new Compostable(0.30F), false)
					.build();
			}
		};
	}

}
