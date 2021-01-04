package tamaized.voidscape.client.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ClientListener;
import tamaized.voidscape.client.StencilBufferUtil;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Talk;
import tamaized.voidscape.turmoil.Turmoil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class OverlayMessageHandler {

	static final ResourceLocation TEXTURE_TEXT_BG = new ResourceLocation(Voidscape.MODID, "textures/ui/text.png");

	private static final Map<ResourceLocation, Supplier<String>> FORMAT_MAP = new HashMap<>();
	private static String remainder = "";
	private static String currentText = "";
	private static String captureText = "";
	private static long captureTick;
	private static long maxTick = 20 * 3;

	static {
		FORMAT_MAP.put(Talk.FORMAT_KEYBIND, () -> ClientListener.KEY_TURMOIL.getTranslatedKeyMessage().getString());
	}

	public static boolean process() {
		currentText = remainder.substring(0, Minecraft.getInstance().font.getSplitter().
				plainIndexAtWidth(remainder, Minecraft.getInstance().getWindow().getGuiScaledWidth() - 2, Style.EMPTY));
		remainder = remainder.substring(currentText.length());
		if (currentText.contains("\n")) {
			String[] temp = currentText.split("\n", 2);
			currentText = temp[0];
			remainder = temp[1].concat(remainder);
		}
		if (currentText.isEmpty())
			captureTick = Objects.requireNonNull(Minecraft.getInstance().player).tickCount;
		else
			captureText = currentText;
		return currentText.isEmpty();
	}

	public static void start(Talk.Entry entry) {
		remainder = entry.getMessage().getString();
		for (Map.Entry<ResourceLocation, Supplier<String>> format : FORMAT_MAP.entrySet())
			remainder = remainder.replaceAll(Pattern.quote(Talk.format(format.getKey())), format.getValue().get());
		process();
		captureTick = Objects.requireNonNull(Minecraft.getInstance().player).tickCount;
	}

	public static void render(MatrixStack stack, float partialTicks) {
		World world = Minecraft.getInstance().level;
		Runnable reset = () -> {
			currentText = "";
			captureText = "";
			captureTick = 0;
		};
		if (world == null || Minecraft.getInstance().player == null) {
			reset.run();
			return;
		}
		Minecraft.getInstance().player.getCapability(SubCapability.CAPABILITY).ifPresent(c -> c.get(Voidscape.subCapTurmoilData).ifPresent(data -> {
			if ((!data.hasStarted() && data.getState() != Turmoil.State.CONSUME) || (!data.isTalking() && Minecraft.getInstance().player.tickCount >= captureTick + maxTick))
				reset.run();
		}));
		MainWindow window = Minecraft.getInstance().getWindow();
		float w = window.getGuiScaledWidth() - 1;
		float h = window.getGuiScaledHeight() * 0.25F;
		float x = 1;
		float y = window.getGuiScaledHeight() * 0.75F - h * 0.5F;
		float z = 402F; // Catch All

		int tick = Minecraft.getInstance().player.tickCount;
		float val = captureTick == 0 ? 1 : MathHelper.clamp((tick - captureTick + partialTicks) / maxTick, 0F, 1F);
		if (currentText.isEmpty())
			val = 1F - val;
		final float perc = val;
		if (perc == 0) {
			captureText = "";
			return;
		}
		if (captureText.isEmpty())
			captureTick = 0;
		RenderSystem.enableBlend();
		RenderSystem.enableAlphaTest();
		{

			BufferBuilder buffer = Tessellator.getInstance().getBuilder();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);

			Consumer<RenderTurmoil.Color24> verticies = color -> {
				final float r = RenderTurmoil.Color24.asFloat(color.bit24);
				final float g = RenderTurmoil.Color24.asFloat(color.bit16);
				final float b = RenderTurmoil.Color24.asFloat(color.bit8);
				final float a = RenderTurmoil.Color24.asFloat(color.bit0);
				buffer.vertex(x, y + h, z).color(r, g, b, a).uv(0F, 1F).endVertex();
				buffer.vertex(x + w, y + h, z).color(r, g, b, a).uv(1F, 1F).endVertex();
				buffer.vertex(x + w, y, z).color(r, g, b, a).uv(1F, 0F).endVertex();
				buffer.vertex(x, y, z).color(r, g, b, a).uv(0F, 0F).endVertex();
			};
			verticies.accept(RenderTurmoil.colorHolder.set(1F, 1F, 1F, 1F));

			Minecraft.getInstance().getTextureManager().bind(RenderTurmoil.TEXTURE_MASK);

			final int stencilIndex = 11;

			StencilBufferUtil.setup(stencilIndex, () -> {
				RenderSystem.alphaFunc(GL11.GL_LESS, perc);
				Tessellator.getInstance().end();
				RenderSystem.defaultAlphaFunc();
			});

			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
			verticies.accept(RenderTurmoil.colorHolder.set(0F, 0F, 0F, 0.9F));

			Minecraft.getInstance().getTextureManager().bind(TEXTURE_TEXT_BG);

			StencilBufferUtil.render(stencilIndex, () -> {
				Tessellator.getInstance().end();
				FontRenderer fontRenderer = Minecraft.getInstance().font;
				stack.pushPose();
				stack.translate(0, 0, z);
				fontRenderer.draw(stack,

						captureText,

						x + w * 0.5F - fontRenderer.width(captureText) * 0.5F,

						y + h * 0.5F - fontRenderer.lineHeight * 0.5F, 0x00FFAAFF);
				stack.popPose();

			}, true);
		}
		RenderSystem.disableAlphaTest();
		RenderSystem.disableBlend();

	}

}
