package tamaized.voidscape.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.util.context.ContextKey;
import net.neoforged.api.distmarker.Dist;
import org.joml.Matrix4f;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.client.DonatorLayerBuffers;
import tamaized.voidscape.client.entity.render.state.ShroudWingLayerRenderStateExtension;

@Configurable
public class ShroudWingLayer<T extends AvatarRenderState, M extends EntityModel<T>> extends RenderLayer<T, M> {

	@Autowired(dist = Dist.CLIENT)
	private DonatorLayerBuffers donatorLayerBuffers;

	@Autowired(dist = Dist.CLIENT)
	private ShroudWingLayerRenderStateExtension shroudWingLayerRenderStateExtension;

	public ShroudWingLayer(RenderLayerParent<T, M> renderer) {
		super(renderer);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, T state, float yRot, float xRot) {
		boolean donatorEnabled = check(shroudWingLayerRenderStateExtension.isDonatorAndEnabled, state);
		if (donatorEnabled || check(shroudWingLayerRenderStateExtension.hasDraconicAttribute, state)) {
			drawWings(
				poseStack,
				donatorLayerBuffers.BUFFERS.get().getBuffer(donatorLayerBuffers.WRAPPED_POS_TEX_COLOR.get()),
				ARGB.colorFromFloat(0.25F, 0F, 0F, 0F)
			);
			Integer donatorColor = state.getRenderData(shroudWingLayerRenderStateExtension.donatorColor);
			drawWings(
				poseStack,
				donatorLayerBuffers.BUFFERS.get().getBuffer(donatorLayerBuffers.WINGS.get()),
				ARGB.color((int) (0.25F * 255), donatorEnabled && donatorColor != null ? donatorColor : 0xFFA4EA)
			);
		}
	}

	public boolean check(ContextKey<Boolean> key, T state) {
		return Boolean.TRUE.equals(state.getRenderData(key));
	}

	private void drawWings(PoseStack stack, VertexConsumer buffer, int color) {
		final float x1 = 0.10F;
		final float y1 = -0.75F;
		final float z1 = 0.75F;
		final float x2 = x1 + 1.0F;
		final float y2 = y1 + 1.0F;
		final float z2 = z1 - 0.75F;

		stack.pushPose();
		{
			stack.mulPose(Axis.ZN.rotationDegrees(-25));
			stack.mulPose(Axis.XN.rotationDegrees(15));
			Matrix4f pose = stack.last().pose();
			buffer.addVertex(pose, x2, y2, z1).setUv(0, 1).setColor(color);
			buffer.addVertex(pose, x2, y1, z1).setUv(0, 0).setColor(color);
			buffer.addVertex(pose, x1, y1, z2).setUv(1, 0).setColor(color);
			buffer.addVertex(pose, x1, y2, z2).setUv(1, 1).setColor(color);
		}
		stack.popPose();
		stack.pushPose();
		{
			Matrix4f pose = stack.last().pose();
			stack.mulPose(Axis.ZN.rotationDegrees(25));
			stack.mulPose(Axis.XN.rotationDegrees(15));
			float offset = -1.2F;
			buffer.addVertex(pose, x1 + offset, y2, z1).setUv(0, 1).setColor(color);
			buffer.addVertex(pose, x1 + offset, y1, z1).setUv(0, 0).setColor(color);
			buffer.addVertex(pose, x2 + offset, y1, z2).setUv(1, 0).setColor(color);
			buffer.addVertex(pose, x2 + offset, y2, z2).setUv(1, 1).setColor(color);
		}
		stack.popPose();
	}
}
