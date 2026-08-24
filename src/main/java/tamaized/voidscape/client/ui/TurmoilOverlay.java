package tamaized.voidscape.client.ui;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.Projection;
import net.minecraft.client.renderer.ProjectionMatrixBuffer;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ConfigureMainRenderTargetEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import org.joml.Matrix4fStack;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.shader.ShaderRenderer;
import tamaized.voidscape.client.shader.Shaders;
import tamaized.voidscape.data.Insanity;
import tamaized.voidscape.registry.ModDataAttachments;

import java.util.Map;
import java.util.function.Supplier;

@Component(dist = Dist.CLIENT)
public class TurmoilOverlay {

	@Autowired(dist = Dist.CLIENT)
	private Shaders shaders;

	@Autowired(dist = Dist.CLIENT)
	private ShaderRenderer shaderRenderer;

	@Autowired(dist = Dist.CLIENT)
	private ModDataAttachments dataAttachments;

	private final Identifier TEXTURE_MASK = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/ui/mask.png");
	private final Identifier TEXTURE_VOIDICINFUSION = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/ui/voidicinfusion.png");
	private final Identifier TEXTURE_WATCHINGYOU = Identifier.fromNamespaceAndPath(Voidscape.MODID, "textures/ui/watchingyou.png");

	private final Vector4fc noColorModulation = new Vector4f(1F, 1F, 1F, 1F);
	private final Projection guiProjection = new Projection();
	private final Supplier<ProjectionMatrixBuffer> guiProjectionBuffer = Suppliers.memoize(() -> new ProjectionMatrixBuffer("Voidscape Turmoil"));

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
		event.registerAboveAll(Identifier.fromNamespaceAndPath(Voidscape.MODID, "turmoil"), (graphics, delta) -> {
			Level world = Minecraft.getInstance().level;
			if (world != null && Minecraft.getInstance().player != null) {
				Insanity data = Minecraft.getInstance().player.getData(dataAttachments.INSANITY);
				renderParanoia(graphics, data);
				renderInfusion(graphics, data);
				float perc = Mth.clamp(Mth.lerp(delta.getGameTimeDeltaPartialTick(false), lastDeltaTick, deltaTick) / 200F, 0F, 1F);
				if (perc > 0)
					renderTeleport(graphics, perc);
			}
		});
	}

	private void renderTeleport(GuiGraphicsExtractor graphics, float perc) {
		GpuTexture depthTexture = Minecraft.getInstance().getMainRenderTarget().getDepthTexture();
		if (depthTexture == null)
			return;
		final float w = graphics.guiWidth();
		final float h = graphics.guiHeight();
		RenderSystem.getDevice().createCommandEncoder().clearStencilTexture(depthTexture, 0);
		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		buffer.addVertex(0F, h, 0F).setUv(0F, 1F).setColor(-1);
		buffer.addVertex(w, h, 0F).setUv(1F, 1F).setColor(-1);
		buffer.addVertex(w, 0F, 0F).setUv(1F, 0F).setColor(-1);
		buffer.addVertex(0F, 0F, 0F).setUv(0F, 0F).setColor(-1);
		guiProjection.setupOrtho(1000F, 11000F, w, h, true);
		RenderSystem.backupProjectionMatrix();
		RenderSystem.setProjectionMatrix(guiProjectionBuffer.get().getBuffer(guiProjection), ProjectionType.ORTHOGRAPHIC);
		Matrix4fStack modelView = RenderSystem.getModelViewStack();
		modelView.pushMatrix();
		modelView.translation(0F, 0F, -11000F);
		shaderRenderer.drawOptimalAlpha(shaders.OPTIMAL_ALPHA_LESSTHAN_POS_TEX_COLOR, buffer.buildOrThrow(), noColorModulation,
			Map.of("Sampler0", TEXTURE_MASK), perc);
		modelView.popMatrix();
		RenderSystem.restoreProjectionMatrix();
		graphics.fill(shaders.STENCIL_MASKED_GUI, 0, 0, graphics.guiWidth(), graphics.guiHeight(), 0xFF000000);
	}

	private void renderParanoia(GuiGraphicsExtractor graphics, Insanity insanity) {
		if (insanity.getParanoia() < 500F)
			return;
		float perc = (insanity.getParanoia() - 500F) / 90F;
		perc = Mth.clamp(perc, 0, 1);
		perc *= 0.25F;
		float endPerc = (insanity.getParanoia() - 590F) / 10F;
		endPerc = Mth.clamp(endPerc, 0, 1);
		endPerc *= 0.15F;
		perc += endPerc;
		blitFullscreen(graphics, TEXTURE_WATCHINGYOU, ARGB.colorFromFloat(perc, 1F, 1F, 1F));
	}

	private void renderInfusion(GuiGraphicsExtractor graphics, Insanity insanity) {
		if (insanity.getInfusion() <= 0)
			return;
		float perc = insanity.getInfusion() / 600F;
		perc = Mth.clamp(perc, 0, 1);
		blitFullscreen(graphics, TEXTURE_VOIDICINFUSION, ARGB.colorFromFloat(perc, 0.4F, 0F, 1F));
	}

	private void blitFullscreen(GuiGraphicsExtractor graphics, Identifier texture, int color) {
		int w = graphics.guiWidth();
		int h = graphics.guiHeight();
		graphics.blit(shaders.POSITION_TEX_COLOR, texture, 0, 0, 0F, 0F, w, h, w, h, color);
	}

}
