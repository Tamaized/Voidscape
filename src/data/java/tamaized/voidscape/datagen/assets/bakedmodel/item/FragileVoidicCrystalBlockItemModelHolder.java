package tamaized.voidscape.datagen.assets.bakedmodel.item;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.ExtendedTextureMapping;
import tamaized.datagenutil.assets.bakedmodel.FurtherExtendedModelTemplateBuilder;
import tamaized.datagenutil.assets.bakedmodel.item.ItemModelHolder;
import tamaized.voidscape.datagen.assets.bakedmodel.block.VoidicCrystalBlockBlockModelHolder;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

import java.util.Objects;

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
	public Identifier finalize(ItemModelGenerators provider, FurtherExtendedModelTemplateBuilder model) {
		Identifier id = parent.get().orElseThrow();
		provider.itemModelOutput.accept(Objects.requireNonNull(itemForName()).value(), ItemModelUtils.plainModel(id));
		return id;
	}

	@Override
	protected void defineTextureSlots(ExtendedTextureMapping mapping) {
	}
}
