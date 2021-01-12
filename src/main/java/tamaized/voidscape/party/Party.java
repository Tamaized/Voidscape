package tamaized.voidscape.party;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.client.ClientPacketUpdatePartyInfo;
import tamaized.voidscape.turmoil.Duties;
import tamaized.voidscape.world.Instance;
import tamaized.voidscape.world.InstanceManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Party {

	private final Duties.Duty duty;
	private final Instance.InstanceType type;
	private final ServerPlayerEntity leader;
	private final List<ServerPlayerEntity> members = new ArrayList<>();
	private String password = "";
	private boolean reserving;
	Instance instance;

	public Party(Duties.Duty duty, Instance.InstanceType type, ServerPlayerEntity leader) {
		this.duty = duty;
		this.type = type;
		this.leader = leader;
		members.add(leader);
		sendPackets();
	}

	public static int maxMembers(Instance.InstanceType type) {
		switch (type) {
			default:
			case Unrestricted:
				return 4;
			case Normal:
				return 4;
			case Insane:
				return 8;
		}
	}

	public boolean ready() {
		return type == Instance.InstanceType.Unrestricted || (size() == maxMembers());
	}

	public void commence() {
		if (ready())
			reserving = true;
		sendPackets();
	}

	public ServerPlayerEntity host() {
		return leader;
	}

	public Duties.Duty duty() {
		return duty;
	}

	public Instance.InstanceType instanceType() {
		return type;
	}

	public boolean isReserving() {
		return reserving;
	}

	public List<ServerPlayerEntity> members() {
		return ImmutableList.copyOf(members);
	}

	public boolean addMember(ServerPlayerEntity player, String password) {
		if (!reserving && this.password.equals(password) && !full() && !members.contains(player)) {
			members.add(player);
			sendPackets();
			return true;
		}
		return false;
	}

	public void removeMember(@Nullable ServerPlayerEntity player) {
		if (player != null && player != leader && members.contains(player)) {
			members.remove(player);
			sendPackets();
			PartyManager.resetClientInfo(player);
		}
		if (player == leader || members.size() == 0)
			PartyManager.disbandParty(this);
	}

	public boolean hasPassword() {
		return !password.isEmpty();
	}

	public String password() {
		return password;
	}

	public boolean password(String test) {
		return !hasPassword() || password.equals(test);
	}

	public boolean isMember(ServerPlayerEntity player) {
		return members.contains(player);
	}

	public boolean full() {
		return members.size() == maxMembers();
	}

	public int size() {
		return members.size();
	}

	public int maxMembers() {
		return maxMembers(type);
	}

	private void sendPackets() {
		members.forEach(member -> Voidscape.NETWORK.send(PacketDistributor.PLAYER.with(() -> member), new ClientPacketUpdatePartyInfo(this, member == host())));
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void tick() {
		if (reserving && instance == null) {
			InstanceManager.findFreeInstanceByGroup(duty().group()).ifPresent(inst -> {
				instance = inst;
				instance.setType(type);
				members().forEach(member -> instance.addPlayer(member));
			});
		}
	}
}
