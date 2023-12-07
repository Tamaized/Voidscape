package tamaized.voidscape.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.DonatorHandler;
import tamaized.voidscape.network.client.ClientPacketDonatorSync;
import tamaized.voidscape.network.client.ClientPacketInsanitySync;

public class DonatorData implements INetworkHandler, INBTSerializable<CompoundTag> {

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
			DonatorHandler.DonatorSettings settings = DonatorHandler.settings.get(parent.getUUID());
			if (settings != null) {
				enabled = settings.enabled;
				color = settings.color;
			} else
				enabled = false;
			sendToClients(parent);
		}
	}

	private void sendToClient(ServerPlayer parent) {
		Voidscape.NETWORK.send(PacketDistributor.PLAYER.with(() -> parent), new ClientPacketDonatorSync(this));
	}

	private void sendToClients(Entity parent) {
		Voidscape.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> parent), new ClientPacketDonatorSync(this, parent));
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		nbt.putBoolean("enabled", enabled);
		nbt.putInt("color", color);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		enabled = nbt.getBoolean("enabled");
		color = nbt.getInt("color");
	}
}
