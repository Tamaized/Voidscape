package tamaized.voidscape.registry;

import com.mojang.authlib.GameProfile;
import tamaized.beanification.Component;

import java.util.UUID;

@Component
public class FakePlayers {

	public final GameProfile FAKE_PLAYER = new GameProfile(UUID.fromString("4B63F35E-2AA1-4BC2-8D13-A3F32C9D8380"), "[Voidscape]");

}
