package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
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
	public void handle(@Nullable Player player) {
		if (player instanceof ServerPlayer && player.getServer() != null) {
			PartyManager.findParty(player.getServer().getPlayerList().getPlayer(host)).ifPresent(party -> {
				boolean flagReserving;
				boolean flagFull = false;
				boolean flagMember = false;
				if ((flagReserving = party.isReserving()) || (flagFull = party.full()) || (flagMember = party.isMember((ServerPlayer) player)) || !party.password(password))
					Voidscape.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new ClientPacketJoinPartyError(flagReserving ? "Party in Progress" : flagFull ? "Party Full" : flagMember ? "Already a Member" : "Wrong Password"));
				else {
					if (!party.addMember((ServerPlayer) player, password))
						Voidscape.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new ClientPacketJoinPartyError("Error joining Party"));
				}
			});
		}
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
		packet.writeUUID(host);
		packet.writeUtf(password);
	}

	@Override
	public ServerPacketRequestJoinParty fromBytes(FriendlyByteBuf packet) {
		host = packet.readUUID();
		password = packet.readUtf(Short.MAX_VALUE);
		return this;
	}

}
