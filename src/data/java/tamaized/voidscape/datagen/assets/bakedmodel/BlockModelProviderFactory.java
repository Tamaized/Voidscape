package tamaized.voidscape.datagen.assets.bakedmodel;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.voidscape.Voidscape;

import java.util.List;
import java.util.Objects;

@Component
public class BlockModelProviderFactory {

	@Directory(BlockModelHolder.class)
	private List<BlockModelHolder> blockModelHolders;

	public BlockModelProvider make(GatherDataEvent event) {
		return new BlockModelProvider(
			event.getGenerator().getPackOutput(),
			Voidscape.MODID,
			event.getExistingFileHelper()
		) {
			@Override
			protected void registerModels() {
				blockModelHolders.forEach(holder -> holder.buildIfEmpty(this));
			}
		};
	}

	public void makeBlockItems(ItemModelProvider provider) {
		blockModelHolders.stream().filter(BlockModelHolder::hasStandardBlockItem).forEach(holder -> holder.buildItemBlockModelIfEmpty(provider));
	}

	public void makeBlockstates(BlockStateProvider provider) {
		blockModelHolders.stream().filter(BlockModelHolder::hasBlockState).forEach(holder -> holder.buildBlockState(provider));
	}

	public void addLangEntries(LanguageProvider provider) {
		blockModelHolders.forEach(holder -> holder.lang().ifPresent(
			lang -> provider.addBlock(Objects.requireNonNull(holder.blockForName()), lang)
		));
	}

}
