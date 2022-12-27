package tamaized.voidscape.client.ui.screen;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.party.ClientPartyInfo;
import tamaized.voidscape.turmoil.Duties;
import tamaized.voidscape.turmoil.Turmoil;
import tamaized.voidscape.world.Instance;

public class ConfirmDutyScreen extends TurmoilScreen {

	private final Duties.Duty duty;

	public ConfirmDutyScreen(Duties.Duty duty) {
		super(Component.translatable(Voidscape.MODID.concat(".screen.confirm")));
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
		addRenderableWidget(Button.builder(
				Component.translatable(duty.isOnlyNormal() ? "Normal" : "Unrestricted"),
				button -> minecraft.setScreen(new PartySearchScreen(duty, Instance.InstanceType.Unrestricted))
		).bounds(
				(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),
				(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F),
				buttonWidth,
				buttonHeight
		).build());
		if (!duty.isOnlyNormal()) {
			addRenderableWidget(Button.builder(
							Component.translatable("Normal"),
							button -> minecraft.setScreen(new PartySearchScreen(duty, Instance.InstanceType.Normal))
					).bounds(
							(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),
							(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F) + spacingHeight,
							buttonWidth,
							buttonHeight
					).build()
			);
			Button insane = Button.builder(
					Component.translatable("Insane"),
					button -> minecraft.setScreen(new PartySearchScreen(duty, Instance.InstanceType.Insane))
			).bounds(
					(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),
					(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F) + spacingHeight * 2,
					buttonWidth,
					buttonHeight
			).build();
			insane.active = data.getProgression().ordinal() > duty.progression().ordinal();
			addRenderableWidget(insane);
		}
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
		if (minecraft == null || minecraft.level == null || minecraft.player == null || ClientPartyInfo.host != null) {
			onClose();
			return;
		}
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
	}
}
