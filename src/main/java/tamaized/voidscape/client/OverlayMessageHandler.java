package tamaized.voidscape.client;

import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tamaized.voidscape.Voidscape;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Voidscape.MODID, value = Dist.CLIENT)
public class OverlayMessageHandler {

	private static final List<Message> QUEUE = new ArrayList<>();

	@SubscribeEvent
	public static void render(RenderGameOverlayEvent.Pre event) {
		/*if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
			return;
		World world = Minecraft.getInstance().world;
		if (world == null) {
			QUEUE.clear();
			return;
		}
		if (QUEUE.isEmpty())
			return;
		Message message = QUEUE.get(0);
		if (message.timeSnapshot == 0)
			message.snapshot(world.getGameTime());
		if (message.isDone(world.getGameTime()))
			QUEUE.remove(0);
		float frameTick = world.getGameTime() + event.getPartialTicks();
		float start = Math.min(((frameTick - message.timeSnapshot) / 20F), 1F);
		float end = MathHelper.clamp((message.timeSnapshot + message.duration - frameTick) / 20F, 0F, 1F);
		float interp = Math.min(start, end);

		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEXTURE);

		MainWindow window = Minecraft.getInstance().getWindow();

		float w = window.getScaledWidth() * 0.5F;
		float h = window.getScaledHeight() * 0.1F;
		float x = window.getScaledWidth() * 0.5F - w * 0.5F;
		float y = window.getScaledHeight() * 0.75F - h * 0.5F;
		float z = 0F;

		float r = 0F;
		float g = 0F;
		float b = 0F;
		float a = interp * 0.75F;

		buffer.vertex(x, y + h, z).color(r, g, b, a).texture(0F, 1F).endVertex();
		buffer.vertex(x + w, y + h, z).color(r, g, b, a).texture(1F, 1F).endVertex();
		buffer.vertex(x + w, y, z).color(r, g, b, a).texture(1F, 0F).endVertex();
		buffer.vertex(x, y, z).color(r, g, b, a).texture(0F, 0F).endVertex();

		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		Tessellator.getInstance().draw();
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();

		FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
		int alpha = (int) (a * 0xFF);
		if (alpha > 0x15)
			fontRenderer.drawString(message.text.getFormattedText(), x + w * 0.5F - fontRenderer.sizeStringToWidth(message.text.getFormattedText(), (int) w - 1) * 0.5F, y + h * 0.5F - fontRenderer.FONT_HEIGHT * 0.5F, 0x00FFAAFF | ((alpha > 0x40 ? alpha : 0x41) << 24));

	*/
	}


	public static void queueMessage(ITextComponent text) {
		queueMessage(text, 20 * 5);
	}

	public static void queueMessage(ITextComponent text, long duration) {
		QUEUE.add(new Message(text, duration));
	}

	private static class Message {

		private final ITextComponent text;
		private final long duration;
		private long timeSnapshot;

		private Message(ITextComponent text, long duration) {
			this.text = text;
			this.duration = duration;
		}

		private Message snapshot(long time) {
			timeSnapshot = time;
			return this;
		}

		private boolean isDone(long time) {
			return time > timeSnapshot + duration;
		}

	}

}
