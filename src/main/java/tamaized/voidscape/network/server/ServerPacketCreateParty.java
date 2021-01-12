package tamaized.voidscape.network.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.PacketDistributor;
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
	public void handle(@Nullable PlayerEntity player) {
		if (duty != null && type != null && player instanceof ServerPlayerEntity && player.getServer() != null) {
			Optional<Party> o = PartyManager.findParty((ServerPlayerEntity) player);
			if (o.isPresent())
				Voidscape.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new ClientPacketUpdatePartyInfo(o.get(), true));
			else
				PartyManager.addParty(new Party(duty, type, (ServerPlayerEntity) player));

		}
	}

	@Override
	public void toBytes(PacketBuffer packet) {
		packet.writeVarInt(Duties.getID(duty));
		packet.writeVarInt(type.ordinal());
	}

	@Override
	public ServerPacketCreateParty fromBytes(PacketBuffer packet) {
		duty = Duties.fromID(packet.readVarInt());
		type = Instance.InstanceType.fromOrdinal(packet.readVarInt());
		return this;
	}

}
