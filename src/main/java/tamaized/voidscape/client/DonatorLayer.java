package tamaized.voidscape.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix4f;
import tamaized.beanification.Autowired;
import tamaized.voidscape.client.ui.RenderTurmoil;
import tamaized.voidscape.data.DonatorData;
import tamaized.voidscape.registry.ModArmors;
import tamaized.voidscape.registry.ModDataAttachments;

import java.util.function.Consumer;

public class DonatorLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

	@Autowired
	private static DonatorLayerBuffers donatorLayerBuffers;

	public DonatorLayer(RenderLayerParent<T, M> p_117346_) {
		super(p_117346_);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource multibuffer, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		DonatorData data = entity.getData(ModDataAttachments.DONATOR);
		if (data.enabled || (ModArmors.draconic(entity.getItemBySlot(EquipmentSlot.CHEST)))) {
			VertexConsumer buffer = donatorLayerBuffers.BUFFERS.getBuffer(donatorLayerBuffers.WRAPPED_POS_TEX_COLOR);

			float x1 = 0.10F;
			float y1 = -0.75F;
			float z1 = 0.75F;

			float x2 = x1 + 1.0F;
			float y2 = y1 + 1.0F;
			float z2 = z1 - 0.75F;

			RenderTurmoil.Color24 color = RenderTurmoil.Color24.INSTANCE;

			Consumer<VertexConsumer> vertexColor = verticies -> verticies.setColor(color.bit16, color.bit8, color.bit0, color.bit24);

			color.set(0.25F, 0F, 0F, 0F);

			stack.pushPose();
			{
				stack.mulPose(Axis.ZN.rotationDegrees(-25));
				stack.mulPose(Axis.XN.rotationDegrees(15));
				Matrix4f pose = stack.last().pose();
				vertexColor.accept(buffer.addVertex(pose, x2, y2, z1).setUv(0, 1));
				vertexColor.accept(buffer.addVertex(pose, x2, y1, z1).setUv(0, 0));
				vertexColor.accept(buffer.addVertex(pose, x1, y1, z2).setUv(1, 0));
				vertexColor.accept(buffer.addVertex(pose, x1, y2, z2).setUv(1, 1));
			}
			stack.popPose();
			stack.pushPose();
			{
				Matrix4f pose = stack.last().pose();
				stack.mulPose(Axis.ZN.rotationDegrees(25));
				stack.mulPose(Axis.XN.rotationDegrees(15));
				float offset = -1.2F;
				vertexColor.accept(buffer.addVertex(pose, x1 + offset, y2, z1).setUv(0, 1));
				vertexColor.accept(buffer.addVertex(pose, x1 + offset, y1, z1).setUv(0, 0));
				vertexColor.accept(buffer.addVertex(pose, x2 + offset, y1, z2).setUv(1, 0));
				vertexColor.accept(buffer.addVertex(pose, x2 + offset, y2, z2).setUv(1, 1));
			}
			stack.popPose();

			buffer = donatorLayerBuffers.BUFFERS.getBuffer(donatorLayerBuffers.WINGS);

			color.unpack(data.enabled ? data.color : 0xFFA4EA);
			color.bit24 = (int) (0.25F * 255);

			stack.pushPose();
			{
				stack.mulPose(Axis.ZN.rotationDegrees(-25));
				stack.mulPose(Axis.XN.rotationDegrees(15));
				Matrix4f pose = stack.last().pose();
				vertexColor.accept(buffer.addVertex(pose, x2, y2, z1).setUv(0, 1));
				vertexColor.accept(buffer.addVertex(pose, x2, y1, z1).setUv(0, 0));
				vertexColor.accept(buffer.addVertex(pose, x1, y1, z2).setUv(1, 0));
				vertexColor.accept(buffer.addVertex(pose, x1, y2, z2).setUv(1, 1));
			}
			stack.popPose();
			stack.pushPose();
			{
				Matrix4f pose = stack.last().pose();
				stack.mulPose(Axis.ZN.rotationDegrees(25));
				stack.mulPose(Axis.XN.rotationDegrees(15));
				float offset = -1.2F;
				vertexColor.accept(buffer.addVertex(pose, x1 + offset, y2, z1).setUv(0, 1));
				vertexColor.accept(buffer.addVertex(pose, x1 + offset, y1, z1).setUv(0, 0));
				vertexColor.accept(buffer.addVertex(pose, x2 + offset, y1, z2).setUv(1, 0));
				vertexColor.accept(buffer.addVertex(pose, x2 + offset, y2, z2).setUv(1, 1));
			}
			stack.popPose();

		}
	}
}
