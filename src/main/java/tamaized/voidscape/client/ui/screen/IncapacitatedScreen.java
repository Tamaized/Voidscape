package tamaized.voidscape.client.ui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.server.ServerPacketSuccumbDeath;
import tamaized.voidscape.turmoil.SubCapability;

public class IncapacitatedScreen extends Screen {
	/**
	 * The integer value containing the number of ticks that have passed since the player's death
	 */
	private int delayTicker;
	private final Component resurrectText;

	public IncapacitatedScreen() { // FIXME: localization
		super(new TranslatableComponent("You are incapacitated"));
		resurrectText = new TranslatableComponent("A Soul Mender may resurrect you");
	}

	@Override
	protected void init() {
		this.delayTicker = 0;
		this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + 72, 200, 20, new TranslatableComponent("Succumb to death"), (button1_) -> {
			if (minecraft.player != null) {
				Voidscape.NETWORK.sendToServer(new ServerPacketSuccumbDeath());
				minecraft.player.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapTurmoilTracked).ifPresent(data -> data.incapacitated = false));
			}
			this.minecraft.setScreen(null);
		}));
		this.renderables.stream().filter(AbstractWidget.class::isInstance).map(AbstractWidget.class::cast).forEach(widget -> widget.active = false);
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.fillGradient(matrixStack, 0, 0, this.width, this.height, 1615855616, -1602211792);
		matrixStack.pushPose();
		matrixStack.scale(2.0F, 2.0F, 2.0F);
		drawCenteredString(matrixStack, this.font, this.title, this.width / 2 / 2, 30, 16777215);
		drawCenteredString(matrixStack, this.font, this.resurrectText, this.width / 2 / 2, 30 + font.lineHeight + 2, 16777215);
		matrixStack.popPose();
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
		if (++delayTicker == 20) {
			this.renderables.stream().filter(AbstractWidget.class::isInstance).map(AbstractWidget.class::cast).forEach(widget -> widget.active = true);
		}

	}
}
