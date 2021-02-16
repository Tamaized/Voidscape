package tamaized.voidscape.network.server;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.PacketDistributor;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.network.client.ClientPacketJoinPartyError;
import tamaized.voidscape.party.PartyManager;

import javax.annotation.Nullable;
import java.util.UUID;

public class ServerPacketRequestJoinParty implements NetworkMessages.IMessage<ServerPacketRequestJoinParty> {

	private UUID host;
	private String password;

	public ServerPacketRequestJoinParty(UUID host, String password) {
		this.host = host;
		this.password = password;
	}

	@Override
	public void handle(@Nullable PlayerEntity player) {
		if (player instanceof ServerPlayerEntity && player.getServer() != null) {
			PartyManager.findParty(player.getServer().getPlayerList().getPlayer(host)).ifPresent(party -> {
				boolean flagReserving;
				boolean flagFull = false;
				boolean flagMember = false;
				if ((flagReserving = party.isReserving()) || (flagFull = party.full()) || (flagMember = party.isMember((ServerPlayerEntity) player)) || !party.password(password))
					Voidscape.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new ClientPacketJoinPartyError(flagReserving ? "Party in Progress" : flagFull ? "Party Full" : flagMember ? "Already a Member" : "Wrong Password"));
				else {
					if (!party.addMember((ServerPlayerEntity) player, password))
						Voidscape.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new ClientPacketJoinPartyError("Error joining Party"));
				}
			});
		}
	}

	@Override
	public void toBytes(PacketBuffer packet) {
		packet.writeUUID(host);
		packet.writeUtf(password);
	}

	@Override
	public ServerPacketRequestJoinParty fromBytes(PacketBuffer packet) {
		host = packet.readUUID();
		password = packet.readUtf(Short.MAX_VALUE);
		return this;
	}

}
