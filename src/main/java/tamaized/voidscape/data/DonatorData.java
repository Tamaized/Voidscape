package tamaized.voidscape.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.UnknownNullability;
import tamaized.beanification.Autowired;
import tamaized.voidscape.network.DonatorHandler;
import tamaized.voidscape.network.client.ClientPacketDonatorSync;

import java.util.Optional;

public class DonatorData implements INetworkHandler, INBTSerializable<CompoundTag> {

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
	public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
		CompoundTag nbt = new CompoundTag();
		nbt.putBoolean("enabled", enabled);
		nbt.putInt("color", color);
		return nbt;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
		enabled = nbt.getBoolean("enabled");
		color = nbt.getInt("color");
	}
}
