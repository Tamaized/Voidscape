package tamaized.voidscape.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.neoforged.api.distmarker.Dist;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.client.shader.ShaderRenderer;
import tamaized.voidscape.client.shader.Shaders;

import java.util.Map;

@Component(dist = Dist.CLIENT)
public class VoidSkyRenderer {

	@Autowired(dist = Dist.CLIENT)
	private Shaders shaders;

	@Autowired(dist = Dist.CLIENT)
	private ShaderRenderer shaderRenderer;

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

		shaderRenderer.draw(shaders.VOIDSKY, vertexbuffer.buildOrThrow(), Map.of(
			"Sampler0", AbstractEndPortalRenderer.END_SKY_LOCATION,
			"Sampler1", AbstractEndPortalRenderer.END_PORTAL_LOCATION
		));
	}

}
