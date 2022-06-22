package tamaized.voidscape.client.ui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.server.ServerPacketLeaveInstance;

public class LeaveInstanceScreen extends Screen {

	public LeaveInstanceScreen() { // FIXME: localization
		super(Component.translatable("Depart from the Duty?"));
	}

	@Override
	protected void init() {
		this.addRenderableWidget(new Button(this.width / 2 - 170, this.height / 4 + 72, 150, 20, Component.translatable("Yes"), (button1_) -> {
			if (minecraft.player != null) {
				Voidscape.NETWORK.sendToServer(new ServerPacketLeaveInstance());
			}
			this.minecraft.setScreen(null);
		}));
		this.addRenderableWidget(new Button(this.width / 2 + 20, this.height / 4 + 72, 150, 20, Component.translatable("No"), (button1_) -> {
			this.minecraft.setScreen(null);
		}));
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.fillGradient(matrixStack, 0, 0, this.width, this.height, 0xa0444444, 0xa0222222);
		matrixStack.pushPose();
		matrixStack.scale(2.0F, 2.0F, 2.0F);
		drawCenteredString(matrixStack, this.font, this.title, this.width / 2 / 2, 30, 16777215);
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
	}
}
