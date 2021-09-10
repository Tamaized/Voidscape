package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.network.client.ClientPacketUpdatePartyInfo;
import tamaized.voidscape.party.Party;
import tamaized.voidscape.party.PartyManager;
import tamaized.voidscape.turmoil.Duties;
import tamaized.voidscape.world.Instance;

import javax.annotation.Nullable;
import java.util.Optional;

public class ServerPacketCreateParty implements NetworkMessages.IMessage<ServerPacketCreateParty> {

	private Duties.Duty duty;
	private Instance.InstanceType type;

	public ServerPacketCreateParty(Duties.Duty duty, Instance.InstanceType type) {
		this.duty = duty;
		this.type = type;
	}

	@Override
	public void handle(@Nullable Player player) {
		if (duty != null && type != null && player instanceof ServerPlayer && player.getServer() != null) {
			Optional<Party> o = PartyManager.findParty((ServerPlayer) player);
			if (o.isPresent())
				Voidscape.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new ClientPacketUpdatePartyInfo(o.get(), true));
			else
				PartyManager.addParty(new Party(duty, type, (ServerPlayer) player));

		}
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
		packet.writeVarInt(Duties.getID(duty));
		packet.writeVarInt(type.ordinal());
	}

	@Override
	public ServerPacketCreateParty fromBytes(FriendlyByteBuf packet) {
		duty = Duties.fromID(packet.readVarInt());
		type = Instance.InstanceType.fromOrdinal(packet.readVarInt());
		return this;
	}

}
