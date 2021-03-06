package tamaized.voidscape.network.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.turmoil.SubCapability;

import javax.annotation.Nullable;

public class ClientPacketSubCapSync implements NetworkMessages.IMessage<ClientPacketSubCapSync> {

	private SubCapability.ISubCap.ISubCapData.INetworkHandler cap;
	private int otherEntity;
	private ResourceLocation id;
	private PacketBuffer data;

	public ClientPacketSubCapSync(@Nullable SubCapability.ISubCap.ISubCapData.INetworkHandler cap, int id) {
		this(cap);
		otherEntity = id;
	}

	public ClientPacketSubCapSync(@Nullable SubCapability.ISubCap.ISubCapData.INetworkHandler cap) {
		this.cap = cap;
	}

	@Override
	public void handle(@Nullable PlayerEntity player) {
		if (player == null || player.level == null || !player.level.isClientSide()) {
			Voidscape.LOGGER.fatal("Warning, client attempted to send malicious packet! ({})", player == null ? "NULL PLAYER" : player.getDisplayName());
			return;
		}
		Entity entity = otherEntity > 0 ? player.level.getEntity(otherEntity) : player;
		if (entity == null)
			return;
		entity.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.network(id).ifPresent(data -> {
			if (data.handle(player.level.isClientSide() ? LogicalSide.CLIENT : LogicalSide.SERVER))
				data.read(this.data);
		}));
	}

	@Override
	public void toBytes(PacketBuffer packet) {
		packet.writeInt(otherEntity);
		packet.writeResourceLocation(cap.id());
		cap.write(packet);
	}

	@Override
	public ClientPacketSubCapSync fromBytes(PacketBuffer packet) {
		otherEntity = packet.readInt();
		id = packet.readResourceLocation();
		data = packet;
		return this;
	}

}
