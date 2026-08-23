package tamaized.voidscape.datagen.data.tag.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.datagenutil.data.tag.ExposedKeyTagProvider;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.bootstrap.RegistryProvider;

import java.util.List;

@Component
public class BlockTagProviderFactory {

	@Autowired
	private RegistryProvider registryProvider;

	@Directory(IBlockTagProviderFactory.class)
	List<IBlockTagProviderFactory> factories;

	public ExposedKeyTagProvider<Block> make(GatherDataEvent event) {
		return new ExposedKeyTagProvider<>(
			event.getGenerator().getPackOutput(),
			Registries.BLOCK,
			registryProvider.retrieve(event),
			Voidscape.MODID
		) {
			@Override
			protected void addTags(HolderLookup.Provider provider) {
				factories.forEach(f -> f.make(this, provider));
			}
		};
	}

}
