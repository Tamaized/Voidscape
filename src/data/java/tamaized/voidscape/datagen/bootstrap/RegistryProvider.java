package tamaized.voidscape.datagen.bootstrap;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.util.CachedBootstrapHolderGetter;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Component
public class RegistryProvider {

	@Directory(IBootstrap.class)
	private List<IBootstrap> bootstraps;

	@Autowired
	private CachedBootstrapHolderGetter cachedBootstrapHolderGetter;

	private RegistrySetBuilder builder = new RegistrySetBuilder();

	@Nullable
	private DatapackBuiltinEntriesProvider value;

	@PostConstruct
	private void setup() {
		cachedBootstrapHolderGetter.invalidate();
		bootstraps.stream()
			.sorted(Comparator.comparingInt(IBootstrap::priority))
			.forEach(bootstrap -> builder = bootstrap.bootstrap(builder));
	}

	public CompletableFuture<HolderLookup.Provider> retrieve(GatherDataEvent event) {
		if (value == null) {
			value = new DatapackBuiltinEntriesProvider(
				event.getGenerator().getPackOutput(),
				event.getLookupProvider(),
				builder,
				Set.of("minecraft", Voidscape.MODID)
			);
			event.getGenerator().addProvider(true, value);
		}
		return value.getRegistryProvider();
	}

	public HolderLookup.Provider join() {
		return Objects.requireNonNull(value).getRegistryProvider().join();
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
			.filter(r -> Objects.requireNonNull(r.getKey()).identifier().getNamespace().equals(Voidscape.MODID))
			.map(Holder.Reference::value);
	}

}
