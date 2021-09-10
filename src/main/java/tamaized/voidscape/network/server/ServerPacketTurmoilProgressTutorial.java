package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.turmoil.Progression;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Talk;
import tamaized.voidscape.turmoil.Turmoil;

import javax.annotation.Nullable;

public class ServerPacketTurmoilProgressTutorial implements NetworkMessages.IMessage<ServerPacketTurmoilProgressTutorial> {

	@Override
	public void handle(@Nullable Player player) {
		if (player != null)
			player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				if (data.getProgression() == Progression.MidTutorial && data.getState() == Turmoil.State.OPEN && !data.isTalking())
					data.talk(Talk.TUTORIAL_SKILLS);
			}));
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {

	}

	@Override
	public ServerPacketTurmoilProgressTutorial fromBytes(FriendlyByteBuf packet) {
		return this;
	}

}
