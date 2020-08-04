package tamaized.voidscape.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import tamaized.voidscape.network.common.CommonPacketSubCapSync;
import tamaized.voidscape.network.server.ServerPacketTurmoilAction;

import java.util.function.Supplier;

public class NetworkMessages {

	private static int index = 0;

	public static void register(SimpleChannel network) {
		registerMessage(network, ServerPacketTurmoilAction.class, ServerPacketTurmoilAction::new);
		registerMessage(network, CommonPacketSubCapSync.class, () -> new CommonPacketSubCapSync(null));
	}

	private static <M extends IMessage<M>> void registerMessage(SimpleChannel network, Class<M> type, Supplier<M> factory) {
		network.registerMessage(index++, type, IMessage::encode, p -> IMessage.decode(p, factory), IMessage::onMessage);
	}

	public interface IMessage<SELF extends IMessage<SELF>> {

		static <M extends IMessage<M>> void encode(M message, PacketBuffer packet) {
			message.toBytes(packet);
		}

		static <M extends IMessage<M>> M decode(PacketBuffer packet, Supplier<M> factory) {
			return factory.get().fromBytes(packet);
		}

		static void onMessage(IMessage message, Supplier<NetworkEvent.Context> context) {
			context.get().enqueueWork(() -> message.handle(getSidedPlayer(context.get().getSender())));
			context.get().setPacketHandled(true);
		}

		static Supplier<Supplier<PlayerEntity>> getSidedPlayer(PlayerEntity test) {
			return () -> test == null ? () -> Minecraft.getInstance().player : () -> test;
		}

		void handle(Supplier<Supplier<PlayerEntity>> sup);

		void toBytes(PacketBuffer packet);

		SELF fromBytes(PacketBuffer packet);

	}
}
