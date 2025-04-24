package tamaized.voidscape.datagen.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceKey;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CachedBootstrapHolderGetter {

	@Autowired
	private BootstrapContextHolderLookupResolver bootstrapContextHolderLookupResolver;

	private final Map<ResourceKey<Registry<?>>, List<ResourceKey<?>>> cache = new HashMap<>();

	public void invalidate() {
		cache.clear();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public <T> List<ResourceKey<T>> retrieve(ResourceKey<Registry<T>> key, HolderGetter<T> provider) {
		return _retrieve((ResourceKey) key, provider);
	}

	private List<ResourceKey<?>> _retrieve(ResourceKey<Registry<?>> key, HolderGetter<?> provider) {
		if (cache.containsKey(key)) {
			return cache.get(key);
		}

		List<ResourceKey<?>> entries = VanillaRegistries.createLookup().lookupOrThrow(key).listElements()
			.map(Holder::unwrapKey)
			.map(Optional::orElseThrow)
			.collect(Collectors.toList());

		entries.addAll(bootstrapContextHolderLookupResolver.getHolders(provider).orElseThrow()
			.values().stream()
			.map(Holder::unwrapKey)
			.map(Optional::orElseThrow)
			.toList());

		cache.put(key, entries);

		return entries;
	}

}
