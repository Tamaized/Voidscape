package tamaized.voidscape.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;

import javax.annotation.Nullable;
import java.util.Optional;

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

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		OverlayMessageHandler.render(matrixStack, partialTicks);
	}

	@Override
	public boolean isMouseOver(double p_231047_1_, double p_231047_3_) {
		return !isTalking() && super.isMouseOver(p_231047_1_, p_231047_3_);
	}

	@Override
	public boolean mouseScrolled(double p_231043_1_, double p_231043_3_, double p_231043_5_) {
		return !isTalking() && super.mouseScrolled(p_231043_1_, p_231043_3_, p_231043_5_);
	}

	@Override
	public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
		return !isTalking() && super.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
	}

	@Override
	public boolean mouseDragged(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_, double p_231045_8_) {
		return !isTalking() && super.mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
	}

	@Override
	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		return !isTalking() && super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
	}

	@Override
	public boolean keyReleased(int p_223281_1_, int p_223281_2_, int p_223281_3_) {
		return !isTalking() && super.keyReleased(p_223281_1_, p_223281_2_, p_223281_3_);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		boolean talking = isTalking();
		if ((keyCode == 256 || ClientListener.KEY.matches(keyCode, scanCode)) && talking) {
			Turmoil data = getData();
			if (data != null)
				data.clientAction();
		}
		return !talking && super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char p_231042_1_, int p_231042_2_) {
		return !isTalking() && super.charTyped(p_231042_1_, p_231042_2_);
	}

	protected boolean isTalking() {
		Turmoil data = getData();
		return data != null && data.isTalking();
	}

	@Nullable
	protected final Turmoil getData() {
		if (minecraft == null || minecraft.player == null) {
			onClose();
			return null;
		}
		Optional<SubCapability.ISubCap> cap = minecraft.player.getCapability(SubCapability.CAPABILITY).resolve();
		if (!cap.isPresent()) {
			onClose();
			return null;
		}
		Optional<Turmoil> data = cap.get().get(Voidscape.subCapTurmoilData);
		if (!data.isPresent()) {
			onClose();
			return null;
		}
		return data.get();
	}

}
