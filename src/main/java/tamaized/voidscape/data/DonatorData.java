package tamaized.voidscape.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import tamaized.beanification.Autowired;
import tamaized.voidscape.network.DonatorHandler;
import tamaized.voidscape.network.client.ClientPacketDonatorSync;

import java.util.Optional;

public class DonatorData implements INetworkHandler, ValueIOSerializable {

	@Autowired
	private static DonatorHandler donatorHandler;

	public boolean enabled;
	public int color;

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeBoolean(enabled);
		buffer.writeInt(color);
	}

	@Override
	public void read(FriendlyByteBuf buffer) {
		enabled = buffer.readBoolean();
		color = buffer.readInt();
	}

	public void tick(Entity parent) {
		if (parent instanceof ServerPlayer && parent.tickCount % 20 == 0) {
			Optional<DonatorHandler.Settings> settings = donatorHandler.getSettings(parent.getUUID());
			if (settings.isPresent()) {
				enabled = settings.get().enabled();
				color = settings.get().color();
			} else
				enabled = false;
			sendToClients(parent);
		}
	}

	private void sendToClient(ServerPlayer parent) {
		PacketDistributor.sendToPlayer(parent, new ClientPacketDonatorSync(this));
	}

	private void sendToClients(Entity parent) {
		PacketDistributor.sendToPlayersTrackingEntityAndSelf(parent, new ClientPacketDonatorSync(this, parent));
	}

	@Override
	public void serialize(ValueOutput output) {
		output.putBoolean("enabled", enabled);
		output.putInt("color", color);
	}

	@Override
	public void deserialize(ValueInput input) {
		enabled = input.getBooleanOr("enabled", false);
		color = input.getIntOr("color", 0);
	}
}
