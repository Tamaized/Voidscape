package tamaized.voidscape.registry;

import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.KnownPack;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;

import java.util.Optional;

@Component
public class ModDatapacks {

	public final Lazy<Pack> AETHER_INTEGRATION = Lazy.of(() -> Pack.readMetaAndCreate(
		new PackLocationInfo(
			"voidscape_aether_compat",
			net.minecraft.network.chat.Component.literal("Voidscape Aether Integration"),
			PackSource.FEATURE,
			Optional.of(new KnownPack(
				Voidscape.MODID,
				"integrations/aether",
				"1.0.0"
			))
		),
		BuiltInPackSource.fromName(name -> new PathPackResources(
			name,
			ModList.get().getModFileById(Voidscape.MODID).getFile().findResource("data", "minecraft", "datapacks", "voidscape_aether_compat")
		)),
		PackType.SERVER_DATA,
		new PackSelectionConfig(
			false,
			Pack.Position.TOP,
			true
		)
	));

	@PostConstruct
	private void init(IEventBus bus) {
		bus.addListener(AddPackFindersEvent.class, event -> {
			if (event.getPackType() == PackType.SERVER_DATA && ModList.get().isLoaded("aether")) {
				event.addRepositorySource(packConsumer -> packConsumer.accept(AETHER_INTEGRATION.get()));
			}
		});
	}

}
