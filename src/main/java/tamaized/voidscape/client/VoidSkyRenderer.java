package tamaized.voidscape.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.resources.ResourceLocation;
import tamaized.voidscape.client.shader.Shaders;

public class VoidSkyRenderer {

	public static void render(int ticks, float partialTicks, PoseStack matrixStack, ClientLevel world, Minecraft mc) {

		BufferBuilder vertexbuffer = Tesselator.getInstance().getBuilder();
		vertexbuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

		double diameter = 200.0D;
		double radius = diameter / 2D;

		double x = -radius;
		double y = -radius;
		double z = -radius;

		vertexbuffer.vertex(x, y, z).endVertex();
		vertexbuffer.vertex(x + diameter, y, z).endVertex();
		vertexbuffer.vertex(x + diameter, y + diameter, z).endVertex();
		vertexbuffer.vertex(x, y + diameter, z).endVertex();

		vertexbuffer.vertex(x, y + diameter, z + diameter).endVertex();
		vertexbuffer.vertex(x + diameter, y + diameter, z + diameter).endVertex();
		vertexbuffer.vertex(x + diameter, y, z + diameter).endVertex();
		vertexbuffer.vertex(x, y, z + diameter).endVertex();

		vertexbuffer.vertex(x, y + diameter, z).endVertex();
		vertexbuffer.vertex(x, y + diameter, z + diameter).endVertex();
		vertexbuffer.vertex(x, y, z + diameter).endVertex();
		vertexbuffer.vertex(x, y, z).endVertex();

		vertexbuffer.vertex(x + diameter, y, z).endVertex();
		vertexbuffer.vertex(x + diameter, y, z + diameter).endVertex();
		vertexbuffer.vertex(x + diameter, y + diameter, z + diameter).endVertex();
		vertexbuffer.vertex(x + diameter, y + diameter, z).endVertex();

		vertexbuffer.vertex(x, y + diameter, z).endVertex();
		vertexbuffer.vertex(x + diameter, y + diameter, z).endVertex();
		vertexbuffer.vertex(x + diameter, y + diameter, z + diameter).endVertex();
		vertexbuffer.vertex(x, y + diameter, z + diameter).endVertex();

		vertexbuffer.vertex(x, y, z + diameter).endVertex();
		vertexbuffer.vertex(x + diameter, y, z + diameter).endVertex();
		vertexbuffer.vertex(x + diameter, y, z).endVertex();
		vertexbuffer.vertex(x, y, z).endVertex();

		RenderSystem.setShaderTexture(0, TheEndPortalRenderer.END_SKY_LOCATION);
		RenderSystem.setShaderTexture(1, TheEndPortalRenderer.END_PORTAL_LOCATION);
		Shaders.VOIDSKY.invokeThenEndTesselator();
	}

}
