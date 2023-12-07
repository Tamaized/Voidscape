package tamaized.voidscape.data;

import net.minecraft.network.FriendlyByteBuf;

public interface INetworkHandler {

	void write(FriendlyByteBuf buffer);

	void read(FriendlyByteBuf buffer);

}
