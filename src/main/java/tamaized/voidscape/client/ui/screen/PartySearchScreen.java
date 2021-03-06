package tamaized.voidscape.client.ui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.server.ServerPacketCreateParty;
import tamaized.voidscape.party.ClientPartyInfo;
import tamaized.voidscape.party.Party;
import tamaized.voidscape.turmoil.Duties;
import tamaized.voidscape.world.Instance;

import java.util.ArrayList;

public class PartySearchScreen extends TurmoilScreen {

	private final Duties.Duty duty;
	private final Instance.InstanceType type;

	private long tick;

	public PartySearchScreen(Duties.Duty duty, Instance.InstanceType type) {
		super(new TranslationTextComponent(Voidscape.MODID.concat(".screen.form")));
		this.duty = duty;
		this.type = type;
	}

	@Override
	protected void init() {
		super.init();
		if (minecraft == null)
			return;

		tick = minecraft.level == null ? 0 : minecraft.level.getGameTime();
		MainWindow window = minecraft.getWindow();
		final int buttonWidth = 180;
		final int buttonHeight = 20;
		final int spacingHeight = (int) (buttonHeight * 1.5F);
		addButton(new Button(

				(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),

				(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F),

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("Find Party"),

				button -> minecraft.setScreen(new PartyListScreen(duty, type))

		));
		addButton(new Button(

				(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),

				(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F) + spacingHeight,

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("Start Party"),

				button -> {
					if (minecraft.player == null)
						return;
					Voidscape.NETWORK.sendToServer(new ServerPacketCreateParty(duty, type));
					ClientPartyInfo.update(minecraft.player.getUUID(), new ArrayList<>(), "", Party.maxMembers(type), duty, type, false);
					minecraft.setScreen(new FormPartyScreen(duty, type));
				}

		));
		addButton(new Button(

				(int) (window.getGuiScaledWidth() / 2F - buttonWidth / 2F),

				window.getGuiScaledHeight() - buttonHeight - 5,

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("Back"),

				button -> minecraft.setScreen(new ConfirmDutyScreen(duty))

		));
	}

	@Override
	public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		if (minecraft == null || minecraft.level == null || minecraft.player == null || ClientPartyInfo.host != null) {
			onClose();
			return;
		}
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
	}

}
