package tamaized.voidscape.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.server.ServerPacketTurmoilSkillClaim;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Turmoil;
import tamaized.voidscape.turmoil.skills.TurmoilSkill;
import tamaized.voidscape.turmoil.skills.TurmoilSkills;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class SkillsScreen extends TurmoilScreen {

	private final Button.ITooltip tooltip = (button, matrixStack, x, y) -> {
		if (button instanceof SkillButton)
			this.renderTooltip(matrixStack, Objects.requireNonNull(this.minecraft).font.split(((SkillButton) button).getTooltip(), Math.max(this.width / 2 - 43, 170)), x, y);
	};
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
		Turmoil data = getData();
		if (data == null)
			return;
		if (isDragging()) {
			dragX += mouseX - lastX;
			dragY += mouseY - lastY;
		}
		buttons.stream().filter(SkillButton.class::isInstance).map(SkillButton.class::cast).forEach(button -> {
			button.x += dragX;
			button.y += dragY;
			button.active = data.canClaim(button.getSkill());
		});
		dragX = dragY = 0;
		lastX = mouseX;
		lastY = mouseY;
		font.draw(stack, new TranslationTextComponent(Voidscape.MODID + ".gui.skills.level", data.getLevel()), 5, 5, 0xFFFF00);
		font.draw(stack, new TranslationTextComponent(Voidscape.MODID + ".gui.skills.points", data.getPoints()), 5, 10 + font.lineHeight, 0xFFFF00);
		super.render(stack, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void init() {
		super.init();
		Turmoil data = getData();
		if (minecraft == null || data == null)
			return;
		MainWindow window = minecraft.getWindow();
		final int buttonWidth = 180;
		final int buttonHeight = 20;

		addButton(new Button(

				(int) (window.getGuiScaledWidth() / 2F - buttonWidth / 2F),

				window.getGuiScaledHeight() - buttonHeight - 5,

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("Back"),

				button -> minecraft.setScreen(new MainScreen())

		));

		addSkill(data, TurmoilSkills.VOIDMANCER_SKILLS.CORE, (int) (window.getGuiScaledWidth() / 2F - buttonHeight / 2F), window.getGuiScaledHeight() - buttonHeight * 2 - 10);
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

	public void addSkill(Turmoil data, TurmoilSkill skill, int x, int y) {
		addButton(new SkillButton(data, skill, x, y, 20));
	}

	class SkillButton extends Button {

		private final TurmoilSkill skill;

		public SkillButton(Turmoil data, TurmoilSkill skill, int x, int y, int s) {
			super(x, y, s, s, new StringTextComponent(""), button -> {
				data.claimSkill(skill);
				Voidscape.NETWORK.sendToServer(new ServerPacketTurmoilSkillClaim(skill.getID()));
			}, tooltip);
			this.skill = skill;
		}

		public TurmoilSkill getSkill() {
			return skill;
		}

		ITextProperties getTooltip() {
			return skill.getTitle().copy().append("\n\n").append(skill.getDescription());
		}
	}

}
