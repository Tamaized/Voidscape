package tamaized.voidscape.datagen.datapack.voidscape_aether_compat;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.data.generators.AetherRegistrySets;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
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

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Component
public class VoidscapeAetherCompatRegistryProvider {

	@Directory(IBootstrap.class)
	private List<IBootstrap> bootstraps;

	@Autowired
	private BootstrapContextHolderLookupResolver bootstrapContextHolderLookupResolver;

	private RegistrySetBuilder builder = new RegistrySetBuilder();

	@PostConstruct
	private void setup(IEventBus bus) {
		bootstraps.stream()
			.sorted(Comparator.comparingInt(IBootstrap::priority))
			.forEach(bootstrap -> builder = bootstrap.bootstrap(builder));

		bus.addListener(GatherDataEvent.class, event -> event.getGenerator()
			.getBuiltinDatapack(event.includeServer(), "minecraft", "voidscape_aether_compat")
			.addProvider(f -> new DatapackBuiltinEntriesProvider(
				new PackOutput(f.getOutputFolder()),
				event.getLookupProvider(),
				builder,
				Set.of("minecraft", Aether.MODID, Voidscape.MODID)
			)));
	}

	public <T> HolderLookup.RegistryLookup<T> lookup(ResourceKey<Registry<T>> key) {
		return bootstrapContextHolderLookupResolver.resolveFor(AetherRegistrySets.BUILDER).patches().lookupOrThrow(key);
	}

}
