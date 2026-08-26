package tamaized.voidscape.client.armor.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.LightCoordsUtil;

public class ModelArmorFullbright<T extends HumanoidRenderState> extends HumanoidModel<T> {

	public ModelArmorFullbright(ModelPart root) {
		super(root);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
		super.renderToBuffer(poseStack, buffer, LightCoordsUtil.FULL_BRIGHT, packedOverlay, color);
	}

}
