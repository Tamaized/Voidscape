package tamaized.voidscape.datagen.assets.bakedmodel;

import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;

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

}
