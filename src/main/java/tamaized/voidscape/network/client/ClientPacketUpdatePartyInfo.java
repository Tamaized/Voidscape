package tamaized.voidscape.network.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.party.ClientPartyInfo;
import tamaized.voidscape.party.Party;
import tamaized.voidscape.turmoil.Duties;
import tamaized.voidscape.world.Instance;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClientPacketUpdatePartyInfo implements NetworkMessages.IMessage<ClientPacketUpdatePartyInfo> {

	private UUID host;
	private List<UUID> members = new ArrayList<>();
	private String password;
	private int max;
	public Duties.Duty duty;
	public Instance.InstanceType type;
	public boolean reserving;

	public ClientPacketUpdatePartyInfo(@Nullable Party party, boolean isHost) {
		if (party != null) {
			host = party.host().getUUID();
			members.addAll(party.members().stream().filter(member -> member != party.host()).map(Entity::getUUID).collect(Collectors.toList()));
			password = isHost ? party.password() : party.hasPassword() ? "secret" : "";
			max = party.maxMembers();
			duty = party.duty();
			type = party.instanceType();
			reserving = party.isReserving();
		}
	}

	@Override
	public void handle(@Nullable PlayerEntity player) {
		if (player == null || player.level == null || !player.level.isClientSide()) {
			Voidscape.LOGGER.fatal("Warning, client attempted to send malicious packet! ({})", player == null ? "NULL PLAYER" : player.getDisplayName());
			return;
		}
		ClientPartyInfo.update(host, members, password, max, duty, type, reserving);
	}

	@Override
	public void toBytes(PacketBuffer packet) {
		packet.writeUUID(host);
		packet.writeVarInt(members.size());
		members.forEach(packet::writeUUID);
		packet.writeUtf(password);
		packet.writeVarInt(max);
		packet.writeVarInt(Duties.getID(duty));
		packet.writeVarInt(type.ordinal());
		packet.writeBoolean(reserving);
	}

	@Override
	public ClientPacketUpdatePartyInfo fromBytes(PacketBuffer packet) {
		host = packet.readUUID();
		int len = packet.readVarInt();
		for (int i = 0; i < len; i++)
			members.add(packet.readUUID());
		password = packet.readUtf();
		max = packet.readVarInt();
		duty = Duties.fromID(packet.readVarInt());
		type = Instance.InstanceType.fromOrdinal(packet.readVarInt());
		reserving = packet.readBoolean();
		return this;
	}

}
