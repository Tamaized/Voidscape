package tamaized.voidscape.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;

public class DetatchedFriendlyByteBuf extends FriendlyByteBuf {

	public DetatchedFriendlyByteBuf(ByteBuf buf) {
		super(buf.copy());
		buf.clear();
	}

}
