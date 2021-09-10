package tamaized.voidscape.client.ui.screen;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.party.ClientPartyInfo;
import tamaized.voidscape.turmoil.Duties;
import tamaized.voidscape.turmoil.Turmoil;
import tamaized.voidscape.world.Instance;

public class ConfirmDutyScreen extends TurmoilScreen {

	private final Duties.Duty duty;

	public ConfirmDutyScreen(Duties.Duty duty) {
		super(new TranslatableComponent(Voidscape.MODID.concat(".screen.confirm")));
		this.duty = duty;
	}

	@Override
	protected void init() {
		super.init();
		if (minecraft == null)
			return;
		Turmoil data = getData(Voidscape.subCapTurmoilData);
		if (data == null)
			return;
		if (data.getProgression().ordinal() < duty.progression().ordinal()) {
			onClose();
			return;
		}
		Window window = minecraft.getWindow();
		final int buttonWidth = 180;
		final int buttonHeight = 20;
		final int spacingHeight = (int) (buttonHeight * 1.5F);
		// FIXME: localization
		addRenderableWidget(new Button(

				(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),

				(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F),

				buttonWidth,

				buttonHeight,

				new TranslatableComponent("Unrestricted"),

				button -> minecraft.setScreen(new PartySearchScreen(duty, Instance.InstanceType.Unrestricted))

		));
		addRenderableWidget(new Button(

				(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),

				(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F) + spacingHeight,

				buttonWidth,

				buttonHeight,

				new TranslatableComponent("Normal"),

				button -> minecraft.setScreen(new PartySearchScreen(duty, Instance.InstanceType.Normal))

		));
		Button insane = new Button(

				(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),

				(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F) + spacingHeight * 2,

				buttonWidth,

				buttonHeight,

				new TranslatableComponent("Insane"),

				button -> minecraft.setScreen(new PartySearchScreen(duty, Instance.InstanceType.Insane))

		);
		insane.active = data.getProgression().ordinal() > duty.progression().ordinal();
		addRenderableWidget(insane);
		addRenderableWidget(new Button(

				(int) (window.getGuiScaledWidth() / 2F - buttonWidth / 2F),

				window.getGuiScaledHeight() - buttonHeight - 5,

				buttonWidth,

				buttonHeight,

				new TranslatableComponent("Back"),

				button -> minecraft.setScreen(new DutyScreen())

		));
	}

	@Override
	public void render(PoseStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		if (minecraft == null || minecraft.level == null || minecraft.player == null || ClientPartyInfo.host != null) {
			onClose();
			return;
		}
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
	}
}
