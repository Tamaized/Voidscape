package tamaized.voidscape.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;

public class ClientUtil {

	@Nullable
	public static PlayerEntity getClientPlayerSafely() {
		return Minecraft.getInstance().player;
	}

}
