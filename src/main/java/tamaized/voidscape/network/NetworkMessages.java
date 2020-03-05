package tamaized.voidscape.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import tamaized.voidscape.network.server.ServerPacketTurmoilTeleport;

import java.util.function.Supplier;

public class NetworkMessages {

	private static int index = 0;

	public static void register(SimpleChannel network) {
		registerMessage(network, ServerPacketTurmoilTeleport.class, IMessage.Side.SERVER);
	}

	private static <M extends IMessage<M>> void registerMessage(SimpleChannel network, Class<M> type, IMessage.Side side) {
		network.registerMessage(index++, type, IMessage::encode, p -> IMessage.decode(p, type), (m, s) -> IMessage.onMessage(m, s, side));
	}

	public interface IMessage<SELF extends IMessage<SELF>> {

		static <M extends IMessage<M>> void encode(M message, PacketBuffer packet) {
			message.toBytes(packet);
		}

		static <M extends IMessage<M>> M decode(PacketBuffer packet, Class<M> type) {
			return UnsafeHacks.newInstance(type).fromBytes(packet);
		}

		static void onMessage(IMessage message, Supplier<NetworkEvent.Context> context, Side side) {
			context.get().enqueueWork(() -> message.handle(side == Side.SERVER ? context.get().getSender() : getClientSidePlayer().get()));
			context.get().setPacketHandled(true);
		}

		@SuppressWarnings({"Convert2Lambda", "Convert2Diamond"})
		static Supplier<PlayerEntity> getClientSidePlayer() { //TODO: Improve
			return new Supplier<PlayerEntity>() {
				@Override
				public PlayerEntity get() {
					return Minecraft.getInstance().player;
				}
			};
		}

		void handle(PlayerEntity player);

		void toBytes(PacketBuffer packet);

		SELF fromBytes(PacketBuffer packet);

		enum Side {
			CLIENT, SERVER
		}

	}
}
