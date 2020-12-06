package tamaized.voidscape.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import tamaized.voidscape.Voidscape;

public class SkillsScreen extends TurmoilScreen {

	private int dragX;
	private int dragY;
	private int lastX;
	private int lastY;

	public SkillsScreen() {
		super(new TranslationTextComponent(Voidscape.MODID.concat(".screen.skills")));
	}

	@Override
	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		setDragging(true);
		return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
	}

	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		if (isDragging()) {
			dragX += mouseX - lastX;
			dragY += mouseY - lastY;
		}
		buttons.stream().limit(buttons.size() - 1).forEach(button -> {
			button.x += dragX;
			button.y += dragY;
		});
		dragX = dragY = 0;
		lastX = mouseX;
		lastY = mouseY;
		super.render(stack, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void init() {
		super.init();
		if (minecraft == null)
			return;
		MainWindow window = minecraft.getWindow();
		final int buttonWidth = 180;
		final int buttonHeight = 20;
		addButton(new Button(

				10,

				10,

				buttonHeight,

				buttonHeight,

				new TranslationTextComponent(""),

				button -> {
				}

		));
		addButton(new Button(

				window.getGuiScaledWidth() - buttonHeight - 10,

				10,

				buttonHeight,

				buttonHeight,

				new TranslationTextComponent(""),

				button -> {
				}

		));
		addButton(new Button(

				window.getGuiScaledWidth() - buttonHeight - 10,

				window.getGuiScaledHeight() - buttonHeight - 10,

				buttonHeight,

				buttonHeight,

				new TranslationTextComponent(""),

				button -> {
				}

		));
		addButton(new Button(

				10,

				window.getGuiScaledHeight() - buttonHeight - 10,

				buttonHeight,

				buttonHeight,

				new TranslationTextComponent(""),

				button -> {
				}

		));
		// Must be last
		addButton(new Button(

				(int) (window.getGuiScaledWidth() / 2F - buttonWidth / 2F),

				window.getGuiScaledHeight() - buttonHeight - 5,

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("Back"),

				button -> minecraft.setScreen(new MainScreen())

		));
	}

}
