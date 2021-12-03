package tamaized.voidscape.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import tamaized.voidscape.Config;
import tamaized.voidscape.Voidscape;

public class ConfigScreen extends Screen {

	private final Screen parent;
	private EditBox input;
	private Checkbox enabled;
	private static final String TEXT_DONATOR_COLOR = "Donator Color (Hex Format AARRGGBB):";

	public ConfigScreen(Minecraft mc, Screen parent) {
		super(new TranslatableComponent(Voidscape.MODID + "_config"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();
		float sw = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		int w = 396;
		float x = (sw - w);
		x -= x / 2F;
		addRenderableWidget(new ExtendedButton((int) x, Minecraft.getInstance().getWindow().getGuiScaledHeight() - 25, w, 20, new TextComponent("Close"), button -> {
			onClose();
			Minecraft.getInstance().setScreen(parent);
		}));
		int ix = font.width(TEXT_DONATOR_COLOR) + 10;
		addRenderableWidget(input = new EditBox(font, ix, 25, Minecraft.getInstance().getWindow().getGuiScaledWidth() - ix - 15, 20, new TextComponent("")));
		input.setValue(Integer.toHexString(Config.CLIENT_CONFIG.DONATOR.color.get()));
		addRenderableWidget(enabled = new Checkbox(ix, 50, 20, 20, new TextComponent("Enabled"), Config.CLIENT_CONFIG.DONATOR.enabled.get()));
	}

	@Override
	public void onClose() {
		Config.CLIENT_CONFIG.DONATOR.enabled.set(enabled.selected());
		Config.CLIENT_CONFIG.DONATOR.enabled.save();
		Config.CLIENT_CONFIG.DONATOR.color.set((int) Long.parseLong(input.getValue(), 16));
		Config.CLIENT_CONFIG.DONATOR.color.save();
		Config.CLIENT_CONFIG.DONATOR.dirty = true;
		super.onClose();
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrixStack);
		drawCenteredString(matrixStack, font, new TextComponent("Voidscape Config"), (int) (Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2F), 5, 0xFFFFFFFF);
		int color = 0xFFFFFFFF;
		try {
			color = Integer.decode(input.getValue());
		} catch (NumberFormatException e) {
			// NO-OP
		}
		RenderSystem.enableBlend();
		drawString(matrixStack, font, TEXT_DONATOR_COLOR, 5, 25 + font.lineHeight / 2 + 1, color);
		RenderSystem.disableBlend();
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
}