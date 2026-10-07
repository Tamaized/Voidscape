package tamaized.voidscape.client.entity.render;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.joml.Matrix4f;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.entity.render.state.RiftEntityRenderState;
import tamaized.voidscape.entity.AbstractRiftEntity;

import java.util.function.Supplier;

public class RenderRiftEntity extends EntityRenderer<AbstractRiftEntity, RiftEntityRenderState> {

	private final Supplier<RenderType> renderType;

	public RenderRiftEntity(EntityRendererProvider.Context context, RenderPipeline pipeline) {
		super(context);
		renderType = Suppliers.memoize(() -> RenderType.create(
			Voidscape.MODID + "_shroud_rift",
			RenderSetup.builder(pipeline)
				.bufferSize(256)
				.sortOnUpload()
				.createRenderSetup()
		));
	}

	@Override
	public RiftEntityRenderState createRenderState() {
		return new RiftEntityRenderState();
	}

	@Override
	public void extractRenderState(AbstractRiftEntity entity, RiftEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.seed = entity.getUUID().hashCode(); // UUID hashcode very cheap and also consistent on all clients
		state.shouldRender = entity.shouldRender(Minecraft.getInstance().player);
	}

	@Override
	protected boolean affectedByCulling(AbstractRiftEntity entity) {
		return false;
	}

	private void vertex(VertexConsumer buffer, Matrix4f matrix, float x, float y, float red, float green, float blue, float texU, float texV) {
		buffer.addVertex(matrix, x, y, 0F)
			.setUv(texU, texV)
			.setColor(red, green, blue, 1F);
	}

	@Override
	public void submit(RiftEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		if (!state.shouldRender)
			return;
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
