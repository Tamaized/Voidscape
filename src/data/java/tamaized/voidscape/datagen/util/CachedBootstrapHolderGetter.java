package tamaized.voidscape.datagen.util;

import com.aetherteam.aether.data.generators.AetherRegistrySets;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceKey;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.datapack.voidscape_aether_compat.VoidscapeAetherCompatRegistryProvider;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CachedBootstrapHolderGetter {

	@Autowired
	private BootstrapContextHolderLookupResolver bootstrapContextHolderLookupResolver;

	@Autowired
	private VoidscapeAetherCompatRegistryProvider aetherCompatRegistryProvider;

	private final Map<ResourceKey<Registry<?>>, List<ResourceKey<?>>> cache = new HashMap<>();

	private HolderGetter<?> currentProvider;

	public void invalidate() {
		cache.clear();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public <T> List<ResourceKey<T>> retrieve(ResourceKey<Registry<T>> key, HolderGetter<T> provider) {
		return _retrieve((ResourceKey) key, provider);
	}

	private List<ResourceKey<?>> _retrieve(ResourceKey<Registry<?>> key, HolderGetter<?> provider) {
		if (currentProvider != provider) {
			invalidate();
			currentProvider = provider;
		}

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
			.sorted()
			.toList());

		entries.addAll(aetherCompatRegistryProvider.lookup(key).orElseThrow()
			.listElements()
			.map(Holder.Reference::unwrapKey)
			.map(Optional::orElseThrow)
			.toList());

		cache.put(key, entries);

		return entries;
	}

}
