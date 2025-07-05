package tamaized.voidscape.client.ui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.StencilBufferUtil;
import tamaized.voidscape.client.shader.Shaders;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.registry.ModDataAttachments;

@Component
public class TurmoilOverlay {

	@Autowired
	private Shaders shaders;

	@Autowired
	private ModDataAttachments dataAttachments;

	private final int STENCIL_INDEX = 10;
	private final ResourceLocation TEXTURE_MASK = ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "textures/ui/mask.png");
	private final ResourceLocation TEXTURE_VOIDICINFUSION = ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "textures/ui/voidicinfusion.png");
	private final ResourceLocation TEXTURE_WATCHINGYOU = ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "textures/ui/watchingyou.png");

	private int tick;
	private float deltaTick;
	private float lastDeltaTick;
	private float lastTeleportTick;

	@PostConstruct
	private void setup(IEventBus modBus, IEventBus bus) {
		bus.addListener(this::tick);
		modBus.addListener(this::renderOverlay);
	}

	private void tick(ClientTickEvent.Post event) {
		if (Minecraft.getInstance().isPaused() || Minecraft.getInstance().level == null)
			return;
		lastDeltaTick = deltaTick;
		tick++;
		if (Minecraft.getInstance().player != null) {
			Insanity data = Minecraft.getInstance().player.getData(dataAttachments.INSANITY);
			if (data.getTeleportTick() >= (lastTeleportTick + 20) || (lastTeleportTick == 0 && data.getTeleportTick() > 0)) {
				Minecraft.getInstance().player.playSound(SoundEvents.CONDUIT_AMBIENT_SHORT, 4F, 1F);
				lastTeleportTick = data.getTeleportTick();
			}
			if (data.getTeleportTick() < lastTeleportTick)
				lastTeleportTick = data.getTeleportTick();
			if (data.getTeleportTick() > deltaTick)
				deltaTick++;
			else if (data.getTeleportTick() < deltaTick)
				deltaTick--;
		}
	}

	private void renderOverlay(RegisterGuiLayersEvent event) {
		event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "turmoil"), (graphics, delta) -> {
			Level world = Minecraft.getInstance().level;
			if (world != null && Minecraft.getInstance().player != null) {
				Insanity data = Minecraft.getInstance().player.getData(dataAttachments.INSANITY);
				renderParanoia(data);
				renderInfusion(data);
				float perc = Mth.clamp(Mth.lerp(delta.getGameTimeDeltaPartialTick(false), lastDeltaTick, deltaTick) / 200F, 0F, 1F);
				if (perc > 0) {
					RenderSystem.enableBlend();
					{
						Window window = Minecraft.getInstance().getWindow();

						final float x = 0F;
						final float y = 0F;
						final float w = window.getGuiScaledWidth();
						final float h = window.getGuiScaledHeight();
						final float z = 9000F; // Catch All

						RenderSystem.setShaderTexture(0, TEXTURE_MASK);
						StencilBufferUtil.setup(STENCIL_INDEX, () -> shaders.OPTIMAL_ALPHA_LESSTHAN_POS_TEX_COLOR.invokeThenUpload(perc, blit(true, 0xFFFFFFFF, x, y, z, w, h)));
						StencilBufferUtil.renderAndFlush(STENCIL_INDEX, () -> shaders.WRAPPED_POS_COLOR.invokeThenUpload(blit(false, 0xFF000000, x, y, z, w, h)));
					}
					RenderSystem.disableBlend();
				}
			}
		});
	}

	private BufferBuilder blit(boolean tex, int color, float x, float y, float z, float w, float h) {
		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, tex ? DefaultVertexFormat.POSITION_TEX_COLOR : DefaultVertexFormat.POSITION_COLOR);
		buffer.addVertex(x, y + h, z);
		if (tex)
			buffer.setUv(0F, 1F);
		buffer.setColor(color);
		buffer.addVertex(x + w, y + h, z);
		if (tex)
			buffer.setUv(1F, 1F);
		buffer.setColor(color);
		buffer.addVertex(x + w, y, z);
		if (tex)
			buffer.setUv(1F, 0F);
		buffer.setColor(color);
		buffer.addVertex(x, y, z);
		if (tex)
			buffer.setUv(0F, 0F);
		buffer.setColor(color);
		return buffer;
	}

	private void renderParanoia(Insanity insanity) {
		if (insanity.getParanoia() < 500F)
			return;
		float perc = (insanity.getParanoia() - 500F) / 90F;
		perc = Mth.clamp(perc, 0, 1);
		perc *= 0.25F;
		float endPerc = (insanity.getParanoia() - 590F) / 10F;
		endPerc = Mth.clamp(endPerc, 0, 1);
		endPerc *= 0.15F;
		perc += endPerc;
		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		RenderSystem.setShaderTexture(0, TEXTURE_WATCHINGYOU);
		final float w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		final float h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		buffer.addVertex(0, h, 0).setUv(0, 1).setColor(1F, 1F, 1F, perc);
		buffer.addVertex(w, h, 0).setUv(1, 1).setColor(1F, 1F, 1F, perc);
		buffer.addVertex(w, 0, 0).setUv(1, 0).setColor(1F, 1F, 1F, perc);
		buffer.addVertex(0, 0, 0).setUv(0, 0).setColor(1F, 1F, 1F, perc);
		RenderSystem.enableBlend();
		shaders.OPTIMAL_ALPHA_GREATERTHAN_POS_TEX_COLOR.invokeThenUpload(0F, buffer);
		RenderSystem.disableBlend();
	}

	private void renderInfusion(Insanity insanity) {
		if (insanity.getInfusion() <= 0)
			return;
		float perc = insanity.getInfusion() / 600F;
		perc = Mth.clamp(perc, 0, 1);
		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		RenderSystem.setShaderTexture(0, TEXTURE_VOIDICINFUSION);
		final float w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		final float h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		buffer.addVertex(0, h, 0).setUv(0, 1).setColor(0.4F, 0F, 1F, perc);
		buffer.addVertex(w, h, 0).setUv(1, 1).setColor(0.4F, 0F, 1F, perc);
		buffer.addVertex(w, 0, 0).setUv(1, 0).setColor(0.4F, 0F, 1F, perc);
		buffer.addVertex(0, 0, 0).setUv(0, 0).setColor(0.4F, 0F, 1F, perc);
		RenderSystem.enableBlend();
		shaders.OPTIMAL_ALPHA_GREATERTHAN_POS_TEX_COLOR.invokeThenUpload(0F, buffer);
		RenderSystem.disableBlend();
	}

}
