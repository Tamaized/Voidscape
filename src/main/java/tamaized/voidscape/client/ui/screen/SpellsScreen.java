package tamaized.voidscape.client.ui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ui.RenderTurmoil;
import tamaized.voidscape.network.server.ServerPacketTurmoilSetSpellBar;
import tamaized.voidscape.turmoil.Turmoil;
import tamaized.voidscape.turmoil.TurmoilStats;
import tamaized.voidscape.turmoil.abilities.TurmoilAbility;
import tamaized.voidscape.turmoil.abilities.TurmoilAbilityInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpellsScreen extends TurmoilScreen {

	private final Button.ITooltip tooltip = (button, matrixStack, x, y) -> {
		if (button instanceof SpellButton && GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_ALT) != GLFW.GLFW_PRESS)
			this.renderTooltip(matrixStack, Objects.requireNonNull(this.minecraft).font.split(((SpellButton) button).getTooltip(), Math.max(this.width / 2 - 43, 170)), x, y);
	};

	private TurmoilAbility drag = null;

	public SpellsScreen() {
		super(new TranslationTextComponent(Voidscape.MODID.concat(".screen.spells")));
	}

	@Override
	public void onClose() {
		TurmoilAbility[] slots = new TurmoilAbility[9];
		buttons.stream().filter(SlotButton.class::isInstance).map(SlotButton.class::cast).forEach(slot -> slots[slot.id] = slot.set);
		Voidscape.NETWORK.sendToServer(new ServerPacketTurmoilSetSpellBar(slots));
		super.onClose();
		if (minecraft != null)
			minecraft.setScreen(new MainScreen());
	}

	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		Turmoil data = getData(Voidscape.subCapTurmoilData);
		if (data == null || minecraft == null || minecraft.level == null)
			return;
		RenderTurmoil.renderSpellBar(stack, 990, partialTicks);
		super.render(stack, mouseX, mouseY, partialTicks);
		if (drag != null) {
			int size = 20;
			int x = mouseX - size / 2;
			int y = mouseY - size / 2;
			BufferBuilder buffer = Tessellator.getInstance().getBuilder();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			buffer.vertex(x, y, 0F).uv(0, 0).endVertex();
			buffer.vertex(x, y + size, 0F).uv(0, 1).endVertex();
			buffer.vertex(x + size, y + size, 0F).uv(1, 1).endVertex();
			buffer.vertex(x + size, y, 0F).uv(1, 0).endVertex();
			minecraft.getTextureManager().bind(drag.getTexture());
			Tessellator.getInstance().end();
		}
	}

	@Override
	public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
		boolean flag = super.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
		drag = null;
		return flag;
	}

	@Override
	protected void init() {
		super.init();
		Turmoil data = getData(Voidscape.subCapTurmoilData);
		if (minecraft == null || data == null)
			return;
		MainWindow window = minecraft.getWindow();

		final int buttonWidth = 180;
		final int buttonHeight = 20;
		final int spacing = buttonHeight + 5;
		final int grid = (window.getGuiScaledWidth() - spacing * 4) / spacing;

		List<TurmoilAbility> spells = new ArrayList<>();
		data.getSkills().forEach(s -> spells.addAll(s.getAbilities()));
		for (int index = 0; index < spells.size(); index++)
			addSpell(spells.get(index), 5 + spacing * (index % grid), 5 + spacing * (index / grid));

		TurmoilStats stats = getData(Voidscape.subCapTurmoilStats);

		for (int i = 0; i < 9; i++) {
			SlotButton slot = new SlotButton(i, window.getGuiScaledWidth() - 60 + 20 * (i % 3), window.getGuiScaledHeight() / 2 - 28 + 20 * (i / 3));
			if (stats != null) {
				TurmoilAbilityInstance inst = stats.getAbility(i);
				if (inst != null)
					slot.set = inst.ability();
			}
			addButton(slot);
		}

		addButton(new Button(

				(int) (window.getGuiScaledWidth() / 2F - buttonWidth / 2F),

				window.getGuiScaledHeight() - buttonHeight - 5,

				buttonWidth,

				buttonHeight,

				new TranslationTextComponent("Back"),

				button -> onClose()

		));
	}

	public void addSpell(TurmoilAbility ability, int x, int y) {
		addButton(new SpellButton(this, ability, x, y, 20));
	}

	class SpellButton extends Button {

		private final TurmoilAbility ability;

		SpellButton(SpellsScreen parent, TurmoilAbility ability, int x, int y, int s) {
			super(x, y, s, s, new StringTextComponent(""), button -> {
				if (!button.active)
					return;
				parent.drag = ability;
			}, tooltip);
			this.ability = ability;
		}

		public TurmoilAbility getAbility() {
			return ability;
		}

		ITextProperties getTooltip() {
			return ability.getTitle().copy().append("\n\n").append(ability.getDescription());
		}

		@Override
		public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
			if (minecraft == null)
				return;
			BufferBuilder buffer = Tessellator.getInstance().getBuilder();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			minecraft.getTextureManager().bind(ability.getTexture());
			Matrix4f matrix = stack.last().pose();
			buffer.vertex(matrix, x, y, getBlitOffset()).uv(0, 0).endVertex();
			buffer.vertex(matrix, x, y + height, getBlitOffset()).uv(0, 1).endVertex();
			buffer.vertex(matrix, x + width, y + height, getBlitOffset()).uv(1, 1).endVertex();
			buffer.vertex(matrix, x + width, y, getBlitOffset()).uv(1, 0).endVertex();
			RenderSystem.enableDepthTest();
			Tessellator.getInstance().end();
			RenderSystem.disableDepthTest();
			if (this.isHovered())
				this.renderToolTip(stack, mouseX, mouseY);
		}
	}

	class SlotButton extends Button {

		private final int id;
		private TurmoilAbility set;

		SlotButton(int id, int x, int y) {
			super(x, y, 16, 16, new StringTextComponent(""), button -> {
			});
			this.id = id;
		}

		@Override
		public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
			if (minecraft == null || set == null)
				return;
			BufferBuilder buffer = Tessellator.getInstance().getBuilder();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			minecraft.getTextureManager().bind(set.getTexture());
			Matrix4f matrix = stack.last().pose();
			buffer.vertex(matrix, x, y, getBlitOffset()).uv(0, 0).endVertex();
			buffer.vertex(matrix, x, y + height, getBlitOffset()).uv(0, 1).endVertex();
			buffer.vertex(matrix, x + width, y + height, getBlitOffset()).uv(1, 1).endVertex();
			buffer.vertex(matrix, x + width, y, getBlitOffset()).uv(1, 0).endVertex();
			Tessellator.getInstance().end();
		}

		@Override
		public void onRelease(double p_231000_1_, double p_231000_3_) {
			if (drag != null)
				set = drag;
			else
				set = null;
		}
	}

}
