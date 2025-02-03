package tamaized.voidscape.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Component;
import tamaized.voidscape.Voidscape;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
public class RegistryProvider {

	private final RegistrySetBuilder builder = new RegistrySetBuilder();
	private DatapackBuiltinEntriesProvider value;

	public CompletableFuture<HolderLookup.Provider> retrieve(GatherDataEvent event) {
		if (value == null) {
			value = new DatapackBuiltinEntriesProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), builder, Set.of("minecraft", Voidscape.MODID));
			event.getGenerator().addProvider(event.includeServer(), value);
		}
		return value.getRegistryProvider();
	}

	public HolderLookup.Provider join() {
		return value.getRegistryProvider().join();
	}

}
