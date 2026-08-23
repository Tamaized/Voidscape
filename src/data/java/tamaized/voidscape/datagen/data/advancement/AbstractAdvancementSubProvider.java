package tamaized.voidscape.datagen.data.advancement;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import tamaized.voidscape.Voidscape;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractAdvancementSubProvider implements AdvancementSubProvider {

	@Nullable
	private AdvancementHolder holder;

	@Override
	public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
		makeIfEmpty(registries, output);
	}

	private void makeIfEmpty(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {
		if (holder == null)
			makeAndSet(registries, saver);
	}

	private AdvancementHolder makeAndSet(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {
		holder = make(registries, saver);
		return holder;
	}

	public final Optional<AdvancementHolder> get() {
		return Optional.ofNullable(holder);
	}

	public final AdvancementHolder getOrMake(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {
		return get().orElseGet(() -> makeAndSet(registries, saver));
	}

	public abstract AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver);

	protected abstract String name();

	protected final Component title() {
		return Component.translatable("advancement." + Voidscape.MODID + "." + name());
	}

	protected final Component description() {
		return Component.translatable("advancement." + Voidscape.MODID + "." + name() + ".desc");
	}

	protected final String location() {
		return Identifier.fromNamespaceAndPath(Voidscape.MODID, name()).toString();
	}

}
