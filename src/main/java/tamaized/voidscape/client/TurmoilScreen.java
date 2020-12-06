package tamaized.voidscape.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;

public class TurmoilScreen extends Screen {

	public TurmoilScreen(ITextComponent p_i51108_1_) {
		super(p_i51108_1_);
	}

	@Override
	public void onClose() {
		super.onClose();
		if (minecraft != null && minecraft.player != null)
			minecraft.player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				data.clientAction();
				data.setState(Turmoil.State.CLOSED);
			}));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

}
