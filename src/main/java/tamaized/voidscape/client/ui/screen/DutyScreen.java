package tamaized.voidscape.client.ui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.party.ClientPartyInfo;
import tamaized.voidscape.turmoil.Duties;
import tamaized.voidscape.turmoil.Progression;
import tamaized.voidscape.turmoil.Turmoil;

import java.util.List;
import java.util.stream.Collectors;

public class DutyScreen extends TurmoilScreen {

	public DutyScreen() {
		super(new TranslationTextComponent(Voidscape.MODID.concat(".screen.duty")));
	}

	@Override
	protected void init() {
		super.init();
		if (minecraft == null)
			return;
		Turmoil data = getData(Voidscape.subCapTurmoilData);
		if (data == null)
			return;
		if (data.getProgression().ordinal() < Progression.CorruptPawnPre.ordinal()) {
			onClose();
			return;
		}
		MainWindow window = minecraft.getWindow();
		final int buttonWidth = 180;
		final int buttonHeight = 20;
		final int spacingHeight = (int) (buttonHeight * 1.5F);
		List<Duties.Duty> filtered = Duties.duties().stream().filter(duty -> duty.progression().ordinal() <= data.getProgression().ordinal()).collect(Collectors.toList());
		for (int index = 0; index < filtered.size(); index++) {
			Duties.Duty duty = filtered.get(index);
			addButton(new Button(

					(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),

					(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F) + spacingHeight * index,

					buttonWidth,

					buttonHeight,

					duty.display(),

					button -> minecraft.setScreen(new ConfirmDutyScreen(duty))

			));
		}
		addButton(new Button(

				(int) (window.getGuiScaledWidth() / 2F - buttonWidth / 2F),

				window.getGuiScaledHeight() - buttonHeight - 5,

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("Back"),

				button -> minecraft.setScreen(new MainScreen())

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
