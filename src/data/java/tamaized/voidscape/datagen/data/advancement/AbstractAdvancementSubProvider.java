package tamaized.voidscape.datagen.data.advancement;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import tamaized.voidscape.Voidscape;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractAdvancementSubProvider implements AdvancementProvider.AdvancementGenerator {

	@Nullable
	private AdvancementHolder holder;

	@Override
	public final void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
		makeAndSet(registries, saver, existingFileHelper);
	}

	private AdvancementHolder makeAndSet(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
		holder = make(registries, saver, existingFileHelper);
		return holder;
	}

	public final Optional<AdvancementHolder> get() {
		return Optional.ofNullable(holder);
	}

	public final AdvancementHolder getOrMake(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
		return get().orElseGet(() -> makeAndSet(registries, saver, existingFileHelper));
	}

	public abstract AdvancementHolder make(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper);

	protected abstract String name();

	protected final Component title() {
		return Component.translatable("advancement." + Voidscape.MODID + "." + name());
	}

	protected final Component description() {
		return Component.translatable("advancement." + Voidscape.MODID + "." + name() + ".desc");
	}

	protected final String location() {
		return ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, name()).toString();
	}

}
