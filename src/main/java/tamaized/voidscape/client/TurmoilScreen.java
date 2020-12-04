package tamaized.voidscape.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;

import java.util.Optional;

public class TurmoilScreen extends Screen {

	public TurmoilScreen() {
		super(new TranslationTextComponent(Voidscape.MODID.concat(".screen.turmoil")));
	}

	@Override
	protected void init() {
		super.init();
		if (minecraft == null)
			return;
		MainWindow window = minecraft.getWindow();
		final int buttonWidth = 50;
		final int buttonHeight = 25;
		buttons.add(new Button(

				(int) (window.getGuiScaledWidth() / 4F - buttonWidth / 2F),

				(int) (window.getGuiScaledHeight() / 4F - buttonHeight / 2F),

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("test"),

				button -> onClose()

		));
	}

	@Override
	public void onClose() {
		super.onClose();
		if (minecraft != null && minecraft.player != null)
			minecraft.player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
				data.action();
				data.setState(Turmoil.State.CLOSED);
			}));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		StencilBufferUtil.render(RenderTurmoil.STENCIL_INDEX, () -> {
			super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		});
		if (minecraft == null || minecraft.player == null) {
			onClose();
			return;
		}
		LazyOptional<SubCapability.ISubCap> o = minecraft.player.getCapability(SubCapability.CAPABILITY);
		if (!o.isPresent()) {
			onClose();
			return;
		}
		o.ifPresent(cap -> {
			Optional<Turmoil> t = cap.get(Voidscape.subCapTurmoilData);
			if (!t.isPresent()) {
				onClose();
				return;
			}
			t.ifPresent(data -> {
				if (data.getState() != Turmoil.State.OPEN)
					onClose();
			});
		});
	}
}
