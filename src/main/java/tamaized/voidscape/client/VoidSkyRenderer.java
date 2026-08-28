package tamaized.voidscape.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.CustomSkyboxRenderer;
import net.neoforged.neoforge.client.event.RegisterCustomEnvironmentEffectRendererEvent;
import org.joml.Matrix4fc;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import tamaized.voidscape.client.shader.ShaderRenderer;
import tamaized.voidscape.client.shader.Shaders;
import tamaized.voidscape.registry.ModDimensions;

import java.util.Map;

@Component(dist = Dist.CLIENT)
public class VoidSkyRenderer implements CustomSkyboxRenderer {

	@Autowired(dist = Dist.CLIENT)
	private Shaders shaders;

	@Autowired(dist = Dist.CLIENT)
	private ShaderRenderer shaderRenderer;

	@Autowired(dist = Dist.CLIENT)
	private ModDimensions dimensions;

	@PostConstruct
	private void init(IEventBus bus) {
		bus.addListener(RegisterCustomEnvironmentEffectRendererEvent.class, event -> event.registerSkyboxRenderer(dimensions.VOID.identifier(), this));
	}

	private void render() {

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
			"Sampler1", AbstractEndPortalRenderer.END_PORTAL_LOCATION
		));
	}

	@Override
	public boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Matrix4fc modelViewMatrix, Runnable setupFog) {
		render();
		return true;
	}

}
