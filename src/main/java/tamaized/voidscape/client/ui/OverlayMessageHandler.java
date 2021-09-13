package tamaized.voidscape.client.ui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ClientListener;
import tamaized.voidscape.client.ClientUtil;
import tamaized.voidscape.client.Shaders;
import tamaized.voidscape.client.StencilBufferUtil;
import tamaized.voidscape.client.ui.screen.TurmoilScreen;
import tamaized.voidscape.turmoil.SubCapability;
import tamaized.voidscape.turmoil.Talk;
import tamaized.voidscape.turmoil.Turmoil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

	public static void render(PoseStack stack, float partialTicks) {
		Level world = Minecraft.getInstance().level;
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
		Window window = Minecraft.getInstance().getWindow();
		float w = window.getGuiScaledWidth() - 1;
		float h = window.getGuiScaledHeight() * 0.25F;
		float x = 1;
		float y = window.getGuiScaledHeight() * 0.75F - h * 0.5F;
		float z = 402F; // Catch All

		int tick = Minecraft.getInstance().player.tickCount;
		float val = captureTick == 0 ? 1 : Mth.clamp((tick - captureTick + partialTicks) / maxTick, 0F, 1F);
		if (currentText.isEmpty())
			val = 1F - val;
		final float perc = val;
		if (perc == 0) {
			captureText = "";
			return;
		}
		if (captureText.isEmpty())
			captureTick = 0;
		if (Minecraft.getInstance().screen != null && !(Minecraft.getInstance().screen instanceof TurmoilScreen) && !Minecraft.getInstance().screen.isPauseScreen())
			Minecraft.getInstance().setScreen(null);
		RenderSystem.enableBlend();
		{
			ClientUtil.bindTexture(RenderTurmoil.TEXTURE_MASK);
			RenderTurmoil.Color24.INSTANCE.set(1F, 1F, 1F, 1F).apply(true, x, y, z, w, h);
			final int stencilIndex = 11;
			StencilBufferUtil.setup(stencilIndex, () -> Shaders.OPTIMAL_ALPHA_LESSTHAN_POS_TEX_COLOR.invokeThenEndTesselator(perc));
			ClientUtil.bindTexture(TEXTURE_TEXT_BG);
			RenderTurmoil.Color24.INSTANCE.set(0F, 0F, 0F, 0.9F).apply(false, x, y, z, w, h);
			StencilBufferUtil.renderAndFlush(stencilIndex, () -> {
				Shaders.WRAPPED_POS_COLOR.invokeThenEndTesselator();
				Font fontRenderer = Minecraft.getInstance().font;
				stack.pushPose();
				stack.translate(0, 0, z);
				fontRenderer.draw(stack,

						captureText,

						x + w * 0.5F - fontRenderer.width(captureText) * 0.5F,

						y + h * 0.5F - fontRenderer.lineHeight * 0.5F, 0x00FFAAFF);
				stack.popPose();
			});
		}
		RenderSystem.disableBlend();

	}

}
