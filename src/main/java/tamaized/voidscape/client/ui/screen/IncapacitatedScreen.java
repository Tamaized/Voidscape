package tamaized.voidscape.client.ui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.server.ServerPacketSuccumbDeath;
import tamaized.voidscape.turmoil.SubCapability;

public class IncapacitatedScreen extends Screen {
	/**
	 * The integer value containing the number of ticks that have passed since the player's death
	 */
	private int delayTicker;
	private final ITextComponent resurrectText;

	public IncapacitatedScreen() {
		super(new TranslationTextComponent("You are incapacitated"));
		resurrectText = new TranslationTextComponent("A Soul Mender may resurrect you");
	}

	@Override
	protected void init() {
		this.delayTicker = 0;
		this.addButton(new Button(this.width / 2 - 100, this.height / 4 + 72, 200, 20, new TranslationTextComponent("Succumb to death"), (button1_) -> {
			if (minecraft.player != null) {
				Voidscape.NETWORK.sendToServer(new ServerPacketSuccumbDeath());
				minecraft.player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilTracked).ifPresent(data -> data.incapacitated = false));
			}
			this.minecraft.setScreen(null);
		}));

		for (Widget widget : this.buttons) {
			widget.active = false;
		}
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.fillGradient(matrixStack, 0, 0, this.width, this.height, 1615855616, -1602211792);
		RenderSystem.pushMatrix();
		RenderSystem.scalef(2.0F, 2.0F, 2.0F);
		drawCenteredString(matrixStack, this.font, this.title, this.width / 2 / 2, 30, 16777215);
		drawCenteredString(matrixStack, this.font, this.resurrectText, this.width / 2 / 2, 30 + font.lineHeight + 2, 16777215);
		RenderSystem.popMatrix();
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		if (minecraft.player == null) {
			onClose();
			return;
		}
		minecraft.player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilTracked).ifPresent(data -> {
			if (!data.incapacitated)
				onClose();
		}));
		++this.delayTicker;
		if (this.delayTicker == 20) {
			for (Widget widget : this.buttons) {
				widget.active = true;
			}
		}

	}
}
