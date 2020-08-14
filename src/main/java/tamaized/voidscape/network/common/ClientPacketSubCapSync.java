package tamaized.voidscape.network.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.turmoil.SubCapability;

import java.util.function.Supplier;

public class ClientPacketSubCapSync implements NetworkMessages.IMessage<ClientPacketSubCapSync> {

	private SubCapability.ISubCap.ISubCapData.INetworkHandler cap;
	private ResourceLocation id;
	private PacketBuffer data;

	public ClientPacketSubCapSync(SubCapability.ISubCap.ISubCapData.INetworkHandler cap) {
		this.cap = cap;
	}

	@Override
	public void handle(Supplier<Supplier<PlayerEntity>> sup) {
		PlayerEntity player = sup.get().get();
		if (player.world == null || !player.world.isRemote) {
			Voidscape.LOGGER.fatal("Warning, client attempted to send malicious packet! ({})", player.getDisplayName());
			return;
		}
		player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.network(id).ifPresent(data -> {
			if (data.handle(player.world.isRemote ? LogicalSide.CLIENT : LogicalSide.SERVER))
				data.read(this.data);
		}));
	}

	@Override
	public void toBytes(PacketBuffer packet) {
		packet.writeResourceLocation(cap.id());
		cap.write(packet);
	}

	@Override
	public ClientPacketSubCapSync fromBytes(PacketBuffer packet) {
		id = packet.readResourceLocation();
		data = packet;
		return this;
	}

}
