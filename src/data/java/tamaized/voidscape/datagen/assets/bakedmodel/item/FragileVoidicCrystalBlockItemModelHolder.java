package tamaized.voidscape.datagen.assets.bakedmodel.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.assets.bakedmodel.ItemModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.VoidicCrystalBlockBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class FragileVoidicCrystalBlockItemModelHolder extends ItemModelHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private VoidicCrystalBlockBlockModelHolder parent;

	@Override
	protected @Nullable DeferredHolder<Item, ? extends Item> itemForName() {
		return blocks.imposterBlocks().FRAGILE_VOIDIC_CRYSTAL_BLOCK_ITEM;
	}

	@Override
	public ModelFile build(ItemModelProvider provider) {
		return provider.withExistingParent(name(), parent.getOrBuildItemBlockModel(provider).getLocation());
	}
}
