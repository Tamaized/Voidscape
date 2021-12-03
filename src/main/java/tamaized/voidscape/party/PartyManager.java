package tamaized.voidscape.party;

import com.google.common.collect.ImmutableList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.client.ClientPacketResetPartyInfo;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Voidscape.MODID)
public final class PartyManager {

	private static final List<Party> parties = new ArrayList<>();

	private PartyManager() {

	}

	public static List<Party> parties() {
		return ImmutableList.copyOf(parties);
	}

	public static void addParty(Party party) {
		parties.add(party);
	}

	public static Optional<Party> findParty(@Nullable ServerPlayer member) {
		if (member == null)
			return Optional.empty();
		return parties.stream().filter(p -> p.isMember(member)).findAny();
	}

	public static void disbandParty(Party party) {
		party.members().forEach(PartyManager::resetClientInfo);
		parties.remove(party);
	}

	public static void resetClientInfo(ServerPlayer player) {
		resetClientInfo(player, true);
	}

	public static void resetClientInfoSilently(ServerPlayer player) {
		resetClientInfo(player, false);
	}

	public static void resetClientInfo(ServerPlayer player, boolean toast) {
		Voidscape.NETWORK.send(PacketDistributor.PLAYER.with(() -> player), new ClientPacketResetPartyInfo(toast));
	}

	@SubscribeEvent
	public static void tick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END)
			return;
		parties.forEach(Party::tick);
		List<Party> toRemove = parties.stream().filter(party -> party.instance != null).collect(Collectors.toList());
		toRemove.forEach(party -> party.members().forEach(PartyManager::resetClientInfoSilently));
		parties.removeAll(toRemove);
	}

}
