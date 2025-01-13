package tamaized.voidscape.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix4f;
import tamaized.beanification.Autowired;
import tamaized.voidscape.data.DonatorData;
import tamaized.voidscape.registry.ModDataAttachments;
import tamaized.voidscape.util.ItemAugmentUtil;

public class DonatorLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

	@Autowired
	private static DonatorLayerBuffers donatorLayerBuffers;

	@Autowired
	private static ModDataAttachments dataAttachments;

	@Autowired
	private static ItemAugmentUtil itemAugmentUtil;

	public DonatorLayer(RenderLayerParent<T, M> p_117346_) {
		super(p_117346_);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource multibuffer, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		DonatorData data = entity.getData(dataAttachments.DONATOR);
		if (data.enabled || (itemAugmentUtil.draconic(entity.getItemBySlot(EquipmentSlot.CHEST)))) {
			drawWings(
				stack,
				donatorLayerBuffers.BUFFERS.getBuffer(donatorLayerBuffers.WRAPPED_POS_TEX_COLOR),
				FastColor.ARGB32.colorFromFloat(0.25F, 0F, 0F, 0F)
			);
			drawWings(
				stack,
				donatorLayerBuffers.BUFFERS.getBuffer(donatorLayerBuffers.WINGS),
				FastColor.ARGB32.color((int) (0.25F * 255), data.enabled ? data.color : 0xFFA4EA)
			);
		}
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
