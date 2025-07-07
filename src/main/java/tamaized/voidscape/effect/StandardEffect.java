package tamaized.voidscape.effect;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;
import net.neoforged.neoforge.common.EffectCure;
import org.joml.Matrix4f;
import tamaized.voidscape.Voidscape;

import java.util.Set;
import java.util.function.Consumer;

public class StandardEffect extends MobEffect {

	private final ResourceLocation texture;
	private final boolean allowCure;

	public StandardEffect(String texture, MobEffectCategory type, int color, boolean allowCure) {
		super(type, color);
		this.texture = ResourceLocation.fromNamespaceAndPath(Voidscape.MODID, "textures/effect/" + texture + ".png");
		this.allowCure = allowCure;
	}

	@Override
	public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
		if (allowCure)
			super.fillEffectCures(cures, effectInstance);
	}

	@SuppressWarnings("removal")
	@Override
	public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
		consumer.accept(new IClientMobEffectExtensions() {
			@Override
			public boolean renderInventoryIcon(MobEffectInstance instance, EffectRenderingInventoryScreen<?> screen, GuiGraphics guiGraphics, int x, int y, int z) {
				RenderSystem.setShaderTexture(0, texture);
				float y1 = y + 7;
				float x2 = x + 18;
				float y2 = y1 + 18;
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
				Matrix4f matrix4f = guiGraphics.pose().last().pose();
				buffer.addVertex(matrix4f, x, y2, z).setUv(0, 1);
				buffer.addVertex(matrix4f, x2, y2, z).setUv(1, 1);
				buffer.addVertex(matrix4f, x2, y1, z).setUv(1, 0);
				buffer.addVertex(matrix4f, x, y1, z).setUv(0, 0);
				BufferUploader.drawWithShader(buffer.buildOrThrow());
				return true;
			}
			@Override
			public boolean renderGuiIcon(MobEffectInstance instance, Gui gui, GuiGraphics guiGraphics, int x, int y, float z, float alpha) {
				RenderSystem.setShaderTexture(0, texture);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
				float x1 = x + 3;
				float y1 = y + 3;
				float x2 = x1 + 18;
				float y2 = y1 + 18;
				z = 0;
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
				Matrix4f matrix4f = guiGraphics.pose().last().pose();
				buffer.addVertex(matrix4f, x1, y2, z).setUv(0, 1);
				buffer.addVertex(matrix4f, x2, y2, z).setUv(1, 1);
				buffer.addVertex(matrix4f, x2, y1, z).setUv(1, 0);
				buffer.addVertex(matrix4f, x1, y1, z).setUv(0, 0);
				BufferUploader.drawWithShader(buffer.buildOrThrow());
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				return true;
			}
		});
	}
}