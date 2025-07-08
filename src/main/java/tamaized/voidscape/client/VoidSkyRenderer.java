package tamaized.voidscape.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.neoforged.api.distmarker.Dist;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.client.shader.Shaders;

@Component(dist = Dist.CLIENT)
public class VoidSkyRenderer {

	@Autowired(dist = Dist.CLIENT)
	private Shaders shaders;

	public void render() {

		BufferBuilder vertexbuffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

		float diameter = 200.0F;
		float radius = diameter / 2F;

		float x = -radius;
		float y = -radius;
		float z = -radius;

		vertexbuffer.addVertex(x, y, z);
		vertexbuffer.addVertex(x + diameter, y, z);
		vertexbuffer.addVertex(x + diameter, y + diameter, z);
		vertexbuffer.addVertex(x, y + diameter, z);

		vertexbuffer.addVertex(x, y + diameter, z + diameter);
		vertexbuffer.addVertex(x + diameter, y + diameter, z + diameter);
		vertexbuffer.addVertex(x + diameter, y, z + diameter);
		vertexbuffer.addVertex(x, y, z + diameter);

		vertexbuffer.addVertex(x, y + diameter, z);
		vertexbuffer.addVertex(x, y + diameter, z + diameter);
		vertexbuffer.addVertex(x, y, z + diameter);
		vertexbuffer.addVertex(x, y, z);

		vertexbuffer.addVertex(x + diameter, y, z);
		vertexbuffer.addVertex(x + diameter, y, z + diameter);
		vertexbuffer.addVertex(x + diameter, y + diameter, z + diameter);
		vertexbuffer.addVertex(x + diameter, y + diameter, z);

		vertexbuffer.addVertex(x, y + diameter, z);
		vertexbuffer.addVertex(x + diameter, y + diameter, z);
		vertexbuffer.addVertex(x + diameter, y + diameter, z + diameter);
		vertexbuffer.addVertex(x, y + diameter, z + diameter);

		vertexbuffer.addVertex(x, y, z + diameter);
		vertexbuffer.addVertex(x + diameter, y, z + diameter);
		vertexbuffer.addVertex(x + diameter, y, z);
		vertexbuffer.addVertex(x, y, z);

		RenderSystem.setShaderTexture(0, TheEndPortalRenderer.END_SKY_LOCATION);
		RenderSystem.setShaderTexture(1, TheEndPortalRenderer.END_PORTAL_LOCATION);
		shaders.VOIDSKY.invokeThenUpload(vertexbuffer);
	}

}
