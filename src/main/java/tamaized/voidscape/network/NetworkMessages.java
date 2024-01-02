package tamaized.voidscape.network;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.client.ClientPacketDonatorSync;
import tamaized.voidscape.network.client.ClientPacketNoFlashOnSetHealth;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.network.client.ClientPacketInsanitySync;
import tamaized.voidscape.network.server.ServerPacketHandlerDonatorSettings;

public class NetworkMessages {

	public static void register(IEventBus busMod) {
		busMod.addListener(RegisterPayloadHandlerEvent.class, event -> {
			IPayloadRegistrar network = event.registrar(Voidscape.MODID)
					.versioned("1")
					.optional();

			network.play(ServerPacketHandlerDonatorSettings.ID, ServerPacketHandlerDonatorSettings::new, side -> side.server(ServerPacketHandlerDonatorSettings::handle));

			network.play(ClientPacketNoFlashOnSetHealth.ID, ClientPacketNoFlashOnSetHealth::new, side -> side.client(ClientPacketNoFlashOnSetHealth::handle));
			network.play(ClientPacketInsanitySync.ID, ClientPacketInsanitySync::new, side -> side.client(ClientPacketInsanitySync::handle));
			network.play(ClientPacketDonatorSync.ID, ClientPacketDonatorSync::new, side -> side.client(ClientPacketDonatorSync::handle));
			network.play(ClientPacketSendParticles.ID, ClientPacketSendParticles::new, side -> side.client(ClientPacketSendParticles::handle));
		});
	}

}
