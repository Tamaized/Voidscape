package tamaized.voidscape.registry;

import com.mojang.authlib.GameProfile;
import tamaized.beanification.Component;

import java.util.UUID;

@Component
public class FakePlayers {

	public final GameProfile GERMINATOR = new GameProfile(UUID.fromString("4b63f35e-2aa1-4bc2-8d13-a3f32c9d8380"), "[Voidscape] Germinator");

	public final GameProfile INFUSER = new GameProfile(UUID.fromString("9c0fff22-43ee-4915-b284-0b00385189c4"), "[Voidscape] Infuser");

}
