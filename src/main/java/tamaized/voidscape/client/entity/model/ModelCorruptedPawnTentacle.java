package tamaized.voidscape.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import tamaized.voidscape.entity.EntityCorruptedPawnTentacle;

public class ModelCorruptedPawnTentacle<T extends EntityCorruptedPawnTentacle> extends EntityModel<T> {

	private ModelPart tentacle;

	public ModelCorruptedPawnTentacle(ModelPart parent) {
		super(RenderType::entityTranslucent);
		tentacle = parent.getChild("head");
	}

	public static LayerDefinition createMesh() {

		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(0, 32).
						addBox(-2F, 0F, -2F, 4, 12, 4).mirror(), PartPose.ZERO);

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if (entityIn.falling() && !Minecraft.getInstance().isPaused())
			tentacle.yRot += Math.toRadians(Minecraft.getInstance().getFrameTime());
	}


	@Override
	public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_) {
		tentacle.render(p_103111_, p_103112_, p_103113_, p_103114_, p_103115_, p_103116_, p_103117_, p_103118_);
	}
}
