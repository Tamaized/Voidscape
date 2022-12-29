package tamaized.voidscape.client.ui.screen;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.server.ServerPacketCreateParty;
import tamaized.voidscape.party.ClientPartyInfo;
import tamaized.voidscape.party.Party;
import tamaized.voidscape.turmoil.Duties;
import tamaized.voidscape.world.Instance;

import java.util.ArrayList;

public class PartySearchScreen extends TurmoilScreen {

	private final Duties.Duty duty;

	private long tick;

	public PartySearchScreen(Duties.Duty duty) {
		super(Component.translatable(Voidscape.MODID.concat(".screen.form")));
		this.duty = duty;
	}

	@Override
	protected void init() {
		super.init();
		if (minecraft == null)
			return;

		tick = minecraft.level == null ? 0 : minecraft.level.getGameTime();
		Window window = minecraft.getWindow();
		final int buttonWidth = 180;
		final int buttonHeight = 20;
		final int spacingHeight = (int) (buttonHeight * 1.5F);
		addRenderableWidget(Button.builder(
				Component.translatable("Find Party"), // FIXME: localize
				button -> minecraft.setScreen(new PartyListScreen(duty))
		).bounds(
				(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),
				(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F),
				buttonWidth,
				buttonHeight
		).build());
		addRenderableWidget(Button.builder(
				Component.translatable("Form Party"),
				button -> {
					if (minecraft.player == null)
						return;
					Voidscape.NETWORK.sendToServer(new ServerPacketCreateParty(duty));
					ClientPartyInfo.update(minecraft.player.getUUID(), new ArrayList<>(), "", duty, false);
					minecraft.setScreen(new FormPartyScreen(duty));
				}
		).bounds(
				(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),
				(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F) + spacingHeight,
				buttonWidth,
				buttonHeight
		).build());
		addRenderableWidget(Button.builder(
				Component.translatable("Back"),
				button -> minecraft.setScreen(new DutyScreen())
		).bounds(
				(int) (window.getGuiScaledWidth() / 2F - buttonWidth / 2F),
				window.getGuiScaledHeight() - buttonHeight - 5,
				buttonWidth,
				buttonHeight
		).build());
	}

	@Override
	public void render(PoseStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		if (minecraft == null || minecraft.level == null || minecraft.player == null) {
			onClose();
			return;
		}
		if (ClientPartyInfo.host != null) {
			minecraft.setScreen(new FormPartyScreen(ClientPartyInfo.duty));
			return;
		}
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
	}

}
