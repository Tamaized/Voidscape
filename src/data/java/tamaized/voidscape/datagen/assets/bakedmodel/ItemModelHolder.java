package tamaized.voidscape.datagen.assets.bakedmodel;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;

public abstract class ItemModelHolder extends ModelHolder {

	public ModelFile getOrBuild(ItemModelProvider provider) {
		return get().orElseGet(() -> {
			buildAndSet(provider);
			return get().orElseThrow();
		});
	}

	public void buildIfEmpty(ItemModelProvider provider) {
		if (get().isEmpty())
			buildAndSet(provider);
	}

	public void buildAndSet(ItemModelProvider provider) {
		set(build(provider));
	}

	public abstract ModelFile build(ItemModelProvider provider);

	protected String name(DeferredHolder<Item, ? extends Item> holder) {
		return "item/" + holder.getId().getPath();
	}

	protected String splitName(DeferredHolder<Item, ? extends Item> holder) {
		return "item/" + String.join("/", holder.getId().getPath().split("_", 2));
	}

}
