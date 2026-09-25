package tamaized.voidscape.client.entity.render;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.neoforged.api.distmarker.Dist;
import org.joml.Matrix4f;
import tamaized.beanification.Autowired;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.render.state.ShroudRiftRenderState;
import tamaized.voidscape.client.shader.Shaders;
import tamaized.voidscape.entity.ShroudRiftEntity;

import java.util.function.Supplier;

public class RenderShroudRift extends EntityRenderer<ShroudRiftEntity, ShroudRiftRenderState> {

	@Autowired(dist = Dist.CLIENT)
	private static Shaders shaders;

	private final Supplier<RenderType> renderType = Suppliers.memoize(() -> RenderType.create(
		Voidscape.MODID + "_shroud_rift",
		RenderSetup.builder(shaders.SHROUD_RIFT)
			.bufferSize(256)
			.sortOnUpload()
			.createRenderSetup()
	));

	public RenderShroudRift(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ShroudRiftRenderState createRenderState() {
		return new ShroudRiftRenderState();
	}

	@Override
	public void extractRenderState(ShroudRiftEntity entity, ShroudRiftRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.seed = entity.getUUID().hashCode(); // UUID hashcode very cheap and also consistent on all clients
	}

	@Override
	protected boolean affectedByCulling(ShroudRiftEntity entity) {
		return false;
	}

	private void vertex(VertexConsumer buffer, Matrix4f matrix, float x, float y, float red, float green, float blue, float texU, float texV) {
		buffer.addVertex(matrix, x, y, 0F)
			.setUv(texU, texV)
			.setColor(red, green, blue, 1F);
	}

	@Override
	public void submit(ShroudRiftRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		final float quadScale = 1.2F;
		final float halfWidth = 0.75F * quadScale;
		final float bottom = 1.5F * (1F - quadScale);
		final float top = 1.5F * (1F + quadScale);
		final float uvMin = 0.5F * (1F - quadScale);
		final float uvMax = 0.5F * (1F + quadScale);
		// This is a VERY hacky way to inject the seed into the shader without adding a new uniform lol
		final float red = ((state.seed >> 16) & 0xFF) / 255F;
		final float green = ((state.seed >> 8) & 0xFF) / 255F;
		final float blue = (state.seed & 0xFF) / 255F;
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotation((float) Math.atan2(camera.pos.x - state.x, camera.pos.z - state.z)));
		submitNodeCollector.submitCustomGeometry(poseStack, renderType.get(), (pose, buffer) -> {
			Matrix4f matrix = pose.pose();
			vertex(buffer, matrix, -halfWidth, bottom, red, green, blue, uvMin, uvMin);
			vertex(buffer, matrix, -halfWidth, top, red, green, blue, uvMin, uvMax);
			vertex(buffer, matrix, halfWidth, top, red, green, blue, uvMax, uvMax);
			vertex(buffer, matrix, halfWidth, bottom, red, green, blue, uvMax, uvMin);
		});
		poseStack.popPose();
		super.submit(state, poseStack, submitNodeCollector, camera);
	}

}
