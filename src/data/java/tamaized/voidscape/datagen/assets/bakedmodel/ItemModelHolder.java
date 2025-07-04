package tamaized.voidscape.datagen.assets.bakedmodel;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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
		return name(null);
	}

	protected String name(@Nullable String suffix) {
		return "item/" + nameToUse() + (suffix == null ? "" : ("_" + suffix));
	}

	/**
	 * Use #name
	 * splitName is a maintainability burden for 0 real gain
	 */
	@Deprecated(forRemoval = true)
	protected String splitName() {
		return splitName(null);
	}

	/**
	 * Use #name
	 * splitName is a maintainability burden for 0 real gain
	 */
	@Deprecated(forRemoval = true)
	protected String splitName(@Nullable String suffix) {
		//return "item/" + String.join("/", nameToUse().split("_", 2)) + (suffix == null ? "" : ("_" + suffix));
		return name(suffix);
	}

	@Nullable
	protected DeferredHolder<Item, ? extends Item> itemForName() {
		return null;
	}

	@Deprecated(forRemoval = true)
	protected String nameToUse() {
		return Objects.requireNonNull(itemForName()).getId().getPath();
	}

}
