package tamaized.voidscape.network.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import tamaized.voidscape.network.NetworkMessages;
import tamaized.voidscape.turmoil.Turmoil;

import java.util.function.Supplier;

public class CommonPacketTurmoilData implements NetworkMessages.IMessage<CommonPacketTurmoilData> {

	private Turmoil.ITurmoilData.ITurmoil.INetworkHandler cap;
	private ResourceLocation id;
	private PacketBuffer data;

	public CommonPacketTurmoilData(Turmoil.ITurmoilData.ITurmoil.INetworkHandler cap) {
		this.cap = cap;
	}

	@Override
	public void handle(Supplier<Supplier<PlayerEntity>> sup) {
		PlayerEntity player = sup.get().get();
		player.getCapability(Turmoil.ITurmoilData.CAPABILITY).ifPresent(cap -> cap.network(id).ifPresent(o -> {
			if (o.handle(player.world.isRemote ? LogicalSide.CLIENT : LogicalSide.SERVER))
				o.read(data);
		}));
	}

	@Override
	public void toBytes(PacketBuffer packet) {
		packet.writeResourceLocation(cap.id());
		cap.write(packet);
	}

	@Override
	public CommonPacketTurmoilData fromBytes(PacketBuffer packet) {
		id = packet.readResourceLocation();
		data = packet;
		return this;
	}

}
