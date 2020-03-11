package tamaized.voidscape.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.network.server.ServerPacketTurmoilTeleport;
import tamaized.voidscape.turmoil.Turmoil;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Voidscape.MODID)
public class RenderTurmoil {

	private static final ResourceLocation TEXTURE_MASK = new ResourceLocation(Voidscape.MODID, "textures/ui/mask.png");
	private static final Color24 colorHolder = new Color24();
	private static float fade = 0F;
	private static DimensionType dimCache;

	@SubscribeEvent
	public static void render(RenderGameOverlayEvent.Pre event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
			return;
		World world = Minecraft.getInstance().world;
		if (world != null) {
			if (dimCache != null && dimCache.getId() != world.dimension.getType().getId()) {
				Turmoil.STATE = Turmoil.State.CLOSED;
				if (dimCache.getId() == Voidscape.getDimensionType().getId())
					fade = 1F;
			}
			dimCache = world.dimension.getType();
		}
		final float rate = 0.0025F;
		final float delta = 60F / Minecraft.debugFPS;
		if (!Minecraft.getInstance().isGamePaused()) {
			if (Turmoil.STATE == Turmoil.State.CLOSED) {
				if (fade > 0) {
					fade -= delta * rate * 2F;
					if (fade <= 0.1F) {
						fade = 0.1F;
						return;
					}
				}
			} else {
				if (fade < 1F) {
					fade += delta * rate;
					if (fade > 1F) {
						fade = 1F;
						if (Turmoil.STATE != Turmoil.State.TELEPORTING) {
							Turmoil.STATE = Turmoil.State.TELEPORTING;
							Voidscape.NETWORK.sendToServer(new ServerPacketTurmoilTeleport());
						}
					}
				}
			}
		}

		RenderSystem.enableBlend();
		RenderSystem.enableAlphaTest();
		{

			BufferBuilder buffer = Tessellator.getInstance().getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEXTURE);

			MainWindow window = Minecraft.getInstance().getWindow();

			float x = 0F;
			float y = 0F;
			float w = window.getScaledWidth();
			float h = window.getScaledHeight();
			float z = 0F;

			Consumer<Color24> verticies = color -> {
				final float r = Color24.asFloat(color.bit24);
				final float g = Color24.asFloat(color.bit16);
				final float b = Color24.asFloat(color.bit8);
				final float a = Color24.asFloat(color.bit0);
				buffer.vertex(x, y + h, z).color(r, g, b, a).texture(0F, 1F).endVertex();
				buffer.vertex(x + w, y + h, z).color(r, g, b, a).texture(1F, 1F).endVertex();
				buffer.vertex(x + w, y, z).color(r, g, b, a).texture(1F, 0F).endVertex();
				buffer.vertex(x, y, z).color(r, g, b, a).texture(0F, 0F).endVertex();
			};
			verticies.accept(colorHolder.set(1F, 1F, 1F, 1F));

			Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE_MASK);

			final int stencilIndex = 10;

			StencilBufferUtil.setup(stencilIndex, () -> {
				RenderSystem.alphaFunc(GL11.GL_LESS, fade);
				Tessellator.getInstance().draw();
				RenderSystem.defaultAlphaFunc();
			});

			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEXTURE);
			verticies.accept(colorHolder.set(0F, 0F, 0F, 1F));

			StencilBufferUtil.render(stencilIndex, () -> {
				RenderSystem.disableTexture();
				Tessellator.getInstance().draw();
				RenderSystem.enableTexture();
			}, true);
		}
		RenderSystem.disableAlphaTest();
		RenderSystem.disableBlend();
	}

	private static class Color24 {

		public int bit24;
		public int bit16;
		public int bit8;
		public int bit0;

		public static float asFloat(int value) {
			return value / 255F;
		}

		public static int asInt(float value) {
			return (int) (value * 255);
		}

		public int packed() {
			return (bit24 << 24) | (bit16 << 16) | (bit8 << 8) | bit0;
		}

		public Color24 unpack(int packed) {
			return set(

					(packed >> 24) & 0xFF,

					(packed >> 16) & 0xFF,

					(packed >> 8) & 0xFF,

					packed & 0xFF

			);
		}

		public Color24 set(int b24, int b16, int b8, int b0) {
			bit0 = b0;
			bit8 = b8;
			bit16 = b16;
			bit24 = b24;
			return this;
		}

		public Color24 set(float b24, float b16, float b8, float b0) {
			set(asInt(b24), asInt(b16), asInt(b8), asInt(b0));
			return this;
		}

	}

}
