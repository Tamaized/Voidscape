package tamaized.voidscape.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.ARGB;
import net.minecraft.util.context.ContextKey;
import net.neoforged.api.distmarker.Dist;
import org.joml.Matrix4f;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import tamaized.voidscape.client.DonatorLayerRenderTypes;
import tamaized.voidscape.client.entity.render.state.ShroudWingLayerRenderStateExtension;
import tamaized.voidscape.client.event.EntityLayerRendererRegistration;

@Configurable
public class ShroudWingLayer<T extends AvatarRenderState, M extends EntityModel<T>> extends RenderLayer<T, M> {

	@Autowired(dist = Dist.CLIENT)
	private DonatorLayerRenderTypes donatorLayerRenderTypes;

	@Autowired(dist = Dist.CLIENT)
	private ShroudWingLayerRenderStateExtension shroudWingLayerRenderStateExtension;

	@Autowired(dist = Dist.CLIENT)
	private EntityLayerRendererRegistration entityLayerRendererRegistration;

	private static final int SUBMIT_ORDER_BACKING = 5;

	private static final int SUBMIT_ORDER_WINGS = 6;

	public ShroudWingLayer(RenderLayerParent<T, M> renderer) {
		super(renderer);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, T state, float yRot, float xRot) {
		boolean donatorEnabled = check(shroudWingLayerRenderStateExtension.isDonatorAndEnabled, state);
		if (donatorEnabled || check(shroudWingLayerRenderStateExtension.hasDraconicAttribute, state)) {
			Integer donatorColor = state.getRenderData(shroudWingLayerRenderStateExtension.donatorColor);
			boolean itemTarget = isRedirectedToItemTarget(state);
			if (!itemTarget)
				submitWings(
					poseStack,
					submitNodeCollector,
					SUBMIT_ORDER_BACKING,
					donatorLayerRenderTypes.WRAPPED_POS_TEX_COLOR.get(),
					ARGB.colorFromFloat(0.25F, 0F, 0F, 0F)
				);
			submitWings(
				poseStack,
				submitNodeCollector,
				SUBMIT_ORDER_WINGS,
				(itemTarget ? donatorLayerRenderTypes.WINGS_ITEM_TARGET : donatorLayerRenderTypes.WINGS).get(),
				ARGB.color((int) (0.25F * 255), donatorEnabled && donatorColor != null ? donatorColor : 0xFFA4EA)
			);
		}
	}

	private boolean isRedirectedToItemTarget(T state) {
		Float infusion = state.getRenderData(entityLayerRendererRegistration.CONTEXT_KEY_INFUSION);
		return infusion != null && infusion > 0F && Minecraft.getInstance().levelRenderer.getItemEntityTarget() != null;
	}

	public boolean check(ContextKey<Boolean> key, T state) {
		return Boolean.TRUE.equals(state.getRenderData(key));
	}

	private void submitWings(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int order, RenderType renderType, int color) {
		submitNodeCollector.order(order).submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
			final float x1 = 0.10F;
			final float y1 = -0.75F;
			final float z1 = 0.75F;
			final float x2 = x1 + 1.0F;
			final float y2 = y1 + 1.0F;
			final float z2 = z1 - 0.75F;

			Matrix4f left = new Matrix4f(pose.pose())
				.rotate(Axis.ZN.rotationDegrees(-25))
				.rotate(Axis.XN.rotationDegrees(15));
			buffer.addVertex(left, x2, y2, z1).setUv(0, 1).setColor(color);
			buffer.addVertex(left, x2, y1, z1).setUv(0, 0).setColor(color);
			buffer.addVertex(left, x1, y1, z2).setUv(1, 0).setColor(color);
			buffer.addVertex(left, x1, y2, z2).setUv(1, 1).setColor(color);

			Matrix4f right = new Matrix4f(pose.pose())
				.rotate(Axis.ZN.rotationDegrees(25))
				.rotate(Axis.XN.rotationDegrees(15));
			float offset = -1.2F;
			buffer.addVertex(right, x1 + offset, y2, z1).setUv(0, 1).setColor(color);
			buffer.addVertex(right, x1 + offset, y1, z1).setUv(0, 0).setColor(color);
			buffer.addVertex(right, x2 + offset, y1, z2).setUv(1, 0).setColor(color);
			buffer.addVertex(right, x2 + offset, y2, z2).setUv(1, 1).setColor(color);
		});
	}
}
