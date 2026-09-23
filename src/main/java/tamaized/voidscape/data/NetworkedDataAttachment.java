package tamaized.voidscape.data;

import net.minecraft.network.FriendlyByteBuf;

public abstract class NetworkedDataAttachment {

	private final String id;

	public NetworkedDataAttachment(String id) {
		this.id = id;
	}

	public final String id() {
		return id;
	}

	public abstract void write(FriendlyByteBuf buffer);

	public abstract void read(FriendlyByteBuf buffer);

}
