package tamaized.voidscape.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleChannel;
import tamaized.voidscape.client.ClientUtil;
import tamaized.voidscape.network.client.ClientPacketDonatorSync;
import tamaized.voidscape.network.client.ClientPacketNoFlashOnSetHealth;
import tamaized.voidscape.network.client.ClientPacketSendParticles;
import tamaized.voidscape.network.client.ClientPacketInsanitySync;
import tamaized.voidscape.network.server.ServerPacketHandlerDonatorSettings;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class NetworkMessages {

	private static int index = 0;

	public static void register(SimpleChannel network) {
		registerMessage(network, ServerPacketHandlerDonatorSettings.class, () -> new ServerPacketHandlerDonatorSettings(new DonatorHandler.DonatorSettings(false, 0)));

		registerMessage(network, ClientPacketNoFlashOnSetHealth.class, ClientPacketNoFlashOnSetHealth::new);
		registerMessage(network, ClientPacketInsanitySync.class, ClientPacketInsanitySync::new);
		registerMessage(network, ClientPacketDonatorSync.class, ClientPacketDonatorSync::new);
		registerMessage(network, ClientPacketSendParticles.class, ClientPacketSendParticles::new);
	}

	private static <M extends IMessage<M>> void registerMessage(SimpleChannel network, Class<M> type, Supplier<M> factory) {
		network.registerMessage(index++, type, IMessage::encode, p -> IMessage.decode(p, factory), IMessage::onMessage);
	}

	public interface IMessage<SELF extends IMessage<SELF>> {

		static <M extends IMessage<M>> void encode(M message, FriendlyByteBuf packet) {
			message.toBytes(packet);
		}

		static <M extends IMessage<M>> M decode(FriendlyByteBuf packet, Supplier<M> factory) {
			return factory.get().fromBytes(packet);
		}

		static <M extends IMessage<M>> void onMessage(M message, NetworkEvent.Context context) {
			context.enqueueWork(() -> message.handle(getSidedPlayer(context.getSender())));
			context.setPacketHandled(true);
		}

		@Nullable
		static Player getSidedPlayer(@Nullable Player test) {
			return test == null ? ClientUtil.getClientPlayerSafely() : test;
		}

		void handle(@Nullable Player player);

		void toBytes(FriendlyByteBuf packet);

		SELF fromBytes(FriendlyByteBuf packet);

	}
}
