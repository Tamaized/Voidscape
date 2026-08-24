package tamaized.voidscape.datagen.datapack.voidscape_aether_compat;

import com.aetherteam.aether.data.generators.AetherRegistrySets;
import com.mojang.serialization.DynamicOps;
import net.minecraft.DetectedVersion;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.Directory;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.datagen.bootstrap.IBootstrap;
import tamaized.voidscape.datagen.util.BootstrapContextHolderLookupResolver;
import tamaized.voidscape.datagen.util.HolderReferenceBinderUtil;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class VoidscapeAetherCompatRegistryProvider {

	@Directory(IBootstrap.class)
	private List<IBootstrap> bootstraps;

	@Autowired
	private BootstrapContextHolderLookupResolver bootstrapContextHolderLookupResolver;

	@Autowired
	private HolderReferenceBinderUtil holderReferenceBinderUtil;

	private RegistrySetBuilder builder = new RegistrySetBuilder();

	@Nullable
	private CompletableFuture<RegistrySetBuilder.PatchedRegistries> aetherRegistries;

	private final HolderOwner<?> owner = new HolderOwner<>() {
		@Override
		public boolean canSerializeIn(HolderOwner<Object> owner) {
			return true;
		}
	};

	@PostConstruct
	private void setup(IEventBus bus) {
		bootstraps.stream()
			.sorted(Comparator.comparingInt(IBootstrap::priority))
			.forEach(bootstrap -> builder = bootstrap.bootstrap(builder));

		bus.addListener(GatherDataEvent.class, event -> {
			event.getGenerator()
				.getBuiltinDatapack(true, "minecraft", "voidscape_aether_compat")
				.addProvider(f -> new DatapackBuiltinEntriesProvider(
					new PackOutput(f.getOutputFolder()),
					event.getLookupProvider().thenApply(v -> new HolderLookup.Provider() {
						@Override
						public Stream<ResourceKey<? extends Registry<?>>> listRegistries() {
							return Stream.concat(v.listRegistries(), getAetherRegistries().join().patches().listRegistries())
								.collect(Collectors.toSet())
								.stream();
						}

						@Override
						public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryKey) {
							if (registryKey.equals(Registries.BIOME))
								return v.lookup(registryKey);

							Optional<HolderLookup.RegistryLookup<T>> root = v.lookup(registryKey);
							Optional<HolderLookup.RegistryLookup<T>> aether = VoidscapeAetherCompatRegistryProvider.this.lookup(registryKey);
							HolderOwner<T> owner = new HolderOwner<>() {
								@Override
								public boolean canSerializeIn(HolderOwner<T> owner) {
									return owner == this || root.map(o -> o.canSerializeIn(owner)).orElse(false) || aether.map(o -> o.canSerializeIn(owner)).orElse(false);
								}
							};
							Map<ResourceKey<T>, Holder.Reference<T>> map = new HashMap<>();
							Stream.of(root, aether).forEach(r -> r.ifPresent(l -> l.listElements().forEach(e -> {
								Holder.Reference<T> ref = Holder.Reference.createStandAlone(owner, Objects.requireNonNull(e.getKey()));
								holderReferenceBinderUtil.bindValue(ref, e.value());
								map.put(e.key(), ref);
							})));
							return Optional.of(RegistrySetBuilder.lookupFromMap(
								registryKey,
								root.or(() -> aether).map(HolderLookup.RegistryLookup::registryLifecycle).orElseThrow(),
								owner,
								map
							));
						}

						@Override
						public <V> RegistryOps<V> createSerializationContext(DynamicOps<V> ops) {
							RegistryOps<V> root = v.createSerializationContext(ops);
							RegistryOps<V> aether = RegistryOps.create(ops, getAetherRegistries().join().patches());
							return RegistryOps.create(ops, new RegistryOps.RegistryInfoLookup() {
								public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
									return root.lookupProvider.lookup(key).or(() -> aether.lookupProvider.lookup(key));
								}
							});
						}
					}),
					builder,
					Set.of(Voidscape.MODID)
				));

			event.getGenerator()
				.getBuiltinDatapack(true, "minecraft", "voidscape_aether_compat")
				.addProvider(f -> new PackMetadataGenerator(new PackOutput(f.getOutputFolder()))
					.add(PackMetadataSection.TYPE, new PackMetadataSection(
							net.minecraft.network.chat.Component.literal("Resources for Voidscape - Aether Compat"),
							DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
							Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE))
						)
					)
				);
		});
	}

	private CompletableFuture<RegistrySetBuilder.PatchedRegistries> getAetherRegistries() {
		if (aetherRegistries == null)
			aetherRegistries = bootstrapContextHolderLookupResolver.resolveFor(AetherRegistrySets.BUILDER);
		return aetherRegistries;
	}

	public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
		return Objects.requireNonNull(getAetherRegistries()).join().patches().lookup(key);
	}

	@SuppressWarnings("unchecked")
	public <T> HolderOwner<T> getHolderOwner() {
		return (HolderOwner<T>) owner;
	}

	public <T> Holder.Reference<T> createOwnedHolderReference(ResourceKey<Registry<T>> registry, ResourceKey<T> key) {
		Holder.Reference<T> ref = Holder.Reference.createStandAlone(getHolderOwner(), key);
		holderReferenceBinderUtil.bindValue(ref, lookup(registry).orElseThrow().get(key).orElseThrow().value());
		return ref;
	}

}
