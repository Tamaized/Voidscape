package tamaized.voidscape.client.shader;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.MeshData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DynamicUniformStorage;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Supplier;

@Component(dist = Dist.CLIENT)
public class ShaderRenderer {

	@Autowired
	private Shaders shaders;

	private final Vector4fc noColorModulation = new Vector4f(1F, 1F, 1F, 1F);

	private final Supplier<DynamicUniformStorage<AlphaUniform>> alphaStorage = Suppliers.memoize(() ->
		new DynamicUniformStorage<>("Voidscape Alpha UBO", AlphaUniform.SIZE, 2));

	private final Supplier<DynamicUniformStorage<AuroraUniform>> auroraStorage = Suppliers.memoize(() ->
		new DynamicUniformStorage<>("Voidscape Aurora UBO", AuroraUniform.SIZE, 2));

	@PostConstruct(PostConstruct.Bus.GAME)
	private void init(IEventBus gameBus) {
		gameBus.addListener(RenderFrameEvent.Post.class, _ -> {
			alphaStorage.get().endFrame();
			auroraStorage.get().endFrame();
		});
	}

	public void draw(RenderPipeline pipeline, MeshData mesh) {
		draw(pipeline, mesh, noColorModulation, Map.of(), null, null);
	}

	public void draw(RenderPipeline pipeline, MeshData mesh, Map<String, Identifier> textures) {
		draw(pipeline, mesh, noColorModulation, textures, null, null);
	}

	public void draw(RenderPipeline pipeline, MeshData mesh, Vector4fc colorModulator) {
		draw(pipeline, mesh, colorModulator, Map.of(), null, null);
	}

	public void drawOptimalAlpha(RenderPipeline pipeline, MeshData mesh, Vector4fc colorModulator, float alpha) {
		draw(pipeline, mesh, colorModulator, Map.of(), shaders.ALPHA_UNIFORM, alphaStorage.get().writeUniform(new AlphaUniform(alpha)));
	}

	public void drawAurora(MeshData mesh, Vector4fc colorModulator, int seed, float x, float y, float z) {
		draw(shaders.THUNDER_AURORA, mesh, colorModulator, Map.of(), shaders.AURORA_UNIFORM,
			auroraStorage.get().writeUniform(new AuroraUniform(seed, x, y, z)));
	}

	private void draw(RenderPipeline pipeline, MeshData mesh, Vector4fc colorModulator, Map<String, Identifier> textures, @Nullable String uniformName, @Nullable GpuBufferSlice uniform) {
		GpuDevice device = RenderSystem.getDevice();
		GpuBufferSlice transforms = RenderSystem.getDynamicUniforms().
			writeTransform(RenderSystem.getModelViewMatrix(), colorModulator, new Vector3f(), new Matrix4f());
		try (mesh) {
			MeshData.DrawState drawState = mesh.drawState();
			ByteBuffer indices = mesh.indexBuffer();
			GpuTextureView colorTextureView = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
			if (colorTextureView == null)
				return;
			try (GpuBuffer ownedIndexBuffer = indices == null ? null : device.createBuffer(() -> "Voidscape shader indices", GpuBuffer.USAGE_INDEX, indices);
				 GpuBuffer vertexBuffer = device.createBuffer(() -> "Voidscape shader vertices", GpuBuffer.USAGE_VERTEX, mesh.vertexBuffer());
				 RenderPass pass = device.createCommandEncoder().createRenderPass(
					 () -> pipeline.getLocation().toString(),
					 colorTextureView,
					 OptionalInt.empty(),
					 pipeline.wantsDepthTexture() ? Minecraft.getInstance().getMainRenderTarget().getDepthTextureView() : null,
					 OptionalDouble.empty())
			) {
				pass.setPipeline(pipeline);
				TextureManager textureManager = Minecraft.getInstance().getTextureManager();
				textures.forEach((name, location) -> {
					AbstractTexture texture = textureManager.getTexture(location);
					pass.bindTexture(name, texture.getTextureView(), texture.getSampler());
				});
				RenderSystem.bindDefaultUniforms(pass);
				pass.setUniform("DynamicTransforms", transforms);
				if (uniformName != null && uniform != null)
					pass.setUniform(uniformName, uniform);
				pass.setVertexBuffer(0, vertexBuffer);
				if (ownedIndexBuffer == null) {
					RenderSystem.AutoStorageIndexBuffer sequential = RenderSystem.getSequentialBuffer(drawState.mode());
					pass.setIndexBuffer(sequential.getBuffer(drawState.indexCount()), sequential.type());
				} else {
					pass.setIndexBuffer(ownedIndexBuffer, drawState.indexType());
				}
				pass.drawIndexed(0, 0, drawState.indexCount(), 1);
			}
		}
	}

	private record AlphaUniform(float alpha) implements DynamicUniformStorage.DynamicUniform {

		private static final int SIZE = new Std140SizeCalculator().putFloat().get();

		@Override
		public void write(ByteBuffer buffer) {
			Std140Builder.intoBuffer(buffer).putFloat(alpha);
		}

	}

	private record AuroraUniform(int seed, float x, float y, float z) implements DynamicUniformStorage.DynamicUniform {

		private static final int SIZE = new Std140SizeCalculator().putInt().putVec3().get();

		@Override
		public void write(ByteBuffer buffer) {
			Std140Builder.intoBuffer(buffer).putInt(seed).putVec3(x, y, z);
		}

	}

}
