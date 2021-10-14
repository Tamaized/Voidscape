package tamaized.voidscape.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import tamaized.voidscape.Voidscape;
import tamaized.voidscape.client.ui.RenderTurmoil;
import tamaized.voidscape.turmoil.SubCapability;

public class DonatorLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Voidscape.MODID, "textures/entity/donator.png");

	public DonatorLayer(RenderLayerParent<T, M> p_117346_) {
		super(p_117346_);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource multibuffer, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		entity.getCapability(SubCapability.CAPABILITY).ifPresent(cap -> cap.get(Voidscape.subCapDonatorData).ifPresent(data -> {
			if (data.enabled) {
				BufferBuilder buffer = Tesselator.getInstance().getBuilder();
				buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

				float x1 = 0.10F;
				float y1 = -0.75F;
				float z1 = 0.75F;

				float x2 = x1 + 1.0F;
				float y2 = y1 + 1.0F;
				float z2 = z1 - 0.75F;

				stack.pushPose();
				{
					stack.mulPose(Vector3f.ZN.rotationDegrees(-25));
					stack.mulPose(Vector3f.XN.rotationDegrees(15));
					Matrix4f pose = stack.last().pose();
					buffer.vertex(pose, x2, y2, z1).uv(0, 1).endVertex();
					buffer.vertex(pose, x2, y1, z1).uv(0, 0).endVertex();
					buffer.vertex(pose, x1, y1, z2).uv(1, 0).endVertex();
					buffer.vertex(pose, x1, y2, z2).uv(1, 1).endVertex();
				}
				stack.popPose();
				stack.pushPose();
				{
					Matrix4f pose = stack.last().pose();
					stack.mulPose(Vector3f.ZN.rotationDegrees(25));
					stack.mulPose(Vector3f.XN.rotationDegrees(15));
					float offset = -1.2F;
					buffer.vertex(pose, x1 + offset, y2, z1).uv(0, 1).endVertex();
					buffer.vertex(pose, x1 + offset, y1, z1).uv(0, 0).endVertex();
					buffer.vertex(pose, x2 + offset, y1, z2).uv(1, 0).endVertex();
					buffer.vertex(pose, x2 + offset, y2, z2).uv(1, 1).endVertex();
				}
				stack.popPose();

				RenderTurmoil.Color24 color = RenderTurmoil.Color24.INSTANCE;
				color.unpack(data.color);
				RenderSystem.setShaderColor(color.bit16 / 255F, color.bit8 / 255F, color.bit0 / 255F, color.bit24 / 255F);

				RenderSystem.defaultBlendFunc();
				RenderSystem.enableBlend();
				RenderSystem.disableCull();
				RenderSystem.enableDepthTest();
				ClientUtil.bindTexture(TEXTURE);
				Shaders.WRAPPED_POS_TEX.invokeThenEndTesselator();
				RenderSystem.disableDepthTest();
				RenderSystem.enableCull();
				RenderSystem.disableBlend();
			}
		}));
	}
}
