package tamaized.voidscape.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.bootstrap.IBootstrap;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Component
public class RegistryProvider {

	@Directory(IBootstrap.class)
	private List<IBootstrap> bootstraps;

	private RegistrySetBuilder builder = new RegistrySetBuilder();
	private DatapackBuiltinEntriesProvider value;

	@PostConstruct
	private void setup() {
		bootstraps.stream()
			.sorted(Comparator.comparingInt(IBootstrap::priority))
			.forEach(bootstrap -> builder = bootstrap.bootstrap(builder));
	}

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

	public <T> Optional<ResourceKey<T>> findKey(ResourceKey<Registry<T>> registry, T t) {
		return findKeyFrom(join(), registry, t);
	}

	public <T> Optional<ResourceKey<T>> findKeyFrom(HolderLookup.Provider provider, ResourceKey<Registry<T>> registry, T t) {
		return provider.lookupOrThrow(registry).listElements()
			.filter(b -> b.value() == t)
			.findAny().orElseThrow()
			.unwrapKey();
	}

	public <T> Stream<T> filterStreamForModFrom(HolderLookup.Provider provider, ResourceKey<Registry<T>> registry) {
		return provider.lookupOrThrow(registry)
			.listElements()
			.filter(r -> Objects.requireNonNull(r.getKey()).location().getNamespace().equals(Voidscape.MODID))
			.map(Holder.Reference::value);
	}

}
