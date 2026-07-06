package tamaized.voidscape.network;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import tamaized.beanification.Bean;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.client.ClientPacketDonatorSync;
import tamaized.voidscape.network.client.ClientPacketNoFlashOnSetHealth;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.network.client.ClientPacketInsanitySync;
import tamaized.voidscape.network.server.ServerPacketDonatorSettings;

import java.util.UUID;

@Component
public class NetworkMessages {

	@PostConstruct
	private void init(IEventBus bus) {
		bus.addListener(RegisterPayloadHandlersEvent.class, event -> {
			PayloadRegistrar network = event.registrar(Voidscape.MODID)
				.versioned("1")
				.optional();

			network.playToServer(ServerPacketDonatorSettings.ID, ServerPacketDonatorSettings.CODEC, ServerPacketDonatorSettings::handle);

			network.playToClient(ClientPacketNoFlashOnSetHealth.ID, ClientPacketNoFlashOnSetHealth.CODEC, ClientPacketNoFlashOnSetHealth::handle);
			network.playToClient(ClientPacketInsanitySync.ID, ClientPacketInsanitySync.CODEC, ClientPacketInsanitySync::handle);
			network.playToClient(ClientPacketDonatorSync.ID, ClientPacketDonatorSync.CODEC, ClientPacketDonatorSync::handle);
			network.playToClient(ClientPacketSendParticles.ID, ClientPacketSendParticles.CODEC, ClientPacketSendParticles::handle);
		});
	}

	@Bean
	private static DonatorHandler donatorHandler() {
		return FMLEnvironment.isProduction() ? new DonatorHandler() : new DonatorHandler() {
			@Override
			public boolean isDonator(UUID uuid) {
				return true;
			}
		};
	}

}
