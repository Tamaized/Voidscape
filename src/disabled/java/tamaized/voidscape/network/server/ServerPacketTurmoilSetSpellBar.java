package tamaized.voidscape.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;
import tamaized.voidscape.turmoil.abilities.TurmoilAbilityInstance;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerPacketTurmoilSetSpellBar implements NetworkMessages.IMessage<ServerPacketTurmoilSetSpellBar> {

	TurmoilAbility[] slots = new TurmoilAbility[9];

	public ServerPacketTurmoilSetSpellBar(TurmoilAbility... slots) {
		System.arraycopy(slots, 0, this.slots, 0, Math.min(slots.length, 9));
	}

	@Override
	public void handle(@Nullable Player player) {
		if (player != null)
			player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				List<TurmoilAbility> spells = new ArrayList<>();
				data.getSkills().forEach(s -> spells.addAll(s.getAbilities()));
				cap.get(Voidscape.subCapTurmoilStats).ifPresent(stats -> {
					Map<TurmoilAbility, TurmoilAbilityInstance> cache = new HashMap<>();
					for (int i = 0; i < 9; i++) {
						TurmoilAbility ability = slots[i];
						TurmoilAbilityInstance instance = null;
						if (ability != null && spells.contains(ability)) {
							if (!cache.containsKey(ability))
								cache.put(ability, instance = new TurmoilAbilityInstance(ability));
							else
								instance = cache.get(ability);
						}
						stats.setSlot(instance, i);
					}
				});
			}));
	}

	@Override
	public void toBytes(FriendlyByteBuf packet) {
		for (TurmoilAbility ability : slots)
			packet.writeVarInt(ability == null ? -1 : ability.id());
	}

	@Override
	public ServerPacketTurmoilSetSpellBar fromBytes(FriendlyByteBuf packet) {
		for (int index = 0; index < 9; index++)
			slots[index] = TurmoilAbility.getFromID(packet.readVarInt());
		return this;
	}

}
