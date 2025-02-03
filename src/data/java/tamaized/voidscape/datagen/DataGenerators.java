package tamaized.voidscape.datagen;

import net.minecraft.DetectedVersion;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.datagen.generator.AssetsGenerator;
import tamaized.voidscape.datagen.generator.DataGenerator;

import java.util.Optional;

@Component
public class DataGenerators {

	@Autowired
	private AssetsGenerator assetsGenerator;

	@Autowired
	private DataGenerator dataGenerator;

	@PostConstruct
	private void register(IEventBus bus) {
		bus.addListener(GatherDataEvent.class, event -> {
			assetsGenerator.generate(event);
			dataGenerator.generate(event);

			event.getGenerator().addProvider(true, new PackMetadataGenerator(event.getGenerator().getPackOutput())
				.add(PackMetadataSection.TYPE, new PackMetadataSection(
						net.minecraft.network.chat.Component.literal("Resources for Voidscape"),
						DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
						Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE))
					)
				)
			);
		});
	}

}
