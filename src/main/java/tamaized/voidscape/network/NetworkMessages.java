package tamaized.voidscape.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import tamaized.voidscape.client.ClientUtil;
import tamaized.voidscape.network.client.ClientPacketJoinPartyError;
import tamaized.voidscape.network.client.ClientPacketResetPartyInfo;
import tamaized.voidscape.network.client.ClientPacketSendPartyList;
import tamaized.voidscape.network.client.ClientPacketSubCapSync;
import tamaized.voidscape.network.client.ClientPacketUpdatePartyInfo;
import tamaized.voidscape.network.server.ServerPacketCommenceDuty;
import tamaized.voidscape.network.server.ServerPacketCreateParty;
import tamaized.voidscape.network.server.ServerPacketDisbandParty;
import tamaized.voidscape.network.server.ServerPacketRemovePartyMember;
import tamaized.voidscape.network.server.ServerPacketRequestJoinParty;
import tamaized.voidscape.network.server.ServerPacketRequestPartyInfo;
import tamaized.voidscape.network.server.ServerPacketRequestPartyList;
import tamaized.voidscape.network.server.ServerPacketSetPartyPassword;
import tamaized.voidscape.network.server.ServerPacketSuccumbDeath;
import tamaized.voidscape.network.server.ServerPacketTurmoilAction;
import tamaized.voidscape.network.server.ServerPacketTurmoilActivateAbility;
import tamaized.voidscape.network.server.ServerPacketTurmoilProgressTutorial;
import tamaized.voidscape.network.server.ServerPacketTurmoilResetSkills;
import tamaized.voidscape.network.server.ServerPacketTurmoilSetSpellBar;
import tamaized.voidscape.network.server.ServerPacketTurmoilSkillClaim;
import tamaized.voidscape.network.server.ServerPacketTurmoilTeleport;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class NetworkMessages {

	private static int index = 0;

	@SuppressWarnings("ConstantConditions")
	public static void register(SimpleChannel network) {
		registerMessage(network, ServerPacketTurmoilAction.class, ServerPacketTurmoilAction::new);
		registerMessage(network, ServerPacketTurmoilProgressTutorial.class, ServerPacketTurmoilProgressTutorial::new);
		registerMessage(network, ServerPacketTurmoilSkillClaim.class, () -> new ServerPacketTurmoilSkillClaim(0));
		registerMessage(network, ServerPacketTurmoilSetSpellBar.class, ServerPacketTurmoilSetSpellBar::new);
		registerMessage(network, ServerPacketTurmoilActivateAbility.class, () -> new ServerPacketTurmoilActivateAbility(0));
		registerMessage(network, ServerPacketTurmoilTeleport.class, ServerPacketTurmoilTeleport::new);
		registerMessage(network, ServerPacketSetPartyPassword.class, () -> new ServerPacketSetPartyPassword(""));
		registerMessage(network, ServerPacketRequestPartyList.class, ServerPacketRequestPartyList::new);
		registerMessage(network, ServerPacketRequestJoinParty.class, () -> new ServerPacketRequestJoinParty(null, null));
		registerMessage(network, ServerPacketRequestPartyInfo.class, ServerPacketRequestPartyInfo::new);
		registerMessage(network, ServerPacketCreateParty.class, () -> new ServerPacketCreateParty(null, null));
		registerMessage(network, ServerPacketCommenceDuty.class, ServerPacketCommenceDuty::new);
		registerMessage(network, ServerPacketDisbandParty.class, ServerPacketDisbandParty::new);
		registerMessage(network, ServerPacketRemovePartyMember.class, () -> new ServerPacketRemovePartyMember(0));
		registerMessage(network, ServerPacketTurmoilResetSkills.class, ServerPacketTurmoilResetSkills::new);
		registerMessage(network, ServerPacketSuccumbDeath.class, ServerPacketSuccumbDeath::new);

		registerMessage(network, ClientPacketSubCapSync.class, () -> new ClientPacketSubCapSync(null));
		registerMessage(network, ClientPacketUpdatePartyInfo.class, () -> new ClientPacketUpdatePartyInfo(null, false));
		registerMessage(network, ClientPacketResetPartyInfo.class, () -> new ClientPacketResetPartyInfo(false));
		registerMessage(network, ClientPacketSendPartyList.class, ClientPacketSendPartyList::new);
		registerMessage(network, ClientPacketJoinPartyError.class, () -> new ClientPacketJoinPartyError(""));
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

		@Nullable
		static PlayerEntity getSidedPlayer(@Nullable PlayerEntity test) {
			return test == null ? ClientUtil.getClientPlayerSafely() : test;
		}

		void handle(@Nullable PlayerEntity sup);

		void toBytes(PacketBuffer packet);

		SELF fromBytes(PacketBuffer packet);

	}
}
