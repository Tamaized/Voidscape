package tamaized.voidscape.datagen.assets.bakedmodel;

import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public abstract class BlockModelHolder extends ModelHolder {

	public ModelFile getOrBuild(BlockModelProvider provider) {
		return get().orElseGet(() -> {
			buildAndSet(provider);
			return get().orElseThrow();
		});
	}

	public void buildIfEmpty(BlockModelProvider provider) {
		if (get().isEmpty())
			buildAndSet(provider);
	}

	public void buildAndSet(BlockModelProvider provider) {
		set(build(provider));
	}

	public abstract ModelFile build(BlockModelProvider provider);

}
