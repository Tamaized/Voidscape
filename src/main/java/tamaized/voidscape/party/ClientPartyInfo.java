package tamaized.voidscape.party;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.Duties;
import tamaized.voidscape.world.Instance;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Voidscape.MODID, value = Dist.CLIENT)
public class ClientPartyInfo {

	private static final IToast TOAST_DISBAND = new SystemToast(SystemToast.Type.TUTORIAL_HINT, new TranslationTextComponent("Voidscape"), new TranslationTextComponent("Party Disbanded"));

	public static GameProfile host;
	public static List<GameProfile> members = new ArrayList<>();
	public static String password = "";
	public static int max;
	public static Duties.Duty duty;
	public static Instance.InstanceType type;
	public static boolean reserving;
	public static ITextComponent error;

	public static final List<Party> PARTIES = new ArrayList<>();

	public static class Party {
		public Duties.Duty duty;
		public Instance.InstanceType type;
		public boolean password;
		public int members;
		public int max;
		public GameProfile host;
		public UUID network_host;
	}

	public static void reset(boolean toast) {
		if (toast && host != null)
			Minecraft.getInstance().getToasts().addToast(TOAST_DISBAND);
		host = null;
		members.clear();
		password = "";
		max = 0;
		duty = null;
		type = null;
		reserving = false;
	}

	public static void update(UUID host, List<UUID> members, String password, int max, Duties.Duty duty, Instance.InstanceType type, boolean reserving) {
		reset(false);
		if (Minecraft.getInstance().player == null || Minecraft.getInstance().level == null)
			return;

		NetworkPlayerInfo player = Minecraft.getInstance().player.connection.getPlayerInfo(host);
		if (player == null)
			return;
		ClientPartyInfo.host = player.getProfile();
		for (UUID uuid : members) {
			player = Minecraft.getInstance().player.connection.getPlayerInfo(uuid);
			if (player == null)
				continue;
			ClientPartyInfo.members.add(player.getProfile());
		}
		ClientPartyInfo.password = password;
		ClientPartyInfo.max = max;
		ClientPartyInfo.duty = duty;
		ClientPartyInfo.type = type;
		ClientPartyInfo.reserving = reserving;
	}

	@SubscribeEvent
	public static void tick(TickEvent.RenderTickEvent event) {
		if (Minecraft.getInstance().player == null || Minecraft.getInstance().level == null)
			reset(false);
	}

}
