package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.skills.TurmoilSkill;

import javax.annotation.Nullable;

public class ServerPacketTurmoilSkillClaim implements NetworkMessages.IMessage<ServerPacketTurmoilSkillClaim> {

	private int id;

	public ServerPacketTurmoilSkillClaim(int id) {
		this.id = id;
	}

	@Override
	public void handle(@Nullable Player player) {
		if (player != null)
			player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> data.claimSkill(TurmoilSkill.getFromID(id))));
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
		packet.writeVarInt(id);
	}

	@Override
	public ServerPacketTurmoilSkillClaim fromBytes(FriendlyByteBuf packet) {
		id = packet.readVarInt();
		return this;
	}

}
