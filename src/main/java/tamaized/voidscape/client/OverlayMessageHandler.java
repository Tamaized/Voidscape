package tamaized.voidscape.client;

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
import tamaized.voidscape.turmoil.Talk;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class OverlayMessageHandler {

	public static final ResourceLocation FORMAT_KEYBIND = new ResourceLocation(Voidscape.MODID, "keybind");
	private static final Map<ResourceLocation, Supplier<String>> FORMAT_MAP = new HashMap<>();
	private static String remainder = "";
	private static String currentText = "";
	private static String captureText = "";
	private static long captureTick;
	private static long maxTick = 20 * 3;

	static {
		FORMAT_MAP.put(FORMAT_KEYBIND, () -> ClientListener.KEY.func_238171_j_().getString());
	}

	public static String format(ResourceLocation key) {
		return "($".concat(key.toString()).concat(")");
	}

	public static boolean process() {
		currentText = remainder.substring(0, Minecraft.getInstance().fontRenderer.func_238420_b_().
				func_238352_a_(remainder, Minecraft.getInstance().getMainWindow().getScaledWidth() - 2, Style.EMPTY));
		remainder = remainder.substring(currentText.length());
		if (currentText.contains("\n")) {
			String[] temp = currentText.split("\n", 2);
			currentText = temp[0];
			remainder = temp[1].concat(remainder);
		}
		if (currentText.isEmpty())
			captureTick = Objects.requireNonNull(Minecraft.getInstance().player).ticksExisted;
		else
			captureText = currentText;
		return currentText.isEmpty();
	}

	public static void start(Talk.Entry entry) {
		remainder = entry.getMessage().getString();
		for (Map.Entry<ResourceLocation, Supplier<String>> format : FORMAT_MAP.entrySet())
			remainder = remainder.replaceAll(Pattern.quote(format(format.getKey())), format.getValue().get());
		process();
		captureTick = Objects.requireNonNull(Minecraft.getInstance().player).ticksExisted;
	}

	public static void render(MatrixStack stack, float partialTicks) {
		World world = Minecraft.getInstance().world;
		if (world == null || Minecraft.getInstance().player == null) {
			currentText = "";
			captureText = "";
			captureTick = 0;
			return;
		}

		MainWindow window = Minecraft.getInstance().getMainWindow();
		float w = window.getScaledWidth() - 1;
		float h = window.getScaledHeight() * 0.1F;
		float x = 1;
		float y = window.getScaledHeight() * 0.75F - h * 0.5F;
		float z = 0F;

		int tick = Minecraft.getInstance().player.ticksExisted;
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

			BufferBuilder buffer = Tessellator.getInstance().getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);

			Consumer<RenderTurmoil.Color24> verticies = color -> {
				final float r = RenderTurmoil.Color24.asFloat(color.bit24);
				final float g = RenderTurmoil.Color24.asFloat(color.bit16);
				final float b = RenderTurmoil.Color24.asFloat(color.bit8);
				final float a = RenderTurmoil.Color24.asFloat(color.bit0);
				buffer.pos(x, y + h, z).color(r, g, b, a).tex(0F, 1F).endVertex();
				buffer.pos(x + w, y + h, z).color(r, g, b, a).tex(1F, 1F).endVertex();
				buffer.pos(x + w, y, z).color(r, g, b, a).tex(1F, 0F).endVertex();
				buffer.pos(x, y, z).color(r, g, b, a).tex(0F, 0F).endVertex();
			};
			verticies.accept(RenderTurmoil.colorHolder.set(1F, 1F, 1F, 1F));

			Minecraft.getInstance().getTextureManager().bindTexture(RenderTurmoil.TEXTURE_MASK);

			final int stencilIndex = 11;

			StencilBufferUtil.setup(stencilIndex, () -> {
				RenderSystem.alphaFunc(GL11.GL_LESS, perc);
				Tessellator.getInstance().draw();
				RenderSystem.defaultAlphaFunc();
			});

			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
			verticies.accept(RenderTurmoil.colorHolder.set(0F, 0F, 0F, 1F));

			StencilBufferUtil.render(stencilIndex, () -> {
				RenderSystem.disableTexture();
				Tessellator.getInstance().draw();
				RenderSystem.enableTexture();
				FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
				fontRenderer.drawString(stack,

						captureText,

						x + w * 0.5F - fontRenderer.getStringWidth(captureText) * 0.5F,

						y + h * 0.5F - fontRenderer.FONT_HEIGHT * 0.5F, 0x00FFAAFF);

			}, true);
		}
		RenderSystem.disableAlphaTest();
		RenderSystem.disableBlend();

	}

}
