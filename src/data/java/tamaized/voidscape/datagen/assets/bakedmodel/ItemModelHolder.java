package tamaized.voidscape.datagen.assets.bakedmodel;

import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public abstract class ItemModelHolder extends ModelHolder {

	public ModelFile getOrBuild(ItemModelProvider provider) {
		return get().orElseGet(() -> {
			build(provider);
			return get().orElseThrow();
		});
	}

	public void buildIfEmpty(ItemModelProvider provider) {
		if (get().isEmpty())
			build(provider);
	}

	public abstract void build(ItemModelProvider provider);

}
