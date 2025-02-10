package tamaized.voidscape.datagen.assets.bakedmodel;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

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

	protected String name() {
		return name((String) null);
	}

	protected String name(@Nullable String suffix) {
		return name(itemForName()) + (suffix == null ? "" : ("_" + suffix));
	}

	protected String name(DeferredHolder<Item, ? extends Item> holder) {
		return "item/" + holder.getId().getPath();
	}

	protected String splitName() {
		return splitName((String) null);
	}

	protected String splitName(@Nullable String suffix) {
		return splitName(itemForName()) + (suffix == null ? "" : ("_" + suffix));
	}

	protected String splitName(DeferredHolder<Item, ? extends Item> holder) {
		return "item/" + String.join("/", holder.getId().getPath().split("_", 2));
	}

	protected abstract DeferredHolder<Item, ? extends Item> itemForName();

}
