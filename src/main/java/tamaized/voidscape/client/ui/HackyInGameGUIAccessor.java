package tamaized.voidscape.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.util.ResourceLocation;

public class HackyInGameGUIAccessor extends IngameGui {
	private HackyInGameGUIAccessor(Minecraft p_i46325_1_) {
		super(p_i46325_1_);
	}

	public static ResourceLocation WIDGETS_LOCATION() {
		return WIDGETS_LOCATION;
	}
}
