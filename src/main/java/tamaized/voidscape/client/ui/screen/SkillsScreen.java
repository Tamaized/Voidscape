package tamaized.voidscape.client.ui.screen;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.util.TriConsumer;
import org.lwjgl.glfw.GLFW;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ClientUtil;
import tamaized.voidscape.client.layout.HealerSkillLayout;
import tamaized.voidscape.client.layout.ISkillLayout;
import tamaized.voidscape.client.layout.MageSkillLayout;
import tamaized.voidscape.client.layout.MeleeSkillLayout;
import tamaized.voidscape.client.layout.TankSkillLayout;
import tamaized.voidscape.network.server.ServerPacketTurmoilSkillClaim;
import tamaized.voidscape.turmoil.Turmoil;
import tamaized.voidscape.turmoil.skills.TurmoilSkill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SkillsScreen extends TurmoilScreen {

	private static final List<ISkillLayout> LAYOUTS = new ArrayList<>();

	static {
		registerLayout(new TankSkillLayout());
		registerLayout(new HealerSkillLayout());
		registerLayout(new MeleeSkillLayout());
		registerLayout(new MageSkillLayout());
	}

	public static void registerLayout(ISkillLayout... layouts) {
		Collections.addAll(LAYOUTS, layouts);
	}

	private final Button.OnTooltip tooltip = (button, matrixStack, x, y) -> {
		if (button instanceof SkillButton && GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_ALT) != GLFW.GLFW_PRESS)
			this.renderTooltip(matrixStack, Objects.requireNonNull(this.minecraft).font.split(((SkillButton) button).getTooltip(), Math.max(this.width / 2 - 43, 170)), x, y);
	};
	private final List<Triple<MutableVec2i, MutableVec2i, SkillButton>> lines = new ArrayList<>();
	private int dragX;
	private int dragY;
	private int lastX;
	private int lastY;

	public SkillsScreen() {
		super(new TranslatableComponent(Voidscape.MODID.concat(".screen.skills")));
	}

	@Override
	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		setDragging(!isTalking());
		return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		Turmoil data = getData(Voidscape.subCapTurmoilData);
		if (data == null || minecraft == null || minecraft.level == null)
			return;
		if (isDragging()) {
			dragX += mouseX - lastX;
			dragY += mouseY - lastY;
		}
		renderables.stream().filter(SkillButton.class::isInstance).map(SkillButton.class::cast).forEach(button -> {
			button.x += dragX;
			button.y += dragY;
			button.active = data.canClaim(button.getSkill());
		});
		for (int offset = 0; offset < lines.size(); offset++) {
			Triple<MutableVec2i, MutableVec2i, SkillButton> line = lines.get(offset);
			line.getLeft().x += dragX;
			line.getLeft().y += dragY;
			line.getMiddle().x += dragX;
			line.getMiddle().y += dragY;
			BufferBuilder buffer = Tesselator.getInstance().getBuilder();
			buffer.begin(VertexFormat.Mode.LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
			MutableVec2i p1 = line.getLeft();
			MutableVec2i p2 = line.getMiddle();
			float theta = (float) Math.atan2(p1.y - p2.y, p1.x - p2.x);
			float cos = Mth.cos(theta);
			float sin = Mth.sin(theta);
			float dist = Mth.sqrt(Mth.square(p2.x - p1.x) + Mth.square(p2.y - p1.y));
			boolean hover = line.getRight().isHovered() || data.hasSkill(line.getRight().getSkill());
			//buffer.vertex(p1.x, p1.y, 0).color(1f, 0, 0, 1f).endVertex();
			for (float t = 0; t < dist + 1; t += 1F) {
				float y = 8F * Mth.sin((float) Math.toRadians(2F * Math.PI + offset * 31 + ClientUtil.tick)) * Mth.sin((float) Math.toRadians(t * Math.PI * 2F - ClientUtil.tick * 3F));
				float xRot = t * cos - y * sin + p2.x;
				float yRot = t * sin + y * cos + p2.y;
				buffer.vertex(xRot, yRot, 0).color(hover ? 0F : 0.4F, hover ? 1F : 0F, hover ? 0F : 1F, 1F).endVertex();
			}
			//buffer.vertex(p2.x, p2.y, 0).color(1f, 0, 0, 1f).endVertex();
			RenderSystem.lineWidth(5F);
			Tesselator.getInstance().end();
		}
		dragX = dragY = 0;
		lastX = mouseX;
		lastY = mouseY;
		super.render(stack, mouseX, mouseY, partialTicks);
		font.draw(stack, new TranslatableComponent(Voidscape.MODID + ".gui.skills.level", data.getLevel()), 5, 5, 0xFFFF00);
		font.draw(stack, new TranslatableComponent(Voidscape.MODID + ".gui.skills.points", data.getPoints()), 5, 10 + font.lineHeight, 0xFFFF00);
	}

	@Override
	protected void init() {
		super.init();
		Turmoil data = getData(Voidscape.subCapTurmoilData);
		if (minecraft == null || data == null)
			return;
		Window window = minecraft.getWindow();

		final int buttonWidth = 180;
		final int buttonHeight = 20;

		final int centerX = window.getGuiScaledWidth() / 2 - buttonHeight / 2;
		final int centerY = window.getGuiScaledHeight() / 2 - buttonHeight / 2;

		TriConsumer<TurmoilSkill, Integer, Integer> add = (skill, x, y) -> addSkill(data, skill, x, y);
		LAYOUTS.forEach(layout -> layout.fill(centerX, centerY, add));

		addRenderableWidget(new Button(

				(int) (window.getGuiScaledWidth() / 2F - buttonWidth / 2F),

				window.getGuiScaledHeight() - buttonHeight - 5,

				buttonWidth,

				buttonHeight,

				new TranslatableComponent("Back"), // FIXME: localize

				button -> minecraft.setScreen(new MainScreen())

		));

		lines.clear();
		renderables.stream().filter(SkillButton.class::isInstance).map(SkillButton.class::cast).forEach(button -> {
			List<TurmoilSkill> req = button.getSkill().getRequired();
			if (!req.isEmpty()) {
				renderables.stream().filter(SkillButton.class::isInstance).map(SkillButton.class::cast).filter(b -> req.contains(b.getSkill())).
						forEach(b -> lines.add(Triple.of(new MutableVec2i(button.x + buttonHeight / 2, button.y + buttonHeight / 2), new MutableVec2i(b.x + buttonHeight / 2, b.y + buttonHeight / 2), button)));
			}
		});
		Collections.shuffle(lines);
	}

	public void addSkill(Turmoil data, TurmoilSkill skill, int x, int y) {
		addRenderableWidget(new SkillButton(data, skill, x, y, 20));
	}

	class SkillButton extends Button {

		private final TurmoilSkill skill;
		private final Turmoil data;

		public SkillButton(Turmoil data, TurmoilSkill skill, int x, int y, int s) {
			super(x, y, s, s, new TextComponent(""), button -> {
				if (!button.active)
					return;
				data.claimSkill(skill);
				Voidscape.NETWORK.sendToServer(new ServerPacketTurmoilSkillClaim(skill.getID()));
			}, tooltip);
			this.skill = skill;
			this.data = data;
			active = !skill.disabled();
		}

		public TurmoilSkill getSkill() {
			return skill;
		}

		FormattedText getTooltip() {
			return skill.getTitle().copy().append("\n\n").append(skill.getDescription());
		}

		@Override
		public void renderButton(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
			if (minecraft == null)
				return;
			BufferBuilder buffer = Tesselator.getInstance().getBuilder();
			buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
			final float color = data.hasSkill(skill) || (isHovered() && !skill.disabled() && data.canClaim(skill)) ? 1.0F : 0.25F;
			final float activeColor = active || data.hasSkill(skill) ? color : 0.0F;
			final float alpha = 1.0F;
			ClientUtil.bindTexture(skill.getTexture());
			Matrix4f matrix = stack.last().pose();
			buffer.vertex(matrix, x, y, getBlitOffset()).color(color, activeColor, activeColor, alpha).uv(0, 0).endVertex();
			buffer.vertex(matrix, x, y + height, getBlitOffset()).color(color, activeColor, activeColor, alpha).uv(0, 1).endVertex();
			buffer.vertex(matrix, x + width, y + height, getBlitOffset()).color(color, activeColor, activeColor, alpha).uv(1, 1).endVertex();
			buffer.vertex(matrix, x + width, y, getBlitOffset()).color(color, activeColor, activeColor, alpha).uv(1, 0).endVertex();
			RenderSystem.enableDepthTest();
			Tesselator.getInstance().end();
			RenderSystem.disableDepthTest();
			if (this.isHovered())
				this.renderToolTip(stack, mouseX, mouseY);
		}
	}

	static class MutableVec2i {
		int x;
		int y;

		MutableVec2i(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

}
