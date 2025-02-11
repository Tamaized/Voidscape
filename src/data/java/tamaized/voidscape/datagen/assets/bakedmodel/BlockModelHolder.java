package tamaized.voidscape.datagen.assets.bakedmodel;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public abstract class BlockModelHolder extends ModelHolder {

	@Nullable
	private ModelFile itemBlockModel;

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

	protected String name() {
		return name(null);
	}

	protected String name(@org.jetbrains.annotations.Nullable String suffix) {
		return "block/" + nameToUse() + (suffix == null ? "" : ("_" + suffix));
	}

	protected String splitName() {
		return splitName(null);
	}

	protected String splitName(@org.jetbrains.annotations.Nullable String suffix) {
		return "block/" + String.join("/", nameToUse().split("_", 2)) + (suffix == null ? "" : ("_" + suffix));
	}

	protected String nameForItemBlock() {
		return nameForItemBlock(null);
	}

	protected String nameForItemBlock(@org.jetbrains.annotations.Nullable String suffix) {
		return "item/" + nameToUse() + (suffix == null ? "" : ("_" + suffix));
	}

	protected String splitNameForItemBlock() {
		return splitNameForItemBlock(null);
	}

	protected String splitNameForItemBlock(@org.jetbrains.annotations.Nullable String suffix) {
		return "item/" + String.join("/", nameToUse().split("_", 2)) + (suffix == null ? "" : ("_" + suffix));
	}

	@Nullable
	protected DeferredHolder<Block, ? extends Block> blockForName() {
		return null;
	}

	protected String nameToUse() {
		return Objects.requireNonNull(blockForName()).getId().getPath();
	}

	public boolean hasStandardBlockItem() {
		return false;
	}

	public final Optional<ModelFile> getItemBlockModel() {
		return Optional.ofNullable(itemBlockModel);
	}

	protected final void setItemBlockModel(ModelFile model) {
		this.itemBlockModel = model;
	}

	public void buildItemBlockModelIfEmpty(ItemModelProvider provider) {
		if (getItemBlockModel().isEmpty())
			buildAndSetItemBlockModel(provider);
	}

	public void buildAndSetItemBlockModel(ItemModelProvider provider) {
		setItemBlockModel(buildItemBlockModel(provider));
	}

	public ModelFile buildItemBlockModel(ItemModelProvider provider) {
		return provider.simpleBlockItem(Objects.requireNonNull(blockForName()).get());
	}

}
