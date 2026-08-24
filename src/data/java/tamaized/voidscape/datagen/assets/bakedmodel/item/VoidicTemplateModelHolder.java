package tamaized.voidscape.datagen.assets.bakedmodel.item;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.assets.bakedmodel.item.BasicItemModelHolder;
import tamaized.voidscape.registry.ModItemComponentDirectory;

import java.util.Optional;

@Component
public class VoidicTemplateModelHolder extends BasicItemModelHolder {

	@Autowired
	private ModItemComponentDirectory items;

	@Override
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return items.augmentItems().VOIDIC_TEMPLATE;
	}

	@Override
	protected ModelTemplate template() {
		return ModelTemplates.FLAT_ITEM;
	}

	@Override
	public Optional<String> lang() {
		return Optional.of("Voidic Smithing Template");
	}
}
