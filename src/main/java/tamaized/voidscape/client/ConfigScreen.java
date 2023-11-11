package tamaized.voidscape.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import tamaized.voidscape.Config;
import tamaized.voidscape.Voidscape;

public class ConfigScreen extends Screen {

	private final Screen parent;
	private EditBox input;
	private Checkbox enabled;
	private static final String TEXT_DONATOR_COLOR = "Donator Color (Hex Format RRGGBB):";

	public ConfigScreen(Minecraft mc, Screen parent) {
		super(Component.translatable(Voidscape.MODID + "_config"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();
		float sw = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		int w = 396;
		float x = (sw - w);
		x -= x / 2F;
		addRenderableWidget(new ExtendedButton((int) x, Minecraft.getInstance().getWindow().getGuiScaledHeight() - 25, w, 20, Component.literal("Close"), button -> {
			onClose();
			Minecraft.getInstance().setScreen(parent);
		}));
		int ix = font.width(TEXT_DONATOR_COLOR) + 10;
		addRenderableWidget(input = new EditBox(font, ix, 25, Minecraft.getInstance().getWindow().getGuiScaledWidth() - ix - 15, 20, Component.literal("")));
		input.setValue(Integer.toHexString(Config.CLIENT_CONFIG.DONATOR.color.get()));
		addRenderableWidget(enabled = new Checkbox(ix, 50, 20, 20, Component.literal("Enabled"), Config.CLIENT_CONFIG.DONATOR.enabled.get()));
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
	public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
		renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
		pGuiGraphics.drawCenteredString(font, Component.literal("Voidscape Config"), (int) (Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2F), 5, 0xFFFFFFFF);
		int color = 0xFFFFFF;
		try {
			color = Integer.decode("0x" + input.getValue());
		} catch (NumberFormatException e) {
			// NO-OP
		}
		RenderSystem.enableBlend();
		pGuiGraphics.drawString(font, TEXT_DONATOR_COLOR, 5, 25 + font.lineHeight / 2 + 1, color);
		RenderSystem.disableBlend();
		super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
	}
}