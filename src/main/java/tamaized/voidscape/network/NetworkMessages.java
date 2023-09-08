package tamaized.voidscape.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import tamaized.voidscape.client.ClientUtil;
import tamaized.voidscape.network.client.ClientPacketNoFlashOnSetHealth;
import tamaized.voidscape.network.client.ClientPacketSubCapSync;
import tamaized.voidscape.network.server.ServerPacketHandlerDonatorSettings;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class NetworkMessages {

	private static int index = 0;

	public static void register(SimpleChannel network) {
		registerMessage(network, ServerPacketHandlerDonatorSettings.class, () -> new ServerPacketHandlerDonatorSettings(new DonatorHandler.DonatorSettings(false, 0)));

		registerMessage(network, ClientPacketNoFlashOnSetHealth.class, ClientPacketNoFlashOnSetHealth::new);
		registerMessage(network, ClientPacketSubCapSync.class, ClientPacketSubCapSync::new);
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

		static <M extends IMessage<M>> void onMessage(M message, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> message.handle(getSidedPlayer(context.get().getSender())));
			context.get().setPacketHandled(true);
		}

		@Nullable
		static Player getSidedPlayer(@Nullable Player test) {
			return test == null ? ClientUtil.getClientPlayerSafely() : test;
		}

		void handle(@Nullable Player sup);

		void toBytes(FriendlyByteBuf packet);

		SELF fromBytes(FriendlyByteBuf packet);

	}
}
